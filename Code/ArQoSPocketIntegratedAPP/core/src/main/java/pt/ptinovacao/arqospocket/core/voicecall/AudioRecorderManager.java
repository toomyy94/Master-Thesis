package pt.ptinovacao.arqospocket.core.voicecall;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioSource;
import android.os.Environment;
import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import pt.ptinovacao.arqospocket.core.producers.JsonTestProducer;

/**
 * Created by pedro on 23/06/2017.
 */
public class AudioRecorderManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioRecorderManager.class);

    public static final String RECORDINGS_DIR = "recordings";

    public static final String WAV = ".wav";

    public static final String TTA = ".tta";

    private final static int[] rates = { 44100, 8000 };

    public static AudioRecorderManager getInstance() {
        AudioRecorderManager result;

        int i = 0;
        do {
            result = new AudioRecorderManager(AudioSource.MIC, rates[i], AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
        } while ((++i < rates.length) & !(result.getState() == AudioRecorderManager.State.INITIALIZING));

        if (result.getState() != AudioRecorderManager.State.INITIALIZING) {
            try {
                throw new Exception("The AudioRecorderManager doesn´t initialize");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * INITIALIZING : recorder is initializing; READY : recorder has been
     * initialized, recorder not yet started RECORDING : recording ERROR :
     * reconstruction needed STOPPED: reset needed
     */
    public enum State {
        INITIALIZING,
        READY,
        RECORDING,
        ERROR,
        STOPPED
    }

    // The interval in which the recorded samples are output to the file
    // Used only in uncompressed mode
    private static final int TIMER_INTERVAL = 120;

    // Recorder used for uncompressed recording
    private AudioRecord audioRecorder = null;

    // Recorder used for compressed recording
    private MediaRecorder mediaRecorder = null;

    // Stores current amplitude (only in uncompressed mode)
    private int cAmplitude = 0;

    // Output file path
    private String filePath = null;

    // Recorder state; see State
    private State state;

    // File writer (only in uncompressed mode)
    private RandomAccessFile randomAccessWriter;

    // Number of channels, sample rate, sample size(size in bits), buffer size,
    // audio source, sample size(see AudioFormat)
    private short nChannels;

    private int sRate;

    private short bSamples;

    private int bufferSize;

    private int aSource;

    private int aFormat;

    // Number of frames written to file on each output(only in uncompressed
    // mode)
    private int framePeriod;

    // Buffer for output(only in uncompressed mode)
    private byte[] buffer;

    // Number of bytes written to file after header(only in uncompressed mode)
    // after stop() is called, this size is written to the header/data chunk in
    // the wave file
    private int payloadSize;

    /**
     * Returns the state of the recorder in a RehearsalAudioRecord.State typed
     * object. Useful, as no exceptions are thrown.
     *
     * @return recorder state
     */
    public State getState() {
        return state;
    }

    /**
     * Method used for recording.
     */
    private AudioRecord.OnRecordPositionUpdateListener updateListener =
            new AudioRecord.OnRecordPositionUpdateListener() {
                public void onPeriodicNotification(AudioRecord recorder) {
                    audioRecorder.read(buffer, 0, buffer.length); // Fill buffer
                    try {
                        randomAccessWriter.write(buffer); // Write buffer to file
                        payloadSize += buffer.length;
                        if (bSamples == 16) {
                            for (int i = 0; i < buffer.length / 2; i++) { // 16bit
                                // sample size
                                short curSample = getShort(buffer[i * 2], buffer[i * 2 + 1]);
                                if (curSample > cAmplitude) { // Check amplitude
                                    cAmplitude = curSample;
                                }
                            }
                        } else { // 8bit sample size
                            for (int i = 0; i < buffer.length; i++) {
                                if (buffer[i] > cAmplitude) { // Check amplitude
                                    cAmplitude = buffer[i];
                                }
                            }
                        }
                    } catch (IOException e) {
                        LOGGER.error(AudioRecorderManager.class.getName(),
                                "Error occured in updateListener, recording is aborted");

                        // stop();
                    }
                }

                public void onMarkerReached(AudioRecord recorder) {
                    // NOT USED
                }
            };

    /**
     * Default constructor
     * <p>
     * Instantiates a new recorder, in case of compressed recording the
     * parameters can be left as 0. In case of errors, no exception is thrown,
     * but the state is set to ERROR
     */
    public AudioRecorderManager(int audioSource, int sampleRate, int channelConfig, int audioFormat) {
        LOGGER.debug("init");
        try {

            if (audioFormat == AudioFormat.ENCODING_PCM_16BIT) {
                bSamples = 16;
            } else {
                bSamples = 8;
            }

            if (channelConfig == AudioFormat.CHANNEL_IN_MONO) {
                nChannels = 1;
            } else {
                nChannels = 2;
            }

            aSource = audioSource;
            sRate = sampleRate;
            aFormat = audioFormat;

            framePeriod = sampleRate * TIMER_INTERVAL / 1000;
            bufferSize = framePeriod * 2 * bSamples * nChannels / 8;
            if (bufferSize <
                    AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)) { // Check to make sure
                // buffer size is not
                // smaller than the
                // smallest allowed one
                bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
                // Set frame period and timer interval accordingly
                framePeriod = bufferSize / (2 * bSamples * nChannels / 8);
                LOGGER.debug(AudioRecorderManager.class.getName(),
                        "Increasing buffer size to " + Integer.toString(bufferSize));
            }

            audioRecorder = new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, bufferSize);

            if (audioRecorder.getState() != AudioRecord.STATE_INITIALIZED) {
                throw new Exception("AudioRecord initialization failed");
            }
            audioRecorder.setRecordPositionUpdateListener(updateListener);
            audioRecorder.setPositionNotificationPeriod(framePeriod);

            cAmplitude = 0;
            filePath = null;
            state = State.INITIALIZING;
        } catch (Exception e) {

            if (e.getMessage() != null) {
                LOGGER.error(AudioRecorderManager.class.getName(), e.getMessage());
            } else {
                LOGGER.error(AudioRecorderManager.class.getName(),
                        "Unknown error occured while initializing recording");
            }
            state = State.ERROR;
        }
    }

    /**
     * Sets output file path, call directly after construction/reset.
     *
     * @param nameFile file path
     */
    public void setOutputFile(String nameFile) {
        try {
            if (state == State.INITIALIZING) {
                filePath = completedPath(nameFile);
                LOGGER.debug("path: " + filePath);
            }
        } catch (Exception e) {

            if (e.getMessage() != null) {
                LOGGER.error(AudioRecorderManager.class.getName(), e.getMessage());
            } else {
                LOGGER.error(AudioRecorderManager.class.getName(), "Unknown error occured while setting output path");
            }
            state = State.ERROR;
        }
    }

    public static String completedPath(String nameFile) {
        return checkDirectory() + File.separator + nameFile;
    }

    @NonNull
    public static String checkDirectory() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                JsonTestProducer.BASE_DIR;
        File recordingsDir = new File(path, RECORDINGS_DIR);
        if (!recordingsDir.exists()) {
            recordingsDir.mkdirs();
        }
        return path + File.separator + RECORDINGS_DIR;
    }

    /**
     * Prepares the recorder for recording, in case the recorder is not in the
     * INITIALIZING state and the file path was not set the recorder is set to
     * the ERROR state, which makes a reconstruction necessary. In case
     * uncompressed recording is toggled, the header of the wave file is
     * written. In case of an exception, the state is changed to ERROR
     */
    public void prepare() {
        LOGGER.debug("prepare");
        try {
            if (state == State.INITIALIZING) {

                if ((audioRecorder.getState() == AudioRecord.STATE_INITIALIZED) & (filePath != null)) {
                    // write file header

                    randomAccessWriter = new RandomAccessFile(filePath, "rw");

                    randomAccessWriter.setLength(0); // Set file length to
                    // 0, to prevent
                    // unexpected behavior
                    // in case the file
                    // already existed
                    randomAccessWriter.writeBytes("RIFF");
                    randomAccessWriter.writeInt(0); // Final file size not
                    // known yet, write 0
                    randomAccessWriter.writeBytes("WAVE");
                    randomAccessWriter.writeBytes("fmt ");
                    randomAccessWriter.writeInt(Integer.reverseBytes(16)); // Sub-chunk
                    // size,
                    // 16
                    // for
                    // PCM
                    randomAccessWriter.writeShort(Short.reverseBytes((short) 1)); // AudioFormat, 1 for
                    // PCM
                    randomAccessWriter.writeShort(Short.reverseBytes(nChannels));// Number of channels,
                    // 1 for mono, 2 for
                    // stereo
                    randomAccessWriter.writeInt(Integer.reverseBytes(sRate)); // Sample
                    // rate
                    randomAccessWriter.writeInt(Integer.reverseBytes(sRate * bSamples * nChannels / 8)); // Byte rate,
                    // SampleRate*NumberOfChannels*BitsPerSample/8
                    randomAccessWriter.writeShort(Short.reverseBytes((short) (nChannels * bSamples / 8))); // Block
                    // align,
                    // NumberOfChannels*BitsPerSample/8
                    randomAccessWriter.writeShort(Short.reverseBytes(bSamples)); // Bits per sample
                    randomAccessWriter.writeBytes("data");
                    randomAccessWriter.writeInt(0); // Data chunk size not
                    // known yet, write 0

                    buffer = new byte[framePeriod * bSamples / 8 * nChannels];
                    state = State.READY;

                } else {
                    mediaRecorder.prepare();
                    state = State.READY;
                }
            } else {
                LOGGER.error(AudioRecorderManager.class.getName(), "prepare() method called on illegal state");

                release();
                state = State.ERROR;
            }
        } catch (Exception e) {

            if (e.getMessage() != null) {
                LOGGER.error(AudioRecorderManager.class.getName(), e.getMessage());
            } else {
                LOGGER.error(AudioRecorderManager.class.getName(), "Unknown error occured in prepare()");
            }
            state = State.ERROR;
        }
    }

    /**
     * Releases the resources associated with this class, and removes the unnecessary files, when necessary
     *
     * @return path of file created
     */
    public String release() {
        LOGGER.debug("release");
        if (state == State.RECORDING) {
            stop();
            return filePath;
        } else {
            if ((state == State.READY)) {
                try {
                    randomAccessWriter.close(); // Remove prepared file
                } catch (IOException e) {

                    LOGGER.error(AudioRecorderManager.class.getName(),
                            "I/O exception occured while closing output file");
                }
                (new File(filePath)).delete();
            }
        }

        if (audioRecorder != null) {
            audioRecorder.release();
        }

        return null;
    }

    /**
     * Resets the recorder to the INITIALIZING state, as if it was just created.
     * In case the class was in RECORDING state, the recording is stopped. In
     * case of exceptions the class is set to the ERROR state.
     *
     * @return success
     */
    public boolean reset() {
        LOGGER.debug("reset");
        try {
            if (state != State.ERROR) {
                release();
                filePath = null; // Reset file path
                cAmplitude = 0; // Reset amplitude

                audioRecorder = new AudioRecord(aSource, sRate, nChannels + 1, aFormat, bufferSize);

                state = State.INITIALIZING;
                return true;
            }
        } catch (Exception e) {

            LOGGER.error(AudioRecorderManager.class.getName(), e.getMessage());
            state = State.ERROR;
        }
        return false;
    }

    /**
     * Starts the recording, and sets the state to RECORDING. Call after
     * prepare().
     *
     * @return success
     */
    public boolean start() {
        LOGGER.debug("start");
        if (state == State.READY) {
            payloadSize = 0;
            audioRecorder.startRecording();
            audioRecorder.read(buffer, 0, buffer.length);
            state = State.RECORDING;
            return true;
        } else {
            LOGGER.error(AudioRecorderManager.class.getName(), "start() called on illegal state");
            state = State.ERROR;
        }
        return false;
    }

    /**
     * Stops the recording, and sets the state to STOPPED. In case of further
     * usage, a reset is needed. Also finalizes the wave file in case of
     * uncompressed recording.
     *
     * @return success
     */
    public boolean stop() {
        LOGGER.debug("stop");
        if (state == State.RECORDING) {
            audioRecorder.stop();

            try {
                randomAccessWriter.seek(4); // Write size to RIFF header
                randomAccessWriter.writeInt(Integer.reverseBytes(36 + payloadSize));

                randomAccessWriter.seek(40); // Write size to Subchunk2Size
                // field
                randomAccessWriter.writeInt(Integer.reverseBytes(payloadSize));

                randomAccessWriter.close();
                return true;
            } catch (IOException e) {

                LOGGER.error(AudioRecorderManager.class.getName(), "I/O exception occured while closing output file");
                state = State.ERROR;
            }

            state = State.STOPPED;
        } else {
            LOGGER.error(AudioRecorderManager.class.getName(), "stop() called on illegal state");
            state = State.ERROR;
        }
        return false;
    }

    /**
     * Converts a byte[2] to a short, in LITTLE_ENDIAN format
     *
     * @param argB1
     * @param argB2
     * @return
     */
    private short getShort(byte argB1, byte argB2) {
        return (short) (argB1 | (argB2 << 8));
    }

}