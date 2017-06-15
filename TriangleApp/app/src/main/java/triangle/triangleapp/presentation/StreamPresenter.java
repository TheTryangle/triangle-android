package triangle.triangleapp.presentation;

import android.view.SurfaceView;

import triangle.triangleapp.logic.StreamManager;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class StreamPresenter {
    private StreamView mView;
    private StreamManager mManager;

    public StreamPresenter(StreamView streamView) {
        mView = streamView;
        mManager = new StreamManager();
    }

    public void stream() {
        mManager.setPreviewView(mView.getPreviewView());
        mManager.stream();
    }

}
