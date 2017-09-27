package pt.ptinovacao.arqospocket.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.requery.reactivex.ReactiveResult;
import pt.ptinovacao.arqospocket.persistence.models.OnDemandEvent;
import pt.ptinovacao.arqospocket.persistence.utils.Hashing;

/**
 * Data Access Object for the {@link OnDemandEvent}.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
public class OnDemandEventDao extends BaseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnDemandEventDao.class);

    OnDemandEventDao(DatabaseHelper helper) {
        super(helper);
    }

    /**
     * Saves a new on demand test to the database. It will validate if the test already exists in the database.
     * Duplicates will be ignored.
     *
     * @param onDemandEvent the on demand event to persist.
     * @return a {@link Single<OnDemandEvent>} that will resolve the inserted test. If the test is ignored the original
     * test is returned.
     */
    public Single<OnDemandEvent> saveOnDemandEvent(OnDemandEvent onDemandEvent) {
        String signature = Hashing.md5(onDemandEvent.getTestData());

        OnDemandEvent existingUnchanged =
                getEntityStore().select(OnDemandEvent.class).where(OnDemandEvent.HASH.eq(signature)).get()
                        .firstOrNull();
        if (existingUnchanged != null) {
            LOGGER.debug("On demand test already persisted and unchanged, ignoring [test_id = {}]",
                    existingUnchanged.getTestId());
            return Single.just(onDemandEvent);
        }

        OnDemandEvent existingChanged =
                getEntityStore().select(OnDemandEvent.class).where(OnDemandEvent.TEST_ID.eq(onDemandEvent.getTestId()))
                        .get().firstOrNull();
        if (existingChanged != null) {
            LOGGER.debug("On demand test already persisted but changed, updating [test_id = {}]",
                    existingChanged.getTestId());

            existingChanged.setExecuting(onDemandEvent.isExecuting());
            existingChanged.setHash(signature);
            existingChanged.setTestData(onDemandEvent.getTestData());
            existingChanged.setTestId(onDemandEvent.getTestId());

            return getEntityStore().update(existingChanged);
        }

        LOGGER.debug("New on demand test, inserting [test_id = {}]", onDemandEvent.getTestId());

        onDemandEvent.setHash(signature);
        onDemandEvent.setExecuting(false);

        return getEntityStore().insert(onDemandEvent);
    }

    /**
     * Gets the on demand executable tests as an observable. This method will return all events.
     *
     * @return the on demand executable tests as an observable.
     */
    public Observable<OnDemandEvent> readAllOnDemandEvents() {
        return getEntityStore().select(OnDemandEvent.class).get().observable();
    }

    /**
     * Gets the on demand executable tests as an observable. This method will only return the events that are
     * currently executing.
     *
     * @return the on demand executable tests as an observable.
     */
    public List<OnDemandEvent> listAllExecutingOnDemandEvents() {
        return getEntityStore().select(OnDemandEvent.class).where(OnDemandEvent.EXECUTING.eq(true)).get().toList();
    }

    public List<OnDemandEvent> listAllOnDemandEvents() {
        return getEntityStore().select(OnDemandEvent.class).get().toList();
    }

    /**
     * Gets the on demand executable tests as an observable. This method will only return the events that are not
     * currently executing.
     *
     * @return the on demand executable tests as an observable.
     */
    public Observable<OnDemandEvent> readInactiveOnDemandEvents() {
        return getEntityStore().select(OnDemandEvent.class).where(OnDemandEvent.EXECUTING.eq(false)).get().observable();
    }

    /**
     * Gets a count of the available on demand events that are not executing.
     *
     * @return the on demand events count.
     */
    public int countAllOnDemandEvents() {
        return getEntityStore().count(OnDemandEvent.class).get().value();
    }

    /**
     * Gets a count of the available on demand events that are not executing.
     *
     * @return the on demand events count.
     */
    public int countInactiveOnDemandEvents() {
        return getEntityStore().count(OnDemandEvent.class).where(OnDemandEvent.EXECUTING.eq(false)).get().value();
    }

    /**
     * Gets a inactive test by it's ID.
     *
     * @param testId the ID of the test to read.
     * @return the read test, if any.
     */
    public ReactiveResult<OnDemandEvent> readInactiveEvent(long testId) {
        return getEntityStore().select(OnDemandEvent.class).where(OnDemandEvent.EXECUTING.eq(false))
                .and(OnDemandEvent.ID.eq(testId)).get();
    }

    /**
     * Gets a on demand test by it's ID.
     *
     * @param testId the ID of the test to read.
     * @return the read test, if any.
     */
    public Maybe<OnDemandEvent> readOnDemandEvent(long testId) {
        return getEntityStore().findByKey(OnDemandEvent.class, testId);
    }

    /**
     * Changes a on demand event execution status.
     *
     * @param testId the ID of the on demand test.
     * @return the updated entity.
     */
    public Single<OnDemandEvent> startOnDemandEventExecuting(long testId) {
        return setOnDemandEventExecuting(testId, true);
    }

    /**
     * Changes a on demand event execution status.
     *
     * @param testId the ID of the on demand test.
     * @param executing if the test is executing.
     * @return the updated entity.
     */
    public Single<OnDemandEvent> setOnDemandEventExecuting(long testId, boolean executing) {
        OnDemandEvent existingChanged =
                getEntityStore().select(OnDemandEvent.class).where(OnDemandEvent.ID.eq(testId)).get().firstOrNull();
        if (existingChanged != null) {
            existingChanged.setExecuting(executing);
            return getEntityStore().update(existingChanged);
        }
        return Single.just(new OnDemandEvent());
    }

    /**
     * Changes a on demand event execution status.
     *
     * @param testId the ID of the on demand test.
     * @return the updated entity.
     */
    public Single<OnDemandEvent> endOnDemandEventExecuting(String testId) {
        OnDemandEvent existingChanged =
                getEntityStore().select(OnDemandEvent.class).where(OnDemandEvent.TEST_ID.eq(testId)).get()
                        .firstOrNull();
        if (existingChanged != null) {
            existingChanged.setExecuting(false);
            return getEntityStore().update(existingChanged);
        }
        return Single.just(new OnDemandEvent());
    }

    /**
     * Deletes a test by it's test ID.
     *
     * @param testId teh ID of the test to delete.
     * @return if any test was deleted.
     */
    public boolean deleteTestByTestId(String testId) {
        return getEntityStore().delete(OnDemandEvent.class).where(OnDemandEvent.TEST_ID.eq(testId)).get().value() > 0;
    }
}
