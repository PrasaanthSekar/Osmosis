package com.mindtree.osmosis.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by M1032467 on 8/17/2016.
 */

public class FOCVolleyQueue {

    private static FOCVolleyQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context appContext;

    private FOCVolleyQueue(Context context) {
        appContext = context;
        mRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(appContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public static synchronized FOCVolleyQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FOCVolleyQueue(context);
        }
        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
