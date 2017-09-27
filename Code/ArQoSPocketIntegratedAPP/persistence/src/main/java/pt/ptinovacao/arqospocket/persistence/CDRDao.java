package pt.ptinovacao.arqospocket.persistence;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

import pt.ptinovacao.arqospocket.persistence.models.CDR;


/**
 * DAO for the cdrs.
 * <p>
 * Created by Tomás Rodrigues on 06/09/2017.
 */

public class CDRDao extends BaseDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CDRDao.class);

    CDRDao(DatabaseHelper helper) {
        super(helper);
    }

    /**
     * Saves a new cdr record to the database.
     *
     * @param cdr the cdr to save.
     * @return the created cdr.
     */
    public Single<CDR> saveCDR(CDR cdr) {
        if (cdr.getCDReportDate() == null) {
            cdr.setCDReportDate(Calendar.getInstance().getTime());
        }

        return getEntityStore().insert(cdr);
    }

    /**
     * Flags an cdr that has been reported to the management service.
     *
     * @param cdrId the ID of the cdr to flag.
     * @return the flagged cdr, or an empty cdr if the provided ID does not match an existing cdr.
     */
    public Single<CDR> flagCDRAsReported(long cdrId) {
        CDR cdr = getEntityStore().findByKey(CDR.class, cdrId).blockingGet();
        if (cdr == null) {
            return Single.just(new CDR());
        }

        cdr.setReported(true);
        return getEntityStore().update(cdr);
    }

    /**
     * Reads all the cdrs in the database.
     *
     * @return a list with the existing cdrs.
     */
    public List<CDR> readAllCDRs() {
        return getEntityStore().select(CDR.class).get().toList();
    }

    public Maybe<CDR> readCDRsById(long id) {
        return getEntityStore().findByKey(CDR.class, id);
    }

//    public List<CDR> readCDRsByCDRId(String cdr_id) {
//        if (Strings.isNullOrEmpty(cdr_id)) {
//            return readAllCDRs();
//        } else {
//            return getEntityStore().select(CDR.class).where(CDR.ID.eq(cdr_id)).get().toList();
//        }
//    }

    /**
     * Gets the cdrs that have not yet been reported to the management service.
     *
     * @return a list with the found cdrs.
     */
    public List<CDR> readUnreportedCDRs() {
        return getEntityStore().select(CDR.class).where(CDR.REPORTED.eq(false)).get().toList();
    }
}
