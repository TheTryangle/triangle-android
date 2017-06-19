package triangle.triangleapp.persistence;

import android.support.annotation.NonNull;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public interface StreamAdapter {
    /**
     * Sends the public key to the stream
     *
     * @param publicKey The public key to send
     */
    void sendPublicKey(@NonNull PublicKey publicKey);

    /**
     * Starts connecting to the stream adapter
     *
     * @param callback The callback on which returns are called.
     */
    void connect(@NonNull final ConnectionCallback callback);

    /**
     * Called when a new file should be sent
     *
     * @param fileInBytes File in bytes
     */
    void sendFile(@NonNull byte[] fileInBytes, @NonNull PrivateKey privateKey);
}
