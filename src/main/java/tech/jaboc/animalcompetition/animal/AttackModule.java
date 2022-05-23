package tech.jaboc.animalcompetition.animal;

/**
 * An abstract class that represents a type of attack that an animal can use. It has accuracy and damage.
 */
public abstract class AttackModule extends AnimalModule {
	public String name;
	
	@AnimalComponent(name = "accuracy", multiplier = false)
	public double baseAccuracy;
	@AnimalComponent(name = "accuracy", multiplier = true)
	public double accuracyMultiplier = 1.0;
	@AnimalComponent(name = "damage", multiplier = false)
	public double baseDamage;
	@AnimalComponent(name = "damage", multiplier = true)
	public double damageMultiplier = 1.0;
	@AnimalComponent(name = "damageRandomRange", multiplier = false)
	public double damageRandomRange;
	
	public boolean canHitAnimal(Animal animal) {
		return true; // TODO: add attack range when animals have locations later
	}
	
	public AttackResult attackAnimal(Animal animal) {
		double animalDodge = animal.getFromModuleIfNotNullElse(DefenseModule.class, x -> x.getDodge(animal), 0.0);
		
		double hit = Math.random() * baseAccuracy;
		
		if (hit > animalDodge) {
			double damage = (Math.random() * damageRandomRange + baseDamage) * damageMultiplier;
			double damageResistance = animal.getFromModuleIfNotNullElse(DefenseModule.class, DefenseModule::getDamageResistance, 0.0);
			if (damageResistance == 0.0) { // Animal has no defense, is probably incapacitated
				damageResistance = 0.0001;
			}
			damage /= damageResistance;
			animal.getModuleOfType(BaseModule.class).takeDamage(damage);
			return new AttackResult(true, damage);
		} else {
			return new AttackResult(false, 0.0);
		}
	}
	
	public record AttackResult(boolean hit, double damage) { }
	
	static {
		registerModule(AttackModule.class);
	}
}
