package tech.jaboc.animalcompetition.animal;

public class DefenseModule extends AnimalModule {
	@AnimalComponent(name = "dodge", multiplier = false)
	public double baseDodge;
	@AnimalComponent(name = "dodge", multiplier = true)
	public double dodgeMultiplier;
	
	public double getDodge() {
		return baseDodge * dodgeMultiplier;
	}
}
