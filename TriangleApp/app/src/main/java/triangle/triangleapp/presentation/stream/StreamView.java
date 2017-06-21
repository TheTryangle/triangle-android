package triangle.triangleapp.presentation.stream;

import android.support.annotation.NonNull;
import android.view.Surface;
import android.view.SurfaceView;

import triangle.triangleapp.helpers.AdapterType;

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
     *
     * @return Surface used for preview.
     */
    Surface getPreviewSurface();

    /**
     * Called when an adapter has connected
     *
     * @param type The type of adapter that has connected
     */
    void connected(@AdapterType int type);

    /**
     * Called when an adapter occurred an error during connecting
     *
     * @param type      The type of adapter
     * @param exception The exception that occurred
     */
    void errorOccurred(@AdapterType int type, @NonNull Exception exception);

    /**
     * Called when some kind of error occurred in the stream.
     *
     * @param type The type of adapter (chat or stream).
     * @param stringResId The ID of the error message to show.
     * @param fatal
     */
    void errorOccurred(@AdapterType int type, @NonNull int stringResId);

    /**
     * Called when some kind of error occurred in the stream.
     *
     * @param type The type of adapter (chat or stream).
     * @param stringResId The ID of the error message to show.
     * @param fatal
     */
    void errorOccurred(@AdapterType int type, @NonNull int stringResId, boolean fatal);
}
