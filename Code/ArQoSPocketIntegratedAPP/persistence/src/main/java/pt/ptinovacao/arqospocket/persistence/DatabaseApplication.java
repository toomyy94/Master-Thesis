package pt.ptinovacao.arqospocket.persistence;

import android.app.Application;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

/**
 * Provides application wide support and access to the database.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public abstract class DatabaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Provides direct access to the store that provides access to the database.
     *
     * @return the database store.
     */
    public ReactiveEntityStore<Persistable> getData() {
        return DatabaseHelper.getInstance(this).getData();
    }

    /**
     * Provides access to the database helper. The database helper should implement all the database access logic.
     *
     * @return the database helper.
     */
    public DatabaseHelper getDatabaseHelper() {
        return DatabaseHelper.getInstance(this);
    }
}