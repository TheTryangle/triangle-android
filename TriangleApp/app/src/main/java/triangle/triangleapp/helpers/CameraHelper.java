package triangle.triangleapp.helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.widget.Toast;

import triangle.triangleapp.view.CameraPreview;

/**
 * Created by Kevin Ly on 6/12/2017.
 */

public class CameraHelper {
    private Context context;
    private CameraPreview mCameraPreview;
    private Camera mCamera;
    private boolean isCameraFront = false;
    private int camId;
    private int amountCameras;

    public CameraHelper(Context context, CameraPreview cameraPreview) {
        this.mCameraPreview = cameraPreview;
        this.context = context;
    }

    public static boolean checkCameraHardware(Context myContext) {
        return myContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public void switchCamera() {
        amountCameras = Camera.getNumberOfCameras();
        if (amountCameras > 1) {
            releaseCamera();
            chooseCamera();
        } else {
            Toast toast = Toast.makeText(context, "Sorry, your phone has only one camera!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public int getAmountCameras() {
        return amountCameras;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    public void chooseCamera() {
        int cameraId = 0;
        if (isCameraFront) {
            cameraId = findBackFacingCamera();
        } else {
            cameraId = findFrontFacingCamera();
        }

        if (cameraId >= 0) {
            mCamera = Camera.open(cameraId);
            mCameraPreview.refreshCamera(mCamera);
        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try{
            c = Camera.open();
        }
        catch (Exception ex){

        }
        return c;
    }

    public void onResume() {
        if (!checkCameraHardware(context)) {
            Toast toast = Toast.makeText(context, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
            toast.show();
        }
        if (mCamera == null) {
            if (findFrontFacingCamera() <= 0) {
                Toast toast = Toast.makeText(context, "No front facing camera found.", Toast.LENGTH_LONG);
                toast.show();
            }
        }

            //mCamera = Camera.open(findBackFacingCamera());
            //mCameraPreview.refreshCamera(mCamera);

    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                isCameraFront = false;
                break;
            }
        }
        camId = cameraId;
        return cameraId;
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                isCameraFront = true;
                break;
            }
        }
        camId = cameraId;
        return cameraId;
    }
}
