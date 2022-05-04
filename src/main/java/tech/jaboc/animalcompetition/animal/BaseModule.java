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
