package triangle.triangleapp.presentation;

import android.support.annotation.NonNull;
import android.view.Surface;

import triangle.triangleapp.domain.ChatAction;
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
     * Called when a message should be shown.
     *
     * @param message The message to show.
     */
    void showMessage(ChatAction message);

    void connected(@AdapterType int type);

    void errorOccurred(@AdapterType int type, @NonNull Exception exception);
}
