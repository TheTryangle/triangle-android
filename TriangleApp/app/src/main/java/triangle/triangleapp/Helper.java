package triangle.triangleapp;

/**
 * Created by D2110175 on 9-6-2017.
 */

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Helper {
    private static final String TAG = "Helper";

    public static String getConfigValue(Context context, String name) {
        Resources resources = context.getResources();

        try {
            InputStream rawResource = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Failed to open config file.");
        }

        return null;
    }
    /*
    Use like this:
        String <varname> = Helper.getConfigValue(< context>, "<key in config file>");
        String apiUrl = Helper.getConfigValue(this, "api_url");
        String apiKey = Helper.getConfigValue(this, "api_key");

     */

}