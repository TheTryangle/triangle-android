package triangle.triangleapp.persistence.impl;

import android.util.Log;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import java.io.File;

import triangle.triangleapp.helpers.MediaFileHelper;
import triangle.triangleapp.persistence.StreamAdapter;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class WebSocketStream implements StreamAdapter {
    private String mUrl;
    private String mProtocol;
    private WebSocket mWebSocket;
    private boolean mIsConnected;

    /**
     * makes an async websocket connection
     * @param url url to connect to
     * @param protocol protocol to be used
     */
    public WebSocketStream(String url, String protocol){
        mUrl = url;
        mProtocol = protocol;
        AsyncHttpClient.getDefaultInstance()
                .websocket(mUrl, mProtocol, new AsyncHttpClient.WebSocketConnectCallback() {
                    @Override
                    public void onCompleted(Exception ex, WebSocket webSocket) {
                        mIsConnected = true;
                        mWebSocket = webSocket;
                    }
                });
    }

    /**
     * gets boolean isConnected, default null, can never return false
     * @return connectionstate
     */
    public boolean isConnected(){
        return mIsConnected;
    }

    /**
     * sends the stream using websocket
     * @param fileName name of file
     */
    @Override
    public void sendFile(String fileName) {
        try{
            if (mIsConnected){
                byte[] bytesToSend = MediaFileHelper.getBytesFromFile(fileName);
                mWebSocket.send(bytesToSend);
            }
        }catch (Exception ex){
            Log.e("WebSocket/sendStream", "Error while sending stream.", ex);
        }
    }
}




