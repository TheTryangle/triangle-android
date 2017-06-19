package triangle.triangleapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Created by marco on 19-6-2017.
 */

public class TriangleApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        TriangleApplication.context = getApplicationContext();
    }

    /**
     * Gets the context of the application
     *
     * @return The ApplicationContext
     */
    public static Context getAppContext() {
        return TriangleApplication.context;
    }
}
