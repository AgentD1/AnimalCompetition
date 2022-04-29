package tech.jaboc.animalcompetition.animal;

import java.lang.reflect.Field;

/**
 * A version of Modifier that uses reflection and a string fieldName instead of lambdas like SimpleModifier.
 * This is slower than SimpleModifier and shouldn't be used unless you can't use lambdas, like when parsing JSON.
 */
public class ReflectiveModifier extends Modifier {
	Field modifyField;
	String fieldName;

	double value;
	boolean multiplier;

	Class<? extends AnimalModule> module = null;

	public ReflectiveModifier(String fieldName, double value, boolean multiplier) {
		this.fieldName = fieldName;
		this.multiplier = multiplier;
		this.value = value;

		String requestedModuleName;

		if(fieldName.contains(".")) {
			requestedModuleName = fieldName.split("\\.", 2)[0];
		} else {
			requestedModuleName = "BaseModule";
		}

		Class<? extends AnimalModule> requestedModule = AnimalModule.classes.get(requestedModuleName);


		if(requestedModule == null) {
			throw new NullPointerException("Requested module is null! (Requested module = " + requestedModuleName + ")");
		}

		String requestedField = fieldName.substring(fieldName.indexOf('.') + 1);

		for(Field field : requestedModule.getDeclaredFields()) {
			if(field.isAnnotationPresent(AnimalComponent.class)) {
				AnimalComponent annotation = field.getAnnotation(AnimalComponent.class);
				if(annotation.name().equals(requestedField) && annotation.multiplier() == multiplier) {
					modifyField = field;
					modifyField.setAccessible(true); // Disable access checks to improve performance
					break;
				}
			}
		}

		if(modifyField == null) {
			throw new NullPointerException("There is no matching field to the name " + requestedField);
		}

		module = requestedModule;
	}

	@Override
	public void add(Animal animal) {
		AnimalModule myModule = animal.getModuleOfType(module);
		try {
			if(multiplier) {
				modifyField.set(myModule, (double)modifyField.get(myModule) * value);
			} else {
				modifyField.set(myModule, (double)modifyField.get(myModule) + value);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove(Animal animal) {
		try {
			if(multiplier) {
				modifyField.set(animal, (double)modifyField.get(animal) / value);
			} else {
				modifyField.set(animal, (double)modifyField.get(animal) - value);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
