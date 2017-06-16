package triangle.triangleapp.persistence.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import triangle.triangleapp.persistence.StreamAdapter;

/**
 * Created by Kevin Ly on 6/16/2017.
 */

public class HttpStream implements StreamAdapter{
    private static final String TAG = "HttpStream";
    private static final String URL = "ws://145.49.30.113:1234/send";
    private RequestQueue mRequestQueue;

    public HttpStream(Context context){
        mRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void sendFile(@NonNull final byte[] fileInBytes) {
        try {
            StringRequest request = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i(TAG, response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, error.getMessage());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return fileInBytes;
                    } catch (Exception ex) {
                        Log.e(TAG, "Error occured while get bytes.", ex);
                        return null;
                    }
                }
            };

            mRequestQueue.add(request);
        } catch (Exception ex){
            Log.e(TAG, "Error occured while send request via Volley", ex);
        }
    }
}
