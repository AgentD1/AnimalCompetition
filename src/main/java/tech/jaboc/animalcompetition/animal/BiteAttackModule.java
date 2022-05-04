package tech.jaboc.animalcompetition.animal;

public class BiteAttackModule extends AttackModule {
	public BiteAttackModule() {
		super();
		name = "Bite";
	}
	
	static {
		registerModule(BiteAttackModule.class);
	}
}
