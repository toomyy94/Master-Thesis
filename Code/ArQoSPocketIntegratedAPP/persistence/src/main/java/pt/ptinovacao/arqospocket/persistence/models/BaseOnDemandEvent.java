package pt.ptinovacao.arqospocket.persistence.models;

import android.os.Parcelable;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Index;
import io.requery.Persistable;
import io.requery.Table;

/**
 * Describes an on demand event entity in the database.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
@Entity
@Table(name = "on_demand_event")
abstract class BaseOnDemandEvent extends BaseEvent implements Parcelable, Persistable {

    /**
     * Gets if the test is still executing. This value will be used to prevent the test to appear in the user request
     * tests lists while the test is executing.
     *
     * @return if the test is still executing.
     */
    @Column(name = "executing")
    abstract boolean isExecuting();

    /**
     * Sets if the test is still executing. This value will be used to prevent the test to appear in the user request
     * tests lists while the test is executing.
     *
     * @param executing if the test is still executing.
     */
    abstract void setExecuting(boolean executing);

    /**
     * Gets the test ID. This value is used to update the test metadata with new values when the test is feed to the
     * scheduler.
     *
     * @return the test ID.
     */
    @Column(name = "test_id")
    @Index(value = "test_id_index")
    abstract String getTestId();

    /**
     * Sets the test ID. This value is used to update the test metadata with new values when the test is feed to the
     * scheduler.
     *
     * @param testId the test ID to set.
     */
    abstract void setTestId(String testId);
}
