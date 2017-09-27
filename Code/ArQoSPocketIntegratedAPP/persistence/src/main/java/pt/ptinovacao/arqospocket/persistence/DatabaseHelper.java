package pt.ptinovacao.arqospocket.persistence;

import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import pt.ptinovacao.arqospocket.persistence.models.Models;

/**
 * Helper class to manage database initializations.
 * <p>
 * Created by Emílio Simões on 28-03-2017.
 */
public class DatabaseHelper {

    private static DatabaseHelper instance;

    private final DatabaseApplication application;

    private ReactiveEntityStore<Persistable> dataStore = null;

    private DatabaseHelper(DatabaseApplication application) {
        this.application = application;
    }

    /**
     * Creates a singleton instance of this class.
     *
     * @param application the application to be used has the context for the database creation and access.
     * @return the instance.
     */
    synchronized static DatabaseHelper getInstance(DatabaseApplication application) {
        if (instance == null) {
            instance = new DatabaseHelper(application);
        }
        return instance;
    }

    /**
     * Gets a reference to the entity data store. The data store is an abstraction of the native
     * Android database API. The data store can be used to persist, read, update and delete entities
     * from the database.
     *
     * @return a reference to the requery entity store.
     */
    public synchronized ReactiveEntityStore<Persistable> getData() {
        if (dataStore == null) {
            DatabaseSource source = new DatabaseSource(application, Models.DEFAULT, 1);
            Configuration configuration = source.getConfiguration();
            dataStore = ReactiveSupport.toReactiveStore(new EntityDataStore<Persistable>(configuration));
        }
        return dataStore;
    }

    /**
     * Creates a new instance of the {@link ScheduledEventDao}.
     *
     * @return the created {@link ScheduledEventDao} instance.
     */
    public ScheduledEventDao createScheduledEventDao() {
        return new ScheduledEventDao(this);
    }

    /**
     * Creates a new instance of the {@link OnDemandEventDao}.
     *
     * @return the created {@link OnDemandEventDao} instance.
     */
    public OnDemandEventDao createOnDemandEventDao() {
        return new OnDemandEventDao(this);
    }

    /**
     * Creates a new instance of the {@link ExecutingEventDao}.
     *
     * @return the created {@link ExecutingEventDao} instance.
     */
    public ExecutingEventDao createExecutingEventDao() {
        return new ExecutingEventDao(this);
    }

    /**
     * Creates a new instance of the {@link RadiologDao}.
     *
     * @return the created {@link RadiologDao} instance.
     */
    public RadiologDao createRadiologDao() {
        return new RadiologDao(this);
    }

    /**
     * Creates a new instance of the {@link AnomalyDao}.
     *
     * @return the created {@link AnomalyDao} instance.
     */
    public AnomalyDao createAnomalyDao() {
        return new AnomalyDao(this);
    }

    /**
     * Creates a new instance of the {@link AlarmDao}.
     *
     * @return the created {@link AlarmDao} instance.
     */
    public AlarmDao createAlarmDao() {
        return new AlarmDao(this);
    }

    /**
     * Creates a new instance of the {@link CDRDao}.
     *
     * @return the created {@link CDRDao} instance.
     */
    public CDRDao createCDRDao() {
        return new CDRDao(this);
    }
}
