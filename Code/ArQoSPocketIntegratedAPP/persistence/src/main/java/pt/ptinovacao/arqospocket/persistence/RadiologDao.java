package pt.ptinovacao.arqospocket.persistence;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import pt.ptinovacao.arqospocket.persistence.models.Anomaly;
import pt.ptinovacao.arqospocket.persistence.models.Radiolog;

/**
 * DAO for the radiologs.
 * <p>
 * Created by Tomás Rodrigues on 02/05/2017.
 */

public class RadiologDao extends BaseDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(RadiologDao.class);


    RadiologDao(DatabaseHelper helper) {
        super(helper);
    }

    /**
     * Saves a new radiolog record to the database.
     *
     * @param radiolog the radiolog to save.
     * @return the created radiolog.
     */
    public Single<Radiolog> saveRadiolog(Radiolog radiolog) {
        if (radiolog.getReportDate() == null) {
            radiolog.setReportDate(Calendar.getInstance().getTime());
        }
        
        return getEntityStore().insert(radiolog);
    }

    /**
     * Flags an radiolog that has been reported to the management service.
     *
     * @param radiologId the ID of the radiolog to flag.
     * @return the flagged radiolog, or an empty radiolog if the provided ID does not match an existing radiolog.
     */
    public Single<Radiolog> flagRadiologAsReported(long radiologId) {
        Radiolog radiolog = getEntityStore().findByKey(Radiolog.class, radiologId).blockingGet();
        if (radiolog == null) {
            return Single.just(new Radiolog());
        }

        radiolog.setReported(true);
        return getEntityStore().update(radiolog);
    }

    /**
     * Reads all the radiologs in the database.
     *
     * @return a list with the existing radiologs.
     */
    public List<Radiolog> readAllRadiologs() {
        return getEntityStore().select(Radiolog.class).get().toList();
    }

    public Maybe<Radiolog> readRadiologsById(long id) {
        return getEntityStore().findByKey(Radiolog.class, id);
    }

    public List<Radiolog> readRadiologsByType(String type) {
        if (Strings.isNullOrEmpty(type)) {
            return readAllRadiologs();
        } else {
            return getEntityStore().select(Radiolog.class).where(Radiolog.REPORT_TYPE.eq(type)).get().toList();
        }
    }

    public int countRadiologsByType(String type) {
        return getEntityStore().count(Radiolog.class).where(Radiolog.REPORT_TYPE.eq(type)).get().value();
    }

    /**
     * Gets the radiologs that have not yet been reported to the management service.
     *
     * @return a list with the found radiologs.
     */
    public List<Radiolog> readUnreportedRadiologs() {
        return getEntityStore().select(Radiolog.class).where(Radiolog.REPORTED.eq(false)).get().toList();
    }
}
