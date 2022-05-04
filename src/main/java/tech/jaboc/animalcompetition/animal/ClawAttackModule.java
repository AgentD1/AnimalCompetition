package tech.jaboc.animalcompetition.animal;

public class ClawAttackModule extends AttackModule {
	public ClawAttackModule() {
		super();
		name = "Claw";
	}
	
	static {
		registerModule(ClawAttackModule.class);
	}
}
