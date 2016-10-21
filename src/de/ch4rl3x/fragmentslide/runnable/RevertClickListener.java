package de.ch4rl3x.fragmentslide.runnable;

import android.view.View;

import de.ch4rl3x.fragmentslide.fragment.AbstractBaseFragment;
import de.ch4rl3x.fragmentslide.interfaces.IOnRevertClickListener;
import de.ch4rl3x.fragmentslide.interfaces.IRevertToastRunnable;

public class RevertClickListener implements IOnRevertClickListener {

	private IRevertToastRunnable runnable;
	private AbstractBaseFragment fragment;
	
	@Override
	public void onClick(View view) {
		assert runnable!= null && fragment !=null:"Runnable and fragment are not allowed to be null";
		this.runnable.stop();
		this.fragment.closeToast();
	}
	
	@Override
	public void setRunnable(IRevertToastRunnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void setFragment(AbstractBaseFragment fragment) {
		this.fragment = fragment;
	}

}
