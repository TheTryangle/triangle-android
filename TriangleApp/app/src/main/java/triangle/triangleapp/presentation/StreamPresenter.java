package triangle.triangleapp.presentation;

import triangle.triangleapp.logic.StreamManager;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class StreamPresenter {
    private StreamView mView;
    private StreamManager mManager;

    public StreamPresenter(StreamView streamView) {
        mView = streamView;
        mManager = new StreamManager();
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
