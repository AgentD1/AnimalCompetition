package tech.jaboc.animalcompetition;

import java.util.ArrayList;
import java.util.List;

public class Event<T> {
	List<IEventHandler<T>> eventHandlers = new ArrayList<>();

	public void addEventHandler(IEventHandler<T> eventHandler) {
		eventHandlers.add(eventHandler);
	}

	public void removeEventHandler(IEventHandler<T> eventHandler) {
		eventHandlers.remove(eventHandler);
	}

	public void invoke(T eventArgs) {
		for (IEventHandler<T> handler : eventHandlers) {
			handler.invoke(eventArgs);
		}
	}

	@FunctionalInterface
	public interface IEventHandler<T> {
		void invoke(T eventArgs);
	}
}
