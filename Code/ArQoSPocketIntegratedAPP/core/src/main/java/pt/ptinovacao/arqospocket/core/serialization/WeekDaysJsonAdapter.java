package pt.ptinovacao.arqospocket.core.serialization;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import pt.ptinovacao.arqospocket.core.utils.WeekDays;

/**
 * Jason adapter for the {@link WeekDays} object.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
public class WeekDaysJsonAdapter extends TypeAdapter<WeekDays> {

    @Override
    public void write(JsonWriter out, WeekDays value) throws IOException {
        if (value == null) {
            out.value((Integer) null);
        } else {
            int result = 0;
            result = value.saturday() ? result | 0b01000000 : result;
            result = value.friday() ? result | 0b00100000 : result;
            result = value.thursday() ? result | 0b00010000 : result;
            result = value.wednesday() ? result | 0b00001000 : result;
            result = value.tuesday() ? result | 0b00000100 : result;
            result = value.monday() ? result | 0b00000010 : result;
            result = value.sunday() ? result | 0b00000001 : result;
            out.value(result);
        }
    }

    @Override
    public WeekDays read(JsonReader in) throws IOException {
        int binaryValue = in.nextInt();
        return new WeekDays(new boolean[] {
                (binaryValue & 0b01000000) == 0b01000000,
                (binaryValue & 0b00100000) == 0b00100000,
                (binaryValue & 0b00010000) == 0b00010000,
                (binaryValue & 0b00001000) == 0b00001000,
                (binaryValue & 0b00000100) == 0b00000100,
                (binaryValue & 0b00000010) == 0b00000010,
                (binaryValue & 0b00000001) == 0b00000001
        });
    }
}
