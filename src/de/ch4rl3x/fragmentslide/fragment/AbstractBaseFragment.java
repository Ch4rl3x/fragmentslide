package de.ch4rl3x.fragmentslide.fragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.ch4rl3x.fragmentslide.AbstractBaseActivity;
import de.ch4rl3x.fragmentslide.interfaces.IMask;
import de.ch4rl3x.fragmentslide.interfaces.IOnRevertClickListener;
import de.ch4rl3x.fragmentslide.interfaces.IRevertToastRunnable;
import de.ch4rl3x.fragmentslide.R;

public abstract class AbstractBaseFragment extends Fragment {

	protected final String LOG_TAG = this.getClass().getSimpleName();
	private static boolean CREATED = false;

	private boolean mainFragment = false;
	private boolean started = false;
	private boolean fragmentStateLater = false;
	private int containerPosition;
	private Bundle initialBundle = null;
	private IMask mask;
	protected FloatingActionButton fab;
	private Thread closeRevertToastThread;
	protected View rootView;
	private Pair<IMask, Bundle> resultBundlePair;

	private boolean fragmentIsClosing = false;
	
	public void setInitialBundle(Bundle bundle) {
		this.initialBundle = bundle;
	}
	
	protected void onAnimationStart(boolean mainFragment) {
		//Empty implementation
    }
	
	protected void onAnimationRepeat(boolean mainFragment) {
		//Empty implementation
    }
	
	protected void onAnimationEnd(boolean mainFragment) {
		//Empty implementation
    }
	
	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		Animation anim = super.onCreateAnimation(transit, enter, nextAnim);
		
		//If not, and an animation is defined, load it now
	    if (anim == null && nextAnim != 0) {
	        anim = AnimationUtils.loadAnimation(getActiviyAbstract(), nextAnim);
	    }
		
