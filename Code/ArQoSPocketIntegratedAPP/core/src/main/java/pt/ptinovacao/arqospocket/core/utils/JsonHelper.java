package pt.ptinovacao.arqospocket.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Helper utilities to handle JSON data.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
public class JsonHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonHelper.class);

    /**
     * Creates a standard {@link Gson} instance with a date pattern set to "yyyyMMddHHmmss" and with pretty print
     * enabled.
     *
     * @return the created {@link Gson} instance.
     */
    public static Gson getGsonInstance() {
        return getGsonInstance(true);
    }

    /**
     * Creates a standard {@link Gson} instance with a date pattern set to "yyyyMMddHHmmss" and with pretty print
     * enabled.
     *
     * @param prettyPrint if the output JSON should be formatted.
     * @return the created {@link Gson} instance.
     */
    public static Gson getGsonInstance(boolean prettyPrint) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());

        if (prettyPrint) {
            gsonBuilder.setPrettyPrinting();
        }
        return gsonBuilder.create();
    }

    public static class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {

            final Date result = DateUtils.maxDate();

            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = simpleDateFormat.parse(json.getAsString());

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.setTimeZone(TimeZone.getDefault());

                return calendar.getTime();
            } catch (ParseException e) {
                LOGGER.debug("Config date: {} {} ", result, e.getMessage());
            }

            return result;
        }
    }

    public static class DateSerializer implements JsonSerializer<Date> {

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            return new JsonPrimitive(simpleDateFormat.format(src));
        }
    }
}
