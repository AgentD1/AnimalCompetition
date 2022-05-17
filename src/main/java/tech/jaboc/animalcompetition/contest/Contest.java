package tech.jaboc.animalcompetition.contest;

import tech.jaboc.animalcompetition.animal.Animal;
import tech.jaboc.animalcompetition.environment.Environment;

import java.io.*;
import java.util.Optional;

public abstract class Contest {
	/**
	 * Resolves a contest
	 *
	 * @param animal1     The first animal
	 * @param animal2     The second animal
	 * @param environment The environment
	 * @param output      The stream to output events
	 * @return The winner, or nothing if it was a draw
	 */
	public abstract Optional<Animal> resolve(Animal animal1, Animal animal2, Environment environment, PrintStream output);
}
