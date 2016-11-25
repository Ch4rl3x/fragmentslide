package de.ch4rl3x.fragmentslide.example.abstracts;

import android.os.Bundle;

import java.util.ArrayList;

import de.ch4rl3x.fragmentslide.example.R;
import de.ch4rl3x.fragmentslide.fragment.AbstractBaseFragment;

public abstract class BaseFragment extends AbstractBaseFragment {

	@Override
	public int getRevertButtonTextColor() {
		return R.color.orange;
	}
	
	@Override
	protected int getRevertButtonText() {
		return R.string.revert;
	}

	@Override
	public int[] getFabButtons() {
		return findFabButtons();
	}
	
	private int[] findFabButtons() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		if(getActiviyAbstract().findViewById(R.id.fab1) != null) {
			list.add(Integer.valueOf(R.id.fab1));
		}
		
		if(getActiviyAbstract().findViewById(R.id.fab2) != null) {
			list.add(Integer.valueOf(R.id.fab2));
		}
		
		if(getActiviyAbstract().findViewById(R.id.fab3) != null) {
			list.add(Integer.valueOf(R.id.fab3));
		}
		
		int[] result = new int[list.size()];
		for(int i=0; i< list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}
	
	@Override
	public void onFragmentStarted(Bundle initialData) {
		super.onFragmentStarted(initialData);
		setHasOptionsMenu(true);
	}
}
