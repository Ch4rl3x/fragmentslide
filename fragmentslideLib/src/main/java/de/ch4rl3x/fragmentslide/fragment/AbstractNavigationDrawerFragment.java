package de.ch4rl3x.fragmentslide.fragment;

import de.ch4rl3x.fragmentslide.interfaces.ITheme;
import de.ch4rl3x.fragmentslide.utilities.ToolBarDrawerToggle;
import de.ch4rl3x.fragmentslide.R;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public abstract class AbstractNavigationDrawerFragment extends AbstractBaseFragment {

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	public ToolBarDrawerToggle mDrawerToggle;

	public DrawerLayout mDrawerLayout;
	private View mFragmentContainerView;

	protected int setActionBarMenuDrawableID() {
		return R.drawable.ic_drawer;
	}

	protected int setContentDescriptionClose() {
		return R.string.navigation_drawer_close;
	}

	protected int setContentDescriptionOpen() {
		return R.string.navigation_drawer_open;
	}

	protected int setNavigationDrawerShadowDrawableID() {
		return R.drawable.drawer_shadow;
	}

	protected int setTransparentIconID() {
		return R.color.transparent;
	}
	
	protected abstract int setNavigationDrawerLayout();
	protected abstract int setAppNameStringID();
	protected abstract ITheme setInitialTheme();

	@Override
	protected View onCreateViewCustom(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(setNavigationDrawerLayout(), container, false);
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}
	
	public void lockDrawer() {
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	}
	
	public void unLockDrawer() {
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 *
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(setNavigationDrawerShadowDrawableID(),GravityCompat.START);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setIcon(setTransparentIconID());
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		setActionBarDrawerToggle();

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	public void setActionBarDrawerToggle() {
		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ToolBarDrawerToggle(getActivity(), /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		getActiviyAbstract().getToolbar(),
		setInitialTheme(),
		setContentDescriptionOpen(), /*
										 * "open drawer" description for
										 * accessibility
										 */
		setContentDescriptionClose() /*
										 * "close drawer" description for
										 * accessibility
										 */
		) {
			
			@Override
			protected void onDrawerOpening() {
				super.onDrawerOpening();
				if (!isAdded()) {
					return;
				}
				if (mCallbacks != null) {
					mCallbacks.onNavigationDrawerOpening();
					onNavigationDrawerFragmentOpening();
				}
				getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
			}
			
			@Override
			protected void onDrawerClosing() {
				super.onDrawerClosing();
				if (!isAdded()) {
					return;
				}
				if (mCallbacks != null) {
					mCallbacks.onNavigationDrawerClosing();
					onNavigationDrawerFragmentClosing();
				}
				getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
			}
		};
	}
	
	protected void onNavigationDrawerFragmentOpening() {
		//Leere implementation
	}
	
	protected void onNavigationDrawerFragmentClosing() {
		//Leere implementation
	}
	
	protected void close() {
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.
	 */
	private void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(setAppNameStringID());
	}

	private ActionBar getActionBar() {
		return ((AppCompatActivity)getActivity()).getSupportActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		void onNavigationDrawerClosing();
		void onNavigationDrawerOpening();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar.
		// See also
		// showGlobalContextActionBar, which controls the top-left area of the
		// action bar.
		if (mDrawerLayout != null && isDrawerOpen()) {
//			inflater.inflate(R.menu.global, menu);
			showGlobalContextActionBar();
		} 
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void toggle() {
		if(mDrawerLayout != null && isDrawerOpen()) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		} else {
			mDrawerLayout.openDrawer(GravityCompat.START);
		}
		
	}
	
	public boolean isClosing() {
		return mDrawerToggle.isClosing();
	}
	
	public boolean isOpening() {
		return mDrawerToggle.isOpening();
	}
}
