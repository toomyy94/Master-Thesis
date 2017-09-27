package pt.ptinovacao.arqospocket.core.voicecall;

import android.content.Context;
import android.media.AudioManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by pedro on 12/04/2017.
 */
public class AudioSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioSystem.class);

    public static final String DEVICE_IN_WIRED_HEADSET = "DEVICE_IN_WIRED_HEADSET";

    public static final String DEVICE_OUT_WIRED_HEADSET = "DEVICE_OUT_WIRED_HEADSET";

    public static final String DEVICE_STATE_AVAILABLE = "DEVICE_STATE_AVAILABLE";

    public static final String DEVICE_STATE_UNAVAILABLE = "DEVICE_STATE_UNAVAILABLE";

    public static void makeWiredHeadsetAvailable(boolean value) {
        LOGGER.debug("makeWiredHeadsetAvailable() value:" + value);
        if (getConstantIntValue(DEVICE_IN_WIRED_HEADSET) != 0) {
            LOGGER.debug("makeWiredHeadsetAvailable()   getAudioConstantIntValue(" + DEVICE_IN_WIRED_HEADSET + ")" +
                    "found ");
            if (value && getDeviceConnectionState(getConstantIntValue(DEVICE_IN_WIRED_HEADSET), "") ==
                    getConstantIntValue(DEVICE_STATE_UNAVAILABLE)) {

                setDeviceConnectionState(getConstantIntValue(DEVICE_IN_WIRED_HEADSET),
                        getConstantIntValue(DEVICE_STATE_AVAILABLE), "", "");

            } else if (getDeviceConnectionState(getConstantIntValue(DEVICE_IN_WIRED_HEADSET), "") ==
                    getConstantIntValue(DEVICE_STATE_AVAILABLE)) {

                setDeviceConnectionState(getConstantIntValue(DEVICE_IN_WIRED_HEADSET),
                        getConstantIntValue(DEVICE_STATE_UNAVAILABLE), "", "");
            }
        }

        if (getConstantIntValue(DEVICE_OUT_WIRED_HEADSET) != 0) {
            LOGGER.debug("makeWiredHeadsetAvailable()   getAudioConstantIntValue(" + DEVICE_OUT_WIRED_HEADSET + ")" +
                    "found ");
            if (value && getDeviceConnectionState(getConstantIntValue(DEVICE_OUT_WIRED_HEADSET), "") ==
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

    public static void setMicrophoneMute(Context context, boolean mute) {
        LOGGER.debug("setMicrophoneMute: " + mute);
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

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
                LOGGER.error("getAudioSystem failed: " + e.toString());
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
            Method setDeviceConnectionState =
                    ReflectionUtil.getMethod(getAudioSystem(), "setDeviceConnectionState", clsArr);
            Class audioSystem = getAudioSystem();
            Object[] objArr = new Object[4];
            objArr[0] = Integer.valueOf(device);
            objArr[1] = Integer.valueOf(state);
            objArr[2] = address;
            objArr[3] = device_name;
            ReflectionUtil.invoke(audioSystem, null, setDeviceConnectionState, objArr);
        } catch (Exception e) {
            LOGGER.error("setDeviceConnectionState failed: " + e.toString());
        }
    }

    private static int getDeviceConnectionState(int device, String address) {
        try {
            Method getDeviceConnectionState =
                    ReflectionUtil.getMethod(getAudioSystem(), "getDeviceConnectionState", Integer.TYPE, String.class);
            return ((Integer) ReflectionUtil
                    .invoke(getAudioSystem(), Integer.valueOf(0), getDeviceConnectionState, Integer.valueOf(device),
                            address)).intValue();
        } catch (Exception e) {
            LOGGER.error("getDeviceConnectionState failed: " + e.toString());
            return 0;
        }
    }

    private static int getConstantIntValue(String constantName) {
        try {
            return ((Integer) ReflectionUtil
                    .getDeclaredField(getAudioSystem(), constantName, Integer.valueOf(0), Integer.TYPE)).intValue();
        } catch (Exception e) {
            LOGGER.error("getConstantValue failed: " + e.toString());
            return 0;
        }
    }
}
