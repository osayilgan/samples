package okan.apps.samples.tabbar;

import okan.apps.samples.R;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class SimpleFragment extends Fragment implements OnClickListener {

	FragmentTransaction transaction;
	FragmentManager fragmentManager;
	
	RelativeLayout parentLayout;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.simple_layout, container, false);
		
		Button callNewFragment = (Button) view.findViewById(R.id.call_new_fragment);
		callNewFragment.setOnClickListener(this);
		
		parentLayout = (RelativeLayout) view.findViewById(R.id.simple_layout);
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		
		switch (id) {
		
		case R.id.call_new_fragment:
			
			onClickButton();
			
			break;

		default:
			break;
		}
	}
	
	private void onClickButton() {
		
		TestFragment fragment = new TestFragment();
		
		// Sending color value with the bundle
		Bundle bundle = new Bundle();
		bundle.putInt("color", Color.RED);
		fragment.setArguments(bundle);
		
		transaction = getActivity().getSupportFragmentManager().beginTransaction();
		
		transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left,
				R.anim.slide_out_right);
		
		transaction.replace(R.id.realtabcontent, fragment);
		
		// Add to custom backstack
		TabBarWithCustomStack.customBackStack.get(TabBarWithCustomStack.mTabHost.getCurrentTabTag()).add(fragment);
		
		transaction.commit();
	}
}
