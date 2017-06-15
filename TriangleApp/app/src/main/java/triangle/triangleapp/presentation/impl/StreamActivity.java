package triangle.triangleapp.presentation.impl;

import android.os.Bundle;
import triangle.triangleapp.R;
import triangle.triangleapp.presentation.StreamPresenter;
import triangle.triangleapp.presentation.StreamView;

import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.SurfaceView;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class StreamActivity extends AppCompatActivity implements StreamView {
    private StreamPresenter mPresenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mPresenter = new StreamPresenter(this);
    }

    @Override
    public void showPreview() {

    }

    @Override
    public Surface getPreviewSurface() {
        return null;
    }
}
