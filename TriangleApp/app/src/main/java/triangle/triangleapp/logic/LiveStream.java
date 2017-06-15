package triangle.triangleapp.logic;

import android.view.Surface;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public interface LiveStream {
    void start(FileRecordedCallback fileRecordedCallback);
    void stop();
    void setPreviewView(Surface view);
}
