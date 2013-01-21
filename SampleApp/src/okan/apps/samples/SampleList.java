package okan.apps.samples;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SampleList extends SherlockListActivity {
	
	private String path;
	
	private List<Map<String, Object>> listData;
	
	protected Dialog mSplashDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Shows splash screen for 3 seconds.
		showSplashScreen();
		
		Intent intent = getIntent();
		path = intent.getStringExtra("com.example.android.apis.Path");
		
		if (path == null) {
			path = "";
		}
		
		listData = new ArrayList<Map<String, Object>>();
		
		listData = getData(path);
		
		setListAdapter(new SimpleAdapter(this, listData, android.R.layout.simple_list_item_1,
				new String[] { "title" }, new int[] { android.R.id.text1 }));
		getListView().setTextFilterEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu sub = menu.addSubMenu("Menu");
		
		for (int i = 0; i < listData.size(); i++) {
			sub.add(0, R.style.Theme_Sherlock, 0, listData.get(i).get("title").toString());
		}
		
		sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home || item.getItemId() == 0) {
			return false;
		}
		
		Intent intent = null;
		
		for (int i = 0; i < listData.size(); i++) {
			if (listData.get(i).get("title").equals(item.getTitle())) {
				intent = (Intent) listData.get(i).get("intent");
			}
		}
	    
		if (intent != null) {
			startActivity(intent);
		} else {
			Toast.makeText(this, "Activity not found", Toast.LENGTH_SHORT).show();
		}
		
		return true;
	}
	
	protected List<Map<String, Object>> getData(String prefix) {
		
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory("okan.apps.samples.SAMPLE");
		
		PackageManager pm = getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

		if (null == list)
			return listData;

		String[] prefixPath;
		String prefixWithSlash = prefix;

		if (prefix.equals("")) {
			prefixPath = null;
		} else {
			prefixPath = prefix.split("/");
			prefixWithSlash = prefix + "/";
		}
		
		int len = list.size();
		
		Map<String, Boolean> entries = new HashMap<String, Boolean>();
		
		for (int i = 0; i < len; i++) {
			ResolveInfo info = list.get(i);
			CharSequence labelSeq = info.loadLabel(pm);
			String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;
			
			if (prefixWithSlash.length() == 0 || label.startsWith(prefixWithSlash)) {
				
				String[] labelPath = label.split("/");
				
				String nextLabel = prefixPath == null ? labelPath[0] : labelPath[prefixPath.length];
				
				if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {
					addItem(listData, nextLabel,
							activityIntent(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));
				} else {
					if (entries.get(nextLabel) == null) {
						addItem(listData, nextLabel, browseIntent(prefix.equals("") ? nextLabel : prefix + "/"
								+ nextLabel));
						entries.put(nextLabel, true);
					}
				}
			}
		}
		
		Collections.sort(listData, sDisplayNameComparator);

		return listData;
	}

	private final static Comparator<Map<String, Object>> sDisplayNameComparator = new Comparator<Map<String, Object>>() {
		private final Collator collator = Collator.getInstance();

		public int compare(Map<String, Object> map1, Map<String, Object> map2) {
			return collator.compare(map1.get("title"), map2.get("title"));
		}
	};

	protected Intent activityIntent(String pkg, String componentName) {
		Intent result = new Intent();
		result.setClassName(pkg, componentName);
		return result;
	}

	protected Intent browseIntent(String path) {
		Intent result = new Intent();
		result.setClass(this, SampleList.class);
		result.putExtra("com.example.android.apis.Path", path);
		return result;
	}

	protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("title", name);
		temp.put("intent", intent);
		data.add(temp);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);

		Intent intent = (Intent) map.get("intent");
		startActivity(intent);
	}
	
	protected void removeSplashScreen() {
		if (mSplashDialog != null) {
			mSplashDialog.dismiss();
			mSplashDialog = null;
		}
	}

	/**
	 * Shows the splash screen over the full Activity
	 */
	protected void showSplashScreen() {
		mSplashDialog = new Dialog(this, R.style.SplashScreen);
		mSplashDialog.setContentView(R.layout.splash_screen);
		mSplashDialog.setCancelable(false);
		mSplashDialog.show();

		// Set Runnable to remove splash screen just in case
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				removeSplashScreen();
			}
		}, 3000);
	}
}