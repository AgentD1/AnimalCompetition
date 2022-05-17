package tech.jaboc.animalcompetition.animal.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import tech.jaboc.animalcompetition.animal.*;

import java.io.IOException;

/**
 * Serializes animals. Is used for JSON.
 */
public class AnimalSerializer extends StdSerializer<Animal> {
	public AnimalSerializer() {
		this(null);
	}
	
	public AnimalSerializer(Class<Animal> t) {
		super(t);
	}
	
	@Override
	public void serialize(Animal a, JsonGenerator jgen, SerializerProvider ser) throws IOException {
		jgen.writeStartObject();
		
		jgen.writeStringField("name", a.name);
		jgen.writeStringField("species", a.species);
		
		jgen.writeArrayFieldStart("modules");
		for (AnimalModule module : a.getModulesOfType(AnimalModule.class)) {
			jgen.writeString(module.getClass().getSimpleName());
		}
		jgen.writeEndArray();
		
		jgen.writeArrayFieldStart("traits");
		for (Trait t : a.getTraits()) {
			ser.defaultSerializeValue(t, jgen);
		}
		jgen.writeEndArray();
		
		jgen.writeEndObject();
	}
}
