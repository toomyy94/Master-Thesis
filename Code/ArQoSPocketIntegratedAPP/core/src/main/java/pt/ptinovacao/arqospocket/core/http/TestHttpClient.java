package pt.ptinovacao.arqospocket.core.http;

import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Random;

/**
 * HTTP client wrapper to request uploads and downloads.
 * <p>
 * Created by Emílio Simões on 26-04-2017.
 */
public class TestHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestHttpClient.class);

    private static final int CONNECTION_TIMEOUT = 60000;

    private static final int CONNECTION_READ_TIMEOUT = 16000;

    private static final String ALPHABET = "0123456789qwertyuioplkjhgfdsazxcvbnm";

    public TestHttpResponse downloadData(String url, String proxy, String userAgent) {
        LOGGER.debug("Starting data download from {}", url);

        long startTime = Calendar.getInstance().getTimeInMillis();

        TestHttpPropertiesSwitch propertiesSwitch = new TestHttpPropertiesSwitch();
        propertiesSwitch.switchUserAgent(userAgent);
        propertiesSwitch.switchProxy(proxy);

        URLConnection connection;
        try {
            connection = new URL(url).openConnection();
        } catch (IOException e) {
            LOGGER.error("Could not open connection", e);
            propertiesSwitch.restoreDefaults();
            return new TestHttpResponse(TestHttpResponseStatus.FAIL);
        }

        long accessTime = (Calendar.getInstance().getTimeInMillis() - startTime) / 1000;

        setDownloadConnectionProperties(connection);

        BufferedInputStream inputStream;
        try {
            inputStream = new BufferedInputStream(connection.getInputStream());
        } catch (IOException e) {
            LOGGER.error("Could not get input stream from connection", e);
            propertiesSwitch.restoreDefaults();
            return new TestHttpResponse(TestHttpResponseStatus.FAIL);
        }

        StringBuilder builder = new StringBuilder();
        int readByte;
        int totalBytes = 0;
        long totalTime = 0;
        try {
            while ((readByte = inputStream.read()) != -1) {
                builder.append((char) readByte);
                totalBytes++;

                totalTime = Calendar.getInstance().getTimeInMillis() - startTime;
                if (totalTime > CONNECTION_READ_TIMEOUT) {
                    break;
                }
            }
            inputStream.close();
        } catch (IOException e) {
            LOGGER.error("Could not read from input stream", e);
            propertiesSwitch.restoreDefaults();
            return new TestHttpResponse(TestHttpResponseStatus.FAIL);
        }

        long throughput = calculateThroughput(totalBytes, totalTime);
        propertiesSwitch.restoreDefaults();

        return createDownloadResponse(url, accessTime, totalBytes, totalTime / 1000, throughput, builder);
    }

    public TestHttpResponse uploadData(String url, String proxy, String content) {
        LOGGER.debug("Starting data upload to {}", url);

        long startTime = Calendar.getInstance().getTimeInMillis();

        TestHttpPropertiesSwitch propertiesSwitch = new TestHttpPropertiesSwitch();
        propertiesSwitch.switchProxy(proxy);

        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            LOGGER.error("Could not open connection", e);
            propertiesSwitch.restoreDefaults();
            return new TestHttpResponse(TestHttpResponseStatus.FAIL);
        }

        long accessTime = (Calendar.getInstance().getTimeInMillis() - startTime) / 1000;

        if (!setUploadConnectionProperties(connection)) {
            propertiesSwitch.restoreDefaults();
            return new TestHttpResponse(TestHttpResponseStatus.FAIL);
        }

        DataOutputStream outputStream;
        try {
            outputStream = new DataOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("Could not get output stream from connection", e);
            propertiesSwitch.restoreDefaults();
            return new TestHttpResponse(TestHttpResponseStatus.FAIL);
        }

        int totalBytes = 0;
        OutputLong totalTime = new OutputLong();
        String responseMessage;
        try {
            if (content == null) {
                totalBytes = sendRandomData(outputStream, startTime, 30000000, totalBytes, totalTime);
            } else {
                totalBytes = sendContent(outputStream, startTime, content, totalBytes, totalTime);
            }
            outputStream.flush();
            outputStream.close();
            responseMessage = connection.getResponseMessage();
        } catch (IOException e) {
            LOGGER.error("Could not write to output stream", e);
            propertiesSwitch.restoreDefaults();
            return new TestHttpResponse(TestHttpResponseStatus.FAIL);
        }

        long throughput = calculateThroughput(totalBytes, totalTime.value);
        propertiesSwitch.restoreDefaults();

        return createUploadResponse(url, accessTime, totalTime.value / 1000, totalBytes, throughput, responseMessage);
    }

    private int sendRandomData(DataOutputStream outputStream, long startTime, long bytesToSend, int totalSendBytes,
            OutputLong totalTime) throws IOException {
        int count = ALPHABET.length();
        Random random = new Random();

        for (int i = 0; i < bytesToSend; i++) {
            outputStream.writeBytes(String.valueOf(ALPHABET.charAt(random.nextInt(count))));
            totalSendBytes++;

            totalTime.value = Calendar.getInstance().getTimeInMillis() - startTime;
            if (totalTime.value > CONNECTION_READ_TIMEOUT) {
                break;
            }
        }

        return totalSendBytes;
    }

    private int sendContent(DataOutputStream outputStream, long startTime, String content, int totalSendBytes,
            OutputLong totalTime) throws IOException {
        int count = content.length();

        for (int i = 0; i < count; i++) {
            outputStream.writeBytes(String.valueOf(content.charAt(count)));
            totalSendBytes++;
            totalTime.value = Calendar.getInstance().getTimeInMillis() - startTime;
            if (totalTime.value > CONNECTION_READ_TIMEOUT) {
                break;
            }
        }

        return totalSendBytes;
    }

    private boolean setUploadConnectionProperties(HttpURLConnection connection) {
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            LOGGER.error("Could not set connection method", e);
            return false;
        }
        connection.setReadTimeout(CONNECTION_READ_TIMEOUT);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");
        return true;
    }

    private void setDownloadConnectionProperties(URLConnection connection) {
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(CONNECTION_READ_TIMEOUT);
    }

    private long calculateThroughput(int totalBytes, long totalTime) {
        long totalBits = totalBytes * 8;
        long totalTimeInSeconds = totalTime / 1000;
        return totalBits / totalTimeInSeconds / 1000;
    }

    @NonNull
    private TestHttpResponse createUploadResponse(String url, long accessTime, long totalTime, int totalBytes,
            long throughput, String responseMessage) {
        return createResponse(url, accessTime, totalTime, totalBytes, throughput, responseMessage);
    }

    @NonNull
    private TestHttpResponse createDownloadResponse(String url, long accessTime, int totalBytes, long totalTime,
            long throughput, StringBuilder builder) {
        return createResponse(url, accessTime, totalTime, totalBytes, throughput, builder.toString());
    }

    @NonNull
    private TestHttpResponse createResponse(String url, long accessTime, long totalTime, int totalBytes, long throughput,
            String responseMessage) {
        TestHttpResponse response = new TestHttpResponse(TestHttpResponseStatus.OK);
        response.setAccessTime(accessTime);
        response.setTotalTime(totalTime);
        response.setTotalBytes(totalBytes);
        response.setThroughput(throughput);
        response.setReceivedData(responseMessage);
        response.setUrl(url);
        return response;
    }

    private class OutputLong {

        long value;
    }
}

