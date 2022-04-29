package tech.jaboc.animalcompetition.animal;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class SimpleModifier extends Modifier {
	BiConsumer<Animal, Double> setFunc;
	Function<Animal, Double> getFunc;
	public double value;
	public boolean multiplier;

	public SimpleModifier(BiConsumer<Animal, Double> setFunc, Function<Animal, Double> getFunc, double value, boolean multiplier) {
		this.setFunc = setFunc;
		this.getFunc = getFunc;
		this.value = value;
		this.multiplier = multiplier;
	}

	@Override
	public void add(Animal animal) {
		if (multiplier) {
			setFunc.accept(animal, getFunc.apply(animal) * value);
		} else {
			setFunc.accept(animal, getFunc.apply(animal) + value);
		}
	}

	@Override
	public void remove(Animal animal) {
		if (multiplier) {
			setFunc.accept(animal, getFunc.apply(animal) / value);
		} else {
			setFunc.accept(animal, getFunc.apply(animal) - value);
		}
	}
}
