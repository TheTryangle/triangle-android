package triangle.triangleapp.helpers;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by danie on 9-6-2017.
 */

public class MediaHelper {
    private boolean isRecording;
    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private final int MEDIA_TYPE_IMAGE = 1;
    private final int MEDIA_TYPE_VIDEO = 2;
    private CameraHelper mCameraHelper;

    public MediaHelper(CameraHelper cameraHelper) {
        mCameraHelper = cameraHelper;
        isRecording = false;
    }

    public void setRecordingEnabled() {
        isRecording = true;
    }

    public void setRecordingDisabled() {
        isRecording = false;
    }

    public boolean isRecording() {
        return isRecording;
    }

    private void initializeVideoRecorder(boolean firstInit) {
        if (firstInit) {
            mCamera = mCameraHelper.getCamera();
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
        final String fileName = getOutputMediaFile(MEDIA_TYPE_VIDEO).toString();
        mMediaRecorder.setOutputFile(fileName);

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
        mMediaRecorder.setOnInfoListener(new OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                // Handle the on duration exceeded
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    stopStreaming(false);
                    startStreaming(true);

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
}
