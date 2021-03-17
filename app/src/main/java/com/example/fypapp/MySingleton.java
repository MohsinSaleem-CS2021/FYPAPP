package com.example.fypapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton mySingleton;
    private RequestQueue requestQueue;
    private Context context;

    private MySingleton(Context c){
        context = c;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context c){
        if(mySingleton == null){
            mySingleton = new MySingleton(c);
        }
        return mySingleton;
    }

    public<T> void addToRequestQueue (Request<T> request){
        getRequestQueue().add(request);
    }
}

