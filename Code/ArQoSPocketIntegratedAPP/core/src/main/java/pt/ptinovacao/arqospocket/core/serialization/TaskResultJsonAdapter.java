package pt.ptinovacao.arqospocket.core.serialization;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Adapter for the {@link TaskResult} objects.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class TaskResultJsonAdapter extends TypeAdapter<TaskResult> {

    @Override
    public void write(JsonWriter out, TaskResult value) throws IOException {
        if (value == null) {
            return;
        }

        if (value.isPipedString()) {
            writeTaskString(out, value);
        } else {
            writeTaskObject(out, value);
        }
    }

    @Override
    public TaskResult read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        if (peek == JsonToken.NULL) {
            return null;
        }
        if (peek == JsonToken.STRING) {
            return parseTaskString(in.nextString());
        } else {
            return readTaskObject(in);
        }
    }

    private void writeTaskObject(JsonWriter out, TaskResult value) throws IOException {
        Map<String, String> result = TaskResultFactory.decomposeObjectToMap(value);

        out.beginObject();
        if (result != null) {
            for (Map.Entry<String, String> entry : result.entrySet()) {
                out.name(entry.getKey());
                out.value(entry.getValue());
            }
        }
        out.endObject();
    }

    private void writeTaskString(JsonWriter out, TaskResult value) throws IOException {
        List<String> result = TaskResultFactory.decomposeObjectToList(value);
        String pipedTask = Joiner.on('|').useForNull("").join(result);
        out.value(pipedTask);
    }

    private TaskResult readTaskObject(JsonReader in) throws IOException {
        HashMap<String, String> propertiesMap = new HashMap<>();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            JsonToken peek = in.peek();
            if (peek == JsonToken.NULL) {
                return null;
            }
            if (peek == JsonToken.BEGIN_ARRAY) {
                in.beginArray();
                List<String> values = new ArrayList<>();
                while (in.hasNext()) {
                    values.add(in.nextString());
                }
                propertiesMap.put(name, Joiner.on('|').join(values));
                in.endArray();
            } else {
                propertiesMap.put(name, in.nextString());
            }
        }
        in.endObject();
        return TaskResultFactory.createFromMap(propertiesMap);
    }

    private TaskResult parseTaskString(String source) {
        String nonEmptySource = Strings.nullToEmpty(source);
        Iterable<String> splitSource = Splitter.on("|").trimResults().split(nonEmptySource);
        List<String> propertiesList = Lists.newArrayList(splitSource);
        return TaskResultFactory.createFromList(propertiesList);
    }
}
