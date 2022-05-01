package tech.jaboc.animalcompetition;

import tech.jaboc.animalcompetition.animal.*;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
	public static void main(String[] args) throws URISyntaxException, IOException {
		new LandMovementModule(); // initialize all modules that aren't explicitly referenced so that they get loaded. Java moment
		new AirMovementModule();
		
		Scanner in = new Scanner(System.in);
		
		System.out.println("Welcome to the Friendly Fighting Farena. Do you want to pick a (p)reset animal or (d)esign your own?");
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
				System.out.println(i++ + ": " + t.name + " (" + String.join(", ", (String[]) Arrays.stream(t.modifiers).map(Object::toString).toArray()));
			}
			int choice = UserInputUtils.getUserIntSelection(in, 1, 3) - 1;
			userAnimal.addTrait(availableBaseTraits.get(choice));
		}
		
		
	}
}
