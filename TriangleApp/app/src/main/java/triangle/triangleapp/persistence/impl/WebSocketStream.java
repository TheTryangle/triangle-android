package triangle.triangleapp.persistence.impl;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Xml;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import org.spongycastle.crypto.params.AsymmetricKeyParameter;
import org.spongycastle.crypto.params.RSAKeyParameters;
import org.spongycastle.crypto.util.PrivateKeyFactory;
import org.spongycastle.openssl.jcajce.JcaPEMWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

import triangle.triangleapp.helpers.CertifcateHelper;
import triangle.triangleapp.helpers.FileHelper;
import triangle.triangleapp.persistence.StreamAdapter;
import triangle.triangleapp.persistence.WebSocketCallback;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class WebSocketStream implements StreamAdapter {
    private static final String URL = "ws://145.49.35.215:1234/send";
    private static final String PROTOCOL = "ws";
    private WebSocket mWebSocket;
    private boolean mIsConnected;

    /**
     * gets boolean isConnected, default null, can never return false
     *
     * @return connectionstate
     */
    public boolean isConnected() {
        return mIsConnected;
    }

    @Override
    public void sendPublicKey(PublicKey publicKey) {
        StringWriter stringWriter = new StringWriter();
        JcaPEMWriter writer = new JcaPEMWriter(stringWriter);
        try {
            writer.writeObject(publicKey);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String pubKey = stringWriter.toString();
        mWebSocket.send("PUBKEY:" + pubKey);
    }

    @Override
    public void connect(final WebSocketCallback callback) {
        AsyncHttpClient.getDefaultInstance()
                .websocket(URL, PROTOCOL, new AsyncHttpClient.WebSocketConnectCallback() {
                    @Override
                    public void onCompleted(Exception ex, WebSocket webSocket) {
                        mIsConnected = true;
                        mWebSocket = webSocket;
                        callback.onConnected();
                    }
                });

    }

    /**
     * sends the stream using websocket
     *
     * @param fileName name of file
     */
    @Override
    public void sendFile(PrivateKey privateKey, @NonNull String fileName) {
        try {
            if (mIsConnected) {
                byte[] bytesToSend = FileHelper.getBytesFromFile(fileName);

                /*String hash = CertifcateHelper.encrypt(privateKey, bytesToSend);

                mWebSocket.send("HASH:" + hash);*/
                mWebSocket.send(bytesToSend);
            }
        } catch (Exception ex) {
            Log.e("WebSocket/sendStream", "Error while sending stream.", ex);
        }
    }
}




