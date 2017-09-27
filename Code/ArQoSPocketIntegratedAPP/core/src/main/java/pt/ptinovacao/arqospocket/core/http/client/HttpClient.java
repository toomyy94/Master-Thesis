package pt.ptinovacao.arqospocket.core.http.client;

import android.support.annotation.Nullable;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Authenticator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.backoff.BackOffManager;
import pt.ptinovacao.arqospocket.core.http.client.response.ProbeNotificationResponse;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkManager;
import pt.ptinovacao.arqospocket.core.network.NetworkUtils;
import pt.ptinovacao.arqospocket.core.serialization.entities.ProbeNotificationResult;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.AlarmFileDataAssocitedResponse;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.CDRsFileDataAssocitedResponse;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.ResultFileAttachmentsProcess;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.ResultFileData;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.ResultFileDataAssocitedResponse;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.ResultFileDataIpUpdate;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.ResultFileDataKeepAlive;
import pt.ptinovacao.arqospocket.core.tests.data.TestResult;

/**
 * HTTP client to execute HTTP requests.
 * <p>
 * Created by Emílio Simões on 11-05-2017.
 */
public class HttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

    private static final MediaType JSON_CONTENT_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * id Probe ArQoS Pocket
     */
    public final static String EQUIPMENT_TYPE = "13";

    private OkHttpClient client;

    private CoreApplication application;

    private String ipAddress;

    private String imei;

    public HttpClient(CoreApplication coreApplication) {
        client = new OkHttpClient.Builder().authenticator(new BasicAuthenticator()).build();
        this.application = coreApplication;
        ipAddress = NetworkUtils.getIPAddress(true);
        BackOffManager.setIpAddress(application);
        imei = MobileNetworkManager.getInstance(application).getDeviceImei();
    }

    public ProbeNotificationResponse postProbeNotificationResultTests(List<TestResult> testResults, String url) {
        ProbeNotificationResult.Builder resultBuilder = new ProbeNotificationResult.Builder();

        ResultFileData data =
                new ResultFileDataAssocitedResponse.Builder().appendResults(testResults).equipmentType(EQUIPMENT_TYPE)
                        .serialNumber(imei).macAddress(imei).ipAddress(ipAddress).timestamp(new Date()).build();

        return getProbeNotificationResponse(url, resultBuilder, data);
    }

    public ProbeNotificationResponse postProbeNotificationResultAlarms(JsonObject alarmData, String url) {
        ProbeNotificationResult.Builder resultBuilder = new ProbeNotificationResult.Builder();


        ResultFileData data =
                new AlarmFileDataAssocitedResponse.Builder().appendPippedAlarm(alarmData).equipmentType(EQUIPMENT_TYPE)
                        .serialNumber(imei).macAddress(imei).ipAddress(ipAddress).timestamp(new Date()).build();

        return getProbeNotificationResponse(url, resultBuilder, data);
    }

    public ProbeNotificationResponse postProbeNotificationResultCDRs(JsonObject CDRsData, String url) {
        ProbeNotificationResult.Builder resultBuilder = new ProbeNotificationResult.Builder();

        ResultFileData data =
                new CDRsFileDataAssocitedResponse.Builder().appendPippedCDRs(CDRsData).equipmentType(EQUIPMENT_TYPE)
                        .serialNumber(imei).macAddress(imei).ipAddress(ipAddress).timestamp(new Date()).build();

        return getProbeNotificationResponse(url, resultBuilder, data);
    }

    public ProbeNotificationResponse postProbeNotificationKeepAlive(int intervale) {
        ProbeNotificationResult.Builder resultBuilder = new ProbeNotificationResult.Builder();

        ResultFileData data =
                new ResultFileDataKeepAlive.Builder().timeToRefresh(intervale).equipmentType(EQUIPMENT_TYPE)
                        .serialNumber(imei).macAddress(imei).ipAddress(ipAddress).timestamp(new Date()).build();

        return getProbeNotificationResponse(RemoteServiceUrlManager.getInstance(application).urlKeepAliveProcess(), resultBuilder, data);
    }

    public ProbeNotificationResponse postProbeNotificationAttachmentsProcess(ArrayList<String> fileNames) {
        ProbeNotificationResult.Builder resultBuilder = new ProbeNotificationResult.Builder();

        ResultFileData data =
                new ResultFileAttachmentsProcess.Builder().associatedfilename(fileNames).equipmentType(EQUIPMENT_TYPE)
                        .serialNumber(imei).macAddress(imei).ipAddress(ipAddress).timestamp(new Date()).build();

        return getProbeNotificationResponse(RemoteServiceUrlManager.getInstance(application).urlAttachmentProcess(), resultBuilder, data);
    }

    public ProbeNotificationResponse postProbeNotificationRadiologsAttachmentsProcess(ArrayList<String> fileNames) {
        ProbeNotificationResult.Builder resultBuilder = new ProbeNotificationResult.Builder();

        ResultFileData data =
                new ResultFileAttachmentsProcess.Builder().associatedfilename(fileNames).equipmentType(EQUIPMENT_TYPE)
                        .serialNumber(imei).macAddress(imei).ipAddress(ipAddress).timestamp(new Date()).build();

        return getProbeNotificationResponse(RemoteServiceUrlManager.getInstance(application).urlRadiologsAttachmentProcess(), resultBuilder, data);
    }

    public ProbeNotificationResponse postProbeNotificationScanlogsAttachmentsProcess(ArrayList<String> fileNames) {
        ProbeNotificationResult.Builder resultBuilder = new ProbeNotificationResult.Builder();

        ResultFileData data =
                new ResultFileAttachmentsProcess.Builder().associatedfilename(fileNames).equipmentType(EQUIPMENT_TYPE)
                        .serialNumber(imei).macAddress(imei).ipAddress(ipAddress).timestamp(new Date()).build();

        return getProbeNotificationResponse(RemoteServiceUrlManager.getInstance(application).urlScanlogsAttachmentProcess(), resultBuilder, data);
    }


    public ProbeNotificationResponse postProbeNotificationIpUpdate(String oldIpAddress) {
        ProbeNotificationResult.Builder resultBuilder = new ProbeNotificationResult.Builder();

        ResultFileData data =
                new ResultFileDataIpUpdate.Builder().oldIpAddress(oldIpAddress).equipmentType(EQUIPMENT_TYPE)
                        .serialNumber(imei).macAddress(imei).ipAddress(ipAddress).timestamp(new Date()).build();

        return getProbeNotificationResponse(RemoteServiceUrlManager.getInstance(application).urlIpUpdate(), resultBuilder, data);
    }

    public ProbeNotificationResponse postProbeNotificationAutoDiscovery() {
        ProbeNotificationResult.Builder resultBuilder = new ProbeNotificationResult.Builder();

        ResultFileData data =
                new ResultFileData.Builder().equipmentType(EQUIPMENT_TYPE).serialNumber(imei).macAddress(imei)
                        .ipAddress(ipAddress).timestamp(new Date()).build();

        return getProbeNotificationResponse(RemoteServiceUrlManager.getInstance(application).urlAutoDiscoveryProcess(), resultBuilder, data);
    }

    private ProbeNotificationResponse getProbeNotificationResponse(String url,
            ProbeNotificationResult.Builder resultBuilder, ResultFileData data) {

        ProbeNotificationResponse probeNotificationResponse =
                validateIpAddressAndImei(data.getNotification().getMacAddress(), data.getNotification().getIpAddress());
        if (probeNotificationResponse != null) {
            return probeNotificationResponse;
        }

        try {
            TestParser parser = new TestParser();
            String body = parser.stringify(data);

            LOGGER.debug("Sending post ({}): {}", url, body);

            Response response = post(url, body);
            if (response.isSuccessful()) {
                LOGGER.debug("Sent with success");
                return new ProbeNotificationResponse.Builder().result(resultBuilder.build()).success().build();
            } else {
                return new ProbeNotificationResponse.Builder().result(resultBuilder.build()).code(response.code()).build();
            }
        } catch (Exception e) {
            LOGGER.error("Error executing request", e);
            ProbeNotificationResult result = resultBuilder.withError(e.getMessage()).build();
            return new ProbeNotificationResponse.Builder().result(result).internalServerError().build();
        }
    }

    private Response post(String url, String body) throws IOException {
        RequestBody requestBody = RequestBody.create(JSON_CONTENT_TYPE, body);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return client.newCall(request).execute();
    }

    private class BasicAuthenticator implements Authenticator {

        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            return authenticateWithStoredCredentials(response);
        }

        /**
         * Responds the the server authentication challenge. We must first check if the 'Authentication' header is
         * already present. If the header is already there then it means that we already responded to the challenge once
         * and the server rejected the credentials. We must return {@code null} to stop the request or we will enter an
         * infinite loop.
         *
         * @param response the response from the server containing the challenge.
         * @return a new request filled with the client credentials.
         */
        @Nullable
        private Request authenticateWithStoredCredentials(Response response) {
            String credential = CredentialStore.basic();
            if (credential.equals(response.request().header(AUTHORIZATION_HEADER))) {
                return null;
            }
            return response.request().newBuilder().header(AUTHORIZATION_HEADER, credential).build();
        }
    }

    private ProbeNotificationResponse validateIpAddressAndImei(String imei, String ipAddress) {
        if (Strings.isNullOrEmpty(imei) || Strings.isNullOrEmpty(ipAddress)) {
            return new ProbeNotificationResponse(
                    new ProbeNotificationResult.Builder().withError("Error on the imei or ipAddress").build(), 412);
        }
        return null;
    }
}
