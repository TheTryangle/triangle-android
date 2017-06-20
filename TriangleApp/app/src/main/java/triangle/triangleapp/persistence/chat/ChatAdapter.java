package triangle.triangleapp.persistence.chat;

import android.support.annotation.NonNull;

import triangle.triangleapp.domain.ChatMessage;
import triangle.triangleapp.persistence.ConnectionCallback;

/**
 * Created by marco on 20-6-2017.
 */

public interface ChatAdapter {
    void connect(@NonNull ConnectionCallback connectionCallback);

    void sendMessage(ChatMessage message);

    void setCallback(ChatCallback chatCallback);
}
