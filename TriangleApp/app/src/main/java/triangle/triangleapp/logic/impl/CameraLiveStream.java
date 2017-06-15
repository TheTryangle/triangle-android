package triangle.triangleapp.logic.impl;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;

import java.io.IOException;

import triangle.triangleapp.helpers.CameraHelper;
import triangle.triangleapp.helpers.MediaFileHelper;
import triangle.triangleapp.helpers.WebSocket;
import triangle.triangleapp.logic.FileRecordedCallback;
import triangle.triangleapp.logic.Livestream;
import triangle.triangleapp.view.CameraPreview;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class CameraLiveStream implements Livestream {
    private static final String TAG = "CameraLive";
    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private int mMediaRecorderMaxDuration = 5000;
    private FileRecordedCallback mCallback;
    private Surface mPreviewSurface;

    @Override
    public void start(FileRecordedCallback fileRecordedCallback) {
        mCallback = fileRecordedCallback;
        startStreaming(true);
    }

    @Override
    public void stop() {
        stopStreaming(true);

    }

    @Override
    public void setPreviewView(Surface surface) {
        mPreviewSurface = surface;
    }

    /**
     * Initializing mediarecorder, set settings and send stream via web socket.
     *
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

        final String fileName = MediaFileHelper.getOutputMediaFile().toString();
        // Step 4: Set output file
        mMediaRecorder.setOutputFile(fileName);

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mPreviewSurface);

        mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                // Handle the on duration exceeded
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    stopStreaming(false);
                    startStreaming(true);

                    mCallback.recordCompleted(fileName);
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
     *
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
    }

    /**
     * Stop streaming from camera to server.
     *
     * @param fullStop To release mediarecorder when it is stopped.
     */
    private void stopStreaming(boolean fullStop) {
        // stop recording
        mMediaRecorder.stop();
        mMediaRecorder.reset();

        // release the MediaRecorder object
        if (fullStop) {
            releaseMediaRecorder();
        }

        // take camera access back from MediaRecorder
        mCamera.lock();
    }

    /**
     * Prepare mediarecorder to record.
     *
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
            // clear recorder configuration
            mMediaRecorder.reset();

            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;

            // lock camera for later use
            mCamera.lock();
        }
    }
}
