package tech.jaboc.animalcompetition.contest;

import tech.jaboc.animalcompetition.animal.*;
import tech.jaboc.animalcompetition.environment.Environment;

import java.io.*;
import java.util.*;

/**
 * A contest where the animals fight to the not death.
 */
public class FightContest extends Contest {
	@Override
	public Optional<Animal> resolve(Animal animal1, Animal animal2, Environment environment, PrintStream output) {
		boolean animal1Turn = true; // animal1 starts first. If you want animal2 to start first, switch them.
		
		animal1.getModuleOfType(BaseModule.class).fullyHeal();
		animal2.getModuleOfType(BaseModule.class).fullyHeal();
		
		
		Random r = new Random();
		
		int turnNumber = 1;
		
		while (animal1.getModuleOfType(BaseModule.class).currentHealth > 0 && animal2.getModuleOfType(BaseModule.class).currentHealth > 0) {
			if (animal1Turn) {
				output.printf("\n--- Turn %d ---\n\n", turnNumber);
			}
			
			Animal aggressor = animal1Turn ? animal1 : animal2;
			Animal victim = animal1Turn ? animal2 : animal1;
			
			// Animals of the same species will be named Species 1 and Species 2, animals of different species will be called by their species name
			String aggressorName = aggressor.species;
			if (aggressorName.equals(victim.species)) aggressorName += (aggressor == animal1 ? " 1" : " 2");
			String victimName = victim.species;
			if (victimName.equals(aggressor.species)) victimName += (victim == animal1 ? " 1" : " 2");
			
			List<AttackModule> attacks = aggressor.getModulesOfType(AttackModule.class);
			attacks = attacks.stream().filter(a -> a.canHitAnimal(victim)).toList();
			
			if (attacks.size() == 0) {
				output.println(aggressorName + " has no attacks that can hit!");
				break;
			}
			
			AttackModule randomAttack = attacks.get(r.nextInt(0, attacks.size()));
			
			output.printf("%s used %s!\n", aggressorName, randomAttack.name);
			
			AttackModule.AttackResult attackResult = randomAttack.attackAnimal(victim);
			
			if (attackResult.hit()) {
				output.printf(aggressorName + " hit for %s damage.\n", attackResult.damage());
			} else {
				output.printf(victimName + " dodged the attack!\n");
			}
			
			animal1Turn = !animal1Turn;
			
			if (animal1Turn) {
				turnNumber++;
				
				if (turnNumber >= 100) {
					output.println("--- After 100 turns, neither animal has won. ---\n");
				}
			} else {
				output.print("\n");
			}
		}
		
		double animal1Health = animal1.getModuleOfType(BaseModule.class).currentHealth;
		double animal2Health = animal2.getModuleOfType(BaseModule.class).currentHealth;
		
		if (animal1Health > 0 == animal2Health > 0) { // If both are alive or both are dead
			return Optional.empty();
		}
		
		if (animal1Health > 0) {
			return Optional.of(animal1);
		} else {
			return Optional.of(animal2);
		}
		
	}
}
