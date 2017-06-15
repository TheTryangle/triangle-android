package triangle.triangleapp.persistence;

import android.support.annotation.NonNull;

import org.spongycastle.crypto.params.AsymmetricKeyParameter;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public interface StreamAdapter {
    void sendPublicKey(AsymmetricKeyParameter publicKey);

    void connect(WebSocketCallback callback);

    /**
     * Called when a new file should be sent
     *
     * @param fileName The file to send
     */
    void sendFile(AsymmetricKeyParameter privateKey, @NonNull String fileName);
}
