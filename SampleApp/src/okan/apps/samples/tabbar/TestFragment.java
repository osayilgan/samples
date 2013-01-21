package okan.apps.samples.tabbar;

import okan.apps.samples.R;
import okan.apps.samples.R.color;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class TestFragment extends Fragment {

	RelativeLayout parentLayout;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			parentLayout.setBackgroundColor(bundle.getInt("color"));
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.test_layout, container, false);
		
		parentLayout = (RelativeLayout) view.findViewById(R.id.parent);
		
		return view;
	}
}
