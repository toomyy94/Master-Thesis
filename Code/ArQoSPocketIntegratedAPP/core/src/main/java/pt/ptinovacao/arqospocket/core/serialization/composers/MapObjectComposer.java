package pt.ptinovacao.arqospocket.core.serialization.composers;

import java.util.List;

import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Utility class to avoid implementing list methods in map serialized composers.
 * <p>
 * Created by Emílio Simões on 18-04-2017.
 */
abstract class MapObjectComposer extends ObjectComposer {

    @Override
    public void fill(TaskData data, List<String> propertyList) {
    }

    @Override
    public void fill(TaskResult data, List<String> propertyList) {
    }

    @Override
    public void collectToList(TaskData data, List<String> propertyList) {
    }

    @Override
    public void collectToList(TaskResult data, List<String> propertyList) {
    }
}

