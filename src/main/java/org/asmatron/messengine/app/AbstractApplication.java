package org.asmatron.messengine.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractApplication<T> implements Application<T> {
	private static final Log log = LogFactory.getLog(AbstractApplication.class);

	private List<AppListener> listeners;

	private ResultProcessor<T> resultProcessor;

	private Module mainModule;

	public AbstractApplication(Module mainModule) {
		this.mainModule = mainModule;
		listeners = new ArrayList<AppListener>();
	}

	public void setMainModule(Module mainModule) {
		this.mainModule = mainModule;
	}

	@Override
	public T execute() {
		try {
			launchInitializers();
			launchPhase();
			launchDestroyers();
		} catch (Exception e) {
			log.debug(e, e);
		}
		if (resultProcessor != null) {
			return resultProcessor.result(getAttributes());
		}
		return null;
	}

	private void launchDestroyers() {
		for (AppListener destroyer : listeners) {
			try {
				destroyer.destroy();
			} catch (ApplicationAbortException e) {
				log.debug(e, e);
				throw e;
			} catch (Exception e) {
				log.debug(e, e);
			}
		}
	}

	private void launchInitializers() {
		for (AppListener initializer : listeners) {
			try {
				initializer.initialize();
			} catch (ApplicationAbortException e) {
				log.debug(e, e);
				throw e;
			} catch (Exception e) {
				log.debug(e, e);
			}
		}
	}

	private void launchPhase() {
		// AppModule currentPhase = null;
		// do {
		// currentPhase = phases.poll();
		// if (currentPhase != null) {
		try {
			mainModule.activate();
		} catch (ApplicationAbortException e) {
			log.debug(e, e);
			throw e;
		} catch (Exception e) {
			log.error(e, e);
		}
		try {
			mainModule.execute(getAttributes());
		} catch (ApplicationAbortException e) {
			log.debug(e, e);
			throw e;
		} catch (Exception e) {
			log.error(e, e);
		}
		try {
			mainModule.destroy();
		} catch (ApplicationAbortException e) {
			log.debug(e, e);
			throw e;
		} catch (Exception e) {
			log.error(e, e);
		}
		// }
		// } while (currentPhase != null);
	}

	@Override
	public void addAppListener(AppListener listener) {
		listeners.add(listener);
	}

	@Override
	public void setResultProcessor(ResultProcessor<T> resultProcessor) {
		this.resultProcessor = resultProcessor;
	}

	protected abstract Attributes getAttributes();

}
