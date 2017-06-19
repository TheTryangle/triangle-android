package triangle.triangleapp.logic;

import android.support.annotation.Nullable;

/**
 * Created by marco on 19-6-2017.
 */

public interface NetworkChangeCallback {
    void onConnected();

    void onDisconnected(@Nullable String reason);
}
