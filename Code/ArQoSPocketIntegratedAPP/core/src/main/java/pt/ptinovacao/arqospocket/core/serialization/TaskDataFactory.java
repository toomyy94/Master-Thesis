package pt.ptinovacao.arqospocket.core.serialization;

import android.support.annotation.NonNull;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ptinovacao.arqospocket.core.serialization.composers.ObjectComposer;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * Factory class to create the proper instances of the task objects based in the provided data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
class TaskDataFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDataFactory.class);

    static TaskData createFromMap(Map<String, String> propertyMap) {
        TaskData data = TaskResolver.taskDataForType(propertyMap.get(TaskData.TASK_ID));
        data.setPipedString(false);
        fillBaseTaskData(data, propertyMap);
        fillSpecificTaskData(data, propertyMap);
        return data;
    }

    static TaskData createFromList(List<String> propertyList) {
        String subType = null;
        if (propertyList.size() > 7) {
            subType = Strings.emptyToNull(propertyList.get(7));
        }
        TaskData data = TaskResolver.taskDataForType(propertyList.get(5), subType);
        data.setPipedString(true);
        fillBaseTaskData(data, propertyList);
        fillSpecificTaskData(data, propertyList);
        return data;
    }

    static Map<String, String> decomposeObjectToMap(TaskData value) {
        if (value == null) {
            return null;
        }

        Map<String, String> propertyMap = collectBaseTaskDataToMap(value);
        collectSpecificTaskDataToMap(value, propertyMap);

        removeNullValues(propertyMap);

        return propertyMap;
    }

    static List<String> decomposeObjectToList(TaskData value) {
        if (value == null) {
            return null;
        }

        List<String> propertyList = collectBaseTaskDataToList(value);
        collectSpecificTaskDataToList(value, propertyList);

        return propertyList;
    }

    private static void fillBaseTaskData(@NonNull TaskData data, @NonNull Map<String, String> propertyMap) {
        data.setMacroId(propertyMap.get(TaskData.MACRO_ID));
        data.setTaskNumber(ObjectComposer.toInt(propertyMap.get(TaskData.TASK_NUMBER)));
        data.setIccid(propertyMap.get(TaskData.ICCID));
        data.setExecutionDelay(propertyMap.get(TaskData.EXECUTION_DELAY));
        data.setTimeout(propertyMap.get(TaskData.TIMEOUT));
        data.setTaskId(propertyMap.get(TaskData.TASK_ID));
        data.setImmediate(propertyMap.get(TaskData.IMMEDIATE));
        data.setTaskName(propertyMap.get(TaskData.TASK_NAME));
    }

    private static void fillSpecificTaskData(@NonNull TaskData data, @NonNull Map<String, String> propertyMap) {
        try {
            ObjectComposer objectComposer = ObjectComposer.getTaskDataObjectComposer(data.getClass());
            objectComposer.fill(data, propertyMap);
        } catch (UnsupportedOperationException e) {
            LOGGER.debug("No object composer for {}", data.getClass());
        }
    }

    private static void fillBaseTaskData(@NonNull TaskData data, @NonNull List<String> propertyList) {
        data.setMacroId(propertyList.get(0));
        data.setTaskNumber(ObjectComposer.toInt(propertyList.get(1)));
        data.setIccid(propertyList.get(2));
        data.setExecutionDelay(propertyList.get(3));
        data.setTimeout(propertyList.get(4));
        data.setTaskId(propertyList.get(5));
        data.setImmediate(propertyList.get(6));
    }

    private static void fillSpecificTaskData(@NonNull TaskData data, @NonNull List<String> propertyList) {
        try {
            ObjectComposer objectComposer = ObjectComposer.getTaskDataObjectComposer(data.getClass());
            objectComposer.fill(data, InfiniteIndexList.fromList(propertyList, ""));
        } catch (UnsupportedOperationException e) {
            LOGGER.debug("No object composer for {}", data.getClass());
        }
    }

    @NonNull
    private static Map<String, String> collectBaseTaskDataToMap(@NonNull TaskData data) {
        Map<String, String> propertyMap = new HashMap<>();

        propertyMap.put(TaskData.MACRO_ID, data.getMacroId());
        propertyMap.put(TaskData.TASK_NUMBER, ObjectComposer.toString(data.getTaskNumber()));
        propertyMap.put(TaskData.ICCID, data.getIccid());
        propertyMap.put(TaskData.EXECUTION_DELAY, data.getExecutionDelay());
        propertyMap.put(TaskData.TIMEOUT, data.getTimeout());
        propertyMap.put(TaskData.TASK_ID, data.getTaskId());
        propertyMap.put(TaskData.IMMEDIATE, data.getImmediate());
        propertyMap.put(TaskData.TASK_NAME, data.getTaskName());

        return propertyMap;
    }

    private static void collectSpecificTaskDataToMap(@NonNull TaskData data, @NonNull Map<String, String> propertyMap) {
        try {
            ObjectComposer objectComposer = ObjectComposer.getTaskDataObjectComposer(data.getClass());
            objectComposer.collectToMap(data, propertyMap);
        } catch (UnsupportedOperationException e) {
            LOGGER.debug("No object composer for {}", data.getClass());
        }
    }

    @NonNull
    private static List<String> collectBaseTaskDataToList(@NonNull TaskData data) {
        List<String> propertyList = new ArrayList<>();

        propertyList.add(data.getMacroId());
        propertyList.add(ObjectComposer.toString(data.getTaskNumber()));
        propertyList.add(data.getIccid());
        propertyList.add(data.getExecutionDelay());
        propertyList.add(data.getTimeout());
        propertyList.add(data.getTaskId());
        propertyList.add(data.getImmediate());

        return propertyList;
    }

    private static void collectSpecificTaskDataToList(@NonNull TaskData data, @NonNull List<String> propertyList) {
        try {
            ObjectComposer objectComposer = ObjectComposer.getTaskDataObjectComposer(data.getClass());
            objectComposer.collectToList(data, propertyList);
        } catch (UnsupportedOperationException e) {
            LOGGER.debug("No object composer for {}", data.getClass());
        }
    }

    private static void removeNullValues(Map<String, String> propertyMap) {
        List<String> nullKeys = new ArrayList<>();
        for (String keys : propertyMap.keySet()) {
            if (propertyMap.get(keys) == null) {
                nullKeys.add(keys);
            }
        }
        for (String key : nullKeys) {
            propertyMap.remove(key);
        }
    }
}

