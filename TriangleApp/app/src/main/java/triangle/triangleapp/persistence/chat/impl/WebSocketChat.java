package triangle.triangleapp.persistence.chat.impl;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.lang.reflect.Type;
import java.util.Date;

import triangle.triangleapp.domain.ChatAction;
import triangle.triangleapp.helpers.ConfigHelper;
import triangle.triangleapp.persistence.chat.ChatAdapter;
import triangle.triangleapp.persistence.chat.ChatCallback;
import triangle.triangleapp.persistence.ConnectionCallback;

/**
 * Created by marco on 20-6-2017.
 */

public class WebSocketChat implements ChatAdapter {
    private static final String URL = ConfigHelper.getInstance().get(ConfigHelper.KEY_CHAT_ADDRESS);
    private static final String PROTOCOL = ConfigHelper.getInstance().get(ConfigHelper.KEY_WEBSOCKET_PROTOCOL);
    private ChatCallback mChatCallback;
    private WebSocket mWebSocket;
    private Gson mGsonInstance;

    public WebSocketChat() {
        GsonBuilder builder = new GsonBuilder();

        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        mGsonInstance = builder.create();
    }

    @Override
    public void connect(@NonNull final ConnectionCallback connectionCallback) {
        AsyncHttpClient.getDefaultInstance()
                .websocket(URL, PROTOCOL, new AsyncHttpClient.WebSocketConnectCallback() {
                    @Override
                    public void onCompleted(Exception ex, WebSocket webSocket) {
                        if (ex == null) {
                            mWebSocket = webSocket;

                            // Send the join message
                            mWebSocket.setStringCallback(new WebSocket.StringCallback() {
                                @Override
                                public void onStringAvailable(String s) {
                                    ChatAction message = mGsonInstance.fromJson(s, ChatAction.class);
                                    mChatCallback.onMessageReceived(message);
                                }
                            });
                            connectionCallback.onConnected();
                        } else {
                            connectionCallback.onError(ex);
                        }
                    }
                });
    }

    @Override
    public void sendMessage(ChatAction message) {
        String jsonChatMessage = mGsonInstance.toJson(message);
        mWebSocket.send(jsonChatMessage);
    }

    @Override
    public void setCallback(ChatCallback chatCallback) {
        mChatCallback = chatCallback;
    }
}
