package triangle.triangleapp;

import android.hardware.Camera;

/**
 * Created by marco on 8-6-2017.
 */

public class CameraHelper {
    public static Camera getCameraInstance() {

        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
