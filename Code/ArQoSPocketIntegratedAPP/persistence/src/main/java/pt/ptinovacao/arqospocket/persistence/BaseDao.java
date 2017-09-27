package pt.ptinovacao.arqospocket.persistence;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

/**
 * Base Data Access Object for the persistence framework.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
abstract class BaseDao {

    private final ReactiveEntityStore<Persistable> entityStore;

    BaseDao(DatabaseHelper helper) {
        entityStore = helper.getData();
    }

    ReactiveEntityStore<Persistable> getEntityStore() {
        return entityStore;
    }
}
