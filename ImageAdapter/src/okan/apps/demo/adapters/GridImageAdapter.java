package okan.apps.demo.adapters;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.MGVolleyUtil;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import okan.apps.demo.custom.ui.SquareNetworkImageView;
import okan.apps.demo.imageadapter.R;
import okan.apps.demo.models.FlickrPhoto;
import okan.apps.demo.utils.ImageViewUtils;
import okan.apps.demo.utils.Utils;
import okan.apps.demo.views.GridFragment.GridFragmentInterface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Base Adapter for thumbnail images in GridView.
 * 
 * @author Okan SAYILGAN
 */
public class GridImageAdapter extends BaseAdapter {
	
	/* Parent activity instance */
	private FragmentActivity activity;
	
	/* Photo List */
	private List<FlickrPhoto> photos;
	
	/* Listener to Communicate with parent Activity */
	private GridFragmentInterface listener;
	
	public GridImageAdapter(FragmentActivity activity, ArrayList<FlickrPhoto> photos, GridFragmentInterface listener) {
		this.activity = activity;
		this.photos = photos;
		this.listener = listener;
	}
	
	@Override
	public int getCount() {
		return photos.size();
	}
	
	@Override
	public FlickrPhoto getItem(int position) {
		return photos.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return photos.get(position).getId();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		SquareNetworkImageView imageView;
		
		if (convertView == null) {
			
			imageView = (SquareNetworkImageView) activity.getLayoutInflater().inflate(R.layout.grid_item, null, false);
			
			imageView.setLayoutParams(new GridView.LayoutParams(Utils.getWindowWidth(activity)/4, 0));
			imageView.setAdjustViewBounds(false);
			imageView.setPadding(2, 2, 2, 2);
			
		} else {
			imageView = (SquareNetworkImageView) convertView;
		}
		
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				/* Notify Main Controller that user Clicked the item in Grid View */
				listener.onGridItemClick(position);
			}
		});
		
		// Set background image of NetworkImageView.
		ImageViewUtils.setImageResources(photos.get(position).getImageUrl(), imageView);
		
		return imageView;
	}
}
