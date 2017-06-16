package triangle.triangleapp.presentation;

import android.content.Context;
import android.view.SurfaceView;

import triangle.triangleapp.logic.StreamManager;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class StreamPresenter {
    private StreamView mView;
    private StreamManager mManager;

    public StreamPresenter(StreamView streamView, Context context) {
        mView = streamView;
        mManager = new StreamManager(context);
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

}
