package org.asmatron.messengine.action;

import javax.swing.SwingUtilities;


public abstract class SwingResponseCallback<T> implements ResponseCallback<T> {
	public abstract void updateGui(T t);

	@Override
	public void onResponse(final T t) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateGui(t);
			}
		});
	}

}
