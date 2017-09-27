package pt.ptinovacao.arqospocket.core.http;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

/**
 * Helper class to update HTTP system properties.
 * <p>
 * Created by Emílio Simões on 26-04-2017.
 */
class TestHttpPropertiesSwitch {

    private static final String HTTP_AGENT = "http.agent";

    private static final String HTTPS_PROXY_HOST = "https.proxyHost";

    private static final String HTTPS_PROXY_PORT = "https.proxyPort";

    private String savedUserAgent = null;

    private String savedProxyAddress = null;

    private String savedProxyPort = null;

    void switchUserAgent(String userAgent) {
        String cleanAgent = Strings.nullToEmpty(userAgent).trim();
        if (cleanAgent.length() > 0) {
            savedUserAgent = System.getProperty(HTTP_AGENT);
            System.setProperty(HTTP_AGENT, userAgent);
        }
    }

    void switchProxy(String proxy) {
        String cleanProxy = Strings.nullToEmpty(proxy).trim();
        if (cleanProxy.length() > 0) {
            String[] proxyParts = Iterables.toArray(Splitter.on(':').split(cleanProxy), String.class);

            savedProxyAddress = System.getProperty(HTTPS_PROXY_HOST);
            savedProxyPort = System.getProperty(HTTPS_PROXY_PORT);

            System.setProperty(HTTPS_PROXY_HOST, proxyParts[0]);
            System.setProperty(HTTPS_PROXY_PORT, proxyParts[1]);
        }
    }

    void restoreDefaults() {
        restoreIfNotNull(HTTP_AGENT, this.savedUserAgent);
        restoreIfNotNull(HTTPS_PROXY_HOST, this.savedProxyAddress);
        restoreIfNotNull(HTTPS_PROXY_PORT, this.savedProxyPort);
    }

    private void restoreIfNotNull(String name, String value) {
        if (value != null) {
            System.setProperty(name, value);
        }
    }
}
