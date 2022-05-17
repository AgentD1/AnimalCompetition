package tech.jaboc.animalcompetition.animal;

/**
 * Marks that an animal can fly (or otherwise move in the air). Extends MovementModule and has no special properties
 */
public class AirMovementModule extends MovementModule {
	
	
	static {
		registerModule(AirMovementModule.class);
	}
}
