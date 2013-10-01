package okan.apps.demo.adapters;

import java.util.ArrayList;
import java.util.List;

import okan.apps.demo.imageadapter.R;
import okan.apps.demo.models.FlickrPhoto;
import okan.apps.demo.utils.ImageViewUtils;
import okan.apps.demo.views.FullScreenImageFragment.FullScreenImageFragmentInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.NetworkImageView;

/**
 * View Pager adapter Class. 
 * 
 * @author Okan SAYILGAN
 */
public class ImageViewPagerAdapter extends PagerAdapter {
	
	/* Parent activity instance */
	private FragmentActivity activity;
	
	/* List Of Photos */
	private ArrayList<FlickrPhoto> photos;
	
	/* Listener to Communciate with parent Activity */
	private FullScreenImageFragmentInterface listener;
	
	public ImageViewPagerAdapter(FragmentActivity activity, List<FlickrPhoto> photos, FullScreenImageFragmentInterface listener) {
		this.activity = activity;
		this.photos = (ArrayList<FlickrPhoto>) photos;
		this.listener = listener;
	}
	
	@Override
	public int getCount() {
		return photos.size();
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}
	
	@Override
	public View instantiateItem(View container, int position) {
		
		RelativeLayout layout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.full_screen_image_view, null, false);
		
		NetworkImageView imageView = (NetworkImageView) layout.findViewById(R.id.image);
		
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				/* Notify Main Controller that user Clicked the item */
				listener.onImageClick();
			}
		});
		
		// Set background image of NetworkImageView.
		ImageViewUtils.setImageResources(photos.get(position).getImageUrl(), imageView);
		
		((ViewPager) container).addView(layout);
		
		return layout;
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
}