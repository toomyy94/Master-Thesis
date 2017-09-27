package pt.ptinovacao.arqospocket.persistence.models;

import android.os.Parcelable;

import java.util.Date;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Table;

/**
 * Represents a generated alarm.
 * <p>
 * Created by Tomás Rodrigues on 02/05/2017.
 */
@Entity
@Table(name = "alarm")
public abstract class BaseAlarm implements Parcelable, Persistable {

    /**
     * Gets the database generated entity ID. This value will be generated when inserting in the database and cannot be
     * manually set.
     *
     * @return the database generated entity ID.
     */
    @Key
    @Generated
    @Column(name = "_id")
    public  abstract long getId();

    /**
     * Gets the alarm report date.
     *
     * @return the alarm report date.
     */
    @Column(name = "report_date")
    abstract Date getReportDate();

    /**
     * Sets the alarm report date.
     *
     * @param reportDate the alarm report date to set.
     */
    abstract void setReportDate(Date reportDate);

    /**
     * Gets the alarm origin_id.
     *
     * @return the alarm origin_id.
     */
    @Column(name = "origin_id")
    abstract Integer getOriginId();

    /**
     * Sets the alarm origin_id.
     *
     * @param origin_id the alarm origin_id to set.
     */
    abstract void setOriginId(Integer origin_id);

    /**
     * Gets the coordinate latitude of the alarm occurrence.
     *
     * @return coordinate latitude of the alarm occurrence.
     */
    @Column(name = "gps_location")
    abstract String getGpsLocation();

    /**
     * Sets the test execution coordinate gps of the alarm occurrence.
     *
     * @param gps_location the coordinate gps of the alarm occurrence.
     */
    abstract void setGpsLocation(String gps_location);

    /**
     * Gets the cellid about the alarm.
     *
     * @return the cellid about the alarm.
     */
    @Column(name = "cell_id")
    abstract String getCellId();

    /**
     * Sets the cellid about the alarm.
     *
     * @param cell_id the cellid about the alarm to set.
     */
    abstract void setCellId(String cell_id);

    /**
     * Gets the alarm_content about the alarm.
     *
     * @return the alarm_content about the alarm.
     */
    @Column(name = "alarm_content")
    abstract String getAlarmContent();

    /**
     * Sets the alarm_content about the alarm.
     *
     * @param userFeedback the alarm_content about the alarm to set.
     */
    abstract void setAlarmContent(String alarm_content);

    /**
     * Gets the start_end about the alarm.
     *
     * @return the start_end about the alarm.
     */
    @Column(name = "start_end")
    abstract String getStartEnd();

    /**
     * Sets the start_end about the alarm.
     *
     * @param start_end the user start_end about the alarm to set.
     */
    abstract void setStartEnd(String start_end);

    /**
     * Gets the alarm_id about the alarm.
     *
     * @return the alarm_id about the alarm.
     */
    @Column(name = "alarm_id")
    abstract String getAlarmId();

    /**
     * Sets the alarm_id about the alarm.
     *
     * @param alarm_id the alarm_id about the alarm to set.
     */
    abstract void setAlarmId(String alarm_id);

    /**
     * Gets the alarm_id about the alarm.
     *
     * @return the alarm_id about the alarm.
     */
    @Column(name = "additional_info")
    abstract String getAdditionalInfo();

    /**
     * Sets the additional_info about the alarm.
     *
     * @param additional_info the additional_info about the alarm to set.
     */
    abstract void setAdditionalInfo(String additional_info);

    /**
     * Gets the user iccid about the alarm.
     *
     * @return the iccid about the alarm.
     */
    @Column(name = "iccid")
    abstract String getIccid();

    /**
     * Sets the user iccid about the alarm.
     *
     * @param iccid the iccid about the alarm to set.
     */
    abstract void setIccid(String iccid);

    /**
     * Gets if an alarm has been reported to the management server. This value is used has a reference and indicates
     * if the test can be safely deleted or need to be reported.
     *
     * @return if an alarm has been reported to the management server.
     */
    @Column(name = "reported")
    abstract boolean isReported();

    /**
     * Sets if an alarm has been reported to the management server. This value is used has a reference and indicates
     * if the test can be safely deleted or need to be reported.
     *
     * @param reported if an alarm has been reported to the management server.
     */
    abstract void setReported(boolean reported);

    @Column(name = "is_notification_sent")
    abstract boolean isNotificationSent();

    abstract void setNotificationSent(boolean isNotificationSent);

    @Column(name = "name_file")
    abstract String getNameFile();
}
