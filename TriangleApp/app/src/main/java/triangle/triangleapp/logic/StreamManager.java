package triangle.triangleapp.logic;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyPair;
import java.security.PublicKey;

import triangle.triangleapp.helpers.ConfigHelper;
import triangle.triangleapp.domain.ChatAction;
import triangle.triangleapp.helpers.IntegrityHelper;
import triangle.triangleapp.logic.impl.CameraLiveStream;
import triangle.triangleapp.persistence.chat.ChatAdapter;
import triangle.triangleapp.persistence.ConnectionCallback;
import triangle.triangleapp.persistence.chat.ChatCallback;
import triangle.triangleapp.persistence.impl.HttpStream;
import triangle.triangleapp.persistence.stream.StreamAdapter;
import triangle.triangleapp.persistence.chat.impl.WebSocketChat;
import triangle.triangleapp.persistence.stream.impl.WebSocketStream;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class StreamManager {
    private static final String TAG = "StreamManager";
    private LiveStream mLiveStream;
    private boolean mIsStreaming;
    private Surface mPreviewView;
    private StreamAdapter mStreamAdapter;
    private ChatAdapter mChatAdapter;
    private KeyPair mKeyPair;
    private StreamManagerCallback mManagerCallback;

    public StreamManager(StreamManagerCallback managerCallback) {
        mManagerCallback = managerCallback;
        mLiveStream = new CameraLiveStream();
        mStreamAdapter = new HttpStream();
        mChatAdapter = new WebSocketChat();

        // Try to get the keypair from the store else we generate
        try {
            mKeyPair = IntegrityHelper.getKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mStreamAdapter.connect(new ConnectionCallback() {
            @Override
            public void onConnected() {
                PublicKey pub = mKeyPair.getPublic();
                mStreamAdapter.sendPublicKey(pub);

                mManagerCallback.streamConnected();

                //Send username to server
                try {
                    String username = ConfigHelper.getInstance().get(ConfigHelper.KEY_USERNAME);
                    JSONObject streamInfo = new JSONObject();
                    streamInfo.put("StreamerName", username);
                    mStreamAdapter.sendText(streamInfo.toString());
                } catch (JSONException ex) {
                    Log.e(TAG, "An error occurred while attempting to send username to server", ex);
                }
                mManagerCallback.streamConnected();
                initChat();
            }

            @Override
            public void onError(@NonNull Exception ex) {
                mManagerCallback.streamError(ex);
                Log.e(TAG, "Error occurred during connecting", ex);
                mManagerCallback.streamError(ex);
            }
        });
    }

    private void initChat() {
        mChatAdapter.connect(new ConnectionCallback() {
            @Override
            public void onConnected() {
                Log.i(TAG, "Chat Adapter has connected!");
                mManagerCallback.chatConnected();

                ChatAction action = ChatAction.join(mStreamAdapter.getId());
                mChatAdapter.sendMessage(action);
                mChatAdapter.setCallback(new ChatCallback() {
                    @Override
                    public void onMessageReceived(ChatAction message) {
                        mManagerCallback.chatMessageReceived(message);
                    }
                });
            }

            @Override
            public void onError(@NonNull Exception ex) {
                Log.e(TAG, "Error occurred during ChatAdapter connection.", ex);
                mManagerCallback.chatError(ex);
            }
        });
    }

    /**
     * Sets the preview view to use for previews
     *
     * @param surface The surface to use for preview
     */
    public void setPreviewView(@NonNull Surface surface) {
        this.mPreviewView = surface;
    }

    /**
     * Starts or stops the streaming
     *
     * @return True if the streaming started, else returns false
     */
    public boolean stream() {
        if (mIsStreaming) {
            mLiveStream.stop();
        } else {
            mLiveStream.setPreviewView(mPreviewView);
            mLiveStream.start(new FileRecordedCallback() {
                @Override
                public void recordCompleted(@NonNull byte[] fileInBytes) {
                    Log.i(TAG, "File completed");
                    mStreamAdapter.sendFile(fileInBytes, mKeyPair.getPrivate());
                }
            });
        }
        mIsStreaming = !mIsStreaming;

        return mIsStreaming;
    }

    /**
     * Sends a chat message using the adapter
     *
     * @param chatAction The message to be sent
     */
    public void sendChatMessage(@NonNull ChatAction chatAction) {
        chatAction.setStreamId(mStreamAdapter.getId());
        mChatAdapter.sendMessage(chatAction);
    }
}
