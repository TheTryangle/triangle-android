package triangle.triangleapp.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import triangle.triangleapp.R;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if we have all the required permissions
        boolean hasPermissions = hasRequiredPermissions();

        if (hasPermissions) {
            // Flow can continue as normal
            initialize();
        } else {
            requestPermissions();
        }
    }

    /**
     * Requests the required permissions for this app.
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 3) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                        // All the permissions have been granted, we can continue normally
                        initialize();
                    } else {
                        // Some permissions where denied

                        // Create and show an alert to re-request.
                        AlertDialog alertDialog = new AlertDialog.Builder(this)
                                .setTitle(R.string.permission_request)
                                .setMessage(R.string.permission_denied_error_title)
                                .setPositiveButton(R.string.permission_grant, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        requestPermissions();
                                    }
                                })
                                .setNegativeButton(R.string.permission_cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                }).create();

                        alertDialog.show();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * Checks if this application has all the required permissions
     *
     * @return true if we have the required permissions else false.
     */
    private boolean hasRequiredPermissions() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA);
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasMicrophonePermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO);

        return PackageManager.PERMISSION_GRANTED == hasCameraPermission && PackageManager.PERMISSION_GRANTED == hasWriteStoragePermission && PackageManager.PERMISSION_GRANTED == hasReadStoragePermission && PackageManager.PERMISSION_GRANTED == hasMicrophonePermission;
    }

    /**
     * Continues the initialization flow.
     */
    private void initialize() {
        Button btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(cameraIntent);
            }
        });
    }
}
