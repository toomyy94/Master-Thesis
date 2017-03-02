package pt.ptinovacao.arqospocket.service.audio;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.media.AudioRecord.STATE_INITIALIZED;

public class AudioRecorderSingleton {
    private static final String TAG = AudioRecorderSingleton.class.getName();

    public static final int RECORDER_SWB_SAMPLE_RATE = 44100;
    public static final int RECORDER_SD_SAMPLE_RATE = 8000;
    public static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    public static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    public static final int RECORDER_BITS_PER_SAMPLE = 16;
    public static final int RECORDER_BUFFER_ELEMENTS = 3;

    private static AudioRecorderSingleton instance;

    private static List<AudioChunkReceiver> callbacks;
    private AudioRecord audioRecorder;

    /*
    * 0 – SD (8 KHz)
    * 1 – SWB (48 KHz)
    * */
    private int audioType = -1;

    private int recBufSize;
    private boolean isRecording;
    private boolean isRecorderInitialized;

    public static AudioRecorderSingleton getInstance() {
        Log.d(TAG, "getInstance");
        if (instance == null) {
            Log.d(TAG, "getInstance :: creating new instance!");
            instance = new AudioRecorderSingleton();
        }

        return instance;
    }

    private AudioRecorderSingleton() {
        Log.d(TAG, "AudioRecorderSingleton");
        callbacks = new ArrayList<AudioChunkReceiver>();
        isRecorderInitialized = false;
        audioType = -1;
        audioRecorder = null;
    }

    public synchronized boolean init(int audioType){
        Log.d(TAG, "init :: audioType: " + audioType);
        Log.d(TAG, "init :: isRecorderInitialized: " + isRecorderInitialized);

        if (isRecorderInitialized){
            //TODO change this to audioType > this.audioType and handle downsampling when disreded audiType < recording audioType
            if (audioType != this.audioType){
                Log.d(TAG, "init :: unsupported audioType: " + audioType + " current is: " + this.audioType);
                return false;
            }

        } else {
            this.audioType = audioType;

            recBufSize = AudioRecord.getMinBufferSize(
                    this.audioType == 1 ? RECORDER_SWB_SAMPLE_RATE : RECORDER_SD_SAMPLE_RATE,
                    RECORDER_CHANNELS,
                    RECORDER_AUDIO_ENCODING);

            Log.d(TAG, "AudioRecorderSingleton :: recBufSize: " + recBufSize);

            audioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    this.audioType == 1 ? RECORDER_SWB_SAMPLE_RATE : RECORDER_SD_SAMPLE_RATE,
                    RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, recBufSize * RECORDER_BUFFER_ELEMENTS);

            isRecorderInitialized = true;
        }

        return true;
    }

    public synchronized boolean record(AudioChunkReceiver listener){
        if (isRecorderInitialized
                && audioRecorder != null && audioRecorder.getState() == STATE_INITIALIZED ){
            addListener(listener);
            return true;
        }
        return false;
    }



    public int getAudioType() {
        return audioType;
    }

    private synchronized void addListener(AudioChunkReceiver audioChunkReceiver) {
        Log.d(TAG, "addListener");
        callbacks.add(audioChunkReceiver);
        if (callbacks.size() == 1) {
            Log.d(TAG, "addListener :: first listener :: going to start the recording");
            startRecording();
        }
    }

    public synchronized void removeListener(AudioChunkReceiver audioChunkReceiver) {
        Log.d(TAG, "removeListener");
        callbacks.remove(audioChunkReceiver);
        if (callbacks.size() == 0) {
            Log.d(TAG, "removeListener :: last listener :: going to stop the recording");
            stopRecording();
        }
    }

    private void startRecording() {


        new Thread() {
            short[] buffer = new short[recBufSize];

            @Override
            public void run() {
                //TODO ver se vale a pena usar um bytbuffer
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                audioRecorder.startRecording();
                isRecording = true;
                while (isRecording) {
                    int readSize = audioRecorder.read(buffer, 0, recBufSize);
                    // receiver.onChunkReceived(buffer, readSize);
                    byte byteBuffer[] = short2byte(buffer);

                    Iterator it = callbacks.iterator();
                    while (it.hasNext()) {
                        try {
                            ((AudioChunkReceiver) it.next()).onChunkReceived(byteBuffer, byteBuffer.length);
                        } catch (Exception e) {
                            Log.e(TAG, "AudioRecorder: One of the mic listeners has a problem.");
                        }
                    }
                }
                Log.d(TAG, "exiting recording thread");
                //audioRecorder.stop();
            }
        }.start();

    }



    private byte[] short2byte(short[] data) {
        int shortArrsize = data.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (data[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (data[i] >> 8);
            data[i] = 0;
        }
        return bytes;

    }

    //ver se passo isto tudo para a tread
    private void stopRecording() {
        Log.d(TAG, "stopRecording");
        isRecording = false;
        if (audioRecorder != null) {
            try {
                Log.d(TAG, "releasing resources");
                audioRecorder.stop();
                audioRecorder.release();
                audioRecorder = null;
                instance = null;
            } catch (IllegalStateException e) {
                Log.d(TAG, e.getMessage());
            } catch (RuntimeException e2 ) {
                Log.d(TAG, e2.getMessage());
            }
        }
    }

    public interface AudioChunkReceiver {
        void onChunkReceived(byte[] chunk, int chunkLength);
    }
}
