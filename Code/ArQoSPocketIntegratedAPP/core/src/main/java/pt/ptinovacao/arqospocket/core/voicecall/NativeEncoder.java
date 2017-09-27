package pt.ptinovacao.arqospocket.core.voicecall;

/**
 * Uses the native library to provide encoding methods for audio files.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public final class NativeEncoder {

    static {
        System.loadLibrary("ttaenc");
    }

    public static NativeEncoder INSTANCE = new NativeEncoder();

    private NativeEncoder() {
    }

    /**
     * Encodes a WAV file into TTA format.
     *
     * @param path the path of the audio file to encode.
     * @param decOrEnc 1 - encode or 2 decode
     * @return the encoding result.
     */
    public native int encodeWav(String path, int decOrEnc);
}
