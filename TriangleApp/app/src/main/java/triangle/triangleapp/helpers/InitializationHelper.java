package triangle.triangleapp.helpers;

import android.content.Context;
import android.util.Log;

import triangle.triangleapp.BuildConfig;
import triangle.triangleapp.Keystore;

/**
 * Created by D2110175 on 16-6-2017.
 */

public class InitializationHelper {
    private static int VERSION_CODE;
    private static String VERSION_NAME;
    // Cannot be final because they cannot be initialized yet.
    private Keystore store;
    private Context context;
    private final String TAG= "InitializationHelper";

    /**
     * Constructor
     * @param c a context
     */
    public InitializationHelper(Context c){
        this.context=c;
        store = Keystore.getInstance(context);
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
            VERSION_NAME="failed";
        }

        int configCode =store.getInt("version_code");
        if(VERSION_NAME.equals("failed")){
            //app cant get own runtime variables, i'd say a crash is approperiate?
            Log.e(TAG,"App cannot load version, crash approperiate?");
        }
        if(VERSION_CODE!=configCode||configCode==0){
            Log.i(TAG,"diff versions: "+VERSION_CODE+" & "+store.getInt("version_code"));
            setConfigValues();
        }else{
            String socketIp =store.get("destination_video_stream_websocket");
            if (socketIp == null || socketIp == "") {
                setConfigValues();
            }
        }

    }

    /**
     * sets default config values, runs after cache is cleared, clean install, version changes and possibly after device reboots
     */
    private void setConfigValues(){
        store.putInt("version_code",VERSION_CODE);
        store.put("version_name",VERSION_NAME);
        store.put("destination_video_stream_websocket","ws://145.49.30.113:1234/send");
        store.put("websocket_protocol","ws");
        store.put("hash_algorithm","SHA-256");
        store.save();
    }
}
