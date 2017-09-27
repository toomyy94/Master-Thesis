package pt.ptinovacao.arqospocket.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

import pt.ptinovacao.arqospocket.core.location.GeoLocationManager;
import pt.ptinovacao.arqospocket.core.producers.TestProducer;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.persistence.DatabaseApplication;

/**
 * Base core application to create an interface that can be accessed by the core module.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public abstract class CoreApplication extends DatabaseApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreApplication.class);

    private final Object locker = new Object();

    private final Stack<Class> activityStack = new Stack<>();

    private boolean isInBackground;

    private Application.ActivityLifecycleCallbacks lifecycleCallbacks = new CoreActivityLifecycleCallbacks();

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(lifecycleCallbacks);

        TestEngine.getInstance(this).bootstrap();
        SharedPreferencesManager.getInstance(this).init();
        GeoLocationManager.getInstance(this).startTracking();

        TestProducer.getBrokenTestInstance().executeOneShot();
    }

    @Override
    public void onTrimMemory(int level) {
        LOGGER.debug("Trim memory: {}", level);
        synchronized (locker) {
            isInBackground = level >= TRIM_MEMORY_UI_HIDDEN;
        }
        if (isInBackground && getOpenActivitiesCount() == 1) {
            GeoLocationManager.getInstance(CoreApplication.this).sleepTracking();
        }

        super.onTrimMemory(level);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        TestEngine.getInstance(this).cleanUp();
        GeoLocationManager.getInstance(this).stopTracking();
    }

    public OnDemandTestManager getOnDemandTestManager() {
        return OnDemandTestManager.getInstance(this);
    }

    private void incrementActivityCount(Class activity) {
        synchronized (locker) {
            activityStack.push(activity);
        }
    }

    private void decrementActivityCount(Class activity) {
        synchronized (locker) {
            if (activityStack.size() > 0) {
                boolean removed;
                do {
                    removed = activityStack.remove(activity);
                } while (removed);
            }
        }
    }

    public int getOpenActivitiesCount() {
        synchronized (locker) {
            return activityStack.size();
        }
    }

    private class CoreActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            incrementActivityCount(activity.getClass());
            LOGGER.debug("Activity created: [{}] {}", getOpenActivitiesCount(), activity.getClass().getName());
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            LOGGER.debug("Activity resumed: {}", activity.getClass().getName());
            if (isInBackground) {
                GeoLocationManager.getInstance(CoreApplication.this).wakeTracking();
            }
            synchronized (locker) {
                isInBackground = false;
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            LOGGER.debug("Activity paused: {}", activity.getClass().getName());
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            decrementActivityCount(activity.getClass());
            LOGGER.debug("Activity destroyed: [{}] {}", getOpenActivitiesCount(), activity.getClass().getName());
        }
    }
}
