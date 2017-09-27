package pt.ptinovacao.arqospocket;

import android.app.IntentService;

/**
 * Base intent service for the application intent services.
 * <p>
 * Created by Emílio Simões on 13-04-2017.
 */
public abstract class BaseIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BaseIntentService(String name) {
        super(name);
    }

    public ArQosApplication getArqosApplication() {
        return (ArQosApplication) getApplication();
    }
}
