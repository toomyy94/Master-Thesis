package pt.ptinovacao.arqospocket.core.tests.tasks;

import android.content.Context;
import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.cdrs.CDRUtils;
import pt.ptinovacao.arqospocket.core.error.ErrorMapper;
import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.core.producers.JsonTestProducer;
import pt.ptinovacao.arqospocket.core.serialization.TaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type2.TelephonyStateManager;
import pt.ptinovacao.arqospocket.core.settings.SharedPreferencesManager;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.ExecutableTest;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.tasks.RecordAudioTaskData;
import pt.ptinovacao.arqospocket.core.tests.results.RecordAudioExecutionResult;
import pt.ptinovacao.arqospocket.core.utils.DateUtils;
import pt.ptinovacao.arqospocket.core.utils.DiskUtils;
import pt.ptinovacao.arqospocket.core.utils.ParseNumberUtil;
import pt.ptinovacao.arqospocket.core.voicecall.NativeEncoder;
import pt.ptinovacao.arqospocket.core.voicecall.audio.AudioRecorderManager;

/**
 * Record audio executable task.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class RecordAudioExecutableTask extends BaseExecutableTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordAudioExecutableTask.class);

    public RecordAudioExecutableTask(TaskData taskData) {
        super(taskData);
    }

    private RecordAudioExecutionResult recordAudioExecutionResult;

    private ExecutableTest ownerTest;

    private int index;

    private Disposable recordAudioSubscribe;

    private long startTime;

    private AudioRecorderManager audioRecorderManager;

    @Override
    public Observable<BaseTaskExecutionResult> execute(ExecutableTest ownerTest, int index) {
        this.ownerTest = ownerTest;
        this.index = index;
        createExecutionResult(index);

        audioRecorderManager = AudioRecorderManager.getInstance(getApplication());

        Single.just(getData().getTaskName()).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull String name) throws Exception {
                startTaskExecution();
            }
        });

        return Observable.empty();
    }

    private void startTaskExecution() {
        startTime = Calendar.getInstance().getTimeInMillis();

        SharedPreferencesManager preferencesManager = SharedPreferencesManager.getInstance(getApplication());
        if (preferencesManager.getPercentageMemoryOccupied() < (100 - DiskUtils.percentageFreeSpace())) {
            finishTask(ErrorMapper.MOBILE_TERMINATION_ERROR_MEMORY_FULL.toString());
            return;
        }

        final RecordAudioTaskData data = (RecordAudioTaskData) getData();
        TelephonyStateManager telephonyStateManager = TelephonyStateManager.getInstance(getApplication());

        if (telephonyStateManager.getCallStatus() != TelephonyStateManager.CallStatus.ACTIVE) {
            finishTask(ErrorMapper.THE_CALL_WAS_NOT_ACTIVE.toString());
            return;
        }

        final String nameFile = data.getAudioRecordingFileName() + "_" + DateUtils.convertDateToStringArqos(Calendar.getInstance().getTime()) ;

        audioRecorderManager.setOutputFile(nameFile+AudioRecorderManager.WAV);
        audioRecorderManager.prepare();

        if (audioRecorderManager.start()) {

            int timeout = ParseNumberUtil.parseNumber(getData().getTimeout());
            if (timeout > 0) {
                recordAudioSubscribe = Single.just(RESULT_TASK_SUCCESS).subscribeOn(Schedulers.newThread())
                        .delay(timeout, TimeUnit.SECONDS).subscribe(new Consumer<String>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull String result) throws Exception {
                                LOGGER.debug("Record audio, finishing task");
                                String releasePath = audioRecorderManager.release();

                                if (releasePath != null) {
                                    File file = new File(releasePath);
                                    String destFileName = file.getAbsolutePath();

                                    String outputFile =
                                            checkDirectory(getApplication()) + File.separator + file.getName();

                                    copyFile(destFileName, outputFile);

                                    NativeEncoder.INSTANCE.encodeWav(outputFile, 1);

                                    Thread.sleep(2000);

                                    String replaceExtension =
                                            outputFile.replace(AudioRecorderManager.WAV, AudioRecorderManager.TTA);
                                    String replaceFinal = file.getAbsolutePath()
                                            .replace(AudioRecorderManager.WAV, AudioRecorderManager.TTA);

                                    copyFile(replaceExtension, replaceFinal);

                                    //delete files of memory internal
                                    ///data/user/0/pt.ptinovacao.arqospocket/app_arqospocket
                                    File fileOriginWav = new File(outputFile);
                                    File fileOriginTta = new File(replaceExtension);
                                    fileOriginWav.delete();
                                    fileOriginTta.delete();

                                    //delete wav of external
                                    ///storage/emulated/0/arqospocket/recordings
                                    File fileWav = new File(releasePath);
                                    fileWav.delete();

                                    String fileSave = file.getName();
                                    fileSave = fileSave.replace(AudioRecorderManager.WAV, AudioRecorderManager.TTA);

                                    recordAudioExecutionResult.setNameFileAttachments(fileSave);
                                }
                                recordAudioExecutionResult.setAudioRecordingFileName(
                                        nameFile + AudioRecorderManager.TTA);
                                finishTask(RESULT_TASK_SUCCESS);
                            }
                        });
            }
        } else {
            audioRecorderManager.release();
            finishTask(ErrorMapper.MEMORY_FAILURE.toString());
        }
    }

    @NonNull
    public static String checkDirectory(Context context) {
        File path = context.getDir(JsonTestProducer.BASE_DIR, Context.MODE_PRIVATE);
        if (!path.exists()) {
            path.mkdir();
        }
        return path.toString();
    }

    private void copyFile(String inputPath, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        } catch (FileNotFoundException fnfe1) {
            LOGGER.error(fnfe1.getMessage());
        } catch (Exception e) {
            LOGGER.error("tag", e.getMessage());
        }

    }

    private void createExecutionResult(int index) {
        TaskResult result = TaskResolver.taskResultForData(getData());
        recordAudioExecutionResult = (RecordAudioExecutionResult) TaskResolver.executionResultForTaskResult(result);
        recordAudioExecutionResult.setExecutionId(index);
        recordAudioExecutionResult.updateStartDate(Calendar.getInstance().getTime());
    }

    @Override
    public BaseTaskExecutionResult fail(String errorCode) {
        TaskResult result = TaskResolver.taskResultForData(getData());
        BaseTaskExecutionResult executionResult = TaskResolver.executionResultForTaskResult(result);
        executionResult.updateStatus(errorCode);
        return executionResult;
    }

    private synchronized void finishTask(String executionStatus) {

        if (recordAudioSubscribe != null && !recordAudioSubscribe.isDisposed()) {
            recordAudioSubscribe.dispose();
            recordAudioSubscribe = null;
        }

        recordAudioExecutionResult.finalizeResult(executionStatus, ConnectionTechnology.MOBILE);

        //CDRs
        if(!recordAudioExecutionResult.getResult().getStatus().contains("NOK"))
            CDRUtils.getInstance(getApplication()).tempInitCallCdr.setRecordCall(1);

        ownerTest.onTaskExecutionFinished(index, recordAudioExecutionResult);
    }
}