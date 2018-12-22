package de.ch4rl3x.fragmentslide.utilities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import de.ch4rl3x.fragmentslide.interfaces.ITheme;

/**
 * A special form of ActionBarDrawerToggle
 *
 * The color of the toolbar will also be animated and changes with the theme
 *
 * @author Alexander Karkossa
 *
 */
public class ToolBarDrawerToggle extends ActionBarDrawerToggle{
	
	private Toolbar toolbar;
	private Activity activity;
	private DrawerLayout drawerLayout;
	
	private int primaryColor;
	private int primaryColorDark;
	
	private boolean changeColor = false;
	
	private boolean isOpening = false;
	private boolean isClosing = false;

	public ToolBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, 
			Toolbar toolbar, 
			ITheme initialTheme,
			int openDrawerContentDescRes, int closeDrawerContentDescRes) {
		super(activity, drawerLayout, openDrawerContentDescRes,
				closeDrawerContentDescRes);
		this.drawerLayout = drawerLayout;
		this.activity = activity;
		this.toolbar = toolbar;
	}
	
	@Override
	public void onDrawerSlide(View drawerView, float slideOffset) {
		super.onDrawerSlide(drawerView, slideOffset);

		if(changeColor) {
			Integer colorFrom = activity.getResources().getColor(primaryColor);
			Integer colorTo = activity.getResources().getColor(primaryColorDark);
			ArgbEvaluator colorSwitcher = new ArgbEvaluator();
			
			toolbar.setBackgroundColor((Integer)colorSwitcher.evaluate(slideOffset, colorFrom, colorTo));
		}
	}

	/**
	 * Animate the button e.g. to back button
	 * 
	 * @param drawerView
	 * @param backButton
	 */
	public void animate(final View drawerView, boolean backButton) {
		animate(drawerView, backButton, 500);
	}

	/**
	 * Animate the button e.g. to back button
	 *
	 * @param drawerView
	 * @param backButton
	 * @param duration
	 */
	public void animate(final View drawerView, boolean backButton, long duration) {
		ValueAnimator anim;
		if (backButton) {
			anim = ValueAnimator.ofFloat(0, 1);
		} else {
			anim = ValueAnimator.ofFloat(1, 0);
		}

		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				float slideOffset = (Float) valueAnimator.getAnimatedValue();
				ToolBarDrawerToggle.super.onDrawerSlide(drawerView, slideOffset);
			}
		});
		anim.setInterpolator(new DecelerateInterpolator());
		anim.setDuration(duration);
		anim.start();
	}
	
	/**
	 * Animate the button to menu
	 * 
	 * @param drawerView
	 */
	public void setMenu(final View drawerView) {
		super.onDrawerSlide(drawerView, 0);
	}

	/**
	 * Change the color for the slide effekt
	 * 
	 * @param theme
	 */
	public void changePrimaryColor(ITheme theme) {
		this.changeColor = true;
		this.primaryColor = theme.getPrimaryColorID();
		this.primaryColorDark = theme.getPrimaryDarkColorID();
	}
	
	@Override
	public void onDrawerStateChanged(int newState) {		
		if(newState == DrawerLayout.STATE_SETTLING || newState == DrawerLayout.STATE_DRAGGING){
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            	onDrawerClosing();
            }else{
            	onDrawerOpening();
            }
        }
		super.onDrawerStateChanged(newState);
	}
	
	@Override
	public void onDrawerClosed(View drawerView) {
		isClosing = false;
		isOpening = false;
		super.onDrawerClosed(drawerView);
	}
	
	@Override
	public void onDrawerOpened(View drawerView) {
		isClosing = false;
		isOpening = false;
		super.onDrawerOpened(drawerView);
	}
	
	protected void onDrawerClosing() {
		isClosing = true;
		isOpening = false;
	}
	
	protected void onDrawerOpening() {
		isClosing = false;
		isOpening = true;
	}

	public boolean isOpening() {
		return isOpening;
	}

	public boolean isClosing() {
		return isClosing;
	}

}
