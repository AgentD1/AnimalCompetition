package tech.jaboc.animalcompetition.animal;

/**
 * The base module has all the stats that are absolutely required by animals, like health
 */
public class BaseModule extends AnimalModule {
	@AnimalComponent(name = "health", multiplier = false)
	public double baseHealth;
	@AnimalComponent(name = "health", multiplier = true)
	public double healthMultiplier = 1.0;
	
	public double currentHealth;
	
	public void fullyHeal() {
		currentHealth = baseHealth * healthMultiplier;
	}
	
	public void takeDamage(double damage) {
		currentHealth -= damage;
	}
	
	static {
		registerModule(BaseModule.class);
	}
}
