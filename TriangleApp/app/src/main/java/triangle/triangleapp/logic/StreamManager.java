package triangle.triangleapp.logic;

import android.util.Log;
import android.view.Surface;

import triangle.triangleapp.logic.impl.CameraLiveStream;
import triangle.triangleapp.persistence.StreamAdapter;
import triangle.triangleapp.persistence.impl.WebSocketStream;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class StreamManager {
    private static final String TAG = "StreamManager";
    private LiveStream mLiveStream;
    private boolean mIsStreaming;
    private Surface mPreviewView;
    private StreamAdapter streamAdapter;

    public StreamManager() {
        mLiveStream = new CameraLiveStream();
        streamAdapter = new WebSocketStream();
    }

    public void setPreviewView(Surface view) {
        this.mPreviewView = view;
    }

    public boolean stream() {
        if (mIsStreaming) {
            mLiveStream.stop();
        } else {
            mLiveStream.setPreviewView(mPreviewView);
            mLiveStream.start(new FileRecordedCallback() {
                @Override
                public void recordCompleted(String fileName) {
                    Log.i(TAG, "File completed, " + fileName);
                    streamAdapter.sendFile(fileName);
                }
            });
        }
        mIsStreaming = !mIsStreaming;

        return mIsStreaming;
    }
}
