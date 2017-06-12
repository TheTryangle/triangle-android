package triangle.triangleapp.Helpers;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import com.koushikdutta.async.http.WebSocket;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by marco on 8-6-2017.
 */

public class CameraHelper {

    private String TAG = "MainActivity";

    public Camera mCamera;
    public MediaHelper mediaHelper;
    public MediaRecorder mMediaRecorder;
    public CameraPreview mPreview;
    public boolean websocketConnected = false;
    public WebSocket mWebSocketInstance;

    public static Camera getCameraInstance() {

        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    public void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    public void record() {
        if (isRecording) {
            // stop recording and release camera
            mMediaRecorder.stop();  // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder

            // inform the user that recording has stopped
            captureButton.setText("Capture");
            isRecording = false;
        }
        else {
            // initialize video camera
            if (mediaHelper.getOutputMediaFile()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();

                // inform the user that recording has started
                captureButton.setText("Stop");
                isRecording = true;
            }
            else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                // inform user
            }
        }
    }

    public boolean prepareVideoRecorder() {

        mCamera = CameraHelper.getCameraInstance();
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

        // Step 4: Set output file
        final String fileName = MediaHelper.getOutputMediaFile(mediaHelper.MEDIA_TYPE_VIDEO).toString();
        mMediaRecorder.setOutputFile(fileName);

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override public void onInfo(MediaRecorder mr, int what, int extra) {
                // Handle the on duration exceeded
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    record();
                    record();

                    if (websocketConnected) {
                        File file = new File(fileName);

                        int size = (int) file.length();
                        byte bytes[] = new byte[size];
                        byte tmpBuff[] = new byte[size];

                        try {
                            FileInputStream fis = new FileInputStream(file);

                            int read = fis.read(bytes, 0, size);

                            if (read < size) {
                                int remain = size - read;
                                while (remain > 0) {
                                    read = fis.read(tmpBuff, 0, remain);
                                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                                    remain -= read;
                                }
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "IoExc", e);
                        }
                        mWebSocketInstance.send(bytes);
                    }
                }
            }
        });

        mMediaRecorder.setMaxDuration(1000);

        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.e(TAG, "IllegalStateException preparing MediaRecorder", e);
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.e(TAG, "IOException preparing MediaRecorder", e);
            releaseMediaRecorder();
            return false;
        }
        return true;
    }
}
