package tech.jaboc.animalcompetition.animal;

/**
 * A module to represent an animal's ability to defend from attacks, both through natural armour and dodging attacks.
 */
public class DefenseModule extends AnimalModule {
	@AnimalComponent(name = "dodge", multiplier = false)
	public double baseDodge;
	@AnimalComponent(name = "dodge", multiplier = true)
	public double dodgeMultiplier;
	
	public double getDodge() {
		return baseDodge * dodgeMultiplier;
	}
}
