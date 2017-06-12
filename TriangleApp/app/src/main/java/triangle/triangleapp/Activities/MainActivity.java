package triangle.triangleapp.Activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import triangle.triangleapp.Helpers.CameraHelper;
import triangle.triangleapp.Helpers.CameraPreview;
import triangle.triangleapp.Helpers.MediaHelper;
import triangle.triangleapp.R;

public class MainActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private CameraHelper cameraHelper = new CameraHelper();
    private MediaHelper mediaHelper = new MediaHelper();
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private Button captureButton;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkCameraHardware(this)) {
            // Create an instance of Camera
            mCamera = CameraHelper.getCameraInstance();

            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);

            captureButton = (Button) findViewById(R.id.button_capture);
            captureButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                cameraHelper.record();
            }
          });
            initWebsocket();
        }
    }

    @Override protected void onPause() {
        super.onPause();
        cameraHelper.releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        cameraHelper.releaseCamera();              // release the camera immediately on pause event
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        // this device has a camera
        // no camera on this device
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void initWebsocket() {
        // Send the file
        String url = "ws://145.49.35.215:1234/send";
        String protocol = "WS";
        AsyncHttpClient.getDefaultInstance()

        .websocket(url, protocol, new AsyncHttpClient.WebSocketConnectCallback() {
            @Override public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                  ex.printStackTrace();
                  return;
                }

                websocketConnected = true;
                mWebSocketInstance = webSocket;
            }
        });
    }

    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(mediaHelper.getOutputMediaFile());
    }


}
