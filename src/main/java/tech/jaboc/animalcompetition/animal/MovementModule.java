package tech.jaboc.animalcompetition.animal;

import java.util.*;

public abstract class MovementModule extends AnimalModule {
	@AnimalComponent(name = "speed", multiplier = false)
	public double baseSpeed;
	@AnimalComponent(name = "speed", multiplier = true)
	public double speedModifier = 1.0;
	
	public double getSpeed() {
		return baseSpeed * speedModifier;
	}
	
	public static double getFastestSpeed(Animal a) {
		List<MovementModule> modules = a.getModulesOfType(MovementModule.class);
		
		MovementModule fastest = modules.stream().min(Comparator.comparingDouble(MovementModule::getSpeed)).orElse(null);
		if (fastest == null) return 0;
		
		return fastest.getSpeed();
	}
	
	static {
		AnimalModule.registerModule(MovementModule.class);
	}
}
