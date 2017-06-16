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
    private Keystore store;
    private Context c;
    private String TAG= "InitializationHelper";
    //TODO: checks version, if version is newer than current version in config, or if config is empty then set default values
    // Update this file with every update if necessary(ie. new IP's)

    public InitializationHelper(Context c){
        this.c=c;
        store = Keystore.getInstance(c);
        checkVersion();


    }

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
        Log.e(TAG,configCode+"");
        if(VERSION_CODE!=configCode||configCode==0){
            //VERSIONS DIFFERENT or config is 0
            Log.e(TAG,"diff versions: "+VERSION_CODE+" & "+store.getInt("version_code"));
            //TODO: re-init config function here
            setConfigValues();
        }else{
            String socketIp =store.get("destination_video_stream_websocket");
            if(socketIp!=null||socketIp!=""){
                //everything OK
            }else{
                setConfigValues();//secondary check
            }
        }

    }

    private void setConfigValues(){
        store.putInt("version_code",VERSION_CODE);
        store.put("version_name",VERSION_NAME);
        store.put("destination_video_stream_websocket","ws://145.49.30.113:1234/send");
        store.put("websocket_protocol","ws");
        store.put("hash_algorithm","SHA-256");
        store.save();
    }
}
