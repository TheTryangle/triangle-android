package triangle.triangleapp;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ConfigHelper {
    private static ConfigHelper store;
    private SharedPreferences SP;
    private static String filename="Keys";

    /**
     * sets variables, internal constructor called from getinstance
     * @param context activity context
     */
    private ConfigHelper(Context context) {
        SP = context.getApplicationContext().getSharedPreferences(filename,0);
    }

    /**
     * gets an instance
     * @param context activity context
     * @return static configHelper instance
     */
    public static ConfigHelper getInstance(Context context) {
        if (store == null) {
            store = new ConfigHelper(context);
        }
        return store;
    }

    /**
     * should be used only after instance is created somewhere
     * @return static configHelper instance
     */
    public static ConfigHelper getInstance() {
        return store;
    }

    /**
     * puts string key-value pair in config, overwrites existing key-value pair if existing
     * @param key string key value
     * @param value string value
     */
    public void put(String key, String value) {
        Editor editor;
        editor = SP.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * gets the value from given key
     * @param key key to get value from
     * @return string value
     */
    public String get(String key) {
        return SP.getString(key, null);
    }

    /**
     * gets int value from key
     * @param key key to get value from
     * @return int value from key
     */
    public int getInt(String key) {
        return SP.getInt(key, 0);
    }

    /**
     * puts int key-value pair in config, overwrites existing key-value pair if existing
     * @param key string key
     * @param num int value
     */
    public void putInt(String key, int num) {
        Editor editor;
        editor = SP.edit();
        editor.putInt(key, num);
        editor.apply();
    }

    /**
     * saves pendng changes
     */
    public void save(){
        Editor editor;
        editor = SP.edit();
        editor.commit();
    }

    /**
     * clears config file
     */
    public void clear(){
        Editor editor;
        editor = SP.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * deletes config file
     */
    public void remove(){
        Editor editor;
        editor = SP.edit();
        editor.remove(filename);
        editor.commit();
    }

    /*
    usage:

    private ConfigHelper store;//Holds our key pairs
     public void test(Context context){
        store = ConfigHelper.getInstance(context);//Creates or Gets our key pairs.  You MUST have access to current context!

        store.putInt("initial_int",5);
        store.put("initial_string","initstring");

        store.putInt(<key>,<int value>);
        store.put(<key>,<string value>);
        store.save();

        int getIntVal= store.getInt("initial_int");
        String getStringVal= store.get("initial_string");

        int getIntVal= store.getInt(<key>);
        String getStringVal= store.get(<key>);

        updating values is possible to simply overwrite them.

    }

     */
}