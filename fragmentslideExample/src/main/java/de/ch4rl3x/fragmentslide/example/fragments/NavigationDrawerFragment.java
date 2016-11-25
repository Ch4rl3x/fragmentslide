package de.ch4rl3x.fragmentslide.example.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import de.ch4rl3x.fragmentslide.example.R;
import de.ch4rl3x.fragmentslide.example.utilities.Mask;
import de.ch4rl3x.fragmentslide.example.utilities.Theme;
import de.ch4rl3x.fragmentslide.fragment.AbstractNavigationDrawerFragment;
import de.ch4rl3x.fragmentslide.interfaces.ITheme;


public class NavigationDrawerFragment extends AbstractNavigationDrawerFragment implements OnClickListener {

	@Override
	protected int setAppNameStringID() {
		return R.string.app_name;
	}

	@Override
	protected int setNavigationDrawerLayout() {
		return R.layout.fragment_navigation_drawer;
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.menu_blue:
			startMask(Mask.Blue);
			break;
		case R.id.menu_green:
			startMask(Mask.Green);
			break;
		case R.id.menu_red:
			startMask(Mask.Red);
			break;
		}

		close();
	}
	
	@Override
	public void onFragmentStarted(Bundle initialData) {
		super.onFragmentStarted(initialData);
				
		view.findViewById(R.id.menu_blue).setOnClickListener(this);
		view.findViewById(R.id.menu_green).setOnClickListener(this);
		view.findViewById(R.id.menu_red).setOnClickListener(this);
	}
	
	@Override
	protected ITheme setInitialTheme() {
		return Theme.ThemeBlue;
	}

	@Override
	public int[] getFabButtons() {
		return new int[0];
	}
}
