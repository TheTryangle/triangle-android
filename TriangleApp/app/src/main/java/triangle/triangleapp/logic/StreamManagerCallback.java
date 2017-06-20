package triangle.triangleapp.logic;

import android.support.annotation.NonNull;

import triangle.triangleapp.domain.ChatMessage;

/**
 * Created by marco on 20-6-2017.
 */

public interface StreamManagerCallback {
    /**
     * Called when a ChatMessage is received
     *
     * @param message The message that is received
     */
    void chatMessageReceived(ChatMessage message);

    void streamConnected();

    void streamError(@NonNull Exception exception);

    void chatConnected();

    void chatError(@NonNull Exception exception);
}
