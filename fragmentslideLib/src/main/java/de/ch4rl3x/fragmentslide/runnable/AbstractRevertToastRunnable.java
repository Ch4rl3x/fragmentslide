package de.ch4rl3x.fragmentslide.runnable;

import de.ch4rl3x.fragmentslide.interfaces.IRevertToastRunnable;

public abstract class AbstractRevertToastRunnable implements IRevertToastRunnable {

	private boolean activated = true;

	@Override
	public final void stop() {
		activated = false;
		onStop();
	}

	@Override
	public final void run() {
		if(activated) {
			innerRun();
		}
	}
	
	/**
	 * Diese Methode wird aufgerufen, wenn nicht vor dem Run des Threads die
	 * Methode stop() aufgerufen wurde
	 */
	protected abstract void innerRun();
	
	/**
	 * Diese Methode wird aufgerufen, wenn der Runnable gestoppt wird
	 */
	protected void onStop() {
		//Leere implementation
	}
}