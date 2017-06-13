package triangle.triangleapp.helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Kevin Ly on 6/12/2017.
 */

public class CameraHelper {
    private static final String TAG = "CameraHelper";

    public CameraHelper() {

    }

    public static boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try{
            c = Camera.open();
        }
        catch (Exception ex){
            Log.e(TAG, "Error occured while open camera.", ex);
        }
        return c;
    }

    public void onResume(Context context) {
        if (!checkCameraHardware(context)) {
            Toast toast = Toast.makeText(context, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
