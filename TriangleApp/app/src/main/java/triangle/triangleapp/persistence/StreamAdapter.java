package triangle.triangleapp.persistence;

import android.support.annotation.NonNull;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public interface StreamAdapter {
    void sendPublicKey(PublicKey publicKey);

    void connect(ConnectionCallback callback);

    /**
     * Called when a new file should be sent
     *
     * @param fileInBytes File in bytes
     */
    void sendFile(@NonNull byte[] fileInBytes, PrivateKey privateKey);
}
