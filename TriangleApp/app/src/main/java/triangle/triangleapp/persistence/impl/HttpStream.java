package triangle.triangleapp.persistence.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;
import org.spongycastle.openssl.jcajce.JcaPEMWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;

import triangle.triangleapp.R;
import triangle.triangleapp.TriangleApplication;
import triangle.triangleapp.domain.KeySerializer;
import triangle.triangleapp.helpers.ConfigHelper;
import triangle.triangleapp.logic.StreamManagerCallback;
import triangle.triangleapp.persistence.ConnectionCallback;
import triangle.triangleapp.persistence.stream.StreamAdapter;
import triangle.triangleapp.presentation.stream.impl.StreamActivity;

/**
 * Created by Kevin Ly on 6/16/2017.
 */

public class HttpStream implements StreamAdapter {
    private static final String TAG = "HttpStream";
    private static final String URL = ConfigHelper.getInstance().get(ConfigHelper.KEY_WEBAPI_DESTINATION_ADDRESS);
    private RequestQueue mRequestQueue;
    private String id;
    private Gson mGsonInstance;
    private StreamManagerCallback mManagerCallback;

    /**
     * Initializing HttpStream
     */
    public HttpStream(StreamManagerCallback managerCallback) {
        mRequestQueue = Volley.newRequestQueue(TriangleApplication.getAppContext());
        mManagerCallback = managerCallback;
        GsonBuilder builder = new GsonBuilder();
        mGsonInstance = builder.create();
    }

    @Override
    public void sendPublicKey(@NonNull PublicKey publicKey) {
        StringWriter stringWriter = new StringWriter();
        JcaPEMWriter writer = new JcaPEMWriter(stringWriter);
        try {
            writer.writeObject(publicKey);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String pubKey = stringWriter.toString();
        final String completeUrl = URL + "stream/sendKey/" + id;

        KeySerializer keySerializer = new KeySerializer(pubKey);

        final String pubKeyJsonObj = mGsonInstance.toJson(keySerializer);

//        final JSONObject pubKeyObj = new JSONObject();
//        try {
//            pubKeyObj.put("publicKey", pubKey);
//        } catch (Exception ex){
//            Log.e(TAG, "Error while put public key in JSON object.", ex);
//        }



        StringRequest keyRequest = new StringRequest(Request.Method.PUT, completeUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Done sending public key to server.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error sending public key to server.", error);
                mManagerCallback.streamError(R.string.err_send_pubkey, true);

            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return pubKeyJsonObj.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Error while get bytes from public key.", e);
                    mManagerCallback.streamError(R.string.err_decode_server_pubkey, true);
                    return null;
                }
            }
        };

        mRequestQueue.add(keyRequest);
    }

    @Override
    public void connect(final ConnectionCallback callback) {
        final String completeUrl = URL + "stream/connect/";

        StringRequest idRequest = new StringRequest(Request.Method.GET, completeUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                id = response.substring(1, response.length() - 1);
                Log.i(TAG, "ID = " + id);
                callback.onConnected();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error getting ID", error);
                mManagerCallback.streamError(R.string.err_get_stream_id, true);
            }
        });

        mRequestQueue.add(idRequest);
    }

    @Override
    public void sendFile(@NonNull byte[] fileInBytes, @NonNull PrivateKey privateKey) {
        try {
            final String completeUrl = URL + "stream/send/" + id;

            MultipartRequest multipartRequest = new MultipartRequest(completeUrl, null, "application/octet-stream", fileInBytes, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    Log.i(TAG, "Done sending data");

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error sending data", error);
                    mManagerCallback.streamError(R.string.err_send_stream_file, false);
                }
            });

            mRequestQueue.add(multipartRequest);


        } catch (Exception ex) {
            Log.e(TAG, "Error occured while send request via Volley", ex);
            mManagerCallback.streamError(R.string.err_send_stream_file, false);
        }
    }

    @Override
    public void sendText(@NonNull String text) {

    }

    @Override
    public String getId() {
        return id;
    }
}
