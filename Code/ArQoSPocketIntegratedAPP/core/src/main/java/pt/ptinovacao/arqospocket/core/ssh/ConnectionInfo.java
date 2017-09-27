package pt.ptinovacao.arqospocket.core.ssh;

import com.google.common.base.MoreObjects;
import com.jcraft.jsch.UserInfo;

/**
 * Contains connection data for the SSH session,. * Created by Emílio Simões on 20-06-2017.
 */
public class ConnectionInfo implements UserInfo {

    private String username;

    private String password;

    private String host;

    private int port;

    public ConnectionInfo(String username, String password, String host) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = 22;
    }

    String getUsername() {
        return username;
    }

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean promptPassword(String message) {
        return true;
    }

    @Override
    public boolean promptPassphrase(String message) {
        return true;
    }

    @Override
    public boolean promptYesNo(String message) {
        return true;
    }

    @Override
    public void showMessage(String message) {
    }

    String getHost() {
        return host;
    }

    int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("username", username).add("password", password).add("host", host)
                .add("port", port).toString();
    }
}
