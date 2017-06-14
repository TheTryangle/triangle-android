package triangle.triangleapp.helpers;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kevin Ly on 6/13/2017.
 */

public class MediaFileHelper {
    private static final String TAG = "MediaFileHelper";
    private static final String SAVE_LOCATION = "TriangleApp";

    /**
     * Obtain media file from storage.
     * @param type 1: .jpg, 2: .mp4.
     * @return The file of directory
     */
    public File getOutputMediaFile(int type) {
        File mediaFile = null;
        try {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), SAVE_LOCATION);
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            switch (type) {
                case 1:
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                    break;
                case 2:
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported file type.");
            }
        } catch (Exception ex){
            Log.e(TAG, "Error occured while get output media file.", ex);
        }
        return mediaFile;
    }

    //FIXME: during run, IOexc is called while trying to read .jpg file. why is this even trying to acces .jpg files?
    /**
     * Convert filename to byte array.
     * @param fileName path to filename.
     * @return fileName in String
     */
    public byte[] getBytesFromFile(String fileName){
        byte[] bytes = null;
        try {
            File file = new File(fileName);
            int size = (int) file.length();
            bytes = new byte[size];

            byte tmpBuff[] = new byte[size];
            try {
                FileInputStream fis = new FileInputStream(file);

                int read = fis.read(bytes, 0, size);
                if (read < size) {
                    int remain = size - read;
                    while (remain > 0) {
                        read = fis.read(tmpBuff, 0, remain);
                        System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                        remain -= read;
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "IoExc", e);
            }
            file.delete();
        } catch (Exception ex){
            Log.e(TAG, "Error occured while get bytes from file.", ex);
        }
        return bytes;
    }
}
