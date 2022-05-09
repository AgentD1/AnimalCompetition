package tech.jaboc.animalcompetition.contest;

import tech.jaboc.animalcompetition.animal.Animal;
import tech.jaboc.animalcompetition.environment.Environment;

import java.io.*;
import java.util.Optional;

public abstract class Contest {
	public abstract Optional<Animal> resolve(Animal animal1, Animal animal2, Environment environment, PrintStream output);
}
