package pt.ptinovacao.arqospocket.service.audio;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static pt.ptinovacao.arqospocket.service.audio.AudioRecorderSingleton.RECORDER_AUDIO_ENCODING;
import static pt.ptinovacao.arqospocket.service.audio.AudioRecorderSingleton.RECORDER_BITS_PER_SAMPLE;
import static pt.ptinovacao.arqospocket.service.audio.AudioRecorderSingleton.RECORDER_CHANNELS;
import static pt.ptinovacao.arqospocket.service.audio.AudioRecorderSingleton.RECORDER_SD_SAMPLE_RATE;
import static pt.ptinovacao.arqospocket.service.audio.AudioRecorderSingleton.RECORDER_SWB_SAMPLE_RATE;
import static pt.ptinovacao.arqospocket.service.audio.AudioSystem.makeWiredHeadsetAvailable;
import static pt.ptinovacao.arqospocket.service.audio.AudioSystem.setMicrophoneMute;
import static pt.ptinovacao.arqospocket.service.utils.Constants.BASE_DIR;
import static pt.ptinovacao.arqospocket.service.utils.Constants.RECORDINGS_DIR;
import static pt.ptinovacao.arqospocket.service.utils.Constants.TESTS_DIR;

/**
 * Created by x00881 on 03-01-2017.
 */

public class AudioRecorderHelper {
    private static final String TAG = AudioRecorderHelper.class.getSimpleName();

    public static final int RECORDING_STATUS_OK = 0;
    public static final int RECORDING_STATUS_NOK = -1;
    public static final int RECORDING_STATUS_UNSUPPORTED_AUDIO_TYPE = -2;


    private static final int MIN_RECORDING_TIME_SEC = 20;


    private String fileName;
    private int recordingDuration;
    private int taskTimeout;
    private int audioType;

    private AudioRecorderHelperListener callback;

    private FileOutputStream os = null;
    private File audioFile;
    private boolean recordStarted;
    private Context context;

    private AudioRecorderSingleton audioRecorderSingleton;
    private AudioRecorderSingleton.AudioChunkReceiver audioChunkReceiver = new AudioRecorderSingleton.AudioChunkReceiver() {
        @Override
        public void onChunkReceived(byte[] chunk, int chunkLength) {
            try {
                os.write(chunk, 0, chunkLength);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    public AudioRecorderHelper(Context context, String fileName, int recordingDuration, int taskTimeout, int audioType, AudioRecorderHelperListener callback) {
        Log.i(TAG, "RecordAudio AudioRecorderHelper :: in :: fileName: " + fileName + " recordingDuration: " + recordingDuration + " audioType: " + audioType);
        this.context = context;
        this.fileName = fileName;
        this.taskTimeout = taskTimeout;
        this.recordingDuration = recordingDuration;
        Log.i(TAG, "recordingDuration: " + recordingDuration);
        if (this.recordingDuration  < MIN_RECORDING_TIME_SEC) {
            Log.i(TAG, "audioRecordingTime < " + MIN_RECORDING_TIME_SEC);
            this.recordingDuration = Math.max(taskTimeout - 5, MIN_RECORDING_TIME_SEC);

        }

        this.audioType = audioType;
        this.callback = callback;
    }

    public boolean startRecording(){
        try {
            //TODO Handle the file and directory nicely
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator
                    + BASE_DIR;
            File recordingsDir = new File(path, RECORDINGS_DIR);
            if (!recordingsDir.exists()) {
                recordingsDir.mkdirs();
            }

            audioFile = new File(recordingsDir, fileName + ".pcm");

            try {
                os = new FileOutputStream(audioFile.getAbsolutePath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            audioRecorderSingleton = AudioRecorderSingleton.getInstance();

            if (audioRecorderSingleton.init(audioType)){
                Log.d(TAG, "startRecording");
                recordStarted = true;
                makeWiredHeadsetAvailable(true);
                audioRecorderSingleton.record(audioChunkReceiver);
                setMicrophoneMute(context, true);

                if (recordingDuration > 0) {
                    //TODO use a OnRecordPositionUpdateListener to control da size
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                // we add 500 to account for little delays - when added the listener remove this
                                sleep((recordingDuration * 1000) + 500);
                                stopRecording();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                }
            } else {
                Log.d(TAG, "Unsupported audioType");
                callback.onRecordingEnded(RECORDING_STATUS_UNSUPPORTED_AUDIO_TYPE, null);
                return false;
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void stopRecording(){
        if (recordStarted) {
            recordStarted = false;
            audioRecorderSingleton.removeListener(audioChunkReceiver);
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            setMicrophoneMute(context, false);
            makeWiredHeadsetAvailable(false);

            new Thread(new Runnable() {
                public void run() {
                    String destFileName = audioFile.getAbsolutePath().replace(".pcm", ".wav");
                    try {
                        copyWaveFile(audioFile.getAbsolutePath(), destFileName);
                        NativeTTAEnc.SingletonHolder.INSTANCE.encodeWav(destFileName);
                        audioFile.delete();
                        if (callback != null) {
                            callback.onRecordingEnded(RECORDING_STATUS_OK, destFileName);
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "Error converting file: " + e.getMessage());
                        if (callback != null) {
                            callback.onRecordingEnded(RECORDING_STATUS_NOK, null);
                        }
                    }


                }
            }, "AudioRecorderThread").start();


        }


    }

    public boolean isRecording(){
        return recordStarted;
    }

    private void copyWaveFile(String inFilename, String outFilename){


        //TODO handle downsample and enconding to tta
        int sampleRate = (audioType == 1 ? RECORDER_SWB_SAMPLE_RATE : RECORDER_SD_SAMPLE_RATE);

        int bufferSize = AudioRecord.getMinBufferSize(sampleRate, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = sampleRate;
        int channels = 1;
        long byteRate = RECORDER_BITS_PER_SAMPLE * sampleRate * channels / 8;

        byte[] data = new byte[bufferSize];

        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            Log.d(TAG, "File size: " + totalDataLen + " outFilename: " + outFilename);

            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);

            while(in.read(data) != -1){
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate) throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = RECORDER_BITS_PER_SAMPLE; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
    }

    public interface AudioRecorderHelperListener {
        public void onRecordingEnded(int status, String filename);
    }
}
