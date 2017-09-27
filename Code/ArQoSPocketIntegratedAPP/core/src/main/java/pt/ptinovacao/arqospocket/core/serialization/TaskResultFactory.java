package pt.ptinovacao.arqospocket.core.serialization;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.ptinovacao.arqospocket.core.serialization.composers.ObjectComposer;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Factory class to create the proper instances of the task result objects based in the provided data.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class TaskResultFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskResultFactory.class);

    private static final Map<String, Integer> SUB_TASK_POSITIONS =
            new ImmutableMap.Builder<String, Integer>().put(TaskType.ANSWER_CALL_TASK, 10)
                    .put(TaskType.MAKE_CALL_TASK, 9).put(TaskType.HANG_UP_CALL_TASK, 46).build();

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static ArrayList<AbstractMap.SimpleEntry> decomposeObjectToArrayList(TaskResult value) {
        ArrayList<AbstractMap.SimpleEntry> simpleEntries = new ArrayList<>();

        if (value == null) {
            return simpleEntries;
        }

        Map<String, String> propertyMap = decomposeObjectToMap(value);

        if (propertyMap != null) {
            Set<Map.Entry<String, String>> entries = propertyMap.entrySet();

            for (Map.Entry<String, String> entry : entries) {
                simpleEntries.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
            }
        }

        return simpleEntries;
    }

    static TaskResult createFromMap(Map<String, String> propertyMap) {
        TaskResult data = TaskResolver.taskResultForType(propertyMap.get(TaskResult.TASK_ID));
        data.setPipedString(false);
        fillBaseTaskResult(data, propertyMap);
        fillSpecificTaskResult(data, propertyMap);
        return data;
    }

    static TaskResult createFromList(List<String> propertyList) {
        String subType = null;
        String type = propertyList.get(4);
        int position = getSubTypePosition(type);
        if (propertyList.size() > position) {
            subType = Strings.emptyToNull(propertyList.get(position));
        }
        TaskResult data = TaskResolver.taskResultForType(type, subType);
        data.setPipedString(true);
        fillBaseTaskResult(data, propertyList);
        fillSpecificTaskResult(data, propertyList);
        return data;
    }

    static Map<String, String> decomposeObjectToMap(TaskResult value) {
        if (value == null) {
            return null;
        }

        Map<String, String> propertyMap = collectBaseTaskResultToMap(value);
        collectSpecificTaskResultToMap(value, propertyMap);

        removeNullValues(propertyMap);

        return propertyMap;
    }

    static List<String> decomposeObjectToList(TaskResult value) {
        if (value == null) {
            return null;
        }

        List<String> propertyList = collectBaseTaskResultToList(value);
        collectSpecificTaskResultToList(value, propertyList);

        return propertyList;
    }

    private static int getSubTypePosition(String type) {
        if (SUB_TASK_POSITIONS.containsKey(type)) {
            return SUB_TASK_POSITIONS.get(type);
        }
        return Integer.MAX_VALUE;
    }

    private static void fillBaseTaskResult(@NonNull TaskResult data, @NonNull Map<String, String> propertyMap) {
        data.setTaskId(propertyMap.get(TaskResult.TASK_ID));
        data.setTaskName(propertyMap.get(TaskResult.TASK_NAME));
        data.setMacroId(propertyMap.get(TaskResult.MACRO_ID));
        data.setTaskNumber(ObjectComposer.toInt(propertyMap.get(TaskResult.TASK_NUMBER)));
        data.setStartDate(parseDate(propertyMap.get(TaskResult.START_DATE)));
        data.setEndDate(parseDate(propertyMap.get(TaskResult.END_DATE)));
        data.setIccid(propertyMap.get(TaskResult.ICCID));
        data.setCellId(propertyMap.get(TaskResult.CELL_ID));
        data.setGpsLocation(propertyMap.get(TaskResult.GPS_LOCATION));
        data.setStatus(propertyMap.get(TaskResult.STATUS));
    }

    private static void fillSpecificTaskResult(@NonNull TaskResult data, @NonNull Map<String, String> propertyMap) {
        try {
            ObjectComposer objectComposer = ObjectComposer.getTaskResultObjectComposer(data.getClass());
            objectComposer.fill(data, propertyMap);
        } catch (UnsupportedOperationException e) {
            LOGGER.debug("No object composer for {}", data.getClass());
        }
    }

    private static void fillBaseTaskResult(@NonNull TaskResult data, @NonNull List<String> propertyList) {
        data.setMacroId(propertyList.get(0));
        data.setTaskNumber(ObjectComposer.toInt(propertyList.get(1)));
        data.setStartDate(parseDate(propertyList.get(2)));
        data.setEndDate(parseDate(propertyList.get(3)));
        data.setTaskId(propertyList.get(4));
        data.setIccid(propertyList.get(5));
        data.setCellId(propertyList.get(6));
        data.setGpsLocation(propertyList.get(7));
        data.setStatus(propertyList.get(8));
    }

    private static void fillSpecificTaskResult(@NonNull TaskResult data, @NonNull List<String> propertyList) {
        try {
            ObjectComposer objectComposer = ObjectComposer.getTaskResultObjectComposer(data.getClass());
            objectComposer.fill(data, InfiniteIndexList.fromList(propertyList, ""));
        } catch (UnsupportedOperationException e) {
            LOGGER.debug("No object composer for {}", data.getClass());
        }
    }

    @NonNull
    private static Map<String, String> collectBaseTaskResultToMap(@NonNull TaskResult data) {
        Map<String, String> propertyMap = new HashMap<>();

        propertyMap.put(TaskResult.TASK_ID, data.getTaskId());
        propertyMap.put(TaskResult.TASK_NAME, data.getTaskName());
        propertyMap.put(TaskResult.MACRO_ID, data.getMacroId());
        propertyMap.put(TaskResult.TASK_NUMBER, ObjectComposer.toString(data.getTaskNumber()));
        propertyMap.put(TaskResult.START_DATE, formatDate(data.getStartDate()));
        propertyMap.put(TaskResult.END_DATE, formatDate(data.getEndDate()));
        propertyMap.put(TaskResult.ICCID, data.getIccid());
        propertyMap.put(TaskResult.CELL_ID, data.getCellId());
        propertyMap.put(TaskResult.GPS_LOCATION, data.getGpsLocation());
        propertyMap.put(TaskResult.STATUS, data.getStatus());

        return propertyMap;
    }

    private static void collectSpecificTaskResultToMap(@NonNull TaskResult data,
            @NonNull Map<String, String> propertyMap) {
        try {
            ObjectComposer objectComposer = ObjectComposer.getTaskResultObjectComposer(data.getClass());
            objectComposer.collectToMap(data, propertyMap);
        } catch (UnsupportedOperationException e) {
            LOGGER.debug("No object composer for {}", data.getClass());
        }
    }

    @NonNull
    private static List<String> collectBaseTaskResultToList(@NonNull TaskResult data) {
        List<String> propertyList = new ArrayList<>();

        propertyList.add(data.getMacroId());
        propertyList.add(ObjectComposer.toString(data.getTaskNumber()));
        propertyList.add(Strings.nullToEmpty(formatDate(data.getStartDate())));
        propertyList.add(Strings.nullToEmpty(formatDate(data.getEndDate())));
        propertyList.add(data.getTaskId());
        propertyList.add(data.getIccid());
        propertyList.add(data.getCellId());
        propertyList.add(data.getGpsLocation());
        propertyList.add(data.getStatus());

        return propertyList;
    }

    private static void collectSpecificTaskResultToList(@NonNull TaskResult data, @NonNull List<String> propertyList) {
        try {
            ObjectComposer objectComposer = ObjectComposer.getTaskResultObjectComposer(data.getClass());
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

    private static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return formatter.format(date);
    }

    private static Date parseDate(String date) {
        if (Strings.emptyToNull(date) == null) {
            return null;
        }
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
