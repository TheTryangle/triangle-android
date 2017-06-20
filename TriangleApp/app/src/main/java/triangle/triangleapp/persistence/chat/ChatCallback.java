package triangle.triangleapp.persistence.chat;

import triangle.triangleapp.domain.ChatMessage;

/**
 * Created by marco on 20-6-2017.
 */

public interface ChatCallback {
    void onMessageReceived(ChatMessage message);
}
