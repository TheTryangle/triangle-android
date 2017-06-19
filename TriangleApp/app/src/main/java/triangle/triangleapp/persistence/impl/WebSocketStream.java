package triangle.triangleapp.persistence.impl;

import android.support.annotation.NonNull;
import android.util.Log;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import triangle.triangleapp.ConfigHelper;
import triangle.triangleapp.helpers.FileHelper;
import triangle.triangleapp.persistence.StreamAdapter;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class WebSocketStream implements StreamAdapter {
    private static final String URL = ConfigHelper.getInstance().get("destination_video_stream_websocket");
    private static final String PROTOCOL = ConfigHelper.getInstance().get("websocket_protocol");
    private WebSocket mWebSocket;
    private boolean mIsConnected;

    /**
     * makes an async websocket connection
     */
    public WebSocketStream(){
        AsyncHttpClient.getDefaultInstance()
                .websocket(URL, PROTOCOL, new AsyncHttpClient.WebSocketConnectCallback() {
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
     * @param fileInBytes file in bytes
     */
    @Override
    public void sendFile(@NonNull byte[] fileInBytes) {
        try{
            if (mIsConnected){
                mWebSocket.send(fileInBytes);
            }
        }catch (Exception ex){
            Log.e("WebSocket/sendStream", "Error while sending stream.", ex);
        }
    }
}




