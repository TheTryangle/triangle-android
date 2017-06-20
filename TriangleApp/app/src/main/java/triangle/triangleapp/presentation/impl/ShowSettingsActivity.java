package triangle.triangleapp.presentation.impl;
import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.widget.TextView;

import triangle.triangleapp.R;

public class ShowSettingsActivity extends Activity {

    /**
     * onCreate
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_settings);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();
        // Append items in order of display.
        // Category 1, Identity
        builder.append("n" + sharedPrefs.getString("username", "NULL"));

        // Category 2, Security
        builder.append("n" + sharedPrefs.getString("key_algorithm", "NULL"));
        builder.append("n" + sharedPrefs.getString("sign_algorithm", "NULL"));
        builder.append("n" + sharedPrefs.getInt("key_size",1024));
        builder.append("n" + sharedPrefs.getString("key_alias", "NULL"));
        builder.append("n" + sharedPrefs.getString("key_store", "NULL"));

        //Category 3, Connection
        builder.append("n" + sharedPrefs.getString("stream_destination_address", "NULL"));

        // Category 4, Device
        builder.append("n" + sharedPrefs.getString("file_helper_save_location", "NULL"));
        builder.append("n" + sharedPrefs.getString("max_record_duration","NULL"));
        builder.append("n" + sharedPrefs.getString("display_orientation","NULL"));

        TextView settingsTextView = (TextView) findViewById(R.id.settings_text_view);
        settingsTextView.setText(builder.toString());
    }

}