		if(anim != null) {
			anim.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					AbstractBaseFragment.this.onAnimationStart(mainFragment);	
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					AbstractBaseFragment.this.onAnimationRepeat(mainFragment);
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					AbstractBaseFragment.this.onAnimationEnd(mainFragment);
				}
			});
		}
		
        return anim;
	}

	public IMask getMaske() {
		return mask;
	}
	
	public void setMaske(IMask mask) {
		this.mask = mask;
	}

	protected String setFragmentInfo() {
		return null;
	}

	@Override
	public void onStop() {
		started = false;
	    super.onStop();
	}
	
	/*
	 * Don't use onResume. Use onFragmentStarted() instead.
	 */
	@Override
	@Deprecated
	public void onResume() {
		super.onResume();	

		onFragmentResumed(initialBundle);
	}
	
	/*
	 * Don't use onResume. Use onFragmentStarted() instead.
	 */
	@Override
	@Deprecated
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);	
	}
	
	/*
	 * Don't use onResume. Use onFragmentStarted() instead.
	 */
	@Override
	@Deprecated
	public void onStart() {
		super.onStart();
		
		onFragmentStarted(initialBundle);
		
		if(fragmentStateLater) {
			onFragmentStateChanged(mainFragment);
		}

		if(!isStarted()) {
			throw new RuntimeException("Not called super.onFragmentStarted() before!");
		}
	}
		
	public void onFragmentStarted(Bundle initialData) {
		started = true;
	}
	
	public void onFragmentResumed(Bundle initialData) {
		//Empty implementation
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), mask.getTheme().getThemeID());
		LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
		rootView = (View) localInflater.inflate(mask.getLayout(), container, false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getActiviyAbstract().getWindow().setStatusBarColor(getResources().getColor(
					mask.getTheme().getStatusBarColorID()));
        }
		
		getActiviyAbstract().getToolbar()
			.setBackgroundColor(getResources().getColor(
				mask.getTheme().getPrimaryColorID()));

	   	return rootView;
	}

	protected void finish() {
		getActiviyAbstract().closeHighestFragment();
	}
	
	/**
     * Look for a child view with the given id.  If this view has the given
     * id, return this view.
     *
     * @param id The id to search for.
     * @return The view that has the given id in the hierarchy or null
     */
    public final View findViewById(int id) {
        return rootView.findViewById(id);
    }
	
    
   	public void startMask(IMask view) {
   		getActiviyAbstract().startMask(view);
	}
   	
   	public void startMask(IMask mask, boolean withoutAnimation) {
   		getActiviyAbstract().startMask(mask, withoutAnimation);
   	}
   	
	public void startMask(IMask view, Bundle bundle) {
   		getActiviyAbstract().startMask(view, bundle);
	}

	public String getName() {
		return getClass().getSimpleName();
	}
	
	protected AbstractBaseActivity getActiviyAbstract() {
		return (AbstractBaseActivity) getActivity();
	}

	/**
	 * On Back Key Pressed
	 * 
	 * @return true, if Back is fully consumed, otherwise
	 *         it will be handled by the activity
	 */
	public boolean onBackPressed() {
		fragmentIsClosing = true;
		closeToast();
		return false;
	}
	
	/**
	 * Checks, if this is the main fragment
	 * 
	 * @return true, if it is the main fragment
	 */
	public boolean isMainFragment() {
		return mainFragment;
	}

	/**
	 * Set these fragment as the main fragment
	 * 
	 * @param mainFragment
	 */
	public void setMainFragment(boolean mainFragment) {
		this.mainFragment = mainFragment;
	}
	
	/**
	 * Checks whether the fragment has started and the onResume method has been passed
	 * 
	 * @return true, if the fragment is started
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * Every time, when a mask is started or closed, these method fires
	 * 
	 * @param mainFragment
	 */
	public void onFragmentStateChanged(final boolean mainFragment) {
		fragmentStateLater = false;
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (mainFragment) {
					if (fab != null) {
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (!fragmentIsClosing) {
							getActiviyAbstract().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									fab.setVisibility(View.VISIBLE);
								}
							});
						}
					}
				} else {
					if (fab != null) {
						try {
							Thread.sleep(150);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						getActiviyAbstract().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								fab.setVisibility(View.INVISIBLE);
							}
						});
					}
				}
			}
		}).start();
	}

	public void enableFabButton(int buttonDrawable, View.OnClickListener clickListener) {
		int currentContainer = getContainerPosition();
		int[] fabButtton = getFabButtons();
		if(currentContainer < fabButtton.length) {
			fab = (FloatingActionButton)getActiviyAbstract().findViewById(fabButtton[currentContainer]);
			fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(getMaske().getTheme().getPrimaryColorID())));
			fab.setImageDrawable(getResources().getDrawable(buttonDrawable));
			fab.setOnClickListener(clickListener);
		}
	}

	public void makeRevertToast(String message, IRevertToastRunnable runnable, IOnRevertClickListener onRevertClickListener) {
		if(fab != null) {
			fab.setVisibility(View.GONE);
		}
		onRevertClickListener.setFragment(this);
		onRevertClickListener.setRunnable(runnable);
		int currentContainer = getContainerPosition();
		int[] toastLayouts = getToastLayouts();
		if(currentContainer < toastLayouts.length) {
			final LinearLayout toastView = (LinearLayout) getActiviyAbstract().findViewById(toastLayouts[currentContainer]);
			if(!CREATED) {
				RelativeLayout inflated = (RelativeLayout) LayoutInflater.from(getActiviyAbstract()).inflate(R.layout.toast_revert, null);
				toastView.addView(inflated);
			} else {
				CREATED = true;
			}

			((TextView)toastView.findViewById(R.id.toast_revert_message)).setText(message);
			((Button) toastView.findViewById(R.id.revert_button)).setTextColor(getResources().getColor(getRevertButtonTextColor()));
			((Button) toastView.findViewById(R.id.revert_button)).setText(getRevertButtonText());
			toastView.findViewById(R.id.revert_button).setOnClickListener(null);
			toastView.findViewById(R.id.revert_button).setOnClickListener(onRevertClickListener);

			animateToast(toastView, runnable);
		}
	}

	public void closeToast() {
		if(closeRevertToastThread != null && closeRevertToastThread.isAlive()) {
			closeRevertToastThread.interrupt();
		}
	}

	private void animateToast(final LinearLayout toastView, final IRevertToastRunnable runnable) {
		final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toastView.getLayoutParams();
		ValueAnimator animator = ValueAnimator.ofInt(params.bottomMargin, -4);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator)
			{
				params.bottomMargin = (Integer) valueAnimator.getAnimatedValue();
				toastView.requestLayout();
			}
		});
		animator.setDuration(250);
		animator.start();

		closeRevertToastThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				getActiviyAbstract().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if(!fragmentIsClosing && fab != null) {
							fab.setVisibility(View.VISIBLE);
						}

						int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -50, getActiviyAbstract().getResources().getDisplayMetrics());
						final ValueAnimator backanimator = ValueAnimator.ofInt(params.bottomMargin, px);
						backanimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
							@Override
							public void onAnimationUpdate(ValueAnimator valueAnimator)
							{
								params.bottomMargin = (Integer) valueAnimator.getAnimatedValue();
								toastView.requestLayout();
							}
						});
						backanimator.setDuration(250);
						backanimator.start();
						runnable.run();
					}
				});
			}
		});
		closeRevertToastThread.start();
	}

	public void onFragmentStateChangedLater() {
		fragmentStateLater = true;		
	}

	/**
	 * Set the container position. 0 for the root container
	 * 
	 * @param containerPosition
	 */
	public void setContainerPosition(int containerPosition) {
		this.containerPosition = containerPosition;
	}
	
	/**
	 * Returns the container position. 0 for the root container
	 *
	 * @return
	 */
	public int getContainerPosition() {
		return this.containerPosition;
	}

	public void onFragmentClosing() {
		if(fab != null) {
			fab.setVisibility(View.INVISIBLE);
		}
	}
	
	protected void setGreyToolbarMode(boolean greyToolbarMode) {
		getActiviyAbstract().setGreyToolbarMode(greyToolbarMode);
	}
	
	public void onHomeClicked() {
		//Empty implementation
	}
	
	public void setResult(IMask target, Bundle resultBundle) {
		this.resultBundlePair = new Pair<IMask, Bundle>(target, resultBundle);
	}
	
	public Pair<IMask, Bundle> getResultBundle() {
		return resultBundlePair;
	}

	protected int getRevertButtonTextColor() {
		return R.color.red;
	}

	protected int getRevertButtonText() {
		return R.string.revert;
	}

	protected abstract int[] getToastLayouts();
	protected abstract int[] getFabButtons();
}
