package tech.jaboc.animalcompetition.environment;

import com.fasterxml.jackson.annotation.*;
import tech.jaboc.animalcompetition.animal.*;

/**
 * An environmental factor. Contains modifiers that this environmental factor inflicts, as well as a name, description, and a few data points for environment generation
 */
public class EnvironmentalFactor {
	public String name;
	public String description = "No Description Provided";
	public Modifier[] modifiers;
	
	public double defaultProbability = -1.0; // -1 represents equally likely (1 / numberOfFactors)
	public FactorModifier[] factorModifiers;
	
	@JsonIgnore
	public double currentProbability;
	
	public EnvironmentalFactor(String name, Modifier[] modifiers, FactorModifier[] factorModifiers) {
		this.name = name;
		this.modifiers = modifiers;
		this.factorModifiers = factorModifiers;
	}
	
	public EnvironmentalFactor(String name, String description, Modifier[] modifiers, FactorModifier[] factorModifiers) {
		this.name = name;
		this.description = description;
		this.modifiers = modifiers;
		this.factorModifiers = factorModifiers;
	}
	
	@JsonCreator
	public EnvironmentalFactor(@JsonProperty("name") String name, @JsonProperty("description") String description, @JsonProperty("modifiers") ReflectiveModifier[] modifiers, @JsonProperty("defaultProbability") double defaultProbability, @JsonProperty("factorModifiers") FactorModifier[] factorModifiers) {
		this.name = name;
		this.description = description;
		this.modifiers = modifiers;
		this.factorModifiers = factorModifiers;
		this.defaultProbability = defaultProbability;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Represents a probability modifier for other environmental factors during generation
	 *
	 * @param factorName The name of the factor this effects
	 * @param multiplier A multiplier to the probability
	 */
	public record FactorModifier(String factorName, double multiplier) { }
}
