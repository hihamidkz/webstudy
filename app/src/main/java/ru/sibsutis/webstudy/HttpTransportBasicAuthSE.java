package ru.sibsutis.webstudy;

import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;
import org.ksoap2.transport.ServiceConnectionSE;

import java.io.IOException;

public class HttpTransportBasicAuthSE extends HttpTransportSE {
    private String username;
    private String password;

    public HttpTransportBasicAuthSE(String url, String username, String password) {
        super(url);
        this.username = username;
        this.password = password;
    }

    public ServiceConnection getServiceConnection() throws IOException {
        ServiceConnectionSE midpConnection = new ServiceConnectionSE(url);
        addBasicAuthentication(midpConnection);
        return midpConnection;
    }

    protected void addBasicAuthentication(ServiceConnection midpConnection) throws IOException {
        if (username != null && password != null) {
            StringBuffer buf = new StringBuffer(username);
            buf.append(':').append(password);
            byte[] raw = buf.toString().getBytes();
            buf.setLength(0);
            buf.append("Basic ");
            org.kobjects.base64.Base64.encode(raw, 0, raw.length, buf);
            midpConnection.setRequestProperty("Authorization", buf.toString());
        }
    }
}
