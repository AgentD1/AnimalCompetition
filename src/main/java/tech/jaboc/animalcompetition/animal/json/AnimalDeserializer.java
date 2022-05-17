package tech.jaboc.animalcompetition.animal.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import tech.jaboc.animalcompetition.animal.*;

import java.io.IOException;
import java.util.Iterator;

public class AnimalDeserializer extends StdDeserializer<Animal> {
	public AnimalDeserializer() {
		this(null);
	}
	
	public AnimalDeserializer(Class<?> vc) {
		super(vc);
	}
	
	@Override
	public Animal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		
		Animal a = new Animal();
		
		a.name = node.get("name").textValue();
		a.species = node.get("species").textValue();
		
		for (Iterator<JsonNode> it = node.get("modules").elements(); it.hasNext(); ) {
			JsonNode i = it.next();
			String type = i.textValue();
			try {
				//noinspection deprecation
				AnimalModule module = AnimalModule.classes.get(type).newInstance();
				
				a.addModule(module);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		
		for (Iterator<JsonNode> it = node.get("traits").elements(); it.hasNext(); ) {
			JsonNode i = it.next();
			Trait trait = deserializationContext.readTreeAsValue(i, Trait.class);
			a.addTrait(trait);
		}
		
		return a;
	}
}
