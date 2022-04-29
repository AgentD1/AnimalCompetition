package tech.jaboc.animalcompetition.animal;

import com.fasterxml.jackson.databind.annotation.*;
import org.jetbrains.annotations.*;
import tech.jaboc.animalcompetition.animal.json.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@JsonSerialize(using = AnimalSerializer.class)
@JsonDeserialize(using = AnimalDeserializer.class)
public class Animal {
	public String name;
	public String species;
	
	List<AnimalModule> modules = new ArrayList<>();
	
	List<Trait> traits = new ArrayList<>();
	
	public List<Trait> getTraits() {
		return Collections.unmodifiableList(traits);
	}
	
	public void addTrait(Trait trait) {
		traits.add(trait);
		for (Modifier mod : trait.modifiers) {
			mod.add(this);
		}
	}
	
	public void removeTrait(Trait trait) {
		if (!traits.contains(trait)) throw new NullPointerException("Animal does not contain this trait! " + trait);
		for (Modifier mod : trait.modifiers) {
			mod.remove(this);
		}
		traits.remove(trait);
	}
	
	
	//region Module Management
	
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
	
	//endregion
}