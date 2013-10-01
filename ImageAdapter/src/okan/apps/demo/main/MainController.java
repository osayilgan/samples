package okan.apps.demo.main;

import java.util.List;

import com.android.volley.MGVolleyUtil;

import okan.apps.demo.imageadapter.R;
import okan.apps.demo.models.FlickrPhoto;
import okan.apps.demo.utils.NetworkUtils;
import okan.apps.demo.views.FullScreenImageFragment;
import okan.apps.demo.views.GridFragment;
import okan.apps.demo.views.FullScreenImageFragment.FullScreenImageFragmentInterface;
import okan.apps.demo.views.GridFragment.GridFragmentInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Controller Class to control Fragments Created from this Controller Class.
 * 
 * @author Okan SAYILGAN
 */
public class MainController extends FragmentActivity implements GridFragmentInterface, FullScreenImageFragmentInterface {
	
	/* Constant Final int value for Fragment Container element as parent for Fragments */
	private final int fragmentContainer = R.id.fragment_container;
	
	/* Fragment Transaction used for Fragments */
	private FragmentTransaction transaction;
	
	/* List of Flickr Photos */
	private static List<FlickrPhoto> photos;
	
	/* Position of the Clicked Image Item */
	private static int clickedItemPosition;
	
	/* No Internet Connection Text View */
	TextView noResultText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_controller);
		
		/* initialize no result text view */
		noResultText = (TextView) findViewById(R.id.mTextView);
		
		/* init NetworkImageView Loader */
		MGVolleyUtil.init(this);
		
		/* Check if the device is connected to internet */
		if (NetworkUtils.isConnected(this)) {
			
			/* Add GridView to FragmentContainer */
			attachGridView();
			
		} else {
			
			/* If Device is not Connected to internet, Show no result TextView */
			noResultText.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * Attaches Grid View with images to the main fragment container.
	 */
	public void attachGridView() {
		
		GridFragment gridFragment = new GridFragment();
		
		transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(fragmentContainer, gridFragment);
		transaction.commit();
	}
	
	/**
	 * Navigates to Full Screen Image View
	 */
	public void goToFullScreenImageView() {
		
		/* Fragment to Show Full Screen Images */
		FullScreenImageFragment imageFragment = new FullScreenImageFragment();
		
		transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
		
		transaction.replace(R.id.fragment_container, imageFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	@Override
	public void onGridItemClick(int position) {
		
		/* Save Previously Clicked item position */
		clickedItemPosition = position;
		
		/* Navigate to Full Screen image Fragment */
		goToFullScreenImageView();
	}
	
	/**
	 * Retrieves lastly clicked Item Position
	 * 
	 * @return  lastly clicked Item Position
	 */
	public static int getClickedItemPosition() {
		return clickedItemPosition;
	}
	
	/**
	 * Saves List of Photos in MainController to be able to retrieve it back in the Fragments.
	 * 
	 * @param photoList		List of FlickrPhoto Object
	 */
	public static void savePhotos(List<FlickrPhoto> photoList) {
		photos = photoList;
	}
	
	/**
	 * Retrieves the Photos stored in Main Activity
	 * @return		List of FlickrPhoto Object
	 */
	public static List<FlickrPhoto> getPhotos() {
		return photos;
	}
	
	@Override
	public void onImageClick() {
		super.onBackPressed();
	}
}
