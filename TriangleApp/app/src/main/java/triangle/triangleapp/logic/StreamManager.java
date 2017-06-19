package triangle.triangleapp.logic;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;

import java.security.KeyPair;
import java.security.PublicKey;

import triangle.triangleapp.helpers.IntegrityHelper;
import triangle.triangleapp.logic.impl.CameraLiveStream;
import triangle.triangleapp.persistence.ConnectionCallback;
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
    private StreamAdapter mStreamAdapter;
    private KeyPair mKeyPair;

    public StreamManager() {
        mLiveStream = new CameraLiveStream();
        mStreamAdapter = new WebSocketStream();

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
            }

            @Override
            public void onError(@NonNull Exception ex) {
                Log.e(TAG, "Error occurred during connecting", ex);
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
}
