package de.ch4rl3x.fragmentslide.utilities;

import java.util.Stack;

import de.ch4rl3x.fragmentslide.interfaces.OnStackSizeChangeListener;

public class NotifyableStack<T> extends Stack<T> {
	
	private OnStackSizeChangeListener listener;
	
	public void setOnStackSizeChangeListener(OnStackSizeChangeListener listener) {
		this.listener = listener;
	}
	
	@Override
	public T push(T object) {
		T result = super.push(object);
		if(listener != null) {
			listener.onStackSizeChanged(size()-1, size());
		}
		return result;
	}
	
	@Override
	public synchronized T pop() {
		T result = super.pop();
		if(listener != null) {
			listener.onStackSizeChanged(size()+1, size());
		}
		return result;
	}

}
