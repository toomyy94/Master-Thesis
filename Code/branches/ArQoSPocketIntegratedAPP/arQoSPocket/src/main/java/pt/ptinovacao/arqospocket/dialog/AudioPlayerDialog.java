package pt.ptinovacao.arqospocket.dialog;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import pt.ptinovacao.arqospocket.R;

import static android.media.AudioManager.MODE_IN_COMMUNICATION;
import static android.media.AudioManager.MODE_NORMAL;

/**
 * Created by x00881 on 16-01-2017.
 */

public class AudioPlayerDialog extends CustomDialog implements View.OnClickListener {
    private static final String TAG = "AudioPlayerDialog";

    private final static int resId = R.layout.dialog_audio_player;

    private static final int AUDIO_PROGRESS_UPDATE_TIME = 100;

    private SeekBar seekBar;
    private ImageView playButton, pauseButton;
    private MediaPlayer player;
    private TextView playbackTime, tvFilename;
    private Handler mProgressUpdateHandler = new Handler();

    private Runnable updateProgressRunnable = new Runnable() {

        public void run() {

            if (seekBar == null) {
                return;
            }

            if (mProgressUpdateHandler != null && player.isPlaying()) {
                seekBar.setProgress((int) player.getCurrentPosition());
                int currentTime = player.getCurrentPosition();
                updatePlaytime(currentTime);
                // repeat the process
                mProgressUpdateHandler.postDelayed(this, AUDIO_PROGRESS_UPDATE_TIME);
            } else {
                // DO NOT update UI if the player is paused
            }
        }
    };

    private Uri filePathUri;
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            // set UI when audio finished playing
            int currentPlayTime = 0;
            seekBar.setProgress((int) currentPlayTime);
            updatePlaytime(currentPlayTime);
            setPlayable();
        }
    };

    public AudioPlayerDialog(Context context, String filePath) {
        super(context, resId);


        this.filePathUri = Uri.parse(filePath);

        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.length());

        tvFilename = (TextView) findViewById(R.id.tv_filename);

        tvFilename.setText(fileName);

        seekBar = (SeekBar) findViewById(R.id.media_seekbar);
        playButton = (ImageView) findViewById(R.id.play);
        pauseButton = (ImageView) findViewById(R.id.pause);
        playbackTime = (TextView) findViewById(R.id.playback_time);

        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);


        //TODO handle audio focus
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(MODE_NORMAL);


        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {

            player.setDataSource(context, filePathUri);
            player.prepare();
            player.setOnCompletionListener(onCompletionListener);
            //player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updatePlaytime(0);
        initMediaSeekBar();


        Log.d(TAG, "filePath: " + filePathUri);


    }

    private void initMediaSeekBar() {

        if (seekBar == null) {
            return;
        }

        // update seekbar
        long finalTime = player.getDuration();
        seekBar.setMax((int) finalTime);

        seekBar.setProgress(0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());

                // if the audio is paused and seekbar is moved,
                // update the play time in the UI.
                updatePlaytime(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });
    }

    private void updatePlaytime(int currentTime) {

        if (playbackTime == null) {
            return;
        }

        /*if (currentTime < 0) {
            throw new IllegalArgumentException(ERROR_PLAYTIME_CURRENT_NEGATIVE);
        }*/

        StringBuilder playbackStr = new StringBuilder();

        // set the current time
        // its ok to show 00:00 in the UI
        playbackStr.append(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) currentTime), TimeUnit.MILLISECONDS.toSeconds((long) currentTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) currentTime))));

        playbackStr.append("/");

        // show total duration.
        long totalDuration = 0;

        if (player != null) {
            try {
                totalDuration = player.getDuration();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // set total time as the audio is being played
        if (totalDuration != 0) {
            playbackStr.append(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) totalDuration), TimeUnit.MILLISECONDS.toSeconds((long) totalDuration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) totalDuration))));
        } else {
            Log.w(TAG, "Something strage this audio track duration in zero");
        }

        playbackTime.setText(playbackStr);

        // DebugLog.i(currentTime + " / " + totalDuration);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                play();
                break;
            case R.id.pause:
                pause();
        }

    }

    public void play() {

        if (filePathUri == null) {
            throw new IllegalStateException("Uri cannot be null. Call init() before calling this method");
        }

        if (player == null) {
            throw new IllegalStateException("Call init() before calling this method");
        }

        if (player.isPlaying()) {
            return;
        }

        mProgressUpdateHandler.postDelayed(updateProgressRunnable, AUDIO_PROGRESS_UPDATE_TIME);

        // enable visibility of all UI controls.
        setViewsVisibility();

        player.start();

        setPausable();
    }

    public void pause() {

        if (player == null) {
            return;
        }

        if (player.isPlaying()) {
            player.pause();
            setPlayable();
        }
    }

    private void setViewsVisibility() {

        if (seekBar != null) {
            seekBar.setVisibility(View.VISIBLE);
        }

        if (playbackTime != null) {
            playbackTime.setVisibility(View.VISIBLE);
        }

        if (playbackTime != null) {
            playbackTime.setVisibility(View.VISIBLE);
        }

        if (playButton != null) {
            playButton.setVisibility(View.VISIBLE);
        }

        if (pauseButton != null) {
            pauseButton.setVisibility(View.VISIBLE);
        }
    }

    private void setPausable() {
        if (playButton != null) {
            playButton.setVisibility(View.GONE);
        }

        if (pauseButton != null) {
            pauseButton.setVisibility(View.VISIBLE);
        }
    }

    private void setPlayable() {
        if (playButton != null) {
            playButton.setVisibility(View.VISIBLE);
        }

        if (pauseButton != null) {
            pauseButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mProgressUpdateHandler.removeCallbacks(updateProgressRunnable);
        updateProgressRunnable = null;
        player.release();
        player = null;
        Log.d(TAG, "AUDIOPLAYA onStop");

    }


}
