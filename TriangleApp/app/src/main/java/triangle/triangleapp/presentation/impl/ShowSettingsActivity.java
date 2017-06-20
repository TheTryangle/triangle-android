package triangle.triangleapp.presentation.impl;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import triangle.triangleapp.R;

public class ShowSettingsActivity extends Activity {

  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_settings);
    }*/

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
        builder.append("n" + sharedPrefs.getString("hash_algorithm", "NULL"));

        //Category 3, Connection
        builder.append("n" + sharedPrefs.getString("stream_destination_address", "NULL"));

        TextView settingsTextView = (TextView) findViewById(R.id.settings_text_view);
        settingsTextView.setText(builder.toString());
    }

}
