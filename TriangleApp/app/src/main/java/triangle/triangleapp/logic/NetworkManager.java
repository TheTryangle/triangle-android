package triangle.triangleapp.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import triangle.triangleapp.presentation.NetworkState;

/**
 * Created by Koen on 16-06-17.
 */

public class NetworkManager extends BroadcastReceiver {

    private static final String TAG = "NetworkManager";
    private NetworkState mNetworkState;

    public NetworkManager(NetworkState networkStateCallback) {
        super();

        mNetworkState = networkStateCallback;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            Log.e(TAG, "Connection established");
            mNetworkState.connected();

        } else {
            Log.e(TAG, "Connection lost");
            mNetworkState.disconnected();
        }
    }
}