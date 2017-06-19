package triangle.triangleapp.presentation;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import triangle.triangleapp.TriangleApplication;
import triangle.triangleapp.logic.NetworkBroadcastReceiver;
import triangle.triangleapp.logic.NetworkChangeCallback;
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

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        NetworkBroadcastReceiver broadcastReceiver = new NetworkBroadcastReceiver();
        broadcastReceiver.setNetworkChangeCallback(new NetworkChangeCallback() {
            @Override
            public void onConnected() {
                mView.networkConnectivityChanged(ConnectionState.CONNECTED);
            }

            @Override
            public void onDisconnected() {
                mView.networkConnectivityChanged(ConnectionState.DISCONNECTED);
            }
        });
        TriangleApplication.getAppContext().registerReceiver(broadcastReceiver, filter);
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
