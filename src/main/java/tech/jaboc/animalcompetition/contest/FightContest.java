package tech.jaboc.animalcompetition.contest;

import tech.jaboc.animalcompetition.animal.*;
import tech.jaboc.animalcompetition.environment.Environment;

import java.io.*;
import java.util.*;

public class FightContest extends Contest {
	@Override
	public Optional<Animal> resolve(Animal animal1, Animal animal2, Environment environment, PrintStream output) {
		boolean playerTurn = true;
		
		animal1.getModuleOfType(BaseModule.class).fullyHeal();
		animal2.getModuleOfType(BaseModule.class).fullyHeal();
		
		Random r = new Random();
		
		while (animal1.getModuleOfType(BaseModule.class).currentHealth > 0 && animal2.getModuleOfType(BaseModule.class).currentHealth > 0) {
			Animal aggressor = playerTurn ? animal1 : animal2;
			Animal victim = playerTurn ? animal2 : animal1;
			
			String aggressorName = aggressor.species;
			if (aggressorName.equals(victim.species)) aggressorName += (aggressor == animal1 ? " 1" : " 2");
			String victimName = victim.species;
			if (victimName.equals(aggressor.species)) victimName += (victim == animal1 ? " 1" : " 2");
			
			List<AttackModule> attacks = aggressor.getModulesOfType(AttackModule.class);
			attacks = attacks.stream().filter(a -> a.canHitAnimal(victim)).toList();
			
			if (attacks.size() == 0) {
				output.println((aggressor == animal1 ? "Player" : "Opponent") + " has no attacks that can hit!");
				break;
			}
			
			AttackModule randomAttack = attacks.get(r.nextInt(0, attacks.size()));
			
			output.printf("%s used %s!\n", aggressorName, randomAttack.name);
			
			AttackModule.AttackResult attackResult = randomAttack.attackAnimal(victim);
			
			if (attackResult.hit()) {
				output.printf(aggressorName + " hit for %s damage.\n", attackResult.damage());
			} else {
				output.println(aggressorName + " missed lol");
			}
			
			playerTurn = !playerTurn;
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
