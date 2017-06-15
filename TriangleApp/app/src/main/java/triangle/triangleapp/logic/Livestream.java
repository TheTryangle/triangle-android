package triangle.triangleapp.logic;

import android.view.Surface;
import android.view.SurfaceView;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public interface Livestream {
    void start(FileRecordedCallback fileRecordedCallback);
    void stop();
    void setPreviewView(Surface view);
}
