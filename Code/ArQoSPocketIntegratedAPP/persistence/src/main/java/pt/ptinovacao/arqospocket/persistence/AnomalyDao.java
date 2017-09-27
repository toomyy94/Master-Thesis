package pt.ptinovacao.arqospocket.persistence;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import pt.ptinovacao.arqospocket.persistence.models.Anomaly;

/**
 * DAO for the anomalies.
 * <p>
 * Created by Emílio Simões on 28-04-2017.
 */
public class AnomalyDao extends BaseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnomalyDao.class);

    AnomalyDao(DatabaseHelper helper) {
        super(helper);
    }

    /**
     * Saves a new anomaly record to the database.
     *
     * @param anomaly the anomaly to save.
     * @return the created anomaly.
     */
    public Single<Anomaly> saveAnomaly(Anomaly anomaly) {
        if (anomaly.getReportDate() == null) {
            anomaly.setReportDate(Calendar.getInstance().getTime());
        }

        return getEntityStore().insert(anomaly);
    }

    /**
     * Flags an anomaly that has been reported to the management service.
     *
     * @param anomalyId the ID of the anomaly to flag.
     * @return the flagged anomaly, or an empty anomaly if the provided ID does not match an existing anomaly.
     */
    public Single<Anomaly> flagAnomalyAsReported(long anomalyId) {
        Anomaly anomaly = getEntityStore().findByKey(Anomaly.class, anomalyId).blockingGet();
        if (anomaly == null) {
            return Single.just(new Anomaly());
        }

        anomaly.setReported(true);
        return getEntityStore().update(anomaly);
    }

    /**
     * Reads all the anomalies in the database.
     *
     * @return a list with the existing anomalies.
     */
    public List<Anomaly> readAllAnomalies() {
        return getEntityStore().select(Anomaly.class).get().toList();
    }

    public Maybe<Anomaly> readAnomaliesById(long id) {
        return getEntityStore().findByKey(Anomaly.class, id);
    }

    public List<Anomaly> readAnomaliesByType(String type) {
        if (Strings.isNullOrEmpty(type)) {
            return readAllAnomalies();
        } else {
            return getEntityStore().select(Anomaly.class).where(Anomaly.REPORT_TYPE.eq(type)).get().toList();
        }
    }

    public int countAnomaliesByType(String type) {
        return getEntityStore().count(Anomaly.class).where(Anomaly.REPORT_TYPE.eq(type)).get().value();
    }

    /**
     * Gets the anomalies that have not yet been reported to the management service.
     *
     * @return a list with the found anomalies.
     */
    public List<Anomaly> readUnreportedAnomalies() {
        return getEntityStore().select(Anomaly.class).where(Anomaly.REPORTED.eq(false)).get().toList();
    }
}
