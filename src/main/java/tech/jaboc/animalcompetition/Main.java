package tech.jaboc.animalcompetition;

import tech.jaboc.animalcompetition.animal.*;

public class Main {
	public static void main(String[] args) {
		Animal animal = new Animal();
		BaseModule module = new BaseModule();
		animal.addModule(module);

		Trait myTrait = new Trait("MyTrait", new Modifier[]{
				new SimpleModifier((x, y) -> x.applyToModuleIfNotNull(BaseModule.class, baseModule -> baseModule.baseHealth = y),
						x -> x.getFromModuleIfNotNull(BaseModule.class, baseModule -> baseModule.baseHealth),
						50, false),
				new ReflectiveModifier("BaseModule.health", 1.1, true),
				new ReflectiveModifier("damage", 100, false)
		});

		animal.addTrait(myTrait);

		BaseModule module1 = animal.getModuleOfType(BaseModule.class);
		System.out.printf("Health: %f (+%f%%), Damage: %f (+%f%%)\n", module1.baseHealth, module1.healthMultiplier, module1.baseDamage, module1.damageMultiplier);
	}
}