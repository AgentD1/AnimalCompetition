package tech.jaboc.animalcompetition.animal;

/**
 * A module to represent an animal's ability to defend from attacks, both through natural armour and dodging attacks.
 */
public class DefenseModule extends AnimalModule {
	@AnimalComponent(name = "dodge", multiplier = false)
	public double baseDodge = 0.2;
	@AnimalComponent(name = "dodge", multiplier = true)
	public double dodgeMultiplier = 1.0;
	
	public double getDodge(Animal animal) {
		return baseDodge * dodgeMultiplier + (MovementModule.getFastestSpeed(animal) / 300.0);
	}
	
	@AnimalComponent(name = "damageResistance", multiplier = true)
	public double damageResistance = 1.0;
	
	@AnimalComponent(name = "heatResistance", multiplier = true)
	public double heatResistance = 1.0;
	
	@AnimalComponent(name = "coldResistance", multiplier = true)
	public double coldResistance = 1.0;
	
	@AnimalComponent(name = "temperature", multiplier = true)
	public double temperature = 1.0;
	
	public double getDamageResistance() {
		if (temperature > 1.0) return damageResistance * heatResistance;
		if (temperature < 1.0) return damageResistance * coldResistance;
		return damageResistance;
	}
	
	static {
		registerModule(DefenseModule.class);
	}
}
