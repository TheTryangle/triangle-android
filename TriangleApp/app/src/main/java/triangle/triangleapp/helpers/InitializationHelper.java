package triangle.triangleapp.helpers;

import android.content.Context;
import android.util.Log;
import triangle.triangleapp.BuildConfig;

import triangle.triangleapp.ConfigHelper;


/**
 * Created by D2110175 on 16-6-2017.
 */

public class InitializationHelper {
    private static int VERSION_CODE;
    private static String VERSION_NAME;
    // Cannot be final because they cannot be initialized yet.

    private ConfigHelper store;
    private Context context;
    private final String TAG= "InitializationHelper";
    private final String VERSION_INVALID="failed";

    private static final String KEY_VERSION_CODE="version_code";
    private static final String KEY_VERSION_NAME="version_name";
    private static final String KEY_DESTINATION_VIDEO_STREAM_WEBSOCKET= "destination_video_stream_websocket";
    private static final String KEY_WEBSOCKET_PROTOCOL="websocket_protocol";
    private static final String KEY_HASH_ALGORITHM="hash_algorithm";


    /**
     * Constructor
     * @param c a context
     */
    public InitializationHelper(Context c){
        this.context=c;
        store = ConfigHelper.getInstance(context);
        checkVersion();
    }

    /**
     * Checks the app version, if different from config version, calls setConfigValues
     */
    private void checkVersion(){
        try {
            VERSION_CODE = BuildConfig.VERSION_CODE;
            VERSION_NAME = BuildConfig.VERSION_NAME;
        }catch (Exception ex){
            ex.printStackTrace();
            VERSION_CODE = 0;
            VERSION_NAME=VERSION_INVALID;
        }

        int configCode =store.getInt(KEY_VERSION_CODE);
        if(VERSION_NAME.equals(VERSION_INVALID)){
            // App cant get own runtime variables, i'd say a crash is approperiate?
            Log.e(TAG,"App cannot load version, crash approperiate?");
        }
        if(VERSION_CODE!=configCode||configCode==0){
            Log.i(TAG,"diff versions: "+VERSION_CODE+" & "+store.getInt(KEY_VERSION_CODE));
            setConfigValues();
        }else{
            String socketIp =store.get(KEY_DESTINATION_VIDEO_STREAM_WEBSOCKET);
            if (socketIp == null || socketIp == "") {
                setConfigValues();
            }
        }

    }

    /**
     * sets default config values, runs after cache is cleared, clean install, version changes and possibly after device reboots
     */
    private void setConfigValues(){
        store.putInt(KEY_VERSION_CODE,VERSION_CODE);
        store.put(KEY_VERSION_NAME,VERSION_NAME);
        store.put(KEY_DESTINATION_VIDEO_STREAM_WEBSOCKET,"ws://145.49.30.113:1234/send");
        store.put(KEY_WEBSOCKET_PROTOCOL,"ws");
        store.put(KEY_HASH_ALGORITHM,"SHA-256");
        store.save();
    }
}
