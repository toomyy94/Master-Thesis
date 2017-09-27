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
 * Represents an radiolog reported by the user.
 * <p>
 * Created by Tomás Rodrigues on 02/05/2017.
 */
@Entity
@Table(name = "radiologs")
public abstract class BaseRadiolog implements Parcelable, Persistable {

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
     * Gets the radiolog report date.
     *
     * @return the radiolog report date.
     */
    @Column(name = "report_date")
    abstract Date getReportDate();

    /**
     * Sets the radiolog report date.
     *
     * @param reportDate the radiolog report date to set.
     */
    abstract void setReportDate(Date reportDate);

    /**
     * Gets the radiolog report type.
     *
     * @return the radiolog report type.
     */
    @Column(name = "report_type")
    abstract String getReportType();

    /**
     * Sets the radiolog report type.
     *
     * @param reportType the radiolog report type to set.
     */
    abstract void setReportType(String reportType);

    /**
     * Gets the coordinate latitude of the radiolog occurrence.
     *
     * @return coordinate latitude of the radiolog occurrence.
     */
    @Column(name = "latitude")
    abstract double getLatitude();

    /**
     * Sets the test execution coordinate latitude of the radiolog occurrence.
     *
     * @param latitude the coordinate longitude of the radiolog occurrence.
     */
    abstract void setLatitude(double latitude);

    /**
     * Gets the coordinate longitude of the radiolog occurrence.
     *
     * @return coordinate longitude of the radiolog occurrence.
     */
    @Column(name = "longitude")
    abstract double getLongitude();

    /**
     * Sets the test execution coordinate longitude of the radiolog occurrence.
     *
     * @param longitude the coordinate longitude of the radiolog occurrence.
     */
    abstract void setLongitude(double longitude);

    /**
     * Gets the user feedback about the radiolog.
     *
     * @return the user feedback about the radiolog.
     */
    @Column(name = "user_feedback")
    abstract String getUserFeedback();

    /**
     * Sets the user feedback about the radiolog.
     *
     * @param userFeedback the user feedback about the radiolog to set.
     */
    abstract void setUserFeedback(String userFeedback);

    /**
     * Gets the user feedback about the radiolog.
     *
     * @return the user feedback about the radiolog.
     */
    @Column(name = "radiolog_content")
    abstract String getRadiologContent();

    /**
     * Sets the user feedback about the radiolog.
     *
     * @param userFeedback the user feedback about the radiolog to set.
     */
    abstract void setRadiologContent(String userFeedback);

    /**
     * Gets if an radiolog has been reported to the management server. This value is used has a reference and indicates
     * if the test can be safely deleted or need to be reported.
     *
     * @return if an radiolog has been reported to the management server.
     */
    @Column(name = "reported")
    abstract boolean isReported();

    /**
     * Sets if an radiolog has been reported to the management server. This value is used has a reference and indicates
     * if the test can be safely deleted or need to be reported.
     *
     * @param reported if an radiolog has been reported to the management server.
     */
    abstract void setReported(boolean reported);

    @Column(name = "is_upload")
    abstract boolean isUpload();

    abstract void setUpload(boolean isUpload);

    @Column(name = "is_notification_sent")
    abstract boolean isNotificationSent();

    abstract void setNotificationSent(boolean isNotificationSent);

    @Column(name = "name_file")
    abstract String getNameFile();
}
