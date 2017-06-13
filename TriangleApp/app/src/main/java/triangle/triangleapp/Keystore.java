package triangle.triangleapp;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Keystore {
    private static Keystore store;
    private SharedPreferences SP;
    private static String filename="Keys";

    private Keystore(Context context) {
        SP = context.getApplicationContext().getSharedPreferences(filename,0);
    }

    public static Keystore getInstance(Context context) {
        if (store == null) {
            Log.v("Keystore","NEW STORE");
            store = new Keystore(context);
        }
        return store;
    }

    public void put(String key, String value) {//Log.v("Keystore","PUT "+key+" "+value);
        Editor editor;

        editor = SP.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String get(String key) {//Log.v("Keystore","GET from "+key);
        return SP.getString(key, null);

    }

    public int getInt(String key) {//Log.v("Keystore","GET INT from "+key);
        return SP.getInt(key, 0);
    }

    public void putInt(String key, int num) {//Log.v("Keystore","PUT INT "+key+" "+String.valueOf(num));
        Editor editor;
        editor = SP.edit();

        editor.putInt(key, num);
        //editor.commit();
        editor.apply();
    }


    public void save(){
        Editor editor;
        editor = SP.edit();
        editor.commit();
    }
    public void clear(){
        Editor editor;
        editor = SP.edit();

        editor.clear();
        editor.commit();
    }

    public void remove(){
        Editor editor;
        editor = SP.edit();

        editor.remove(filename);
        editor.commit();
    }

    /*
    usage:

    private Keystore store;//Holds our key pairs
     public void test(Context context){
        store = Keystore.getInstance(context);//Creates or Gets our key pairs.  You MUST have access to current context!

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