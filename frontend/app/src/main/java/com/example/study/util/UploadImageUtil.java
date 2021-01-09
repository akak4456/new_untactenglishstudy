package com.example.study.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.example.study.constant.Constant;
import com.example.study.util.volley.MultipartRequest;
import com.example.study.util.volley.VolleySingleton;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadImageUtil {
    private Context context;
    public UploadImageUtil(Context context){
        this.context = context;
    }
    public void uploadImage(final Bitmap bitmap, final String uuid,Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener){
        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST,
                Constant.rootAddress+"/upload",
                listener,
                errorListener) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("upload", new DataPart(uuid + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        VolleySingleton.getInstance(context).addToRequestQueue(multipartRequest);
    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
