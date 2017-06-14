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
    private static final String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int mDisplayOrientation = 90;

    /**
     * Initialize CameraPreview
     * @param context Context of the application
     * @param camera Camera to use for preview
     */
    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);
    }

    /**
     * Create preview from surface, setPreviewDisplay and start Preview
     * @param holder The holder of the surface
     */
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setDisplayOrientation(mDisplayOrientation);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException ioe) {
            Log.e(TAG, "Error setting camera preview", ioe);
        }
    }

    /**
     * Destroy preview from surface
     * @param holder The holder of the surface
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    /**
     * Change preview from surface
     * @param holder The holder of surface
     * @param format The format of surface
     * @param w The width of the surface
     * @param h The height of the surface
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null){
            return;
        }

        try{
            mCamera.stopPreview();
        } catch (Exception ex){
            Log.e(TAG, "Error stopping preview", ex);
        }

        try{
            mCamera.setDisplayOrientation(mDisplayOrientation);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }
        catch (Exception ex){
            Log.e(TAG, "Error starting camera preview", ex);
        }
    }
}
