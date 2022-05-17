package tech.jaboc.animalcompetition.animal;

import com.fasterxml.jackson.annotation.*;

/**
 * Represents a trait: a series of modifiers with a name.
 */
public class Trait {
	public String name;
	public Modifier[] modifiers;
	
	public Trait(String name, Modifier[] modifiers) {
		this.name = name;
		this.modifiers = modifiers;
	}
	
	@JsonCreator
	public Trait(@JsonProperty("name") String name, @JsonProperty("modifiers") ReflectiveModifier[] modifiers) {
		this.name = name;
		this.modifiers = modifiers;
	}
}
