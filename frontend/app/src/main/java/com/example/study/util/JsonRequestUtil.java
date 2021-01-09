package com.example.study.util;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.example.study.constant.Constant;
import com.example.study.util.volley.GsonRequest;
import com.example.study.util.volley.VolleySingleton;
import com.google.gson.Gson;

import java.util.Map;

public class JsonRequestUtil<T> {
    private Context context;
    public JsonRequestUtil(Context context){
        this.context = context;
    }
    public void request(int method, String url, Class<T> clazz, Map<String,String> header, Response.Listener<T> listener, Response.ErrorListener errorListener){
        GsonRequest<T> req = new GsonRequest<>(
                method,
                Constant.rootAddress+url,
                clazz,
                header,
                listener,
                errorListener,
                context
        );
        int socketTimeout = 30000;//30 seconds - change to what you want
        //이메일 같은 경우는 request에 대한 응답을 받는데 다소 시간이 걸린다.
        //그런데 socketTimeout이 짧은 경우에는 응답을 받기 전에 다시 request를 보내게 된다.
        //그래서 회원가입과 같은 경우에 문제가 생기므로 다음과 같은 코드를 쓴다
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        VolleySingleton.getInstance(context).addToRequestQueue(req);
    }
    public void request(int method, String url, Class<T> clazz, Map<String,String> header,Object body, Response.Listener<T> listener, Response.ErrorListener errorListener){
        GsonRequest<T> req = new GsonRequest<>(
                method,
                Constant.rootAddress+url,
                clazz,
                header,
                listener,
                errorListener,
                new Gson().toJson(body),
                context
        );
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        VolleySingleton.getInstance(context).addToRequestQueue(req);
    }
}
