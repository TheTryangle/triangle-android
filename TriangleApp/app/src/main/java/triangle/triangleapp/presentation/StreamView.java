package triangle.triangleapp.presentation;

import android.view.Surface;
import android.view.SurfaceView;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public interface StreamView {
    /**
     * Called when the stream is started
     */
    void streamStarted();

    /**
     * Called when the stream is stopped
     */
    void streamStopped();

    /**
     * Called when the preview surface should be shown
     */
    void showPreview();

    /**
     * Called when the preview surface should be hide
     */
    void hidePreview();

    /**
     * Gets the surface to be used for preview
     * @return Surface used for preview.
     */
    Surface getPreviewSurface();
}
