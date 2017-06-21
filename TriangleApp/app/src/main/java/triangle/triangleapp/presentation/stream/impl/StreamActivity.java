package triangle.triangleapp.presentation.stream.impl;

import android.net.Uri;
import android.os.Bundle;

import triangle.triangleapp.R;
import triangle.triangleapp.domain.ChatAction;
import triangle.triangleapp.helpers.AdapterType;
import triangle.triangleapp.presentation.impl.ChatFragment;
import triangle.triangleapp.presentation.stream.StreamPresenter;
import triangle.triangleapp.presentation.stream.StreamView;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Kevin Ly on 6/15/2017.
 */

public class StreamActivity extends AppCompatActivity implements StreamView, ChatFragment.OnFragmentInteractionListener {
    private static final String TAG = "StreamActivity";
    private StreamPresenter mPresenter;
    private SurfaceView mCameraSurfaceView;

    private Button mButtonStream, mButtonChat;
    private ChatFragment chatFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mPresenter = new StreamPresenter(this);

        mCameraSurfaceView = (SurfaceView) findViewById(R.id.surface_camera_preview);
        mButtonStream = (Button) findViewById(R.id.btn_stream);
        mButtonStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start or stop the stream
                mPresenter.stream();
            }
        });

        chatFrag = ChatFragment.newInstance();
        mButtonChat = (Button) findViewById(R.id.btnChat);
        mButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out);*/

                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (chatFrag.isAdded()) { // if the fragment is already in container
                    if (chatFrag.isHidden()) {
                        ft.show(chatFrag);
                    } else {
                        ft.hide(chatFrag);
                    }
                } else { // fragment needs to be added to frame container
                    ft.add(R.id.frameLayout, chatFrag, "A");
                }

                ft.commit();
            }
        });
    }

    @Override
    public void streamStarted() {
        mButtonStream.setText(R.string.btn_stop_streaming);
        Toast.makeText(this, R.string.notify_streaming_started, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void streamStopped() {
        mButtonStream.setText(R.string.btn_start_streaming);
        Toast.makeText(this, R.string.notify_streaming_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPreview() {
        mCameraSurfaceView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePreview() {
        mCameraSurfaceView.setVisibility(View.INVISIBLE);
    }

    @Override
    public Surface getPreviewSurface() {
        SurfaceHolder holder = mCameraSurfaceView.getHolder();
        if (holder != null) {
            return holder.getSurface();
        }
        return null;
    }

    @Override
    public void connected(@AdapterType int type) {
        Log.i(TAG, "Connected adapter = " + type);
    }

    @Override
    public void errorOccurred(@AdapterType int type, @NonNull Exception exception) {
        Log.e(TAG, "Error adapter = " + type, exception);
    }

    @Override
    public void showMessage(@NonNull ChatAction message) {
        Log.i(TAG, "Received message, message = " + message.getMessage());
        ChatFragment chatFrag = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        chatFrag.addReceivedMessage(message.getMessage());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSendMessage(String msg) {
        mPresenter.sendChatMessage(msg);
    }
}
