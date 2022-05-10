package tech.jaboc.animalcompetition.animal;

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
		double animalDodge = animal.getFromModuleIfNotNullElse(DefenseModule.class, DefenseModule::getDodge, 0.0);
		
		double hit = Math.random() * baseAccuracy;
		
		if(hit > animalDodge) {
			double damage = (Math.random() * damageRandomRange + baseDamage) * damageMultiplier;
			animal.getModuleOfType(BaseModule.class).takeDamage(damage);
			return new AttackResult(true, damage);
		} else {
			return new AttackResult(false, 0.0);
		}
	}
	
	public record AttackResult(boolean hit, double damage) {}
	
	static {
		registerModule(AttackModule.class);
	}
}