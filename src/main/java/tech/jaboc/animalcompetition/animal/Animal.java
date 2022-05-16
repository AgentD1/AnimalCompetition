package tech.jaboc.animalcompetition.animal;

import com.fasterxml.jackson.databind.annotation.*;
import org.jetbrains.annotations.*;
import tech.jaboc.animalcompetition.animal.json.*;
import tech.jaboc.animalcompetition.environment.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An animal is the base class for a competitor in the competition.
 */
@JsonSerialize(using = AnimalSerializer.class)
@JsonDeserialize(using = AnimalDeserializer.class)
public class Animal {
	public String name;
	public String species;
	
	List<AnimalModule> modules = new ArrayList<>();
	
	List<Trait> traits = new ArrayList<>();
	
	/**
	 * Gets an unmodifiable list of the animal's traits
	 * @return An unmodifiable list of the animal's traits
	 */
	public List<Trait> getTraits() {
		return Collections.unmodifiableList(traits);
	}
	
	/**
	 * Adds a trait to the animal
	 * @param trait The trait to add
	 */
	public void addTrait(Trait trait) {
		traits.add(trait);
		for (Modifier mod : trait.modifiers) {
			mod.add(this);
		}
	}
	
	/**
	 * Remove a trait from the animal
	 * @param trait The trait to remove
	 */
	public void removeTrait(Trait trait) {
		if (!traits.contains(trait)) throw new NullPointerException("Animal does not contain this trait! " + trait);
		for (Modifier mod : trait.modifiers) {
			mod.remove(this);
		}
		traits.remove(trait);
	}
	
	/**
	 * Registers an environment for use on this animal. Can technically be used to register multiple environments, but shouldn't
	 * @param environment The environment to register
	 */
	public void addEnvironment(Environment environment) {
		for (EnvironmentalFactor f : environment.features) {
			for (Modifier mod : f.modifiers) {
				mod.add(this);
			}
		}
	}
	
	/**
	 * Unregisters an environment for use on this animal. Can technically be used on environments that were never added, but shouldn't
	 * @param environment The environment to unregister
	 */
	public void removeEnvironment(Environment environment) {
		for (EnvironmentalFactor f : environment.features) {
			for (Modifier mod : f.modifiers) {
				mod.remove(this);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(String.format("Animal of species %s, named %s.\n", species, name));
		b.append("Stats:\n");
		for (AnimalModule m : modules) {
			b.append(m.getClass().getSimpleName());
			b.append('\n');
			
			try {
				HashMap<String, Double> other = new HashMap<>();
				for (Field field : m.getClass().getFields()) {
					if (field.isAnnotationPresent(AnimalComponent.class)) {
						AnimalComponent c = field.getAnnotation(AnimalComponent.class);
						String name = c.name();
						double value = field.getDouble(m);
						if (c.multiplier()) {
							if (other.containsKey(c.name())) { // Use format: name: value xMultiplier% = finalValue
								// TODO: change this to just display the whole number later
								b.append(String.format("  %s: %.2f x%.0f%% = %.2f\n", name, other.get(name), value * 100, other.get(name) * value));
								other.remove(name);
							} else {
								other.put(name, value);
							}
						} else {
							if (other.containsKey(c.name())) {
								b.append(String.format("  %s: %.2f x%.0f%% = %.2f\n", name, value, other.get(name) * 100, other.get(name) * value));
								other.remove(name);
							} else {
								other.put(name, value);
							}
						}
					}
				}
				
				for(var entry : other.entrySet()) {
					b.append(String.format("  %s: %.2f x%.0f%% = %.2f\n", entry.getKey(), entry.getValue(), 1.0 * 100, entry.getValue()));
				}
			} catch (IllegalAccessException e) { // This will not happen, and if it does, I need to know about it
				throw new RuntimeException(e);
			}
		}
		
		b.append("Traits:\n");
		for (Trait t : traits) {
			b.append(t.name);
			b.append('\n');
		}
		
		b.deleteCharAt(b.length() - 1);
		
		return b.toString();
	}
	
	//region Module Management
	
	/**
	 * Gets a module of a given type, if there is one present. Returns null otherwise. This works with superclasses.
	 * @param clazz The class of the module type to search for
	 * @param <T> The type of the module to search for
	 * @return The module of the given type, or null
	 */
	@Contract(pure = true)
	public <T extends AnimalModule> T getModuleOfType(Class<T> clazz) {
		for (AnimalModule module : modules) {
			if (clazz.isAssignableFrom(module.getClass())) {
				//noinspection unchecked
				return (T) module;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets all the modules of a given type. This works with superclasses.
	 * @param clazz The class of the module type to search for
	 * @param <T> The type of the module to search for
	 * @return A list of all modules of this type
	 */
	@Contract(pure = true)
	public <T extends AnimalModule> List<T> getModulesOfType(Class<T> clazz) {
		List<T> matches = new ArrayList<>();
		
		for (AnimalModule module : modules) {
			if (clazz.isAssignableFrom(module.getClass())) {
				//noinspection unchecked
				matches.add((T) module);
			}
		}
		
		return matches;
	}
	
	@Contract(pure = true)
	public boolean hasModuleOfType(Class<? extends AnimalModule> clazz) {
		for (AnimalModule module : modules) {
			if (module.getClass().isAssignableFrom(clazz)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void addModule(AnimalModule module) {
		if (modules.stream().anyMatch(x -> x.getClass().getName().equals(module.getClass().getName()))) {
			throw new IllegalStateException("Cannot insert module of type " + module + " to " + this + ", one already exists in the list");
		}
		modules.add(module);
	}
	
	public <T extends AnimalModule> boolean applyToModuleIfNotNull(Class<T> clazz, Consumer<T> function) {
		T module = getModuleOfType(clazz);
		if (module == null) return false;
		function.accept(module);
		return true;
	}
	
	public <T extends AnimalModule, R> R getFromModuleIfNotNull(Class<T> clazz, Function<T, R> function) {
		T module = getModuleOfType(clazz);
		if (module == null) return null;
		return function.apply(module);
	}
	
	public <T extends AnimalModule, R> R getFromModuleIfNotNullElse(Class<T> clazz, Function<T, R> function, R elseResult) {
		T module = getModuleOfType(clazz);
		if (module == null) return elseResult;
		return function.apply(module);
	}
	
	//endregion
}