package pt.ptinovacao.arqospocket.core.producers;

import android.os.Environment;
import android.os.FileObserver;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.TestConsumer;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.alarms.AlarmType;
import pt.ptinovacao.arqospocket.core.alarms.AlarmUtils;
import pt.ptinovacao.arqospocket.core.alarms.AlarmsManager;
import pt.ptinovacao.arqospocket.core.tests.TestExecutionType;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;

/**
 * Producer that listens for file updates at a determinate directory and parses the found files providing the collected
 * tests to the tests consumer.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public class JsonTestProducer extends TestProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonTestProducer.class);

    public static final String BASE_DIR = "arqospocket";

    private static final String TESTS_DIR = "tests";

    private final FileObserver fileObserver;

    private final FilenameFilter fileFilter;

    private final Observable<Integer> observable;

    private ObservableEmitter<Integer> filesSubscriber;

    private int fileProcessingId = 0;

    private SparseArray<Observable<List<TestData>>> pendingFiles = new SparseArray<>();

    /**
     * Constructor for the test producer.
     *
     * @param application the application to access the global context.
     * @param consumer the consumer to consume the tests.
     */
    JsonTestProducer(CoreApplication application, TestConsumer consumer) {
        super(application, consumer);

        fileFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return isValidFile(new File(dir, name));
            }
        };

        observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> subscriber) throws Exception {
                filesSubscriber = subscriber;
            }
        });
        observable.debounce(300, TimeUnit.MILLISECONDS).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Integer event) throws Exception {
                processFiles();
            }
        });

        fileObserver = new FileObserver(getTestsDirectory().getAbsolutePath(),
                FileObserver.MODIFY | FileObserver.ATTRIB | FileObserver.CLOSE_WRITE | FileObserver.MOVED_TO |
                        FileObserver.CREATE) {
            @Override
            public void onEvent(int event, String path) {
                LOGGER.debug("Received file event: {}", event);
                filesSubscriber.onNext(event);
            }
        };
    }

    @Override
    void start() {
        if (validateDirectories()) {
            LOGGER.debug("Watching '{}' ==> {}", getTestsDirectory().getAbsolutePath(), getTestsDirectory().exists());
            fileObserver.startWatching();
            processFiles();
        }
    }

    @Override
    void stop() {
        fileObserver.stopWatching();
        observable.ignoreElements();
        filesSubscriber.onComplete();
    }

    @NonNull
    private File getTestsDirectory() {
        return new File(getStorageDirectory(), TESTS_DIR);
    }

    @NonNull
    private File getStorageDirectory() {
        return new File(Environment.getExternalStorageDirectory(), BASE_DIR);
    }

    private boolean validateDirectories() {
        if (!getTestsDirectory().exists()) {
            if (!getTestsDirectory().mkdirs()) {
                LOGGER.warn("Watch directory creating has failed, file watch will fail.");
                return false;
            }
        }
        return true;
    }

    private boolean isValidFile(File file) {
        return file.isFile() && file.canRead() && file.getName().endsWith(".json");
    }

    private void processFiles() {
        File[] files = getTestsDirectory().listFiles(fileFilter);
        LOGGER.debug("Found files: {}", files.length);
        for (File file : files) {
            processFile(file);
        }
    }

    private void processFile(final File file) {
        final String fileName = file.getAbsolutePath();
        LOGGER.debug("Processing file: {}", fileName);

        TestParser parser = new TestParser();
        Observable<List<TestData>> fileObservable = parser.parseTests(file);
        final int id = ++fileProcessingId;
        pendingFiles.append(id, fileObservable);
        fileObservable.subscribe(new Consumer<List<TestData>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<TestData> tests) throws Exception {
                getConsumer().consumeAll(tests, TestExecutionType.SCHEDULED);
                pendingFiles.remove(id);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                AlarmsManager.getInstance(getApplication()).generateAlarm(AlarmUtils.INICIO, AlarmType.A075.name(), AlarmType.A075.getAlarmContent(), fileName);
                //                LOGGER.error("Could not parse JSON file [" + fileName + "]", throwable);
                pendingFiles.remove(id);
            }
        });
    }
}
