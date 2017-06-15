package triangle.triangleapp.persistence.impl;

import android.support.annotation.NonNull;
import android.util.Log;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import org.spongycastle.crypto.params.AsymmetricKeyParameter;
import org.spongycastle.openssl.jcajce.JcaPEMWriter;

import java.io.IOException;
import java.io.StringWriter;
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
     * @return connectionstate
     */
    public boolean isConnected(){
        return mIsConnected;
    }

    @Override
    public void sendPublicKey(AsymmetricKeyParameter publicKey) {
        StringWriter stringWriter = new StringWriter();
        JcaPEMWriter writer = new JcaPEMWriter(stringWriter);
        try {
            writer.writeObject((RSAPublicKey) publicKey);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mWebSocket.send("PUBKEY:" + stringWriter.toString());
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
     * @param fileName name of file
     */
    @Override
    public void sendFile(AsymmetricKeyParameter privateKey, @NonNull String fileName) {
        try{
            if (mIsConnected){
                byte[] bytesToSend = FileHelper.getBytesFromFile(fileName);

                String hash = CertifcateHelper.encrypt(privateKey, bytesToSend);

                mWebSocket.send("HASH:" + hash);
                mWebSocket.send(bytesToSend);
            }
        }catch (Exception ex){
            Log.e("WebSocket/sendStream", "Error while sending stream.", ex);
        }
    }
}




