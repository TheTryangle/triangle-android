package triangle.triangleapp.persistence;

import android.support.annotation.NonNull;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public interface StreamAdapter {
    /**
     * Called when connection is established.
     * @param callback The callback which send to.
     */
    void connect(ConnectionCallback callback);

    /**
     * Called when a new file should be sent
     *
     * @param fileInBytes File in bytes
     */
    void sendFile(@NonNull byte[] fileInBytes);
}
