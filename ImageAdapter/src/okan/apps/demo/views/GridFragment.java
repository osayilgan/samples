package okan.apps.demo.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okan.apps.demo.adapters.GridImageAdapter;
import okan.apps.demo.imageadapter.R;
import okan.apps.demo.main.MainController;
import okan.apps.demo.models.FlickrPhoto;
import okan.apps.demo.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Fragment to represent Grid View with images retrieved from Flickr API.
 * 
 * @author Okan SAYILGAN
 */
public class GridFragment extends Fragment {
	
	/* Constant KEY values to parse the JSON Returned from Flickr API */
	private static final String key_photos_json = "photos";
	private static final String key_photos_array = "photo";
	
	/* Necessary Fields to Request Search Result from Flickr API */
	private String flickrMainUrl = "http://api.flickr.com/services/rest/?method=flickr.photos.search";
	private String flickrNumberOfPages = "&per_page=20";
	private String flickrNoJsonCallback = "&nojsoncallback=1";
	private String flickrQueryFormat = "&format=json";
	private String flickrQueryTag = "&tags=amsterdam";
	private String flickrQueryKey = "&api_key=";
	
	/** API KEY for Flickr User */
	private String flickrApiKey = "dff92785c6b2dd9265425f3ba2090275";
	
	/**
	 * Communication interface between Parent Controller and Grid Image View.
	 * Must be implemented on parent Fragment Activity Class.
	 * 
	 * @author Okan SAYILGAN
	 */
	public static interface GridFragmentInterface {
		
		/** invoked when an item is Clicked on GridView */
		void onGridItemClick(int position);
	}
	
	/* interface instance to communicate with Parent FragmentActivity */
	private GridFragmentInterface listener;
	
	/* Grid Adapter Class */
	private GridImageAdapter gridAdapter;
	
	/* Grid View */
	private GridView gridView;
	
	/* No result text */
	TextView noResultText;
	
	/* List of Flickr Photos */
	private List<FlickrPhoto> photos;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		/* Attach interface to parent which wants to init this fragment */
		listener = (GridFragmentInterface) activity;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		/* Get Photo List from MainController */
		this.photos = MainController.getPhotos();
		
		/* Check if the Photos is null */
		if (photos != null) {
			
			gridAdapter = new GridImageAdapter(getActivity(), (ArrayList<FlickrPhoto>) photos, listener);
			gridView.setAdapter(gridAdapter);
			
		} else {
			
			/* Run Async Image Loader Class */
			new ImageLoaderTask().execute();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.image_gallery_layout, null, false);
		
		gridView = (GridView) layout.findViewById(R.id.myGrid);
		noResultText = (TextView) layout.findViewById(R.id.mTextView);
		
		return layout;
	}
	
	/* Async inner class to request information and set the grid adapter after retrieving response from Flickr api */
	private class ImageLoaderTask extends AsyncTask<Void, Void, Void> {
		
		private String jsonResponse;
		private String flickerSearchUrl;
		
		/* Loading Indicator */
		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			dialog = ProgressDialog.show(getActivity(), "", getActivity().getResources().getString(R.string.loading));
			
			/* initialize variables */
			flickerSearchUrl = null;
			jsonResponse = null;
			photos = new ArrayList<FlickrPhoto>();
			
			/* Creating the URL to get json response */
			StringBuilder builder = new StringBuilder();
			builder.append(flickrMainUrl);
			builder.append(flickrNumberOfPages);
			builder.append(flickrNoJsonCallback);
			builder.append(flickrQueryFormat);
			builder.append(flickrQueryTag);
			builder.append(flickrQueryKey);
			builder.append(flickrApiKey);
			flickerSearchUrl = builder.toString();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				/* Get JSON Response From Server */
				jsonResponse = NetworkUtils.getJsonFromServer(flickerSearchUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			/* Check if the Response from server is NULL */
			if (jsonResponse != null) {
				
				JSONObject responseJSON = null;
				JSONArray responseArray = null;
				
				try {
					
					/* Get Photos JSON */
					responseJSON = new JSONObject(jsonResponse).getJSONObject(key_photos_json);
					
					if (responseJSON != null) {
						
						/* Get Photo JSON Array if the JSON Object is not Null */
						responseArray = responseJSON.getJSONArray(key_photos_array);
						
						/* fill up the Array with Objects retrieved from service api */
						for (int i = 0; i < responseArray.length(); i++) {
							
							/* Parse Object */
							FlickrPhoto flickrPhoto = FlickrPhoto.parseFlickrPhotoObject(responseArray.getJSONObject(i));
							
							/* Add Object to the List */
							photos.add(flickrPhoto);
						}
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			// Hide Loading indicator
			dialog.dismiss();
			
			/* Check if we retrieved some photos */
			if (photos.size() > 0) {
				
				gridAdapter = new GridImageAdapter(getActivity(), (ArrayList<FlickrPhoto>) photos, listener);
				gridView.setAdapter(gridAdapter);
				
			} else {
				noResultText.setVisibility(View.VISIBLE);
			}
			
			/* Save Photos to a static variable in Main Controller to use it later */
			MainController.savePhotos(photos);
		}
	}
}
