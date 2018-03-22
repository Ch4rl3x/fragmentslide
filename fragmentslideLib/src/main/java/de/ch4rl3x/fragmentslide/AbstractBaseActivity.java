package de.ch4rl3x.fragmentslide;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import de.ch4rl3x.fragmentslide.fragment.AbstractBaseFragment;
import de.ch4rl3x.fragmentslide.fragment.AbstractNavigationDrawerFragment;
import de.ch4rl3x.fragmentslide.interfaces.IMask;
import de.ch4rl3x.fragmentslide.interfaces.OnStackSizeChangeListener;
import de.ch4rl3x.fragmentslide.utilities.NotifyableStack;

/**
 * The Basic Activity
 *
 * These class controls all fragments.
 * 
 * @author Alexander Karkossa
 *
 */
public abstract class AbstractBaseActivity extends AppCompatActivity implements OnStackSizeChangeListener {

	protected AbstractNavigationDrawerFragment mNavigationDrawerFragment;
	private Toolbar toolbar;
	private boolean enabledMenu = true;
	private IMask currentMask;
	private int[] fragmentContainer;
	private int[] separator;
	private boolean greyToolbarMode = false;
	private NotifyableStack<AbstractBaseFragment> fragmentStack = new NotifyableStack<AbstractBaseFragment>();
	private Pair<IMask, Bundle> resultBundlePair;

	public AbstractNavigationDrawerFragment getNavigationDrawerFragment() {
		return mNavigationDrawerFragment;
	}
	
	@Override
	public void onBackPressed() {
		boolean handled = fragmentStack.get(fragmentStack.size()-1).onBackPressed();
		if(!handled && currentMask != null) {
			closeHighestFragment();
		}
	}

	public IMask getCurrentMask() {
		return currentMask;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			
			if (fragmentStack.size() > fragmentContainer.length && !greyToolbarMode) {
				closeHighestFragment();

				return true;
			} else if(greyToolbarMode) {
				// The click should be handled by the fragemnts
				for(AbstractBaseFragment fragment : fragmentStack) {
					fragment.onHomeClicked();
				}
			} else {
				if(enabledMenu) {
					mNavigationDrawerFragment.toggle();
				}
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		fragmentStack.get(fragmentStack.size()-1).onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	/**
	 * Recreates a fragments, so it could be bound to another container
	 * 
	 * @param fragment
	 * @return the new fragment
	 */
	private AbstractBaseFragment recreateFragment(AbstractBaseFragment fragment) {
		try {
			AbstractBaseFragment.SavedState savedState = getSupportFragmentManager()
					.saveFragmentInstanceState(fragment);
			AbstractBaseFragment newInstance = fragment.getClass()
					.newInstance();
			newInstance.setMaske(fragment.getMaske());
			newInstance.setInitialSavedState(savedState);
			if(resultBundlePair != null && fragment.getMaske() == resultBundlePair.first) {
				newInstance.setInitialBundle(resultBundlePair.second);
			}
			return newInstance;
		} catch (Exception e) {
			throw new RuntimeException("Cannot reinstantiate fragment "
					+ fragment.getClass().getName(), e);
		}
	}

	/**
	 * Moves all fragments to the left. One will disapear on the left side
	 * and on the right side there will be a free space for the new fragment
	 */
	private void moveAllFragementsToTheLeft() {
		int highestFragment = fragmentStack.size() - 2;
		int highestContainer = fragmentContainer.length - 1;

		if (highestContainer == highestFragment) {
			setBackIcon(true);
		}

		while (highestContainer > 0) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();

			AbstractBaseFragment fragmentToMove = fragmentStack
					.get(highestFragment);
			fragmentStack.remove(highestFragment);
			AbstractBaseFragment newInstance = recreateFragment(fragmentToMove);
			fragmentStack.add(highestFragment, newInstance);

			fragmentTransaction.setCustomAnimations(setSlideLeftInAnimID(),
					setSlideLeftOutAnimID(), setSlideLeftInAnimID(),
					setSlideLeftOutAnimID());
			fragmentTransaction.remove(fragmentToMove);
			fragmentTransaction.commit();
			fragmentManager.executePendingTransactions();

			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.setCustomAnimations(setSlideLeftInAnimID(),
					setSlideLeftOutAnimID());
			newInstance.setContainerPosition(highestContainer - 1);
			fragmentTransaction.replace(
					fragmentContainer[highestContainer - 1], newInstance,
					newInstance.getName());
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
			fragmentManager.executePendingTransactions();
			

			highestFragment--;
			highestContainer--;
		}
	}

	/**
	 * Moves all fragments to the right e.g. if one fragment is closed
	 */
	private void moveAllFragementsToTheRight() {
		int fragmentToMove = fragmentStack.size() - 2;
		int targetContainer = fragmentContainer.length - 1;

		if (fragmentToMove == targetContainer) {
			setBackIcon(false);
		}

		if (fragmentStack.size() > fragmentContainer.length) {

			while (fragmentToMove >= 0 && targetContainer >= 0) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();

				AbstractBaseFragment toMove = fragmentStack.get(fragmentToMove);
				fragmentStack.remove(toMove);
				AbstractBaseFragment newInstance = recreateFragment(toMove);
				fragmentStack.add(fragmentToMove, newInstance);

				fragmentTransaction.setCustomAnimations(
						setSlideRightInAnimID(), setSlideRightOutAnimID(),
						setSlideLeftInAnimID(), setSlideLeftOutAnimID());
				fragmentTransaction.remove(toMove);
				fragmentTransaction.commit();
				fragmentManager.executePendingTransactions();

				fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.setCustomAnimations(
						setSlideRightInAnimID(), setSlideRightOutAnimID());
				newInstance.setContainerPosition(targetContainer);
				fragmentTransaction.replace(fragmentContainer[targetContainer],
						newInstance, newInstance.getName());
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				fragmentManager.executePendingTransactions();

				fragmentToMove--;
				targetContainer--;
			}
		}
	}

