package triangle.triangleapp.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by marco on 8-6-2017.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private final String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException ioe) {
            Log.e(TAG, "Error setting camera preview", ioe);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        refreshCamera(mCamera);
    }

    public void refreshCamera(Camera camera) {
        if (mHolder.getSurface() == null) {
            return;
        }

        if (mCamera == null) {
            return;
        }

        try {
            mCamera.stopPreview();
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            mCamera = camera;
        } catch (Exception ex) {
            Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + ex.getMessage());
        }
    }
}
