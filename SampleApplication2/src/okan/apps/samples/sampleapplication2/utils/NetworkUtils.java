package okan.apps.samples.sampleapplication2.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This Class is used for Connection Methods
 * 
 * @author Okan SAYILGAN
 */
public class NetworkUtils {
	
	/**
	 * Checks If the device is Connected to a Network
	 * @param context		Activity Context
	 * @return				True if the Devices connected to a Network and have internet connection, False otherwise
	 */
	public static boolean isConnected(Context context) {
		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo ni = cm.getActiveNetworkInfo();
		
		if ((ni != null) && (ni.isAvailable()) && (ni.isConnected())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Retrieves the JSON from Server
	 * IMPORTANT : Check if the Returned value is Null.
	 * 
	 * @param url				URL to get JSON from
	 * @return					JSON response If the response code is 200, NULL otherwise
	 * @throws IOException
	 */
	public static String getJsonFromServer(Context context, String url) throws IOException {
		
		StringBuilder stringBuilder = new StringBuilder();
		int responseCode;
		
		BufferedReader inputStream = null;
		
		URL jsonUrl = new URL(url);
		URLConnection urlConnection = jsonUrl.openConnection();
		
		HttpURLConnection  httpConnection = (HttpURLConnection) urlConnection;
		httpConnection.setConnectTimeout(2 * 1000);
		httpConnection.setReadTimeout(2 * 1000);
		
		responseCode = httpConnection.getResponseCode();
		
		if (responseCode == 200) {
			/* Means Successful */
			
			inputStream = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			
			String line = null;
			
			while ((line = inputStream.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			
			return stringBuilder.toString();
		} else {
			
			return null;
		}
	}
}
