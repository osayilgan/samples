package okan.apps.samples.tabbar;

import okan.apps.samples.R;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CustomFragment extends Fragment implements OnClickListener {
	
	Button createNewFragment;
	
	FragmentTransaction transaction;
	FragmentManager fragmentManager;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.custom_layout, container, false);
		
		createNewFragment = (Button) view.findViewById(R.id.call_new_custom_fragment);
		createNewFragment.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		
		switch (id) {
		case R.id.call_new_custom_fragment:
			
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
		bundle.putInt("color", Color.BLUE);
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
