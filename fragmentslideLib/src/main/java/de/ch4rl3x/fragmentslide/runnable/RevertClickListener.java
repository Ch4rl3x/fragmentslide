package de.ch4rl3x.fragmentslide.runnable;

import android.view.View;

import de.ch4rl3x.fragmentslide.fragment.AbstractBaseFragment;
import de.ch4rl3x.fragmentslide.interfaces.IOnRevertClickListener;
import de.ch4rl3x.fragmentslide.interfaces.IRevertToastRunnable;

public class RevertClickListener implements IOnRevertClickListener {

	private IRevertToastRunnable runnable;

	@Override
	public final void onClick(View view) {
		assert runnable!= null :"Runnable is not allowed to be null";
		this.runnable.stop();
		onRevertClicked(view);
	}

	public void onRevertClicked(View view) {
		//Leere Implementation
	}
	
	@Override
	public void setRunnable(IRevertToastRunnable runnable) {
		this.runnable = runnable;
	}
}
