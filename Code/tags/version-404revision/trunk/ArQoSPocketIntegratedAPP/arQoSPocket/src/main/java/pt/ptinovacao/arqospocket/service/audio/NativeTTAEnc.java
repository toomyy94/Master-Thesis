package pt.ptinovacao.arqospocket.service.audio;

import android.util.Log;

/**
 * Created by x00881 on 16-01-2017.
 */

public class NativeTTAEnc {

    public static final String TAG = "NativeTTAEnc";
    private static boolean done;

    static {
        done = false;
        loadTTAEncoder();
    }

    public static synchronized void loadTTAEncoder() {
        synchronized (NativeTTAEnc.class) {
            if (!done) {
                System.loadLibrary("ttaenc");
                Log.d(TAG, "Loaded native library.");
                done = true;
            }
        }
    }

    public native int encodeWav(String path);

    static final class SingletonHolder {
        static final NativeTTAEnc INSTANCE;

        private SingletonHolder() {
        }

        static {
            INSTANCE = new NativeTTAEnc();
        }
    }
}
