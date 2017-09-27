package pt.ptinovacao.arqospocket.persistence.models;

import android.os.Parcelable;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;
import io.requery.Table;

/**
 * Contains .
 * data associated with a single task execution
 * Created by pedro on 27/04/2017.
 */
@Entity
@Table(name = "task_event")
public abstract class BaseTaskEvent implements Parcelable, Persistable {

    /**
     * Gets the database generated entity ID. This value will be generated when inserting in the database and cannot be
     * manually set.
     *
     * @return the database generated entity ID.
     */
    @Key
    @Generated
    @Column(name = "_id")
    public abstract long getId();

    /**
     * Gets the coordinate latitude of the tests.
     *
     * @return coordinate latitude in double.
     */
    @Column(name = "latitude")
    public abstract double getLatitude();

    /**
     * Sets the test execution coordinate latitude in double.
     *
     * @param latitude the coordinate longitude.
     */
    public abstract void setLatitude(double latitude);

    /**
     * Gets the coordinate longitude of the tests.
     *
     * @return coordinate longitude in double.
     */
    @Column(name = "longitude")
    public abstract double getLongitude();

    /**
     * Sets the test execution coordinate longitude in double.
     *
     * @param longitude the coordinate longitude.
     */
    public abstract void setLongitude(double longitude);

    /**
     * Gets the connection technologies of the tests.
     *
     * @return int of connection technologies, that.
     */
    @Column(name = "connection_technology")
    public abstract int getConnectionTechnology();

    /**
     * Sets the connection technologies of the tests, that will be converted to an enum ConnectionTechnology.
     *
     * @param connectionTechnology an int of enum ConnectionTechnology.
     */
    public abstract void setConnectionTechnology(int connectionTechnology);

    /**
     * Gets the associated test event.
     *
     * @return the associated test event.
     */
    @ManyToOne
    abstract BaseExecutingEvent getEvent();

    /**
     * Sets the associated test event.
     *
     * @param event the associated test event to set.
     */
    abstract void setEvent(BaseExecutingEvent event);

    @Column(name = "start_date")
    public abstract long getStartDate();

    abstract void setStartDate(long startDate);

    @Column(name = "end_date")
    public abstract long getEndDate();

    abstract void setEndDate(long endDate);

    @Column(name = "task_data_id")
    public abstract String getTaskDataId();

    public abstract void setTaskDataId(String taskDataId);

    @Column(name = "is_upload")
    abstract boolean isUpload();

    abstract void setUpload(boolean isUpload);

    @Column(name = "is_notification_sent")
    abstract boolean isNotificationSent();

    abstract void setNotificationSent(boolean isNotificationSent);

    @Column(name = "name_file")
    abstract String getNameFile();
}
