package okan.apps.demo.utils;

import okan.apps.demo.imageadapter.R;

import com.android.volley.MGVolleyUtil;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Utils class to be used for general purpose of ImageViews.
 * 
 * @author Okan SAYILGAN
 */
public class ImageViewUtils {
	
	/**
	 * Sets the ImageView background with the given image url. If any error
	 * occurs, sets a place holder into image view.
	 * 
	 * @param imageUrl
	 *            URL of the Image to use as ImageView background image.
	 * @param imageView
	 *            Image View to set images.
	 */
	public static void setImageResources(String imageUrl, NetworkImageView imageView) {
		
		if (imageUrl != null && !imageUrl.equals("") && imageUrl.startsWith("http")) {
			
			if (imageView.getTag() == null || imageView.getTag().equals(imageUrl) == false) {
				imageView.setTag(imageUrl);
				imageView.setImageUrl(imageUrl, MGVolleyUtil.getImageLoader());
			}
			
		} else {
			imageView.setImageResource(R.drawable.ic_launcher);
		}
	}
}
