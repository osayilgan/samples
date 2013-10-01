package okan.apps.demo.views;

import java.util.ArrayList;

import okan.apps.demo.adapters.ImageViewPagerAdapter;
import okan.apps.demo.imageadapter.R;
import okan.apps.demo.main.MainController;
import okan.apps.demo.models.FlickrPhoto;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;


public class FullScreenImageFragment extends Fragment {
	
	/* Listener to Communicate with Parent Activity */
	private FullScreenImageFragmentInterface listener;
	
	/* View Pager */
	private ViewPager mViewPager;
	
	/* Circle Page Indicator */
	private PageIndicator indicator;
	
	/* Pager Image View Adapter */
	private ImageViewPagerAdapter pagerAdapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		pagerAdapter = new ImageViewPagerAdapter(getActivity(), MainController.getPhotos(), listener);
		
		/* set adapter */
		mViewPager.setAdapter(pagerAdapter);
		
		/* Set Initial Item Position */
		mViewPager.setCurrentItem(MainController.getClickedItemPosition());
		
		/* Set Page indicator to View Pager */
		indicator.setViewPager(mViewPager);
		
		/* Set Current Item of Page Indicator */
		indicator.setCurrentItem(MainController.getClickedItemPosition());
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		listener = (FullScreenImageFragmentInterface) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.full_screen_image, null, false);
		
		mViewPager = (ViewPager) layout.findViewById(R.id.pager);
		indicator = (CirclePageIndicator)layout.findViewById(R.id.indicator);
		
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				listener.onImageClick();
			}
		});
		
		return layout;
	}
	
	/**
	 * Communication interface between FullScreenImage View and Parent Controller.
	 * 
	 * Must be implemented in Parent Controller.
	 * 
	 * @author Okan SAYILGAN
	 */
	public static interface FullScreenImageFragmentInterface {
		
		/** Invoked on Click on Image View */
		void onImageClick();
	}
}
