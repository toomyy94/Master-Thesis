package pt.ptinovacao.arqospocket.persistence.models;

import android.os.Parcelable;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Index;
import io.requery.Persistable;
import io.requery.Table;

/**
 * Describes a scheduled event entity in the database.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
@Entity
@Table(name = "schedule_event")
abstract class BaseScheduledEvent extends BaseEvent implements Parcelable, Persistable {

    /**
     * Gets the test execution start date. This date will be used on boot to check if the test need to be rescheduled or
     * can be safely deleted from the database. Old tests are not maintained in the database.
     *
     * @return the test execution start date.
     */
    @Column(name = "start_date")
    abstract long getStartDate();

    /**
     * Sets the test execution start date. This date will be used on boot to check if the test need to be rescheduled or
     * can be safely deleted from the database. Old tests are not maintained in the database.
     *
     * @param startDate the test execution start date.
     */
    abstract void setStartDate(long startDate);

    /**
     * Gets the test ID. This value is used to update the test metadata with new values when the test is feed to the
     * scheduler.
     *
     * @return the test ID.
     */
    @Column(name = "test_id")
    @Index(value = "scheduled_test_id_index")
    abstract String getTestId();

    /**
     * Sets the test ID. This value is used to update the test metadata with new values when the test is feed to the
     * scheduler.
     *
     * @param testId the test ID to set.
     */
    abstract void setTestId(String testId);
}
