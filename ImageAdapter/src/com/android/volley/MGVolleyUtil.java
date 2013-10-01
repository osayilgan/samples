package com.android.volley;

import android.content.Context;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class MGVolleyUtil {
	private static RequestQueue requestQueue;
	private static ImageLoader imageLoader;

	public static void init(Context context) {
		requestQueue = Volley.newRequestQueue(context);
		imageLoader = new ImageLoader(requestQueue, new MGImageCache(context));
	}

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}
	
	public static RequestQueue getRequestQueue() {
		return requestQueue;
	}
}
