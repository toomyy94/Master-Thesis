package pt.ptinovacao.arqospocket.core.voicecall.audio;

import android.media.MediaPlayer;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.voicecall.NativeEncoder;

/**
 * Created by pedro on 03/07/2017.
 */

public class MediaPlayerHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaPlayerHelper.class);

    private static final String PATH = AudioRecorderManager.checkDirectory() + File.separator;

    private static MediaPlayerHelper ourInstance;

    private static CoreApplication coreApplication;

    private MediaPlayer mediaPlayer;

    private String pathFileAtual;

    public static MediaPlayerHelper getInstance(CoreApplication coreApplication) {
        if (ourInstance == null) {
            ourInstance = new MediaPlayerHelper(coreApplication);
        }
        return ourInstance;
    }

    private MediaPlayerHelper(CoreApplication coreApplication) {
        MediaPlayerHelper.coreApplication = coreApplication;
    }

    public boolean startAudio(String fileName, final UpdateUI updateUI) {
        stopPlaying();

        pathFileAtual = PATH + fileName;

        final File locationFileOrigin = new File(pathFileAtual);

        if (!locationFileOrigin.isFile()) {
            return false;
        }

        NativeEncoder.INSTANCE.encodeWav(PATH + fileName, 2);

        final String replaceExtension = fileName.replace(AudioRecorderManager.TTA, AudioRecorderManager.WAV);
        final String path = PATH + replaceExtension;

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(PATH + replaceExtension);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (updateUI != null) {
                        updateUI.update();
                    }
                    new File(path).delete();
                    LOGGER.debug("End of audio!");
                }
            });
            mediaPlayer.start();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }

        return true;
    }

    public boolean isStart() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    public boolean stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

            if (!Strings.isNullOrEmpty(pathFileAtual)) {
                pathFileAtual = pathFileAtual.replace(AudioRecorderManager.TTA, AudioRecorderManager.WAV);
                new File(pathFileAtual).delete();
            }

            LOGGER.debug("stop");
            return true;
        }
        return false;
    }

    public interface UpdateUI {

        void update();
    }

}
