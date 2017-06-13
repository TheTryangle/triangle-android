package triangle.triangleapp.helpers;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import triangle.triangleapp.view.CameraPreview;

/**
 * Created by danie on 9-6-2017.
 */

public class MediaHelper {
    private final String TAG = "MediaHelper";
    private boolean isRecording;
    private MediaRecorder mMediaRecorder;
    private CameraPreview mCameraPreview;
    private Camera mCamera;
    private final int MEDIA_TYPE_IMAGE = 1;
    private final int MEDIA_TYPE_VIDEO = 2;
    private CameraHelper mCameraHelper;
    private WebSocket webSocket;
    private String url;
    private String protocol;

    public MediaHelper(CameraHelper cameraHelper, CameraPreview cameraPreview) {
        mCameraPreview = cameraPreview;
        mCameraHelper = cameraHelper;
        isRecording = false;
        url = "ws://145.49.35.215:1234/send";
        protocol = "ws";
        webSocket = new WebSocket(url, protocol);
    }

    public void record(){
        if (isRecording) {
            isRecording = false;
            stopStreaming(true);
        } else {
            startStreaming(true);
        }
    }

    public boolean isRecording() {
        return isRecording;
    }

    private void initializeVideoRecorder(boolean firstInit) {
        if (firstInit) {
            mCamera = CameraHelper.getCameraInstance();
            mMediaRecorder = new MediaRecorder();

            mCamera.setDisplayOrientation(90);

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
        final String fileName = getOutputMediaFile(MEDIA_TYPE_VIDEO).toString();
        mMediaRecorder.setOutputFile(fileName);

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mCameraPreview.getHolder().getSurface());

        mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                // Handle the on duration exceeded
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    stopStreaming(false);
                    startStreaming(true);

                    if (webSocket.isConnected()) {
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
                        webSocket.sendStream(bytes);
                        file.delete();
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

        mMediaRecorder.setMaxDuration(5000);
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        switch (type) {
            case MEDIA_TYPE_IMAGE:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                break;
            case MEDIA_TYPE_VIDEO:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
                break;
        }
        return mediaFile;
    }

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
        isRecording = true;
    }

    private void stopStreaming(boolean fullStop) {
        // stop recording and release camera
        mMediaRecorder.stop();  // stop the recording
        mMediaRecorder.reset();
        if (fullStop) {
            releaseMediaRecorder(); // release the MediaRecorder object
        }
        mCamera.lock();         // take camera access back from MediaRecorder
    }

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

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }
}
