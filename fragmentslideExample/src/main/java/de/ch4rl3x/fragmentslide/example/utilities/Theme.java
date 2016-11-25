package de.ch4rl3x.fragmentslide.example.utilities;

import de.ch4rl3x.fragmentslide.example.R;
import de.ch4rl3x.fragmentslide.interfaces.ITheme;

public enum Theme implements ITheme{
	
	ThemeBlue(R.style.Theme_Blue, R.color.blue, R.color.blueDark, R.color.blueStatusBar),
	ThemeGreen(R.style.Theme_Green, R.color.green, R.color.greenDark, R.color.greenStatusBar),
	ThemeRed(R.style.Theme_Red, R.color.red, R.color.redDark, R.color.redStatusBar);
	
	int theme;
	int primaryColor;
	int primaryDarkColor;
	int statusBarColor;
	
	Theme(int theme, int primaryColor, int primaryDarkColor, int statusBarColor) {
		this.theme = theme;
		this.primaryColor = primaryColor;
		this.primaryDarkColor = primaryDarkColor;
		this.statusBarColor = statusBarColor;
	}

	@Override
	public int getPrimaryColorID() {
		return primaryColor;
	}
	
	@Override
	public int getPrimaryDarkColorID() {
		return primaryDarkColor;
	}
	
	@Override
	public int getStatusBarColorID() {
		return statusBarColor;
	}

	@Override
	public int getThemeID() {
		return theme;
	}

	@Override
	public int getGreyColorID() {
		return R.color.grey;
	}

	@Override
	public int getGreyStatusBarColorID() {
		return R.color.greyDarker;
	}
	
	

}
