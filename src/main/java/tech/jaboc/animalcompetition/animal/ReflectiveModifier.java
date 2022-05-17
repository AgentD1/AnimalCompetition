package tech.jaboc.animalcompetition.animal;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tech.jaboc.animalcompetition.animal.json.ReflectiveModifierDeserializer;

import java.lang.reflect.*;
import java.util.List;

/**
 * A version of Modifier that uses reflection and a string fieldName instead of lambdas like SimpleModifier.
 * This is slower than SimpleModifier, although it is significantly easier.
 */
@JsonDeserialize(using = ReflectiveModifierDeserializer.class)
public class ReflectiveModifier extends Modifier {
	@JsonIgnore
	public Field modifyField;
	public String fieldName;
	
	public double value;
	public boolean multiplier;
	
	public boolean strict, autoAdd;
	
	@JsonIgnore
	Class<? extends AnimalModule> module;
	
	/**
	 * Creates a new ReflectiveModifier
	 * @param fieldName The name of the field to use. This should match the name in the AnimalComponent Attribute
	 * @param value The value of this modifier
	 * @param multiplier Whether this modifier is a multiplier (true) or an adder (false)
	 * @param strict Whether this modifier should throw an exception if the requested modifier doesn't exist (false means it will do nothing)
	 * @param autoAdd Whether this modifier should automatically add a new modifier to the animal of the specified type if one does not already
	 *                exist. (false means it will do nothing)
	 */
	@JsonCreator
	public ReflectiveModifier(String fieldName, double value, boolean multiplier, boolean strict, boolean autoAdd) {
		this.fieldName = fieldName;
		this.multiplier = multiplier;
		this.value = value;
		
		this.strict = strict;
		this.autoAdd = autoAdd;
		
		if (strict && autoAdd) {
			throw new IllegalArgumentException("Strict and AutoAdd can't both be null!");
		}
		
		String requestedModuleName;
		
		if (fieldName.contains(".")) {
			requestedModuleName = fieldName.split("\\.", 2)[0];
		} else {
			requestedModuleName = "BaseModule";
		}
		
		Class<? extends AnimalModule> requestedModule = AnimalModule.classes.get(requestedModuleName);
		
		if (requestedModule == null) {
			throw new NullPointerException("Requested module is null! (Requested module = " + requestedModuleName + ")");
		}
		
		String requestedField = fieldName.substring(fieldName.indexOf('.') + 1);
		
		for (Field field : requestedModule.getFields()) {
			if (field.isAnnotationPresent(AnimalComponent.class)) {
				AnimalComponent annotation = field.getAnnotation(AnimalComponent.class);
				if (annotation.name().equals(requestedField) && annotation.multiplier() == multiplier) {
					modifyField = field;
					modifyField.setAccessible(true); // Disable access checks to improve performance
					break;
				}
			}
		}
		
		if (modifyField == null) {
			throw new NullPointerException("There is no matching field to the name " + requestedField);
		}
		
		module = requestedModule;
	}
	
	public ReflectiveModifier(String fieldName, double value, boolean multiplier) {
		this(fieldName, value, multiplier, true, false);
	}
	
	/**
	 * Adds this modifier to an animal
	 * @param animal The animal to add to
	 */
	@Override
	public void add(Animal animal) {
		List<? extends AnimalModule> myModules = animal.getModulesOfType(module);
		if (myModules.size() == 0) {
			if (strict) {
				throw new NullPointerException("Module " + module.getName() + " not found on animal " + animal + "!");
			} else if (autoAdd) {
				try {
					//noinspection deprecation
					AnimalModule myModule = module.newInstance();
					animal.addModule(myModule);
					add(animal); // Can't add to list declared with wildcards, so just re-call the function and return
					return;
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		for (var myModule : myModules) { // ? extends AnimalModule
			try {
				if (multiplier) {
					modifyField.set(myModule, (double) modifyField.get(myModule) * value);
				} else {
					modifyField.set(myModule, (double) modifyField.get(myModule) + value);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e); // This will never happen randomly, and if it does, it means that the variable in the module isn't public.
			}
		}
	}
	
	/**
	 * Removes this modifier from an animal
	 * @param animal The animal to remove
	 */
	@Override
	public void remove(Animal animal) {
		for(var myModule : animal.getModulesOfType(module)) {
			try {
				if (multiplier) {
					modifyField.set(myModule, (double) modifyField.get(modifyField) / value);
				} else {
					modifyField.set(myModule, (double) modifyField.get(modifyField) - value);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %.2f%s", fieldName, (value >= 0 ? "+" : "-"), Math.abs(value * (multiplier ? 100.0 : 1.0)), multiplier ? "%" : "");
	}
}
