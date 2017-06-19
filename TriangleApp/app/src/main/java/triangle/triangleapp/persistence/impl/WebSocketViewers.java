package triangle.triangleapp.persistence.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

/**
 * Created by Koen on 16-06-17.
 */

public class WebSocketViewers {

    private static final String URL = "ws://145.49.26.75:1234/ViewCount";
    private static final String PROTOCOL = "ws";
    private WebSocket mWebSocket;
    private boolean mIsConnected;

    /**
     * makes an async websocket connection
     */
    public WebSocketViewers(){
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
//    @Override
//    public void getViewCount() {
//
//    }
}
