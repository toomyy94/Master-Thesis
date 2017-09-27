package pt.ptinovacao.arqospocket.core.http.server;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;

/**
 * Helper class to handle the {@link IHTTPSession} data.
 * <p>
 * Created by Emílio Simões on 10-05-2017.
 */
class SessionWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionWrapper.class);

    private static final String APPLICATION_JSON = "application/json";

    private IHTTPSession session;

    private String contentType = null;

    SessionWrapper(IHTTPSession session) {
        this.session = session;
    }

    boolean isPost() {
        return Method.POST.equals(session.getMethod());
    }

    boolean isOptions() {
        return Method.OPTIONS.equals(session.getMethod());
    }

    boolean isRequestUri() {
        return Strings.nullToEmpty(session.getUri()).trim().equals("/cgi-bin/arqos.cgi");
    }

    boolean isJsonContent() {
        if (contentType == null) {
            String contentTypeHeader = session.getHeaders().get("content-type");

            StringTokenizer tokenizer;
            if (contentTypeHeader != null) {
                tokenizer = new StringTokenizer(contentTypeHeader, ",; ");
                if (tokenizer.hasMoreTokens()) {
                    contentType = Strings.nullToEmpty(tokenizer.nextToken());
                }
            }
        }

        return APPLICATION_JSON.equals(contentType);
    }

    public String getContent() {
        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
        } catch (Exception e) {
            LOGGER.error("Error parsing request body", e);
        }
        return files.get("postData");
    }
}
