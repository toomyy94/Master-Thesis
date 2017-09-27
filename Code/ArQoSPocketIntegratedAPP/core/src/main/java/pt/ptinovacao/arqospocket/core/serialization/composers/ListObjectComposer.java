package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.Map;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Utility class to avoid implementing map methods in list serialized composers.
 * <p>
 * Created by Emílio Simões on 18-04-2017.
 */
abstract class ListObjectComposer extends ObjectComposer {

    @Override
    public void fill(TaskData data, Map<String, String> propertyMap) {
    }

    @Override
    public void fill(TaskResult data, Map<String, String> propertyMap) {
    }

    @Override
    public void collectToMap(TaskData data, Map<String, String> propertyMap) {
    }

    @Override
    public void collectToMap(TaskResult data, Map<String, String> propertyMap) {
    }
}
