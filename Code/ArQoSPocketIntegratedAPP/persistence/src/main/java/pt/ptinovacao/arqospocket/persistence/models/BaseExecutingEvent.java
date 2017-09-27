package pt.ptinovacao.arqospocket.persistence.models;

import android.os.Parcelable;

import io.requery.Column;
import io.requery.Entity;
import io.requery.OneToMany;
import io.requery.Persistable;
import io.requery.Table;
import io.requery.query.MutableResult;

/**
 * Describes an executing event entity in the database.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
@Entity
@Table(name = "executing_event")
abstract class BaseExecutingEvent extends BaseEvent implements Parcelable, Persistable {

    /**
     * Gets if the test is still executing. This value will be used to check which tests are executing when displaying
     * the running tests list.
     *
     * @return if the test is still executing.
     */
    @Column(name = "executing")
    abstract boolean isExecuting();

    /**
     * Sets if the test is still executing. This value will be used to check which tests are executing when displaying
     * the running tests list.
     *
     * @param executing if the test is still executing.
     */
    abstract void setExecuting(boolean executing);

    /**
     * Gets if the test has failed. This value will be used to check which tests have failed when displaying the tests
     * history list.
     *
     * @return if the test has failed.
     */
    @Column(name = "test_failed")
    abstract boolean isTestFailed();

    /**
     * Sets if the test has failed. This value will be used to check which tests have failed when displaying the tests
     * history list.
     *
     * @param testFailed if the test has failed.
     */
    abstract void setTestFailed(boolean testFailed);

    /**
     * Gets if a test has been reported to the management server. This value is used has a reference and indicates if
     * the test can be safely deleted or need to be reported.
     *
     * @return if a test has been reported to the management server.
     */
    @Column(name = "reported")
    abstract boolean isReported();

    /**
     * Sets if a test has been reported to the management server. This value is used has a reference and indicates if
     * the test can be safely deleted or need to be reported.
     *
     * @param reported if a test has been reported to the management server.
     */
    abstract void setReported(boolean reported);

    /**
     * Gets the test execution result data in JSON format. This data will allow the result to be restored at any time
     * and displayed in the application or sent to the management server.
     *
     * @return the test execution result data in JSON format.
     */
    @Column(name = "result_data")
    abstract String getResultData();

    /**
     * Sets the test execution result data in JSON format. This data will allow the result to be restored at any time
     * and displayed in the application or sent to the management server.
     *
     * @param resultData the test execution result data in JSON format.
     */
    abstract void setResultData(String resultData);

    /**
     * Gets the {@link BaseTaskEvent} associated with the test.
     *
     * @return the {@link BaseTaskEvent} associated with the test.
     */

    @OneToMany
    abstract MutableResult<BaseTaskEvent> getTaskEvents();

    /**
     * Gets the test start execution date. This date is the date on which the test is supposed to start if no delay
     * occurs.
     *
     * @return the test start execution date.
     */
    @Column(name = "start_date")
    abstract long getStartDate();

    /**
     * Sets the test start execution date. This date is the date on which the test is supposed to start if no delay
     * occurs.
     *
     * @param startDate the test start execution date to set.
     */
    abstract void setStartDate(long startDate);

    /**
     * Gets the test estimated end date. This date assumes a scenario where all the task have timed out and is an
     * indicator of when the test will end in a worst case scenario. This is not the real test end date. To get the real
     * test end date check the test result data.
     *
     * @return the test estimated end date.
     */
    @Column(name = "end_date")
    abstract long getEndDate();

    /**
     * Sets the test estimated end date. This date assumes a scenario where all the task have timed out and is an
     * indicator of when the test will end in a worst case scenario. This is not the real test end date. To get the real
     * test end date check the test result data.
     *
     * @param endDate the test estimated end date to set.
     */
    abstract void setEndDate(long endDate);

    @Column(name = "test_id")
    abstract String getTestId();

    abstract void setTestId(String testId);

    @Column(name = "executing_test")
    abstract boolean isExecutingTest();

    abstract void setExecutingTest(boolean executing);
}
