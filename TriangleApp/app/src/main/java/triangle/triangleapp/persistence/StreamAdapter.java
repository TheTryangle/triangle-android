package triangle.triangleapp.persistence;

import android.support.annotation.NonNull;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public interface StreamAdapter {
    /**
     * Called when a new file should be sent
     *
     * @param fileName The file to send
     */
    void sendFile(@NonNull String fileName);
}
