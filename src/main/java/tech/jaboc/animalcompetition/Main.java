package tech.jaboc.animalcompetition;

import tech.jaboc.animalcompetition.animal.*;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
	public static void main(String[] args) throws URISyntaxException, IOException {
		new LandMovementModule(); // initialize all modules that aren't explicitly referenced so that they get loaded. Java moment
		new AirMovementModule();
		new BaseModule();
		
		Scanner in = new Scanner(System.in);
		
		System.out.println("Welcome to the Friendly Fighting Arena (FFA for short). Do you want to pick a (p)reset animal or (d)esign your own?");
		boolean usePreset = UserInputUtils.getUserCharSelection(in, "pd", "Enter 'p' for preset or 'd' for design") == 'p';
		
		int preset = 0;
		if (usePreset) {
			System.out.println("Do you want to choose a (l)ion, (e)agle, or (c)heetah?");
			preset = "lec".indexOf(UserInputUtils.getUserCharSelection(in, "lec", "Enter 'l' for lion, 'e' for eagle, or 'c' for cheetah"));
		}
		
		List<Trait> availableBaseTraits = List.of(
				new Trait("Lion Body", new ReflectiveModifier[] {
						new ReflectiveModifier("BaseModule.health", 100.0, false, false, true),
						new ReflectiveModifier("BaseModule.damage", 50.0, false, false, true),
						new ReflectiveModifier("LandMovementModule.speed", 20.0, false, false, true),
				}),
				new Trait("Eagle Body", new ReflectiveModifier[] {
						new ReflectiveModifier("BaseModule.health", 50.0, false, false, true),
						new ReflectiveModifier("BaseModule.damage", 100.0, false, false, true),
						new ReflectiveModifier("LandMovementModule.speed", 1.0, false, false, true),
						new ReflectiveModifier("AirMovementModule.speed", 40.0, false, false, true),
				}),
				new Trait("Cheetah Body", new ReflectiveModifier[] {
						new ReflectiveModifier("BaseModule.health", 75.0, false, false, true),
						new ReflectiveModifier("BaseModule.damage", 75.0, false, false, true),
						new ReflectiveModifier("LandMovementModule.speed", 30.0, false, false, true),
				})
		);
		
		Animal userAnimal = new Animal();
		
		if (usePreset) {
			userAnimal.addTrait(availableBaseTraits.get(preset));
		} else {
			System.out.println("Choose a body to build from:");
			int i = 0;
			for (Trait t : availableBaseTraits) {
				System.out.println(++i + ": " + t.name + " (" + String.join(", ", Arrays.stream(t.modifiers).map(Object::toString).toList().toArray(new String[0])) + ")");
			}
			int choice = UserInputUtils.getUserIntSelection(in, 1, 3) - 1;
			userAnimal.addTrait(availableBaseTraits.get(choice));
		}
		
		List<Trait> otherTraits = List.of(
				new Trait("Fast", new ReflectiveModifier[] {
						new ReflectiveModifier("MovementModule.speed", 1.5, true, false, false),
				}),
				new Trait("Slow", new ReflectiveModifier[] {
						new ReflectiveModifier("MovementModule.speed", 0.666667, true, false, false),
				}),
				new Trait("Violent", new ReflectiveModifier[] {
						new ReflectiveModifier("BaseModule.damage", 1.25, true, false, false),
				}),
				new Trait("Glass Cannon", new ReflectiveModifier[] {
						new ReflectiveModifier("BaseModule.health", 0.75, true, false, false),
						new ReflectiveModifier("BaseModule.damage", 1.25, true, false, false),
				}),
				new Trait("Large Feet", new ReflectiveModifier[] {
						new ReflectiveModifier("LandMovementModule.speed", 1.5, true, false, true),
				})
		);
		
		if(usePreset) {
			switch (preset) {
				case 0 -> userAnimal.addTrait(otherTraits.get(2));
				case 1 -> userAnimal.addTrait(otherTraits.get(4));
				case 2 -> userAnimal.addTrait(otherTraits.get(1));
			}
		} else {
			System.out.println("It's time to choose your extra traits! Here are the options (enter -1 to stop adding traits):");
			int i = 0;
			for (Trait t : otherTraits) {
				System.out.println(i++ + ": " + t.name + " (" + String.join(", ", Arrays.stream(t.modifiers).map(Object::toString).toList().toArray(new String[0])) + ")");
			}
			while (true) {
				int choice = UserInputUtils.getUserIntSelection(in, -1, otherTraits.size() - 1);
				if (choice == -1) break;
				userAnimal.addTrait(otherTraits.get(choice));
				System.out.println("Added trait " + otherTraits.get(choice).name + "!");
			}
		}
		
		if (usePreset) {
			userAnimal.species = new String[] { "Lion", "Eagle", "Cheetah" }[preset];
		} else {
			System.out.println("What species is this animal?");
			userAnimal.species = in.nextLine();
		}
		
		System.out.println("For the final touches, please give your animal a name!");
		userAnimal.name = in.nextLine();
		
		System.out.println("Your animal is complete!");
		System.out.println(userAnimal);
	}
}
