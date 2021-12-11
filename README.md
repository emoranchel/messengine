# MessEngine
## Overview
Simple java messaging framework for applications that allows decoupled interaction between modules.

Framework that helps in the control and interaction in applications applying mvc pattern using events, actions and messages for comunication between different layers.

## Usage 
1. 
  * Download messengine.jar and include in classpath
  * Maven:
```
<dependency>
	<groupId>org.asmatron</groupId>
	<artifactId>messengine</artifactId>
	<version>0.1.0</version>
</dependency>
```
2. Create configuration
3. Create Actions/Events/Messages/Model catalog
4. Annotate classes
5. Send actions in view, send events and messages in services.
6. Enjoy.
  
## Goals

Unify communication between different layers of an application and keep it simple.

## Details

While designing and programming we divide the system in layers, View layer, Control layer, Service layer, Data layer and communicate them by injecting dependencies but sometimes one component of the application ends up with many dependencies inside.

Example: A control object that has 10+ dependencies injected and not all them are used in every method.

While it can be argued that this is a design problem in itself sometimes it is unavoidable. With desktop applications for example or in those where you have a Persistent View State a problem that might be solved in many ways. MessEngine tries to solve it by staying in the middle to communicate all this layers gracefully.

Let�s see a swing desktop application before and after messengine for a clear example on how messEngine might help you.

A desktop application has many ways of accomplishing a task, menu bar, button, key shortcut, and sometimes you have many buttons do the same thing. For example a twitter app might have a search user button, a search user menu item, a right click menu search user, a shortcut to find user and finally a link in the user name to search for it, how do we handle this?

OLD SKOOL:

![https://messengine.googlecode.com/svn/wiki/diagram01.jpg](https://messengine.googlecode.com/svn/wiki/diagram01.jpg)

What do we have here? Simple inject the service whenever you need it, and handle the view flow on the view, right? If we need to show a popup then we add that code on the eventListener of the menu, when things get complex add another layer of control or flow, and then keep growing.
The problem being is that the menu can do a lot of more things, so it suddenly needs more collaborators ending in classes that have lots and lots of dependencies.

We had exactly this problem and at first it doesn�t look so bad but with time the problem show its true face: You cannot separate projects since there is so much dependencies that even if you try you can�t sort it out.

Why is that relevant? Building a jar with 20 classes completely tested takes seconds, Building the entire application even if something very little changed can take minutes.
So our problem was: We want to separate concerns unit test them and have them run very fast.

But how to do it if everything is dependent on everything and unit tests adds up to very different things.
Well we created this engine for that purpose and now we are sharing it to you, how do the same system looks after we refactored with the engine.

![https://messengine.googlecode.com/svn/wiki/diagram02.jpg](https://messengine.googlecode.com/svn/wiki/diagram02.jpg)

Now how it works, pretty much the same, we have the services HANDLING ACTIONS the view DISPATCHING ACTIONS and then services FIRING EVENTS and the view LISTENING EVENTS, what does this gives us?

The view doesn�t need to know what services are, or where they lie in the system, the view only wants to execute them. The view reacts to events no matter where they may come from.

The service simply receives method calls, and in turn they send events to the messEngine.

WAIT! There's more!

Now between service we have dependencies right?

Sometimes a service might fire a process on another service. What to do? Inject?

NO SIR

MessEngine has another module (actually of outmost importance) a simple messaging service that allows any service to send a message and listeners might handle it exactly the same as the above picture (but now with services on both ends and messages instead of actions and events)

Now in code how do this looks?

## CODE EXAMPLE

1. Create a view that SENDS actions and LISTENS events [View.java](https://github.com/emoranchel/MessEngineDemo/blob/main/src/main/java/org/asmastron/messEngineDemo/View.java)
  * See the invocation inside the swing action listener? that's a call to our engine, note the only dependency is the `ViewEngine`
  * Note also that two methods are annotated using `EventMethod` this methods will be called when those events occur.
  * The engine uses **ids** for actions, events and messages. ids are simple Strings, so you better have a good catalog (an interface with those declarations should be enough)
  * note the service call is **typesafe** in fact the `ActionId` object can be constant to ensure type safety
  * `@EventsMethod` methods receive:
    * `()` nothing
    * `(EventObject)` the actual event object
    * `(Object)` which must be the wrapped ValueEvent object type see AutoWrapping
  * That is true for `@ActionMethod` and `@MessageMethod`
2. Create the backing service that HANDLES actions, FIRES events and SENDS messages to other unknown services. [Service.java](https://github.com/emoranchel/MessEngineDemo/blob/main/src/main/java/org/asmastron/messEngineDemo/Service.java)
  * Two dependencies one to FIRE events to the view `ControlEngine` and the other to SEND messages to other services `MessEngine`
  * An `@ActionMethod` to handle the action sent in the view.
3. Create another service that just LISTEN to messages. [AnotherService.java](https://github.com/emoranchel/MessEngineDemo/blob/main/src/main/java/org/asmastron/messEngineDemo/AnotherService.java)
  * No dependencies since we are not going to notify anyone
  * A `@MessageMethod` that handles a message sent from a service.
4. Glue it toguether using an engine (and a main method) [Main.java](https://github.com/emoranchel/MessEngineDemo/blob/main/src/main/java/org/asmastron/messEngineDemo/Main.java)
  * First thing create the engines. Note we create a `DefaultEngine` which is the three engines at the same time.
  * Now create the objects and send them their dependency, note they don't need and should not know what implementation they are using.
  * Now configure the objects using an `EngineConfigurator` so that the annotations do their work.
  * Finally start the engine if not threads will not be created and nothing will work.
  * Show your app
5. Enjoy.

## What's next?

* Be sure to check our FrameworkNotes
* Want to build an app with this?
  * Check BuildingMyFirstApp
* Want to migrate your app to this?
  * Check MigrationGuide
* Want to use us on the web?
  * check WebUssage
* Asynchronicity? Is this synched or not?
  * Go to EngineConfiguration
* Got some ideas? SURE!
  * Send us a mail, comment add issues we WILL look at them and get in touch with you.
* Have questions?
  * Check our FAQ