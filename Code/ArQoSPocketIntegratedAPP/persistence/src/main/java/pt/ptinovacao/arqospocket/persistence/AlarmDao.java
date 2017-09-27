package pt.ptinovacao.arqospocket.persistence;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import pt.ptinovacao.arqospocket.persistence.models.Alarm;

/**
 * DAO for the alarms.
 * <p>
 * Created by Tomás Rodrigues on 06/09/2017.
 */

public class AlarmDao extends BaseDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmDao.class);


    AlarmDao(DatabaseHelper helper) {
        super(helper);
    }

    /**
     * Saves a new alarm record to the database.
     *
     * @param alarm the alarm to save.
     * @return the created alarm.
     */
    public Single<Alarm> saveAlarm(Alarm alarm) {
        if (alarm.getReportDate() == null) {
            alarm.setReportDate(Calendar.getInstance().getTime());
        }
        
        return getEntityStore().insert(alarm);
    }

    /**
     * Flags an alarm that has been reported to the management service.
     *
     * @param alarmId the ID of the alarm to flag.
     * @return the flagged alarm, or an empty alarm if the provided ID does not match an existing alarm.
     */
    public Single<Alarm> flagAlarmAsReported(long alarmId) {
        Alarm alarm = getEntityStore().findByKey(Alarm.class, alarmId).blockingGet();
        if (alarm == null) {
            return Single.just(new Alarm());
        }

        alarm.setReported(true);
        return getEntityStore().update(alarm);
    }

    /**
     * Reads all the alarms in the database.
     *
     * @return a list with the existing alarms.
     */
    public List<Alarm> readAllAlarms() {
        return getEntityStore().select(Alarm.class).get().toList();
    }

    public Maybe<Alarm> readAlarmsById(long id) {
        return getEntityStore().findByKey(Alarm.class, id);
    }

    public List<Alarm> readAlarmsByAlarmId(String alarm_id) {
        if (Strings.isNullOrEmpty(alarm_id)) {
            return readAllAlarms();
        } else {
            return getEntityStore().select(Alarm.class).where(Alarm.ALARM_ID.eq(alarm_id)).get().toList();
        }
    }

    /**
     * Gets the alarms that have not yet been reported to the management service.
     *
     * @return a list with the found alarms.
     */
    public List<Alarm> readUnreportedAlarms() {
        return getEntityStore().select(Alarm.class).where(Alarm.REPORTED.eq(false)).get().toList();
    }
}
