package okan.apps.samples.tabbar;

import java.util.HashMap;
import java.util.Stack;

import okan.apps.samples.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

/**
 * 
 * @author OKAN SAYILGAN
 * 
 *         Since ActivityGroup and TabActivity is deprecated, we are no longer
 *         able to have different back stack for each of the elements under Tab
 *         Host.
 * 
 *         This class is used to provide separated back stack for each element
 *         in Tab Host It keeps the back stack track separately for each tab.
 * 
 */
public class TabBarWithCustomStack extends FragmentActivity {
	
	public static HashMap<String, Stack<Fragment>> customBackStack;
	
	private static Stack<Fragment> simpleStack;
	private static Stack<Fragment> contactsStack;
	private static Stack<Fragment> customStack;
	private static Stack<Fragment> throttleStack;

	// Stack final names
	private static final String STACK_SIMPLE = "Simple";
	private static final String STACK_CONTACTS = "Contacts";
	private static final String STACK_CUSTOM = "Custom";
	private static final String STACK_THROTTLE = "Throttle";
	
	// Tab String for onSaveInstanceState method
	private static final String INSTANCE_STATE_TAB = "tab";
	
	
	public static TabHost mTabHost;
	public static TabManager mTabManager;
	public static ViewPager mViewPager;

	public static final int HOME_TAB_INDEX = 5;
	
	FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tabs_pager);
		
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		customBackStack = new HashMap<String, Stack<Fragment>>();
		
		simpleStack = new Stack<Fragment>();
		contactsStack = new Stack<Fragment>();
		customStack = new Stack<Fragment>();
		throttleStack = new Stack<Fragment>();
		
		// Put stacks to custom back stact
		customBackStack.put(STACK_SIMPLE, simpleStack);
		customBackStack.put(STACK_CONTACTS, contactsStack);
		customBackStack.put(STACK_CUSTOM, customStack);
		customBackStack.put(STACK_THROTTLE, throttleStack);
		
		customBackStack.get(STACK_SIMPLE).push(new SimpleFragment());
		customBackStack.get(STACK_CONTACTS).push(new ContactsFragment());
		customBackStack.get(STACK_CUSTOM).push(new CustomFragment());
		customBackStack.get(STACK_THROTTLE).push(new ThrottleFragment());
		
		mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);
		
		mTabManager.addTab(mTabHost.newTabSpec(STACK_SIMPLE).setIndicator(STACK_SIMPLE), SimpleFragment.class, null);
		mTabManager.addTab(mTabHost.newTabSpec(STACK_CONTACTS).setIndicator(STACK_CONTACTS), ContactsFragment.class, null);
		mTabManager.addTab(mTabHost.newTabSpec(STACK_CUSTOM).setIndicator(STACK_CUSTOM), CustomFragment.class, null);
		mTabManager.addTab(mTabHost.newTabSpec(STACK_THROTTLE).setIndicator(STACK_THROTTLE), ThrottleFragment.class, null);
		
		// Check for previously saved instance state
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString(INSTANCE_STATE_TAB));
		} else {
			mTabHost.setCurrentTabByTag(STACK_SIMPLE);
		}
	}

	@Override
	public void onBackPressed() {

		Stack<Fragment> stack = customBackStack.get(mTabHost.getCurrentTabTag());

		if (stack.isEmpty()) {
			super.onBackPressed();
		} else {

			Fragment fragment = stack.pop();

			if (fragment.isVisible()) {
				if (stack.isEmpty()) {
					super.onBackPressed();
				} else {
					Fragment frg = stack.pop();
					customBackStack.get(mTabHost.getCurrentTabTag()).push(frg);

					transaction = getSupportFragmentManager().beginTransaction();
					transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,
							R.anim.slide_in_right, R.anim.slide_out_left);
					
					Toast.makeText(this, "Backstack pulls the fragment back", Toast.LENGTH_SHORT).show();
					
					transaction.replace(R.id.realtabcontent, frg).commit();
				}
			} else {
				getSupportFragmentManager().beginTransaction().replace(R.id.realtabcontent, fragment).commit();
			}
		}
	}

	/**
	 * This is a helper class that implements a generic mechanism for
	 * associating fragments with the tabs in a tab host. It relies on a trick.
	 * Normally a tab host has a simple API for supplying a View or Intent that
	 * each tab will show. This is not sufficient for switching between
	 * fragments. So instead we make the content part of the tab host 0dp high
	 * (it is not shown) and the TabManager supplies its own dummy view to show
	 * as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct fragment shown in a separate content area whenever
	 * the selected tab changes.
	 */
	public static class TabManager implements TabHost.OnTabChangeListener {

		private final FragmentActivity mActivity;
		private final TabHost mTabHost;
		private final int mContainerId;

		private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();

		private TabInfo mLastTab;

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;
			private Fragment fragment;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabManager(FragmentActivity activity, TabHost tabHost, int mContainerId) {
			mActivity = activity;
			mTabHost = tabHost;
			this.mContainerId = mContainerId;
			mTabHost.setOnTabChangedListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mActivity));
			String tag = tabSpec.getTag();
			
			TabInfo info = new TabInfo(tag, clss, args);

			info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
			if (info.fragment != null && !info.fragment.isDetached()) {
				FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
				ft.detach(info.fragment);
				ft.commit();
			}
			
			mTabs.put(tag, info);
			mTabHost.addTab(tabSpec);
		}

		public void addInvisibleTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args, int childID) {
			tabSpec.setContent(new DummyTabFactory(mActivity));
			String tag = tabSpec.getTag();
			TabWidget tabWidget = mTabHost.getTabWidget();
			TabInfo info = new TabInfo(tag, clss, args);

			info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
			if (info.fragment != null && !info.fragment.isDetached()) {
				FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
				ft.detach(info.fragment);
				ft.commit();
			}

			mTabs.put(tag, info);
			mTabHost.addTab(tabSpec);

			// Makes tab invisible
			tabWidget.getChildAt(childID).setVisibility(View.GONE);
		}

		@Override
		public void onTabChanged(String tabId) {
			TabInfo newTab = mTabs.get(tabId);
			if (mLastTab != newTab) {
				FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
				if (mLastTab != null) {
					if (mLastTab.fragment != null) {
						ft.detach(mLastTab.fragment);
					}
				}
				if (newTab != null) {
					if (newTab.fragment == null) {
						if (!customBackStack.get(tabId).isEmpty()) {
							Fragment fragment = customBackStack.get(tabId).pop();
							customBackStack.get(tabId).push(fragment);
							ft.replace(mContainerId, fragment);
						}
					} else {
						if (!customBackStack.get(tabId).isEmpty()) {
							Fragment fragment = customBackStack.get(tabId).pop();
							customBackStack.get(tabId).push(fragment);
							ft.replace(mContainerId, fragment);
						}
					}
				}

				mLastTab = newTab;
				ft.commit();
				mActivity.getSupportFragmentManager().executePendingTransactions();
			}
		}

	}
}
