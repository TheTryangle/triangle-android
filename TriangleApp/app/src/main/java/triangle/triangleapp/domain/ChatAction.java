package triangle.triangleapp.domain;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import static triangle.triangleapp.domain.ChatAction.ActionType.ACTION_JOIN;
import static triangle.triangleapp.domain.ChatAction.ActionType.ACTION_LEAVE;
import static triangle.triangleapp.domain.ChatAction.ActionType.ACTION_MESSAGE;
import static triangle.triangleapp.domain.ChatAction.ActionType.ACTION_NONE;

/**
 * Created by marco on 20-6-2017.
 */

public class ChatAction {
    @SerializedName("Message")
    private String message;

    @SerializedName("Timestamp")
    private Date timeStamp;

    @SerializedName("ActionType")
    @ActionType
    private int actionType;

    @SerializedName("StreamId")
    private String streamId;

    public ChatAction() {
        this.timeStamp = new Date();
    }

    public ChatAction(String message, int actionType, String streamId) {
        super();
        this.message = message;
        this.actionType = actionType;
        this.streamId = streamId;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public int getActionType() {
        return actionType;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    @IntDef({ACTION_NONE, ACTION_MESSAGE, ACTION_JOIN, ACTION_LEAVE})
    public @interface ActionType {
        int ACTION_NONE = 0;
        int ACTION_MESSAGE = 1;
        int ACTION_JOIN = 2;
        int ACTION_LEAVE = 3;
    }

    public static ChatAction join(String streamId) {
        return new ChatAction("", ActionType.ACTION_JOIN, streamId);
    }

    public static ChatAction message(String message, String streamId) {
        return new ChatAction(message, ActionType.ACTION_MESSAGE, streamId);
    }
}
