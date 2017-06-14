package triangle.triangleapp.activities;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import triangle.triangleapp.helpers.CameraHelper;
import triangle.triangleapp.view.CameraPreview;
import triangle.triangleapp.R;
import triangle.triangleapp.helpers.MediaHelper;

/**
 * Created by Kevin Ly on 6/12/2017.
 */

public class CameraActivity extends AppCompatActivity{
    private CameraPreview cameraPreview;
    private Camera mCamera;
    private MediaHelper mediaHelper;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (CameraHelper.checkCameraHardware(this)){
            mCamera = CameraHelper.getCameraInstance();
            cameraPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(cameraPreview);
        }

        mediaHelper = new MediaHelper(cameraPreview);

        Button btnCapture = (Button) findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaHelper.record();
            }
        });
    }
}
