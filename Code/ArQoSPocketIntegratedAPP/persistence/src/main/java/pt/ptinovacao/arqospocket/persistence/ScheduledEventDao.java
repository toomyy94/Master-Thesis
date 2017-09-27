package pt.ptinovacao.arqospocket.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.requery.reactivex.ReactiveResult;
import io.requery.reactivex.ReactiveScalar;
import pt.ptinovacao.arqospocket.persistence.models.ScheduledEvent;
import pt.ptinovacao.arqospocket.persistence.utils.ExecutedOperation;
import pt.ptinovacao.arqospocket.persistence.utils.Hashing;
import pt.ptinovacao.arqospocket.persistence.utils.ScheduledEventResult;

/**
 * Data Access Object for the {@link ScheduledEvent}.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
public class ScheduledEventDao extends BaseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledEventDao.class);

    ScheduledEventDao(DatabaseHelper helper) {
        super(helper);
    }

    /**
     * Saves a new scheduled test to the database. It will validate if the test has expired or if already exists in the
     * database. Expired tests and duplicates will be ignored.
     *
     * @param scheduledEvent the scheduled event to persist.
     * @return a {@link Single<ScheduledEvent>} that will resolve the inserted test. If the test is ignored the original
     * test is returned.
     */
    public Single<ScheduledEventResult> saveScheduledEvent(final ScheduledEvent scheduledEvent) {
        long now = Calendar.getInstance().getTimeInMillis();
        if (scheduledEvent.getStartDate() < now) {
            LOGGER.debug("Test is too old, ignoring");
            return Single.just(new ScheduledEventResult(scheduledEvent, ExecutedOperation.NONE));
        }

        String signature = Hashing.md5(scheduledEvent.getTestData());

        ScheduledEvent existingUnchanged =
                getEntityStore().select(ScheduledEvent.class).where(ScheduledEvent.HASH.eq(signature)).get()
                        .firstOrNull();
        if (existingUnchanged != null) {
            LOGGER.debug("Scheduled test already persisted, ignoring [test_id = {}]", existingUnchanged.getTestId());
            return Single.just(new ScheduledEventResult(scheduledEvent, ExecutedOperation.NONE));
        }

        ScheduledEvent existingChanged = getEntityStore().select(ScheduledEvent.class)
                .where(ScheduledEvent.TEST_ID.eq(scheduledEvent.getTestId())).get().firstOrNull();
        if (existingChanged != null) {
            LOGGER.debug("Scheduled already persisted but changed, updating [test_id = {}]",
                    existingChanged.getTestId());

            existingChanged.setStartDate(scheduledEvent.getStartDate());
            existingChanged.setHash(signature);
            existingChanged.setTestData(scheduledEvent.getTestData());
            existingChanged.setTestId(scheduledEvent.getTestId());

            return getEntityStore().update(existingChanged).map(new Function<ScheduledEvent, ScheduledEventResult>() {
                @Override
                public ScheduledEventResult apply(@NonNull ScheduledEvent scheduledEvent) throws Exception {
                    return new ScheduledEventResult(scheduledEvent, ExecutedOperation.UPDATE);
                }
            });
        }

        LOGGER.debug("New scheduled test, inserting [test_id = {}]", scheduledEvent.getTestId());

        scheduledEvent.setHash(signature);
        return getEntityStore().insert(scheduledEvent).map(new Function<ScheduledEvent, ScheduledEventResult>() {
            @Override
            public ScheduledEventResult apply(@NonNull ScheduledEvent scheduledEvent) throws Exception {
                return new ScheduledEventResult(scheduledEvent, ExecutedOperation.INSERT);
            }
        });
    }

    /**
     * Gets a scheduled test by it's ID.
     *
     * @param testId the ID of the test to read.
     * @return the read test, if any.
     */
    public Maybe<ScheduledEvent> readScheduledEvent(long testId) {
        return getEntityStore().findByKey(ScheduledEvent.class, testId);
    }

    public boolean isExistTest(String testId) {
        return getEntityStore().count(ScheduledEvent.class).where(ScheduledEvent.TEST_ID.eq(testId)).get().value() > 0;
    }

    /**
     * Reads all the scheduled event in the database.
     *
     * @return the existing scheduled events.
     */
    public ReactiveResult<ScheduledEvent> readAllScheduledEvents() {
        return getEntityStore().select(ScheduledEvent.class).get();
    }

    public List<ScheduledEvent> readAllScheduledEventsBetweenDates(long startDate, long endDate) {
        return getEntityStore().select(ScheduledEvent.class)
                .where(ScheduledEvent.START_DATE.between(startDate, endDate)).get().toList();
    }

    public Observable<ScheduledEvent> readAllScheduledEvent() {
        return getEntityStore().select(ScheduledEvent.class).get().observable();
    }

    public Observable<ScheduledEvent> readAllScheduledEventForTests() {
        return getEntityStore().select(ScheduledEvent.class).where(ScheduledEvent.START_DATE.gt(System.currentTimeMillis())).get().observable();
    }

    public List<ScheduledEvent> readAllScheduledEventList() {
        return getEntityStore().select(ScheduledEvent.class).get().toList();
    }

    public int countAllScheduledEvent() {
        return getEntityStore().count(ScheduledEvent.class).get().value();
    }

    public boolean isAnyScheduledTestBeforeDate(long endDate) {
        return getEntityStore().count(ScheduledEvent.class).where(ScheduledEvent.START_DATE.lessThan(endDate)).get()
                .value() > 0;
    }

    /**
     * Deletes all the events whose ID matches on of the provided ID's.
     *
     * @param ids the ID's to delete.
     */
    public ReactiveScalar<Integer> deleteExpiredEvents(List<Long> ids) {
        return getEntityStore().delete(ScheduledEvent.class).where(ScheduledEvent.ID.in(ids)).get();
    }

    /**
     * Tries to find a scheduled event based on the event data.
     *
     * @param data the data of the event to find.
     */
    public ScheduledEvent findScheduledEventFromData(String data) {
        String signature = Hashing.md5(data);

        return getEntityStore().select(ScheduledEvent.class).where(ScheduledEvent.HASH.eq(signature)).get()
                .firstOrNull();
    }

    /**
     * Deletes a tests that has already been executed.
     *
     * @param testId the ID of the test to delete.
     */
    public ReactiveScalar<Integer> deleteExecutedEvent(long testId) {
        return getEntityStore().delete(ScheduledEvent.class).where(ScheduledEvent.ID.eq(testId)).get();
    }

    /**
     * Deletes a test by it's test ID.
     *
     * @param testId teh ID of the test to delete.
     */
    public Maybe<Long> deleteTestByTestId(final String testId) {
        return Maybe.create(new MaybeOnSubscribe<Long>() {
            @Override
            public void subscribe(MaybeEmitter<Long> e) throws Exception {
                ScheduledEvent scheduledEvent =
                        getEntityStore().select(ScheduledEvent.class).where(ScheduledEvent.TEST_ID.eq(testId)).get()
                                .firstOrNull();
                if (scheduledEvent != null) {
                    long id = scheduledEvent.getId();
                    getEntityStore().delete(ScheduledEvent.class).where(ScheduledEvent.ID.eq(id)).get().value();
                    e.onSuccess(id);
                } else {
                    e.onComplete();
                }
            }
        });
    }
}
