package pt.ptinovacao.arqospocket.core;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pt.ptinovacao.arqospocket.core.serialization.entities.ManagementMessage;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.ResultFileData;
import pt.ptinovacao.arqospocket.core.tests.data.TestData;
import pt.ptinovacao.arqospocket.core.tests.data.TestResult;
import pt.ptinovacao.arqospocket.core.utils.JsonHelper;

/**
 * This class is responsible to parse the tests input in JSON format and return a list with the tests to execute.
 * <p>
 * Created by Emílio Simões on 06-04-2017.
 */
public class TestParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestParser.class);

    /**
     * Parses a file with multiple tests.
     *
     * @param input the input file to read and parse the tests.
     * @return an {@link Observable<List<TestData>>} that will resolve the parsed tests.
     */
    @SuppressWarnings("JavaDoc")
    public Observable<List<TestData>> parseTests(final File input) {
        LOGGER.debug("Parsing tests in file {}", input.getAbsolutePath());
        return Observable.fromCallable(new Callable<List<TestData>>() {
            @Override
            public List<TestData> call() throws Exception {
                return parseFile(input);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Parses a file with multiple tests.
     *
     * @param input the input file to read and parse the tests.
     * @return an {@link List<TestData>} that will resolve the parsed tests.
     */
    public ManagementMessage parseMessage(String input) {
        return parseFile(input);
    }

    /**
     * Parses a string with a single test on it.
     *
     * @param testData the string with the test data.
     * @return the parsed {@link TestData} instance.
     */
    public TestData parseSingleTest(String testData) {
        Gson gson = JsonHelper.getGsonInstance();
        return gson.fromJson(testData, TestData.class);
    }

    /**
     * Parses a string with a single test on it.
     *
     * @param testResult the string with the test data.
     * @return the parsed {@link TestResult} instance.
     */
    public TestResult parseSingleResult(String testResult) {
        Gson gson = JsonHelper.getGsonInstance();
        return gson.fromJson(testResult, TestResult.class);
    }

    /**
     * Converts a {@link TestData} object into a JSON string.
     *
     * @param testData the {@link TestData} to convert.
     * @return the converted object.
     */
    public String stringify(TestData testData) {
        return stringify(testData, true);
    }

    /**
     * Converts a {@link TestData} object into a JSON string.
     *
     * @param testData the {@link TestData} to convert.
     * @param prettyPrint if the output JSON should be formatted.
     * @return the converted object.
     */
    String stringify(TestData testData, boolean prettyPrint) {
        Gson gson = JsonHelper.getGsonInstance(prettyPrint);
        return gson.toJson(testData);
    }

    /**
     * Converts a {@link TestResult} object into a JSON string.
     *
     * @param testData the {@link TestResult} to convert.
     * @return the converted object.
     */
    String stringify(TestResult testData) {
        return stringify(testData, false);
    }

    /**
     * Converts a {@link TestResult} object into a JSON string.
     *
     * @param testData the {@link TestResult} to convert.
     * @param prettyPrint if the output JSON should be formatted.
     * @return the converted object.
     */
    String stringify(TestResult testData, boolean prettyPrint) {
        Gson gson = JsonHelper.getGsonInstance(prettyPrint);
        return gson.toJson(testData);
    }

    /**
     * Converts a {@link ResultFileData} object into a JSON string.
     *
     * @param data the {@link ResultFileData} to convert.
     * @return the converted object.
     */
    public String stringify(ResultFileData data) {
        Gson gson = JsonHelper.getGsonInstance(false);
        return gson.toJson(data);
    }

    @NonNull
    private ManagementMessage parseFile(String input) {

        Gson gson = JsonHelper.getGsonInstance();
        return readJsonFile(input, gson);
    }

    @NonNull
    private List<TestData> parseFile(File input) {
        List<TestData> tests = new ArrayList<>();
        if (input == null || !input.isFile() || !input.canRead()) {
            LOGGER.warn("Input file is not valid => '{}'", input != null ? input.getAbsolutePath() : null);
            return tests;
        }

        Gson gson = JsonHelper.getGsonInstance();
        tests = readJsonFile(input, gson);

        return tests;
    }

    @NonNull
    private ManagementMessage readJsonFile(String input, Gson gson) {
        ManagementMessage data = gson.fromJson(input, ManagementMessage.class);
        return data;
    }

    @NonNull
    private List<TestData> readJsonFile(File input, Gson gson) {
        FileInputStream inputStream = null;
        InputStreamReader reader = null;
        try {
            inputStream = new FileInputStream(input);
            reader = new InputStreamReader(inputStream);
            ManagementMessage data = gson.fromJson(reader, ManagementMessage.class);

            if (data != null && data.getTests() != null) {
                LOGGER.debug("Found {} tests in file", data.getTests().size());
                return data.getTests();
            }

            return new ArrayList<>();
        } catch (FileNotFoundException e) {
            LOGGER.error("Error reading from JSON test file", e);
            return new ArrayList<>();
        } finally {
            close(reader);
            close(inputStream);
        }
    }

    private void close(Closeable inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOGGER.error("Error closing input stream", e);
            }
        }
    }
}