package triangle.triangleapp.logic;

import android.support.annotation.NonNull;

/**
 * Created by marco on 21-6-2017.
 */

public interface StreamManagerCallback {

    /**
     * Called when the stream is connected
     */
    void streamConnected();

    /**
     * Called when an error occurs during streaming
     * @param exception The exception that occurred.
     */
    void streamError(@NonNull Exception exception);
}
