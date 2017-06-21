package triangle.triangleapp.presentation.stream.impl;

import android.os.Bundle;

import triangle.triangleapp.R;
import triangle.triangleapp.domain.ChatAction;
import triangle.triangleapp.helpers.AdapterType;
import triangle.triangleapp.presentation.stream.StreamPresenter;
import triangle.triangleapp.presentation.stream.StreamView;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class StreamActivity extends AppCompatActivity implements StreamView {
    private static final String TAG = "StreamActivity";
    private StreamPresenter mPresenter;
    private SurfaceView mCameraSurfaceView;
    private Button mButtonStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mPresenter = new StreamPresenter(this);

        mCameraSurfaceView = (SurfaceView) findViewById(R.id.surface_camera_preview);
        mButtonStream = (Button) findViewById(R.id.btn_stream);
        mButtonStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start or stop the stream
                mPresenter.stream();
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
    public void connected(@AdapterType int type) {
        Log.i(TAG, "Connected adapter = " + type);
    }

    @Override
    public void errorOccurred(@AdapterType int type, @NonNull final Exception exception, boolean fatal) {
        Log.e(TAG, "Error adapter = " + type, exception);

        //Determine error text
        String exceptionType = exception.getClass().getSimpleName();

        final String errorMsg;
        switch(exceptionType) {
            case "ConnectException":
                errorMsg = getString(R.string.err_connect_server);
                break;
            case "PEMException":
                errorMsg = getString(R.string.err_decode_server_pubkey);
                break;
            default:
                Log.e(TAG, "Unhandled exception type: " + exceptionType);
                errorMsg = getString(R.string.err_unknown);
                break;
        }

        //Show toast on UI thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int toastDuration = Toast.LENGTH_LONG;
                Toast.makeText(StreamActivity.this, errorMsg, toastDuration).show();
            }
        });

        if(fatal)
        {
            this.finish();
        }
    }

    @Override
    public void showMessage(@NonNull ChatAction message) {
        Log.i(TAG, "Received message, message = " + message.getMessage());
    }

    public void showCount() {

    }
}
