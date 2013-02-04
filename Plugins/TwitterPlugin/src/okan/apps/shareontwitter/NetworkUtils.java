package okan.apps.shareontwitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkUtils {

	private static final String TAG = NetworkUtils.class.getName();
	public static String DEFAULT_ENCODING = "UTF-8";

	public static String getUrl(String urlString) throws IOException {
		String response = null;
		
		URL url;
		try {
			url = new URL(urlString);
 
			HttpURLConnection urlConnection;
			urlConnection = (HttpURLConnection) url.openConnection();

			try {
				response = NetworkUtils.readStream(urlConnection
						.getInputStream());
			} finally {
				urlConnection.disconnect();
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	public static String postUrl(String urlString, KeyValuePair[] parameters)
			throws IOException {
		String response = null;
		URL url;
		try {
			url = new URL(urlString);

			HttpURLConnection urlConnection;
			urlConnection = (HttpURLConnection) url.openConnection();

			try {
				urlConnection.setDoOutput(true);
				urlConnection.setRequestMethod("POST");
				urlConnection.setChunkedStreamingMode(0);

				OutputStream out = urlConnection.getOutputStream();
				writeStream(out, keyValuePairToQuery(parameters));

				InputStream in = urlConnection.getInputStream();
				response = readStream(in);
			} finally {
				urlConnection.disconnect();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	private static String keyValuePairToQuery(KeyValuePair[] kvp) {
		StringBuilder sb = new StringBuilder();
		for (KeyValuePair keyValuePair : kvp) {
			if (sb.length() != 0) {
				sb.append("&");
			}

			try {
				String key = URLEncoder.encode(keyValuePair.getKey(),
						DEFAULT_ENCODING);
				String value = URLEncoder.encode(keyValuePair.getValue(),
						DEFAULT_ENCODING);
				sb.append(key).append("=").append(value);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	private static void writeStream(OutputStream out, String body) {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		try {
			Log.i(TAG, "Writing to stream: " + body);
			writer.write(body);
			writer.flush();
		} catch (IOException e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
	}

	public static String readStream(InputStream is) throws IOException {

		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = "";
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}

		return sb.toString();
	}

	public static class KeyValuePair {
		private String key;
		private String value;

		public KeyValuePair(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		public String getKey() {
			return key;
		}
		
		public void setKey(String key) {
			this.key = key;
		}
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	/**
	 * Check if device has network connection
	 * 
	 * @return Boolean
	 */
	public static boolean hasConnection(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}
		return false;
	}

}
