package triangle.triangleapp.persistence.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import org.spongycastle.openssl.jcajce.JcaPEMWriter;

import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.PublicKey;

import triangle.triangleapp.helpers.IntegrityHelper;
import triangle.triangleapp.persistence.StreamAdapter;
import triangle.triangleapp.persistence.ConnectionCallback;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class WebSocketStream implements StreamAdapter {
    private static final String URL = "ws://145.49.30.113:1234/send";
    private static final String PROTOCOL = "ws";
    private WebSocket mWebSocket;
    private boolean mIsConnected;

    /**
     * Determines if the WebSocket is connected.
     *
     * @return True if connected else false
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
    public void connect(final ConnectionCallback callback) {
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
     * @param fileInBytes file in bytes
     */
    @Override
    public void sendFile(@NonNull byte[] fileInBytes, PrivateKey privateKey) {
        try {
            if (mIsConnected) {
                String hash = IntegrityHelper.sign(fileInBytes, privateKey);
                mWebSocket.send("HASH:" + hash);
                mWebSocket.send(fileInBytes);
            }
        } catch (Exception ex) {
            Log.e("WebSocket/sendStream", "Error while sending stream.", ex);
        }
    }
}




