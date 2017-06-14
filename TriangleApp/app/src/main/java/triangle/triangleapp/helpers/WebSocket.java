package triangle.triangleapp.helpers;

import android.util.Log;
import com.koushikdutta.async.http.AsyncHttpClient;

/**
 * Created by Kevin Ly on 6/12/2017.
 */

public class WebSocket {
    private com.koushikdutta.async.http.WebSocket webSocket;
    private boolean isConnected;

    /**
     * makes an async websocket connection
     * @param url url to connect to
     * @param protocol protocol to be used
     */
    public WebSocket(String url, String protocol){
        AsyncHttpClient.getDefaultInstance()
            .websocket(url, protocol, new AsyncHttpClient.WebSocketConnectCallback() {
                @Override
                public void onCompleted(Exception ex, com.koushikdutta.async.http.WebSocket socket) {
                    isConnected = true;
                    webSocket = socket;
                }
            });
    }

    /**
     * gets boolean isConnected, default null, can never return false
     * @return boolean
     */
    public boolean isConnected(){
        return isConnected;
    }

    /**
     * sends the stream using websocket
     * @param streamInBytes byte array
     */
    public void sendStream(byte[] streamInBytes){
        try
        {
            if (isConnected){
                webSocket.send(streamInBytes);
            }
        }catch (Exception ex){
            Log.e("WebSocket/sendStream", "Error while sending stream.", ex);
        }
    }
}
