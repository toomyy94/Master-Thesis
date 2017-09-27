package pt.ptinovacao.arqospocket.persistence.models;

import android.os.Parcelable;

import io.requery.Column;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Superclass;

/**
 * Base class for the database events data.
 * <p>
 * Created by Emílio Simões on 12-04-2017.
 */
@Superclass
public abstract class BaseEvent implements Parcelable, Persistable {

    /**
     * Gets the database generated entity ID. This value will be generated when inserting in the database and cannot be
     * manually set.
     *
     * @return the database generated entity ID.
     */
    @Key
    @Generated
    @Column(name = "_id")
    public  abstract long getId();

    /**
     * Gets the test data in JSON format. This data will allow the test to be restored after the device has booted.
     *
     * @return the test date in JSON format.
     */
    @Column(name = "test_data")
    public abstract String getTestData();

    /**
     * Sets the test data in JSON format. This data will allow the test to be restored after the device has booted.
     *
     * @param testData the test data in JSON format to set.
     */
    abstract void setTestData(String testData);

    /**
     * Gets the entity hash. The entity hash is a unique signature of the stored test data that will allow the
     * application to check if a certain test is already persisted in the database.
     *
     * @return the entity hash.
     */
    @Column(name = "hash")
    abstract String getHash();

    /**
     * Sets the entity hash. The entity hash is a unique signature of the stored test data that will allow the
     * application to check if a certain test is already persisted in the database.
     *
     * @param hash the entity hash.
     */
    abstract void setHash(String hash);
}
