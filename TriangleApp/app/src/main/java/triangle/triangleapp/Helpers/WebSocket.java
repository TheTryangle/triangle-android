package triangle.triangleapp.Helpers;

import android.util.Log;
import com.koushikdutta.async.http.AsyncHttpClient;

/**
 * Created by Kevin Ly on 6/12/2017.
 */

public class WebSocket {
    private com.koushikdutta.async.http.WebSocket webSocket;
    private boolean isConnected;

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

    public boolean isConnected(){
        return isConnected;
    }

    public void sendStream(byte[] streamInBytes){
        try
        {
            if (isConnected){
                webSocket.send(streamInBytes);
            }
        }
        catch (Exception ex){
            Log.e("WebSocket/sendStream", "Error while sending stream.", ex);
        }
    }
}
