package tech.jaboc.animalcompetition;

import tech.jaboc.animalcompetition.animal.*;

public class Main {
	public static void main(String[] args) {
		new LandMovementModule();
		new AirMovementModule();
		
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
		
		Animal animal2 = new Animal();
		animal2.addModule(new BaseModule());
		
		Trait landMovementTrait = new Trait("MyTrait", new Modifier[]{
				new ReflectiveModifier("LandMovementModule.speed", 1.1, true, false, true),
				new ReflectiveModifier("LandMovementModule.speed", 100, false, false, true)
		});
		Trait airMovementTrait = new Trait("MyTrait", new Modifier[]{
				new ReflectiveModifier("AirMovementModule.speed", 1.2, true, false, true),
				new ReflectiveModifier("AirMovementModule.speed", 200, false, false, true)
		});
		
		animal.addTrait(landMovementTrait);
		animal2.addTrait(airMovementTrait);
		
		System.out.println(animal.getModuleOfType(MovementModule.class).baseSpeed);
		System.out.println(animal2.getModuleOfType(MovementModule.class).baseSpeed);
	}
}