	/**
	 * Start a new mask (fragment)
	 * 
	 * @param maske
	 */
	public void startMask(IMask maske) {
		startMask(maske, null, false);
	}
	
	/**
	 * Start a new mask (fragment)
	 * 
	 * @param maske
	 */
	public void startMask(IMask maske, boolean withoutAnimation) {
		startMask(maske, null, withoutAnimation);
	}
	
	/**
	 * Start a new mask (fragment)
	 * 
	 * @param maske
	 * @param bundle
	 */
	public void startMask(IMask maske, Bundle bundle) {
		startMask(maske, bundle, false);
	}

	/**
	 * Start a new mask (fragment)
	 * 
	 * @param mask
	 * @param bundle
	 * @param withoutAnimation
	 */
	public void startMask(IMask mask, Bundle bundle, boolean withoutAnimation) {

		if (separator == null) {
			separator = setSeparartor();
		}

		int ebene = mask.getEbene();

		if(fragmentStack.size() == 0 && ebene > 0) {
			//We returned to the app and now we have to recreate the mask struktur
			startMask(mask.getParentView());
		}



		boolean closeEbeneZero = ebene == 0;
		boolean withReplaceLast = (fragmentStack.size() - 1) == ebene;
		if (fragmentStack.size() > ebene && fragmentStack.size() != 0) {
			//A lower Mask should be started

			// -> Stop all higher mask
			// -> Start correct mask

			int countToClose = fragmentStack.size() - ebene;

			int countToMove = (fragmentStack.size() - 1) - ebene;

			while (countToClose > 0) {
				if (countToMove == 0) {
					withReplaceLast = true;
				}
				closeHighestFragment(withReplaceLast, closeEbeneZero, withoutAnimation);
				countToClose--;
				countToMove--;
			}
		}

		if (fragmentContainer == null) {
			fragmentContainer = setFragmentContainer();
		}

		if (separator.length != 0 && ebene != 0
				&& (ebene - 1) < separator.length
				&& findViewById(separator[ebene - 1]) != null) {
			findViewById(separator[ebene - 1]).setVisibility(View.VISIBLE);
		}

		AbstractBaseFragment fragment = null;
		getIntent().putExtra(IMask.INTENT_KEY_MASKE, mask.getName());
		try {
			fragment = mask.getFragmentClass().newInstance();
			if(fragment != null) {
				fragment.setMaske(mask);
				fragment.setInitialBundle(bundle);
				fragmentStack.push(fragment);
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();

				if (ebene > fragmentContainer.length - 1 && ebene != 0) {

					if (!withReplaceLast) {
						moveAllFragementsToTheLeft();
						fragmentTransaction.setCustomAnimations(setSlideLeftInAnimID(),
								setSlideLeftOutAnimID());
					} else {
						fragmentTransaction.setCustomAnimations(
								setSlideRightInAnimID(), setSlideRightOutAnimID());
					}

					fragment.setContainerPosition(fragmentContainer.length - 1);
					fragmentTransaction.replace(
							fragmentContainer[fragmentContainer.length - 1], fragment,
							fragment.getName());
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
					fragmentManager.executePendingTransactions();

				} else {

					//Level 0 or there is currently a free slot
					if ((currentMask != null
							&& currentMask.getEbene() > fragmentContainer.length - 1)) {
						setBackIcon(false);
					}

					if(!withoutAnimation) {
						fragmentTransaction.setCustomAnimations(
								setSlideRightInDoubleAnimID(),
								setSlideRightOutDoubleAnimID());
					}


					fragment.setContainerPosition(ebene);
					fragmentTransaction.replace(fragmentContainer[ebene], fragment,
							fragment.getName());
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commitAllowingStateLoss();
					fragmentManager.executePendingTransactions();
				}

				currentMask = mask;

				refreshActionBar();
				clearUnusedMaskenSeperator(ebene);
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fragmentStack.setOnStackSizeChangeListener(this);
		
		if (getResources().getBoolean(setLandscapeID())) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		setContentView(setContentView());
			
		toolbar = (Toolbar) findViewById(setToolbarID());
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}

        mNavigationDrawerFragment = (AbstractNavigationDrawerFragment) getSupportFragmentManager().findFragmentById(setNavigationDrawerID());
		mNavigationDrawerFragment.setUp(setNavigationDrawerID(),(DrawerLayout) findViewById(setDrawerLayoutID()));
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public void disableMenu() {
		enabledMenu = false;
		getSupportActionBar().setHomeButtonEnabled(false);
		mNavigationDrawerFragment.lockDrawer();
	}

	public void enableMenu() {
		enabledMenu = true;
		if(currentMask.getEbene() == 0) {
			mNavigationDrawerFragment.mDrawerToggle.setMenu(mNavigationDrawerFragment.mDrawerLayout);
		} else {
			
		}
		getSupportActionBar().setHomeButtonEnabled(true);
		mNavigationDrawerFragment.unLockDrawer();
	}

	public void setBackIcon(boolean backButton) {
		mNavigationDrawerFragment.mDrawerToggle.animate(mNavigationDrawerFragment.mDrawerLayout, backButton);
	}

	protected void refreshActionBar() {
		if (fragmentStack.size() > fragmentContainer.length) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			mNavigationDrawerFragment.lockDrawer();
		} else {
			if (currentMask.isHideMenuToggle()) {
				disableMenu();
			} else {
				enableMenu();
			}

		}

		styleSeparators();
		setActionBarTitle();
	}

	protected void setActionBarTitle() {
		int titleID = currentMask.getTitle();
		if(titleID != -1 && titleID != 0) {
			if(mNavigationDrawerFragment.isOpening()) {
				getSupportActionBar().setSubtitle(null);
			} else {
				getSupportActionBar().setSubtitle(titleID);
			}
		} else {
			getSupportActionBar().setSubtitle(null);
		}
	}
	
	public void setGreyToolbarMode(boolean greyToolbarMode) {
		this.greyToolbarMode = greyToolbarMode;
		int statusbarColorID;
		int toolbarColorID;
		if(greyToolbarMode) {
			if ((currentMask != null
					&& currentMask.getEbene() <= fragmentContainer.length - 1)) {
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				mNavigationDrawerFragment.lockDrawer();
				setBackIcon(true);
			}
			
			getSupportActionBar().setTitle(null);
			getSupportActionBar().setSubtitle(null);
			statusbarColorID = currentMask.getTheme().getGreyStatusBarColorID();
			toolbarColorID = currentMask.getTheme().getGreyColorID();
		} else {
			
			
			if ((currentMask != null
					&& currentMask.getEbene() <= fragmentContainer.length - 1)) {
				if (currentMask.isHideMenuToggle()) {
					disableMenu();
				} else {
					enableMenu();
				}
				setBackIcon(false);
			}
			
			getSupportActionBar().setTitle(currentMask.getAppTitle());
			setActionBarTitle();
			statusbarColorID = currentMask.getTheme().getStatusBarColorID();
			toolbarColorID = currentMask.getTheme().getPrimaryColorID();
		}
		
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setStatusBarColor(getResources().getColor(statusbarColorID));
        }
		
		getToolbar().setBackgroundColor(getResources().getColor(toolbarColorID));
	}

