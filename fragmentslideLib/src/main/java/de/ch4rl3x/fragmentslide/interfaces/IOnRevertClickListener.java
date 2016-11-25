package de.ch4rl3x.fragmentslide.interfaces;

import android.view.View.OnClickListener;

import de.ch4rl3x.fragmentslide.fragment.AbstractBaseFragment;

public interface IOnRevertClickListener extends OnClickListener {
	
	public void setRunnable(IRevertToastRunnable runnable);

}
