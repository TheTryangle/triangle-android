package triangle.triangleapp.presentation;

import android.view.Surface;
import android.view.SurfaceView;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public interface StreamView {
    void showPreview();

    Surface getPreviewSurface();
}
