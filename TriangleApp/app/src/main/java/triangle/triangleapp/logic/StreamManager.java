package triangle.triangleapp.logic;

import android.view.Surface;
import android.view.SurfaceView;
import triangle.triangleapp.logic.impl.CameraLiveStream;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class StreamManager {
    private Livestream mLivestream;
    private boolean mIsStreaming;
    private Surface mPreviewView;

    public StreamManager() {
        mLivestream = new CameraLiveStream();
    }

    public void setPreviewView(Surface view) {
        this.mPreviewView = view;
    }

    public void stream() {
        if (mIsStreaming) {
            mLivestream.stop();
        } else {
            mLivestream.setPreviewView(mPreviewView);
            mLivestream.start(new FileRecordedCallback() {
                @Override
                public void recordCompleted(String fileName) {
                    // call sendFile from Persistence
                }
            });
        }

        mIsStreaming = !mIsStreaming;
    }
}
