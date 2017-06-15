package triangle.triangleapp.logic;

import android.view.Surface;
import android.view.SurfaceView;

import java.io.File;

import triangle.triangleapp.helpers.MediaFileHelper;
import triangle.triangleapp.logic.impl.CameraLiveStream;
import triangle.triangleapp.persistence.StreamAdapter;
import triangle.triangleapp.persistence.impl.WebSocketStream;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class StreamManager {
    private Livestream mLivestream;
    private boolean mIsStreaming;
    private Surface mPreviewView;
    private StreamAdapter streamAdapter;

    public StreamManager() {
        mLivestream = new CameraLiveStream();
        streamAdapter = new WebSocketStream();
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
                    streamAdapter.sendFile(fileName);
                }
            });
        }
        mIsStreaming = !mIsStreaming;
    }
}
