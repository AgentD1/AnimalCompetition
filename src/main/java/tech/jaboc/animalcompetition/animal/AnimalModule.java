package tech.jaboc.animalcompetition.animal;

import java.util.HashMap;
import java.util.Map;

/**
 * A module is a data class that is on animal. It contains variables and functions that are localized to something only certain animals can do,
 * like attacking, flying, or walking.
 */
public abstract class AnimalModule {
	public static Map<String, Class<? extends AnimalModule>> classes = new HashMap<>();
	
	public static void registerModule(Class<? extends AnimalModule> clazz) {
		classes.put(clazz.getSimpleName(), clazz);
	}
	
	public void addModule(Animal animal) {
		animal.addModule(this);
	}
}
