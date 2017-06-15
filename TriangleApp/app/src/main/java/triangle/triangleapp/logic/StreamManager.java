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

    public boolean stream() {
        if (mIsStreaming) {
            mLivestream.stop();
        } else {
            mLivestream.setPreviewView(mPreviewView);
            mLivestream.start(new FileRecordedCallback() {
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
