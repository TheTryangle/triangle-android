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

import org.json.JSONObject;
import org.spongycastle.openssl.jcajce.JcaPEMWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;

import triangle.triangleapp.TriangleApplication;
import triangle.triangleapp.helpers.ConfigHelper;
import triangle.triangleapp.persistence.ConnectionCallback;
import triangle.triangleapp.persistence.stream.StreamAdapter;

/**
 * Created by Kevin Ly on 6/16/2017.
 */

public class HttpStream implements StreamAdapter {
    private static final String TAG = "HttpStream";
    private static final String URL = ConfigHelper.getInstance().get(ConfigHelper.KEY_WEBAPI_DESTINATION_ADDRESS);
    private RequestQueue mRequestQueue;
    private String id;

    /**
     * Initializing HttpStream
     */
    public HttpStream() {
        mRequestQueue = Volley.newRequestQueue(TriangleApplication.getAppContext());
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
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return pubKeyObj.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Error while get bytes from public key.", e);
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
                }
            });

            mRequestQueue.add(multipartRequest);


        } catch (Exception ex) {
            Log.e(TAG, "Error occured while send request via Volley", ex);
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
