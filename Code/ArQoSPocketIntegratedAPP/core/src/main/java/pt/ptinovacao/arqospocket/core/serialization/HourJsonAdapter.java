package pt.ptinovacao.arqospocket.core.serialization;

import android.annotation.SuppressLint;

import com.google.common.base.Strings;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Adapter for the parameter data hour field the converts an hour representation to a {@link Date} object.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
public class HourJsonAdapter extends TypeAdapter<Date> {

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.value((String) null);
        } else {
            out.value(formatter.format(value));
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        String stringDate = Strings.nullToEmpty(in.nextString());
        if (stringDate.trim().length() != 6) {
            throw new IOException("Invalid hour value");
        }
        try {
            return formatter.parse(stringDate);
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }
}
