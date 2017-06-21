package triangle.triangleapp.presentation.stream;

import android.support.annotation.NonNull;

import triangle.triangleapp.domain.ChatAction;
import triangle.triangleapp.helpers.AdapterType;
import triangle.triangleapp.logic.StreamManager;
import triangle.triangleapp.logic.StreamManagerCallback;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class StreamPresenter implements StreamManagerCallback {
    private StreamView mView;
    private StreamManager mManager;

    public StreamPresenter(StreamView streamView) {
        mView = streamView;
        mManager = new StreamManager(this);
    }

    /**
     * Starts or stops the streaming
     */
    public void stream() {
        mView.showPreview();
        mManager.setPreviewView(mView.getPreviewSurface());
        boolean isStreaming = mManager.stream();

        if (isStreaming) {
            mView.streamStarted();
        } else {
            mView.hidePreview();
            mView.streamStopped();
        }
    }

    @Override
    public void chatMessageReceived(@NonNull ChatAction message) {
        mView.showMessage(message);
    }

    @Override
    public void streamConnected() {
        mView.connected(AdapterType.TYPE_STREAM);
    }

    @Override
    public void streamError(@NonNull Exception exception) {
        mView.errorOccurred(AdapterType.TYPE_STREAM, exception);
    }

    @Override
    public void chatConnected() {

    }

    @Override
    public void chatError(@NonNull Exception exception) {

    }

    /**
     * Sends a chat message
     *
     * @param message The message to be sent
     */
    public void sendChatMessage(@NonNull String message) {
        mManager.sendChatMessage(message);
    }
}
