package tech.jaboc.animalcompetition;

import com.fasterxml.jackson.databind.ObjectMapper;
import tech.jaboc.animalcompetition.animal.*;
import tech.jaboc.animalcompetition.environment.*;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

public class Main {
	public static void main(String[] args) throws URISyntaxException, IOException {
		// Currently, the main method is just for testing.
		
		new LandMovementModule(); // initialize all modules that aren't explicitly referenced so that they get loaded. Java moment
		new AirMovementModule();
		
		ObjectMapper om = new ObjectMapper();
		
		Environment.JsonEnvironmentalFactorList loadedFactorList = om.readValue(new File("src/main/resources/environmentalFactors.json"), Environment.JsonEnvironmentalFactorList.class);
		
		List<Environment> generatedEnvironments = Stream.generate(() -> Environment.generateEnvironment(loadedFactorList)).limit(100).toList();
		
		System.out.println(om.writeValueAsString(generatedEnvironments));
		
		Animal animal = new Animal();
		BaseModule module = new BaseModule();
		animal.addModule(module);
		
		Trait myTrait = new Trait("MyTrait", new Modifier[] {
//				new SimpleModifier((x, y) -> x.applyToModuleIfNotNull(BaseModule.class, baseModule -> baseModule.baseHealth = y),
//						x -> x.getFromModuleIfNotNull(BaseModule.class, baseModule -> baseModule.baseHealth),
//						50, false),
				new ReflectiveModifier("BaseModule.health", 1.1, true),
				new ReflectiveModifier("damage", 100, false)
		});
		
		animal.addTrait(myTrait);
		
		BaseModule module1 = animal.getModuleOfType(BaseModule.class);
		System.out.printf("Health: %f (+%f%%), Damage: %f (+%f%%)\n", module1.baseHealth, module1.healthMultiplier, module1.baseDamage, module1.damageMultiplier);
		
		Animal animal2 = new Animal();
		animal2.addModule(new BaseModule());
		
		Trait landMovementTrait = new Trait("MyTrait", new Modifier[] {
				new ReflectiveModifier("LandMovementModule.speed", 1.1, true, false, true),
				new ReflectiveModifier("LandMovementModule.speed", 100, false, false, true)
		});
		Trait airMovementTrait = new Trait("MyTrait", new Modifier[] {
				new ReflectiveModifier("AirMovementModule.speed", 1.2, true, false, true),
				new ReflectiveModifier("AirMovementModule.speed", 200, false, false, true)
		});
		
		animal.addTrait(landMovementTrait);
		animal2.addTrait(airMovementTrait);
		
		System.out.println(animal.getModuleOfType(MovementModule.class).baseSpeed);
		System.out.println(animal2.getModuleOfType(MovementModule.class).baseSpeed);
		
		Path filePath = Paths.get("src/main/resources/animals.json");
		File file = filePath.toFile();
		
		try {
			om.writerWithDefaultPrettyPrinter().writeValue(file, new Animal[] { animal, animal2 });
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		try {
			Animal[] parsedAnimals = om.readValue(file, Animal[].class);
			System.out.println(parsedAnimals[0].getModuleOfType(MovementModule.class).getSpeed());
			System.out.println(parsedAnimals[1].getModuleOfType(MovementModule.class).getSpeed());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
