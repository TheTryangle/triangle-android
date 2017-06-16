package triangle.triangleapp.logic;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;

import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.params.RSAKeyParameters;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

import triangle.triangleapp.helpers.CertifcateHelper;
import triangle.triangleapp.logic.impl.CameraLiveStream;
import triangle.triangleapp.persistence.StreamAdapter;
import triangle.triangleapp.persistence.WebSocketCallback;
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
    private KeyPair mKeyPair;

    public StreamManager() {
        mLiveStream = new CameraLiveStream();
        streamAdapter = new WebSocketStream();

        try {
            mKeyPair = CertifcateHelper.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        streamAdapter.connect(new WebSocketCallback() {
            @Override
            public void onConnected() {
                PublicKey pub = mKeyPair.getPublic();


                streamAdapter.sendPublicKey(pub);
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
                public void recordCompleted(String fileName) {
                    Log.i(TAG, "File completed, " + fileName);

                    streamAdapter.sendFile(mKeyPair.getPrivate(), fileName);
                }
            });
        }
        mIsStreaming = !mIsStreaming;

        return mIsStreaming;
    }
}
