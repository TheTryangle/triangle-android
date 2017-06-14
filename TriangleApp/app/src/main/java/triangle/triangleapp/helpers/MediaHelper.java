package triangle.triangleapp.helpers;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import java.io.IOException;
import triangle.triangleapp.view.CameraPreview;

/**
 * Created by danie on 9-6-2017.
 */

public class MediaHelper {
    private static final String TAG = "MediaHelper";
    private boolean mIsRecording;
    private MediaRecorder mMediaRecorder;
    private MediaFileHelper mMediaFileHelper;
    private CameraPreview mCameraPreview;
    private Camera mCamera;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int MEDIA_TYPE_VIDEO = 2;
    private WebSocket mWebSocket;
    private String mUrl;
    private String mProtocol;
    private int mMediaRecorderMaxDuration;

    public MediaHelper(CameraPreview cameraPreview) {
        mCameraPreview = cameraPreview;
        mIsRecording = false;
        mMediaRecorderMaxDuration = 5000;
        mMediaFileHelper = new MediaFileHelper();
        mUrl = "ws://145.49.35.215:1234/send";
        mProtocol = "ws";
        mWebSocket = new WebSocket(mUrl, mProtocol);
    }

    /**
     * Start or stop record of stream.
     */
    public void record(){
        if (mIsRecording) {
            mIsRecording = false;
            stopStreaming(true);
        } else {
            startStreaming(true);
        }
    }

    /**
     * Initializing mediarecorder, set settings and send stream via web socket.
     * @param firstInit Check initializing on first time.
     */
    private void initializeVideoRecorder(boolean firstInit) {
        if (firstInit) {
            mCamera = CameraHelper.getCameraInstance();
            mMediaRecorder = new MediaRecorder();

            // Step 1: Unlock and set camera to MediaRecorder
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);
        }

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

        // Step 4: Set output file
        mMediaRecorder.setOutputFile(mMediaFileHelper.getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mCameraPreview.getHolder().getSurface());

        mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                // Handle the on duration exceeded
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    stopStreaming(false);
                    startStreaming(true);

                    if (mWebSocket.isConnected()) {
                        try {
                            mWebSocket.sendStream(mMediaFileHelper.getBytesFromFile(mMediaFileHelper.getOutputMediaFile(MEDIA_TYPE_VIDEO).toString()));
                        }
                        catch (Exception ex){
                            Log.e(TAG, "Error while send stream to server.", ex);
                        }
                    }
                }
            }
        });

        mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mediaRecorder, int what, int extra) {
                Log.d(TAG, "Error in mediarecorder, " + what + ", " + extra);
            }
        });

        mMediaRecorder.setMaxDuration(mMediaRecorderMaxDuration);
    }

    /**
     * Start streaming from camera to server.
     * @param firstStart Check if videorecorder is started for the first time.
     */
    private void startStreaming(boolean firstStart) {
        initializeVideoRecorder(firstStart);

        if (!prepareVideoRecorder()) {
            releaseMediaRecorder();
            return;
        }

        try {
            mMediaRecorder.start();
        } catch (Exception ex) {
            Log.e(TAG, "Error during start", ex);
        }
        mIsRecording = true;
    }

    /**
     * Stop streaming from camera to server.
     * @param fullStop To release mediarecorder when it is stopped.
     */
    private void stopStreaming(boolean fullStop) {
        // stop recording and release camera
        mMediaRecorder.stop();  // stop the recording
        mMediaRecorder.reset();
        if (fullStop) {
            releaseMediaRecorder(); // release the MediaRecorder object
        }
        mCamera.lock();         // take camera access back from MediaRecorder
    }

    /**
     * Prepare mediarecorder to record.
     * @return return state of preparation.
     */
    private boolean prepareVideoRecorder() {
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

    /**
     * Release and reset the mediarecorder. Locking the camera.
     */
    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }
}
