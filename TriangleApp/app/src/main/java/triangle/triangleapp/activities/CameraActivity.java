package triangle.triangleapp.activities;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import triangle.triangleapp.helpers.CameraHelper;
import triangle.triangleapp.view.CameraPreview;
import triangle.triangleapp.R;
import triangle.triangleapp.helpers.MediaHelper;

/**
 * Created by Kevin Ly on 6/12/2017.
 */

public class CameraActivity extends AppCompatActivity{
    private Button btnCapture, btnSwitchCamera;
    private CameraHelper cameraHelper;
    private CameraPreview cameraPreview;
    private Camera mCamera;
    private MediaHelper mediaHelper;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (CameraHelper.checkCameraHardware(getApplicationContext())){
            cameraPreview = new CameraPreview(this);
            cameraHelper = new CameraHelper(getApplicationContext(), cameraPreview);
        }

        mediaHelper = new MediaHelper(cameraHelper);

        btnCapture = (Button) findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaHelper.setRecordingEnabled();
            }
        });

        btnSwitchCamera = (Button) findViewById(R.id.btnSwitchCamera);
        btnSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraHelper != null && !mediaHelper.isRecording() && cameraHelper.getAmountCameras() > 1){
                    cameraHelper.switchCamera();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraHelper != null) {
            cameraHelper.onResume();
            finish();
        }
    }
}
