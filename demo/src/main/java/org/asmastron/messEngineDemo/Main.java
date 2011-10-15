package org.asmastron.messEngineDemo;

import org.asmatron.messengine.engines.DefaultEngine;
import org.asmatron.messengine.engines.Engine;
import org.asmatron.messengine.engines.support.EngineConfigurator;

public class Main {

	public static void main(String[] args) {
		// Create the engine
		Engine engine = new DefaultEngine();
		// Create our objects
		View view = new View(engine);
		Service service = new Service(engine, engine);
		AnotherService anotherService = new AnotherService();

		// Configure them
		EngineConfigurator configurator = new EngineConfigurator(engine);
		configurator.setup(view, service, anotherService);

		// Start your engines
		engine.start();

		// run the app
		view.setVisible(true);
	}
}
