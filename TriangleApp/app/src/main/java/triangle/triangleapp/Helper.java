package triangle.triangleapp;

/**
 * Created by D2110175 on 9-6-2017.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Deprecated
public final class Helper {
    //NOTE acces only, writing doesntwork
    private static final String TAG = "Helper";

    public static String getConfigValue(Context context, String name) {
        Resources resources = context.getResources();

        try {
            InputStream rawResource = resources.openRawResource(R.raw.config);
           // InputStream rawResource = resources.openRawResource(resources.getIdentifier("products","raw", getPackageName()));
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Failed to open config file to get");
        }

        return null;
    }

    public static void addNewProperty(Context context, String key, String value) {
        Properties prop = new Properties();
        Resources resources = context.getResources();

        try {
            String propertiesPath = context.getFilesDir().getPath().toString() + "/config.properties";
            FileOutputStream out = new FileOutputStream(propertiesPath, true);
            prop.setProperty(key, value);
            prop.store(out, null);
            out.close();
        } catch (IOException e) {
            System.err.println("Failed to open config.properties file to add");
            e.printStackTrace();
        }

    }


    @TargetApi(Build.VERSION_CODES.N)
    public static boolean updateProperty(Context context, String key, String newValue){
        Properties prop = new Properties();
        Resources resources = context.getResources();

        try {
          //  FileOutputStream out = new FileOutputStream(resources.getResourceName(R.raw.config), true);
            String propertiesPath = context.getFilesDir().getPath().toString() + "/config.properties";
            FileOutputStream out = new FileOutputStream(propertiesPath,true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                prop.replace(key,newValue);

            }
            prop.store(out, null);
            out.close();
        } catch (IOException e) {
            System.err.println("Failed to open config.properties file for update");
            e.printStackTrace();
        }
        return true;
    }

    /*
    Use like this:
        String <varname> = Helper.getConfigValue(< context>, <key in config file>);
        String apiUrl = Helper.getConfigValue(this, "api_url");
        String apiKey = Helper.getConfigValue(this, "api_key");

        String <varname> = Helper.addNewProperty(<context>, <key>, <value>);

     */

}