package org.asmatron.messengine.app;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Semaphore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ApplicationUtils {
	
	private static final Log LOG = LogFactory.getLog(ApplicationUtils.class);

	public static void showFrameAndWaitForClose(Window window) {
		try {
			final Semaphore lock = new Semaphore(0);
			window.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					lock.release();
				}
			});
			window.setVisible(true);
			lock.acquire();
		} catch (Exception e) {
			LOG.error(e, e);
		}
	}

}
