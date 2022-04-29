package tech.jaboc.animalcompetition.animal;

public abstract class MovementModule extends AnimalModule {
	@AnimalComponent(name = "speed", multiplier = false)
	public double baseSpeed;
	@AnimalComponent(name = "speed", multiplier = true)
	public double speedModifier = 1.0;
}
