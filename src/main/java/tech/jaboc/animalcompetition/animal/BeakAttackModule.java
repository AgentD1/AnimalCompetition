package tech.jaboc.animalcompetition.animal;

public class BeakAttackModule extends AttackModule {
	public BeakAttackModule() {
		super();
		name = "Beak";
	}
	
	static {
		registerModule(BeakAttackModule.class);
	}
}
