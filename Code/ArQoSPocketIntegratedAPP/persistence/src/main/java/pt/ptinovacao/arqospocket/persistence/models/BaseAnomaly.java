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
 * Represents an anomaly reported by the user.
 * <p>
 * Created by Emílio Simões on 28-04-2017.
 */
@Entity
@Table(name = "anomaly")
abstract class BaseAnomaly implements Parcelable, Persistable {

    /**
     * Gets the database generated entity ID. This value will be generated when inserting in the database and cannot be
     * manually set.
     *
     * @return the database generated entity ID.
     */
    @Key
    @Generated
    @Column(name = "_id")
    abstract long getId();

    /**
     * Gets the anomaly report date.
     *
     * @return the anomaly report date.
     */
    @Column(name = "report_date")
    abstract Date getReportDate();

    /**
     * Sets the anomaly report date.
     *
     * @param reportDate the anomaly report date to set.
     */
    abstract void setReportDate(Date reportDate);

    /**
     * Gets the anomaly logo ID.
     *
     * @return the anomaly logo ID.
     */
    @Column(name = "logo_id")
    abstract String getLogoId();

    /**
     * Sets the anomaly logo ID.
     *
     * @param logoId the anomaly logo ID to set.
     */
    abstract void setLogoId(String logoId);

    /**
     * Gets the anomaly report type.
     *
     * @return the anomaly report type.
     */
    @Column(name = "report_type")
    abstract String getReportType();

    /**
     * Sets the anomaly report type.
     *
     * @param reportType the anomaly report type to set.
     */
    abstract void setReportType(String reportType);

    /**
     * Gets the anomaly report sub type.
     *
     * @return the anomaly report sub type.
     */
    @Column(name = "report_sub_type")
    abstract int getReportSubType();

    /**
     * Sets the anomaly report sub type.
     *
     * @param reportSubType the anomaly report sub type to set.
     */
    abstract void setReportSubType(int reportSubType);

    /**
     * Gets the coordinate latitude of the anomaly occurrence.
     *
     * @return coordinate latitude of the anomaly occurrence.
     */
    @Column(name = "latitude")
    abstract double getLatitude();

    /**
     * Sets the test execution coordinate latitude of the anomaly occurrence.
     *
     * @param latitude the coordinate longitude of the anomaly occurrence.
     */
    abstract void setLatitude(double latitude);

    /**
     * Gets the coordinate longitude of the anomaly occurrence.
     *
     * @return coordinate longitude of the anomaly occurrence.
     */
    @Column(name = "longitude")
    abstract double getLongitude();

    /**
     * Sets the test execution coordinate longitude of the anomaly occurrence.
     *
     * @param longitude the coordinate longitude of the anomaly occurrence.
     */
    abstract void setLongitude(double longitude);

    /**
     * Gets the user feedback about the anomaly.
     *
     * @return the user feedback about the anomaly.
     */
    @Column(name = "user_feedback")
    abstract String getUserFeedback();

    /**
     * Sets the user feedback about the anomaly.
     *
     * @param userFeedback the user feedback about the anomaly to set.
     */
    abstract void setUserFeedback(String userFeedback);

    /**
     * Gets if an anomaly has been reported to the management server. This value is used has a reference and indicates
     * if the test can be safely deleted or need to be reported.
     *
     * @return if an anomaly has been reported to the management server.
     */
    @Column(name = "reported")
    abstract boolean isReported();

    /**
     * Sets if an anomaly has been reported to the management server. This value is used has a reference and indicates
     * if the test can be safely deleted or need to be reported.
     *
     * @param reported if an anomaly has been reported to the management server.
     */
    abstract void setReported(boolean reported);
}
