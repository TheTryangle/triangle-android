package triangle.triangleapp.domain;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by marco on 20-6-2017.
 */

public class ChatMessage {
    @SerializedName("text")
    private String message;

    @SerializedName("date")
    private Date date;

    public ChatMessage(@NonNull String message) {
        this.message = message;
        date = new Date();
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }
}
