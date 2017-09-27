package pt.ptinovacao.arqospocket.views.anomaly.data;

import java.util.HashMap;

import pt.ptinovacao.arqospocket.R;

/**
 * Organizes the possible anomalies in a queriable structured map.
 * <p>
 * Created by Emílio Simões on 02-05-2017.
 */
public class AnomalyTopics {

    private final HashMap<AnomalyReport, AnomalyTypeModel[]> typeHashMap = new HashMap<>();

    private final AnomalyTypeModel[] ANOMALY_TYPES_VOICE = {
            new AnomalyTypeModel(1, R.string.anomaly_type_dropped_call),
            new AnomalyTypeModel(2, R.string.anomaly_type_call_not_established),
            new AnomalyTypeModel(3, R.string.anomaly_type_lost_call),
            new AnomalyTypeModel(4, R.string.anomaly_type_bad_call_audio),
            new AnomalyTypeModel(5, R.string.anomaly_type_another_anomaly_type)
    };

    private final AnomalyTypeModel[] ANOMALY_TYPES_INTERNET = {
            new AnomalyTypeModel(1, R.string.anomaly_type_no_data_access),
            new AnomalyTypeModel(2, R.string.anomaly_type_intermittent_access),
            new AnomalyTypeModel(3, R.string.anomaly_type_slow_connection),
            new AnomalyTypeModel(4, R.string.anomaly_type_message_not_sent),
            new AnomalyTypeModel(5, R.string.anomaly_type_another_anomaly_type)
    };

    private final AnomalyTypeModel[] ANOMALY_TYPES_MESSAGING = {
            new AnomalyTypeModel(1, R.string.anomaly_type_message_not_sent),
            new AnomalyTypeModel(2, R.string.anomaly_type_message_not_received),
            new AnomalyTypeModel(3, R.string.anomaly_type_message_slow_dispatch),
            new AnomalyTypeModel(4, R.string.anomaly_type_message_delayed_recepetion),
            new AnomalyTypeModel(5, R.string.anomaly_type_another_anomaly_type)
    };

    private final AnomalyTypeModel[] ANOMALY_TYPES_COVERAGE = {
            new AnomalyTypeModel(1, R.string.anomaly_type_no_indoor_coverage),
            new AnomalyTypeModel(2, R.string.anomaly_type_no_outdoor_coverage)
    };

    private final AnomalyTypeModel[] ANOMALY_TYPES_OTHER = {
            new AnomalyTypeModel(1, R.string.anomaly_type_another_anomaly_type)
    };

    public HashMap<AnomalyReport, AnomalyTypeModel[]> getTypeHashMap() {
        return typeHashMap;
    }

    public AnomalyTopics() {
        typeHashMap.put(AnomalyReport.VOICE, ANOMALY_TYPES_VOICE);
        typeHashMap.put(AnomalyReport.INTERNET, ANOMALY_TYPES_INTERNET);
        typeHashMap.put(AnomalyReport.MESSAGING, ANOMALY_TYPES_MESSAGING);
        typeHashMap.put(AnomalyReport.COVERAGE, ANOMALY_TYPES_COVERAGE);
        typeHashMap.put(AnomalyReport.OTHER, ANOMALY_TYPES_OTHER);
    }
}
