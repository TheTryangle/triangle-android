package triangle.triangleapp.helpers;

import android.content.Context;
import android.util.Log;

import triangle.triangleapp.BuildConfig;

import triangle.triangleapp.ConfigHelper;


/**
 * Created by D2110175 on 16-6-2017.
 */

public class InitializationHelper {
    private static int versionCode;
    private static String versionName;
    // Cannot be final because they cannot be initialized yet.

    private ConfigHelper store;
    private Context context;
    private final String TAG = "InitializationHelper";
    private final String VERSION_INVALID = "failed";

    private static final String KEY_VERSION_CODE = "versionCode";
    private static final String KEY_VERSION_NAME = "versionName";

    private static final String KEY_USERNAME = "username";
    private static final String KEY_WEBSOCKET_PROTOCOL = "websocket_protocol";

    private static final String KEY_KEY_ALGORITHM = "key_algorithm";
    private static final String KEY_SIGN_ALGORITHM = "sign_algorithm";
    private static final String KEY_KEY_SIZE = "key_size";
    private static final String KEY_KEY_ALIAS = "key_alias";
    private static final String KEY_KEY_STORE = "key_store";
    private static final String KEY_WEBAPI_DESTINATION_ADDRESS = "webapi_destination_address";
    private static final String KEY_FILE_HELPER_SAVE_LOCATION = "file_helper_save_location";
    private static final String KEY_MAX_RECORD_DURATION = "max_record_duration";
    private static final String KEY_DISPLAY_ORIENTATION = "display_orientation";


    /**
     * Constructor
     *
     * @param c a context
     */
    public InitializationHelper(Context c) {
        this.context = c;
        store = ConfigHelper.getInstance(context);
        checkVersion();
    }

    /**
     * Checks the app version, if different from config version, calls setConfigValues
     */
    private void checkVersion() {

        versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;


        int configCode = store.getInt(KEY_VERSION_CODE);

        if (versionCode != configCode || configCode == 0) {
            Log.i(TAG, "diff versions: " + versionCode + " & " + store.getInt(KEY_VERSION_CODE));
            setConfigValues();
        } else {
            String socketIp = store.get(KEY_WEBSOCKET_PROTOCOL);
            if (socketIp == null || socketIp.equals("")) {
                setConfigValues();
            }
        }

    }

    /**
     * sets default config values, runs after cache is cleared, clean install, version changes and possibly after device reboots
     */
    private void setConfigValues() {
        store.putInt(KEY_VERSION_CODE, versionCode);
        store.put(KEY_VERSION_NAME, versionName);
        store.put(KEY_WEBSOCKET_PROTOCOL, "ws");

        store.put(KEY_USERNAME, "anoniem");

        store.put(KEY_WEBAPI_DESTINATION_ADDRESS, "http://188.226.164.87/server/api/");

        store.put(KEY_KEY_ALGORITHM, "RSA");
        store.put(KEY_SIGN_ALGORITHM, "SHA1withRSA");
        store.put(KEY_KEY_SIZE, "1024");
        store.put(KEY_KEY_ALIAS, "TriangleKey");
        store.put(KEY_KEY_STORE, "AndroidKeyStore");
        store.put(KEY_FILE_HELPER_SAVE_LOCATION, "TriangleApp");
        store.put(KEY_MAX_RECORD_DURATION, "4000");
        store.put(KEY_DISPLAY_ORIENTATION, "90");
        store.save();


    }
}
