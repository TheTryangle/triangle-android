package triangle.triangleapp.presentation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by marco on 19-6-2017.
 */

@IntDef({ConnectionState.UNKNOWN, ConnectionState.CONNECTED, ConnectionState.DISCONNECTED})
@Retention(RetentionPolicy.SOURCE)
public @interface ConnectionState {
    int UNKNOWN = 0;
    int CONNECTED = 1;
    int DISCONNECTED = 2;
}

