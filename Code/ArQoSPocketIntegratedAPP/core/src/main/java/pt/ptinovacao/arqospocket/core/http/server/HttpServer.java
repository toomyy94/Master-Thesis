package pt.ptinovacao.arqospocket.core.http.server;

import android.support.annotation.NonNull;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import fi.iki.elonen.NanoHTTPD;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.TestParser;
import pt.ptinovacao.arqospocket.core.alarms.AlarmType;
import pt.ptinovacao.arqospocket.core.alarms.AlarmUtils;
import pt.ptinovacao.arqospocket.core.alarms.AlarmsManager;
import pt.ptinovacao.arqospocket.core.http.client.HttpClient;
import pt.ptinovacao.arqospocket.core.network.MobileNetworkManager;
import pt.ptinovacao.arqospocket.core.network.NetworkUtils;
import pt.ptinovacao.arqospocket.core.network.WifiNetworkManager;
import pt.ptinovacao.arqospocket.core.network.data.wifi.WifiInfoData;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.ResultFileData;

/**
 * Web server to receive requests from the management service.
 * <p>
 * Created by Emílio Simões on 10-05-2017.
 */
public class HttpServer extends NanoHTTPD {

    private static final String APPLICATION_JSON = "application/json; charset=UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    private static final int PORT = 8080;

    private final String address;

    private final CoreApplication application;

    private final HttpRequestListener listener;

    public HttpServer(CoreApplication application, HttpRequestListener listener) {
        super(PORT);
        this.application = application;
        this.listener = listener;

        WifiInfoData data = WifiNetworkManager.getInstance(application).getWifiInfoData();
        address = "http://" + data.getIpAddress() + ":" + PORT;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public Response serve(IHTTPSession session) {
        SessionWrapper wrapper = new SessionWrapper(session);

        if (wrapper.isOptions()) {
            return newPreFlightResponse();
        }

        if (wrapper.isPost()) {
            if (!wrapper.isJsonContent()) {
                return newBadRequest("Unsupported content type, only 'application/json' is allowed", 1);
            }
            if (wrapper.isRequestUri()) {
                String content = Strings.nullToEmpty(wrapper.getContent());
                int contentLength = content.length();
                LOGGER.debug("Received content length: {}", contentLength);

                if (contentLength == 0) {
                    return newBadRequest("Received empty body", 1);
                }

                try {
                    HttpRequestListener.ProcessedResponse response = null;
                    if (listener != null) {
                        response = listener.onRequestReceived(content);
                    }
                    if (response != null) {
                        if (response.isSuccess()) {
                            return newSuccessResponse(response.getBody());
                        } else {
                            return newBadRequest(response.getBody(), response.getErrorCode());
                        }
                    }
                } catch (Exception e) {
                    //Alarm A062
                    AlarmsManager.getInstance(application).generateAlarm(AlarmUtils.INICIO, AlarmType.A062.name(), AlarmType.A062.getAlarmContent(), e.toString());
                    LOGGER.error("Error processing content", e);

                    return newInternalServerError(e.getMessage());
                }

                return newSuccessResponse();
            }
        }

        return newNotFoundResponse();
    }

    private Response newInternalServerError(String message) {
        String error = getErrorResponseBody(message, 0);
        Response response = newFixedLengthResponse(Response.Status.INTERNAL_ERROR, APPLICATION_JSON, error);
        addCorsHeaders(response);
        return response;

    }

    private Response newBadRequest(String message, int errorCode) {
        String error = getErrorResponseBody(message, errorCode);
        Response response = newFixedLengthResponse(Response.Status.BAD_REQUEST, APPLICATION_JSON, error);
        addCorsHeaders(response);
        return response;
    }

    @NonNull
    private Response newSuccessResponse() {
        Response response = newFixedLengthResponse(Response.Status.OK, APPLICATION_JSON, getSuccessResponseBody());
        addCorsHeaders(response);
        return response;
    }

    @NonNull
    private Response newSuccessResponse(String body) {
        Response response = newFixedLengthResponse(Response.Status.OK, APPLICATION_JSON, body);
        addCorsHeaders(response);
        return response;
    }

    @NonNull
    private Response newPreFlightResponse() {
        Response response = newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, null);
        addCorsHeaders(response, true);
        return response;
    }

    private Response newNotFoundResponse() {
        String error = getErrorResponseBody(null, 0);
        Response response = newFixedLengthResponse(Response.Status.NOT_FOUND, APPLICATION_JSON, error);
        addCorsHeaders(response);
        return response;
    }

    private void addCorsHeaders(Response response) {
        addCorsHeaders(response, false);
    }

    private void addCorsHeaders(Response response, boolean isPreFlight) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        if (isPreFlight) {
            response.addHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
            response.addHeader("Access-Control-Max-Age", "86400");
        }
    }

    private String getSuccessResponseBody() {
        return getResponseBody(true, null, -1);
    }

    private String getErrorResponseBody(String error, int errorCode) {
        return getResponseBody(false, Strings.emptyToNull(error), errorCode);
    }

    private String getResponseBody(boolean success, String error, int errorCode) {
        String imei = MobileNetworkManager.getInstance(application).getDeviceImei();

        ResultFileData.Builder builder =
                new ResultFileData.Builder().equipmentType(HttpClient.EQUIPMENT_TYPE).serialNumber(imei)
                        .macAddress(imei).ipAddress(NetworkUtils.getIPAddress(true))
                        .timestamp(Calendar.getInstance().getTime());
        if (Strings.nullToEmpty(error).trim().length() > 0) {
            builder.error(error);
        }
        if (success) {
            builder.success();
        } else {
            builder.fail(errorCode);
        }
        ResultFileData data = builder.build();

        TestParser parser = new TestParser();
        return parser.stringify(data);
    }
}
