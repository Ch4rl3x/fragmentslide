package de.ch4rl3x.fragmentslide.interfaces;

import de.ch4rl3x.fragmentslide.fragment.AbstractBaseFragment;

public interface IMask {
	
	public static final String INTENT_KEY_MASKE = "mask";
	
	public IMask getParentView();

	public Class<? extends AbstractBaseFragment> getFragmentClass();
	
	public ITheme getTheme();
		
	public int getLayout();

	public int getTitle();

	public int getEbene();
	
	public String getName();
	
	public boolean isHideMenuToggle();
	
	public int getAppTitle();
	
}
