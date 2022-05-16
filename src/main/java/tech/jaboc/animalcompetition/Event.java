package tech.jaboc.animalcompetition;

import java.util.ArrayList;
import java.util.List;

/**
 * A classic C#-style event. Can be hooked onto by multiple event-handlers. Can have arguments (must be 1 class, just use a record if necessary)
 * @param <T> The event arguments
 */
public class Event<T> {
	List<IEventHandler<T>> eventHandlers = new ArrayList<>();
	
	/**
	 * Registers an event-handler to the event
	 * @param eventHandler The event-handler to register
	 */
	public void addEventHandler(IEventHandler<T> eventHandler) {
		eventHandlers.add(eventHandler);
	}
	
	/**
	 * Unregisters an event-handler from the event
	 * @param eventHandler The event-handler to unregister
	 */
	public void removeEventHandler(IEventHandler<T> eventHandler) {
		eventHandlers.remove(eventHandler);
	}
	
	/**
	 * Invokes all registered event-handlers
	 * @param eventArgs The arguments
	 */
	public void invoke(T eventArgs) {
		for (IEventHandler<T> handler : eventHandlers) {
			handler.invoke(eventArgs);
		}
	}
	
	/**
	 * The event-handler interface. Is a functional interface to mark that we can use lambdas.
	 * @param <T> The event arguments type
	 */
	@FunctionalInterface
	public interface IEventHandler<T> {
		void invoke(T eventArgs);
	}
}
