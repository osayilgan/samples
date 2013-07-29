package okan.apps.samples.sampleapplication2.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Display;

public class Calculator {
	
	/**
	 * Checks Weather the date is Today or Not
	 * 
	 * @return
	 */
	public static boolean isToday(String date) {
		
		Locale netherlands = new Locale("nl", "NL");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", netherlands);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));
		
		Calendar currentDate = Calendar.getInstance();
		String dateNow = simpleDateFormat.format(currentDate.getTime());
		
		return dateNow.equals(date);
	}
	
	/**
	 * Retrieves Today's date in the format "yyyy-MM-dd"
	 * 
	 * @return
	 */
	public static String getTodaysDate() {
		
		Locale netherlands = new Locale("nl", "NL");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", netherlands);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));
		
		Calendar currentDate = Calendar.getInstance();
		return simpleDateFormat.format(currentDate.getTime());
	}
	
	public static String parseMonthName(String fullDate) {
		
		Locale netherlands = new Locale("nl", "NL");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", netherlands);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));
		
		Calendar calendar = Calendar.getInstance();
		
		Date tempDate;
		String monthName;
		
		try {
			
			tempDate = (Date) simpleDateFormat.parse(fullDate);
			calendar.setTime(tempDate);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, netherlands);
		
		/* Convert First Letter to Upper Case */
		return monthName.substring(0, 1).toUpperCase(new Locale("nl", "NL")) + monthName.substring(1);
	}
	
	/**
	 * Calculates Screen Dimensions
	 * 
	 * @return	Screen With in Pixels.
	 */
	public static int getWindowWidth(FragmentActivity activity) {
		
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.x;
	}
	
	/**
	 * Converts Given DP Value to Pixels.
	 * 
	 * @param activity		Fragment Activity instance to react resources.
	 * @param dp			int DP Value to be converted to Pixels.
	 * @return				DP in pixels.
	 */
	public static int convertDPtoPixel(FragmentActivity activity, int dp) {
		DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
		return (int)((dp * displayMetrics.density) + 0.5);
	}
}
