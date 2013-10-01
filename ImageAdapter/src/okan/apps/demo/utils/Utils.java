package okan.apps.demo.utils;

import com.android.volley.MGVolleyUtil;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.WindowManager;

/**
 * General Purpose Utility Methods.
 * 
 * @author Okan SAYILGAN
 */
public class Utils {
	
	/**
	 * Calculates Screen Dimensions
	 * 
	 * @return	Screen Width in Pixels.
	 */
	public static int getWindowWidth(FragmentActivity activity) {
		
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display.getWidth();
	}
}
