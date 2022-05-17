package tech.jaboc.animalcompetition.animal;

/**
 * A modifier is a class that changes variables in an animal. It can be added or removed from an animal.
 * The animal MUST be in the same state as before, after a modifier is added, then removed
 */
public abstract class Modifier {
	public abstract void add(Animal animal);
	public abstract void remove(Animal animal);
}
