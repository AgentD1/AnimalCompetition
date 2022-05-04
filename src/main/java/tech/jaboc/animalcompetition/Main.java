package tech.jaboc.animalcompetition;

import tech.jaboc.animalcompetition.animal.*;

import java.util.*;
import java.util.stream.IntStream;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		new LandMovementModule(); // initialize all modules that aren't explicitly referenced so that they get loaded. Java moment
		new AirMovementModule();
		new BaseModule();
		new BeakAttackModule();
		new ClawAttackModule();
		new BiteAttackModule();
		
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
						new ReflectiveModifier("ClawAttackModule.damage", 20.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.damageRandomRange", 5.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.accuracy", 0.8, false, false, true),
						new ReflectiveModifier("BiteAttackModule.damage", 10.0, false, false, true),
						new ReflectiveModifier("BiteAttackModule.damageRandomRange", 10.0, false, false, true),
						new ReflectiveModifier("BiteAttackModule.accuracy", 0.8, false, false, true),
				}),
				new Trait("Eagle Body", new ReflectiveModifier[] {
						new ReflectiveModifier("BaseModule.health", 50.0, false, false, true),
						new ReflectiveModifier("BaseModule.damage", 100.0, false, false, true),
						new ReflectiveModifier("LandMovementModule.speed", 1.0, false, false, true),
						new ReflectiveModifier("AirMovementModule.speed", 40.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.damage", 40.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.damageRandomRange", 10.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.accuracy", 0.7, false, false, true),
						new ReflectiveModifier("BeakAttackModule.damage", 10.0, false, false, true),
						new ReflectiveModifier("BeakAttackModule.damageRandomRange", 5.0, false, false, true),
						new ReflectiveModifier("BeakAttackModule.accuracy", 1.1, false, false, true),
				}),
				new Trait("Cheetah Body", new ReflectiveModifier[] {
						new ReflectiveModifier("BaseModule.health", 75.0, false, false, true),
						new ReflectiveModifier("BaseModule.damage", 75.0, false, false, true),
						new ReflectiveModifier("LandMovementModule.speed", 30.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.damage", 20.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.damageRandomRange", 5.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.accuracy", 0.9, false, false, true),
						new ReflectiveModifier("BiteAttackModule.damage", 10.0, false, false, true),
						new ReflectiveModifier("BiteAttackModule.damageRandomRange", 10.0, false, false, true),
						new ReflectiveModifier("BiteAttackModule.accuracy", 0.9, false, false, true),
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
						new ReflectiveModifier("AttackModule.damage", 1.25, true, false, false),
						new ReflectiveModifier("BaseModule.damage", 1.25, true, false, false),
				}),
				new Trait("Glass Cannon", new ReflectiveModifier[] {
						new ReflectiveModifier("BaseModule.health", 0.75, true, false, false),
						new ReflectiveModifier("BaseModule.damage", 1.5, true, false, false),
						new ReflectiveModifier("AttackModule.damage", 1.5, true, false, false),
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
		
		
		System.out.println("I'm going to generate an animal as your opponent now.");
		Trait baseTrait = availableBaseTraits.get((int) (Math.random() * (double)availableBaseTraits.size()));
		Trait otherTrait = otherTraits.get((int) (Math.random() * (double)otherTraits.size()));
		
		Animal opponent = new Animal();
		opponent.addTrait(baseTrait);
		opponent.addTrait(otherTrait);
		
		Random r = new Random();
		opponent.name = IntStream.generate(() -> r.nextInt('a', 'z')).limit(20).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		
		opponent.species = IntStream.generate(() -> r.nextInt('a', 'z')).limit(20).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		
		System.out.println(opponent);
		
		System.out.println();
		
		System.out.println();
		
		System.out.println("Time to fight lol");
		
		userAnimal.getModuleOfType(BaseModule.class).fullyHeal();
		opponent.getModuleOfType(BaseModule.class).fullyHeal();
		
		boolean playerTurn = true;
		
		while(userAnimal.getModuleOfType(BaseModule.class).currentHealth > 0 && opponent.getModuleOfType(BaseModule.class).currentHealth > 0) {
			Animal aggressor = playerTurn ? userAnimal : opponent;
			Animal victim = playerTurn ? opponent : userAnimal;
			List<AttackModule> attacks = aggressor.getModulesOfType(AttackModule.class);
			attacks = attacks.stream().filter(a -> a.canHitAnimal(victim)).toList();
			
			if(attacks.size() == 0) {
				System.out.println(aggressor.name + " has no attacks that can hit!");
				break;
			}
			
			AttackModule randomAttack = attacks.get(r.nextInt(0, attacks.size()));
			AttackModule.AttackResult attackResult = randomAttack.attackAnimal(victim);
			
			if(attackResult.hit()) {
				System.out.printf(aggressor.name + " hit for %s damage.\n", attackResult.damage());
			} else {
				System.out.println(aggressor.name + " missed lol");
			}
			
			playerTurn = !playerTurn;
			Thread.sleep(100);
		}
	}
}
