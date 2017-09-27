package pt.ptinovacao.arqospocket.core.serialization;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;

/**
 * Adapter for the {@link TestData} objects.
 * <p>
 * Created by Emílio Simões on 07-04-2017.
 */
public class TaskDataJsonAdapter extends TypeAdapter<TaskData> {

    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TaskDataJsonAdapter.class);


    @Override
    public void write(JsonWriter out, TaskData value) throws IOException {
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
    public TaskData read(JsonReader in) throws IOException {
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

    private void writeTaskObject(JsonWriter out, TaskData value) throws IOException {
        Map<String, String> result = TaskDataFactory.decomposeObjectToMap(value);

        out.beginObject();
        if (result != null) {
            for (Map.Entry<String, String> entry : result.entrySet()) {
                out.name(entry.getKey());
                out.value(entry.getValue());
            }
        }
        out.endObject();
    }

    private void writeTaskString(JsonWriter out, TaskData value) throws IOException {
        List<String> result = TaskDataFactory.decomposeObjectToList(value);
        String pipedTask = Joiner.on('|').useForNull("").join(result);
        out.value(pipedTask);
    }

    private TaskData readTaskObject(JsonReader in) throws IOException {
        HashMap<String, String> propertiesMap = new HashMap<>();
        in.beginObject();
        while (in.hasNext()) {
            propertiesMap.put(in.nextName(), in.nextString());
        }
        in.endObject();
        return TaskDataFactory.createFromMap(propertiesMap);
    }

    private TaskData parseTaskString(String source) {
        if (source.startsWith("|")) source = source.substring(1,source.length());
        String nonEmptySource = Strings.nullToEmpty(source);
        Iterable<String> splitSource = Splitter.on("|").trimResults().split(nonEmptySource);
        List<String> propertiesList = Lists.newArrayList(splitSource);
        return TaskDataFactory.createFromList(propertiesList);
    }
}
