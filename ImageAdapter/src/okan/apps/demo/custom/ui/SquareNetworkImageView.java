package okan.apps.demo.custom.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Custom Network Image View with Square Dimensions.
 * Width of Image View will be assigned to Height. Both will be same to have Square Image View.
 * 
 * @author Okan SAYILGAN
 */
public class SquareNetworkImageView extends NetworkImageView {

	public SquareNetworkImageView(Context context) {
		super(context);
	}
	
	public SquareNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public SquareNetworkImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
}
