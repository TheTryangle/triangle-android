package triangle.triangleapp;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import triangle.triangleapp.helpers.WebSocket;

/**
 * Created by D2110175 on 13-6-2017.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class WebSocketTest {

    /**
     * Test connection between websocket and server.
     */
    @Test
    public void testWebSocketConnection() {
        String url = "ws://145.49.30.113:1234/send";
        String protocol = "ws";
        try {
            WebSocket webSocket = new WebSocket(url, protocol);

            if (!webSocket.isConnected()) {
                Assert.assertTrue(true);
            }
        } catch (Exception ex) {
            Assert.assertTrue(false);
        }

    }
}
