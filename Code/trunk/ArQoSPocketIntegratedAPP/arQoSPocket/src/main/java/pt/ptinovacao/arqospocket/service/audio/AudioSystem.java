package pt.ptinovacao.arqospocket.service.audio;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by x00881 on 10-01-2017.
 */

public class AudioSystem {
    private static final String TAG = AudioSystem.class.getSimpleName();

    public static final String DEVICE_IN_WIRED_HEADSET = "DEVICE_IN_WIRED_HEADSET";
    public static final String DEVICE_OUT_WIRED_HEADSET = "DEVICE_OUT_WIRED_HEADSET";
    public static final String DEVICE_STATE_AVAILABLE = "DEVICE_STATE_AVAILABLE";
    public static final String DEVICE_STATE_UNAVAILABLE = "DEVICE_STATE_UNAVAILABLE";

    public static void makeWiredHeadsetAvailable(boolean value) {
        Log.d(TAG, "makeWiredHeadsetAvailable() value:" + value);
        if (getConstantIntValue(DEVICE_IN_WIRED_HEADSET) != 0 ) {
            Log.d(TAG, "makeWiredHeadsetAvailable()   getAudioConstantIntValue(" + DEVICE_IN_WIRED_HEADSET + ")" + "found ");
            if (value &&
                    getDeviceConnectionState(getConstantIntValue(DEVICE_IN_WIRED_HEADSET) , "") ==
                            getConstantIntValue(DEVICE_STATE_UNAVAILABLE)) {

                    setDeviceConnectionState(getConstantIntValue(DEVICE_IN_WIRED_HEADSET),
                            getConstantIntValue(DEVICE_STATE_AVAILABLE), "", "");

            } else if (getDeviceConnectionState(getConstantIntValue(DEVICE_IN_WIRED_HEADSET), "") ==
                    getConstantIntValue(DEVICE_STATE_AVAILABLE)) {

                setDeviceConnectionState(getConstantIntValue(DEVICE_IN_WIRED_HEADSET),
                        getConstantIntValue(DEVICE_STATE_UNAVAILABLE), "", "");

            }

        }

        if (getConstantIntValue(DEVICE_OUT_WIRED_HEADSET) != 0 ) {
            Log.d(TAG, "makeWiredHeadsetAvailable()   getAudioConstantIntValue(" + DEVICE_OUT_WIRED_HEADSET + ")" + "found ");
            if (value &&
                    getDeviceConnectionState(getConstantIntValue(DEVICE_OUT_WIRED_HEADSET) , "") ==
                            getConstantIntValue(DEVICE_STATE_UNAVAILABLE)) {

                setDeviceConnectionState(getConstantIntValue(DEVICE_OUT_WIRED_HEADSET),
                        getConstantIntValue(DEVICE_STATE_AVAILABLE), "", "");

            } else if (getDeviceConnectionState(getConstantIntValue(DEVICE_OUT_WIRED_HEADSET), "") ==
                    getConstantIntValue(DEVICE_STATE_AVAILABLE)) {

                setDeviceConnectionState(getConstantIntValue(DEVICE_OUT_WIRED_HEADSET),
                        getConstantIntValue(DEVICE_STATE_UNAVAILABLE), "", "");

            }

        }

    }

    public static void setMicrophoneMute(Context context, boolean mute){
        Log.d(TAG, "setMicrophoneMute: " + mute);
        AudioManager audioManager = (AudioManager)
                context.getSystemService(Context.AUDIO_SERVICE);

        // get original mode
        int originalMode = audioManager.getMode();

        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setMicrophoneMute(mute);

        audioManager.setMode(originalMode);
    }

    private static Class<?> getAudioSystem() {
        Class<?> audioSystem = null;
        if (audioSystem == null) {
            try {
                audioSystem = ReflectionUtil.getClass("android.media.AudioSystem");
            } catch (Exception e) {
                Log.e(TAG, "getAudioSystem failed: " + e.toString());
            }
        }
        return audioSystem;
    }

    private static void setDeviceConnectionState(int device, int state, String address, String device_name) {
        try {
            Class[] clsArr = new Class[4];
            clsArr[0] = Integer.TYPE;
            clsArr[1] = Integer.TYPE;
            clsArr[2] = String.class;
            clsArr[3] = String.class;
            Method setDeviceConnectionState = ReflectionUtil.getMethod(getAudioSystem(), "setDeviceConnectionState", clsArr);
            Class audioSystem = getAudioSystem();
            Object[] objArr = new Object[4];
            objArr[0] = Integer.valueOf(device);
            objArr[1] = Integer.valueOf(state);
            objArr[2] = address;
            objArr[3] = device_name;
            ReflectionUtil.invoke(audioSystem, null, setDeviceConnectionState, objArr);
        } catch (Exception e) {
            Log.e(TAG, "setDeviceConnectionState failed: " + e.toString());
        }
    }

    private static int getDeviceConnectionState(int device, String address) {
        try {
            Method getDeviceConnectionState = ReflectionUtil.getMethod(getAudioSystem(), "getDeviceConnectionState", Integer.TYPE, String.class);
            return ((Integer) ReflectionUtil.invoke(getAudioSystem(), Integer.valueOf(0), getDeviceConnectionState, Integer.valueOf(device), address)).intValue();
        } catch (Exception e) {
            Log.e(TAG, "getDeviceConnectionState failed: " + e.toString());
            return 0;
        }
    }

    private static int getConstantIntValue(String constantName) {
        try {
            return ((Integer) ReflectionUtil.getDeclaredField(getAudioSystem(), constantName, Integer.valueOf(0), Integer.TYPE)).intValue();
        } catch (Exception e) {
            Log.e(TAG, "getConstantValue failed: " + e.toString());
            return 0;
        }
    }
}