	private void styleSeparators() {
		int colorID = currentMask.getTheme().getPrimaryColorID();
		for(int i=0; i<separator.length; i++) {
			if(findViewById(separator[i]) != null) {
				findViewById(separator[i]).setBackgroundColor(getResources().getColor(colorID));
			}
		}
	}

	private void clearUnusedMaskenSeperator(int currentEbene) {
		ArrayList<Integer> toClearIDs = new ArrayList<Integer>();

		for (int i = (currentEbene + 1); i < fragmentContainer.length; i++) {
			toClearIDs.add(Integer.valueOf(fragmentContainer[i]));
		}

		for (int i = currentEbene; i < separator.length; i++) {
			if (findViewById(separator[i]) != null) {
				findViewById(separator[i]).setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * Closes the highest mask
	 */
	public void closeHighestFragment() {
		closeHighestFragment(false, false, false);
	}

	/**
	 * Closes the highest mask
	 * 
	 * @param withNext
	 *            true, if the fragments shouldn't move, because of the creation of a new fragment
	 * @param closeEbeneZero
	 *            true, if a mask from level 0 is started
	 * @param withoutAnimation
	 * 			  true, if there should be no animation
	 */
	private void closeHighestFragment(boolean withNext, boolean closeEbeneZero, boolean withoutAnimation) {
		currentMask = currentMask.getParentView();


		if (fragmentStack.size() > 1
				|| (fragmentStack.size() >= 1 && closeEbeneZero)) {
			AbstractBaseFragment toDelete = fragmentStack.lastElement();
			toDelete.onFragmentClosing();
			resultBundlePair = toDelete.getResultBundle();

			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();

			if(!withoutAnimation) {
				if (fragmentStack.size() > fragmentContainer.length && !withNext) {
					fragmentTransaction.setCustomAnimations(
							setSlideRightInAnimID(), setSlideRightOutAnimID(),
							setSlideRightInAnimID(), setSlideRightOutAnimID());
				} else {
					fragmentTransaction.setCustomAnimations(setSlideLeftInAnimID(),
							setSlideLeftOutAnimID(), setSlideLeftInAnimID(),
							setSlideLeftOutAnimID());
				}
			}
			
			fragmentTransaction.remove(toDelete);
			fragmentTransaction.commitAllowingStateLoss();
			fragmentManager.executePendingTransactions();

			if (fragmentStack.size() > fragmentContainer.length && !withNext) {
				moveAllFragementsToTheRight();
			}

			fragmentStack.pop();

			if (currentMask != null) {
				clearUnusedMaskenSeperator(currentMask.getEbene());
				refreshActionBar();
			}

		} else {
			fragmentStack.get(0).perpareClosing();
			finish();
		}
	}
	
	/**
	 * Fires, if the fragmentStack changed the size (Each time a mask is started or closed)
	 *
	 * Checks for the main fragment an then calls the onFragmentStateChanged() or the
	 * onFragmentStateChangedLater() for notify the fragments after start
	 * @param oldSize
	 * @param newSize
	 */
	@Override
	public void onStackSizeChanged(int oldSize, int newSize) {
		if(newSize > 0) {
			for(int i=0; i < newSize; i++) {
				boolean mainFragment = false;
				if(i == newSize-1) {
					mainFragment = true;
				}
				fragmentStack.get(i).setMainFragment(mainFragment);
				if(fragmentStack.get(i).isStarted()) {
					fragmentStack.get(i).onFragmentStateChanged(mainFragment);
				} else {
					fragmentStack.get(i).onFragmentStateChangedLater();
				}
			}
		}
	}

	public Toolbar getToolbar() {
		return toolbar;
	}

	protected int setLandscapeID() {
		return R.bool.landscape;
	}

	protected int setActionBarBackDrawableID() {
		return R.drawable.ic_ab_back_holo_dark_am;
	}

	protected int setActionBarMenuDrawableID() {
		return R.drawable.ic_drawer;
	}

	protected int setSlideLeftInAnimID() {
		return R.anim.slide_left_in;
	}

	protected int setSlideLeftOutAnimID() {
		return R.anim.slide_left_out;
	}

	protected int setSlideRightInAnimID() {
		return R.anim.slide_right_in;
	}

	protected int setSlideRightOutAnimID() {
		return R.anim.slide_right_out;
	}

	protected int setSlideRightInDoubleAnimID() {
		return R.anim.slide_right_in_double;
	}

	protected int setSlideRightOutDoubleAnimID() {
		return R.anim.slide_right_out_double;
	}
	
	protected abstract int setTitleRes();
	protected abstract int[] setFragmentContainer();
	protected abstract int[] setSeparartor();
	protected abstract int setToolbarID();
	protected abstract int setContentView();
	protected abstract int setNavigationDrawerID();
	protected abstract int setDrawerLayoutID();
	public abstract IMask findMaskeByName(String name);
}
