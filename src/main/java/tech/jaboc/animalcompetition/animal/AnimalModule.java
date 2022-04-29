package tech.jaboc.animalcompetition.animal;

import java.util.HashMap;
import java.util.Map;

public abstract class AnimalModule {
	public static Map<String, Class<? extends AnimalModule>> classes = new HashMap<>();

	public static void registerModule(Class<? extends AnimalModule> clazz) {
		classes.put(clazz.getSimpleName(), clazz);
	}

	public void addModule(Animal animal) {
		animal.addModule(this);
	}
}
