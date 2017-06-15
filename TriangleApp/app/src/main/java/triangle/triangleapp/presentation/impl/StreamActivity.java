package triangle.triangleapp.presentation.impl;

import android.os.Bundle;

import triangle.triangleapp.R;
import triangle.triangleapp.presentation.StreamPresenter;
import triangle.triangleapp.presentation.StreamView;

import android.support.v7.app.AppCompatActivity;
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
    private StreamPresenter mPresenter;
    private SurfaceView mCameraSurfaceView;
    private Button mButtonCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mPresenter = new StreamPresenter(this);

        mCameraSurfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        mButtonCapture = (Button) findViewById(R.id.btnCapture);
        mButtonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start or stop the stream
                mPresenter.stream();
            }
        });
    }

    @Override
    public void streamStarted() {
        mButtonCapture.setText("Stop streaming");
        Toast.makeText(this, "Streaming started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void streamStopped() {
        mButtonCapture.setText("Start streaming");
        Toast.makeText(this, "Streaming stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Surface getPreviewSurface() {
        SurfaceHolder holder = mCameraSurfaceView.getHolder();
        if (holder != null) {
            return holder.getSurface();
        }
        return null;
    }
}
