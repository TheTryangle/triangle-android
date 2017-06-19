package triangle.triangleapp.presentation;

/**
 * Created by Koen on 19-06-17.
 */

public interface NetworkState {

    /**
     * Called when the connection is established
     */
    void connected();

    /**
     * Called when the connection is lost
     */
    void disconnected();
}
