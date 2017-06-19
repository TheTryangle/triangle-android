package triangle.triangleapp.persistence;

/**
 * Created by danie on 15-6-2017.
 */

public interface ConnectionCallback {
    /**
     * Called when the adapter is connected
     */
    void onConnected();

    /**
     * Called when an error occurs
     * @param ex The error that occurred
     */
    void onError(Exception ex);
}
