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

    /**
     * Called when an error occurs during streaming
     * @param stringResId The resource ID of the error message.
     */
    void streamError(@NonNull int stringResId);

    /**
     * Called when an error occurs during streaming
     * @param stringResId The resource ID of the error message.
     * @param fatal Whether the stream should be stopped or not.
     */
    void streamError(@NonNull int stringResId, boolean fatal);
}
