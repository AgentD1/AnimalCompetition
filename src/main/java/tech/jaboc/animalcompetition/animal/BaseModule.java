package tech.jaboc.animalcompetition.animal;

public class BaseModule extends AnimalModule {
	@AnimalComponent(name = "health", multiplier = false)
	public double baseHealth;
	@AnimalComponent(name = "health", multiplier = true)
	public double healthMultiplier = 1.0;

	@AnimalComponent(name = "damage", multiplier = false)
	public double baseDamage;
	@AnimalComponent(name = "damage", multiplier = true)
	public double damageMultiplier = 1.0;


	static {
		registerModule(BaseModule.class);
	}
}
