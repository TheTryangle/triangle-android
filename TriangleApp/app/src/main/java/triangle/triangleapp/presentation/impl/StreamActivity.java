package triangle.triangleapp.presentation.impl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import triangle.triangleapp.R;
import triangle.triangleapp.logic.ConnectionManager;
import triangle.triangleapp.logic.NetworkBroadcastReceiver;
import triangle.triangleapp.logic.StreamManager;
import triangle.triangleapp.presentation.ConnectionState;
import triangle.triangleapp.presentation.StreamPresenter;
import triangle.triangleapp.presentation.StreamView;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class StreamActivity extends AppCompatActivity implements StreamView {
    private StreamPresenter mPresenter;
    private SurfaceView mCameraSurfaceView;
    private Button mButtonStream;
    private ConnectionManager mConnectionManager;
    private static boolean firstStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mPresenter = new StreamPresenter(this);
        mConnectionManager = new ConnectionManager(this);

        mCameraSurfaceView = (SurfaceView) findViewById(R.id.surface_camera_preview);
        mButtonStream = (Button) findViewById(R.id.btn_stream);
        mButtonStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mConnectionManager.isNetworkAvailable()) {
                    mPresenter.stream();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.notify_disconnected, Toast.LENGTH_SHORT);
                }



//                Toast.makeText(getApplicationContext(), "DEZE MOET DIE NIET LATEN ZIEN", Toast.LENGTH_SHORT);
//
//                if (isNetworkAvailable()) {

                    // Start or stop the stream


//                    Toast.makeText(getApplicationContext(), "DEZE MOET DIE OOK NIET LATEN ZIEN", Toast.LENGTH_SHORT);
//
//                } else {
//
//                }
            }
        });
    }

    @Override
    public void streamStarted() {
        mButtonStream.setText(R.string.btn_stop_streaming);
        Toast.makeText(this, R.string.notify_streaming_started, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void streamStopped() {
        mButtonStream.setText(R.string.btn_start_streaming);
        Toast.makeText(this, R.string.notify_streaming_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPreview() {
        mCameraSurfaceView.setVisibility(View.VISIBLE);

    }

    @Override
    public void hidePreview() {
        mCameraSurfaceView.setVisibility(View.INVISIBLE);
    }

    @Override
    public Surface getPreviewSurface() {
        SurfaceHolder holder = mCameraSurfaceView.getHolder();
        if (holder != null) {
            return holder.getSurface();
        }
        return null;
    }

    @Override
    public void networkConnectivityChanged(@ConnectionState int state, @Nullable String reason) {

        // Do not execute on start
        if (!firstStart) {

            switch (state) {
                case ConnectionState.CONNECTED: {

                    Toast.makeText(getApplicationContext(), R.string.notify_connected, Toast.LENGTH_LONG).show();

                    break;
                }
                case ConnectionState.DISCONNECTED: {

                    Toast.makeText(getApplicationContext(), R.string.notify_disconnected, Toast.LENGTH_LONG).show();
                    if (mPresenter.isStreaming()) {
                        mPresenter.stream();
                    }

                    break;
                }
            }

        } else {
            firstStart = false;
        }
    }
}