package triangle.triangleapp.persistence;

import android.support.annotation.NonNull;

import org.spongycastle.crypto.params.AsymmetricKeyParameter;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public interface StreamAdapter {
    void sendPublicKey(PublicKey publicKey);

    void connect(WebSocketCallback callback);

    /**
     * Called when a new file should be sent
     *
     * @param fileName The file to send
     */
    void sendFile(PrivateKey privateKey, @NonNull String fileName);
}
