package tech.jaboc.animalcompetition.animal.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import tech.jaboc.animalcompetition.animal.ReflectiveModifier;

import java.io.IOException;

/**
 * Deserializes ReflectiveModifiers. Is used for JSON.
 */
public class ReflectiveModifierDeserializer extends StdDeserializer<ReflectiveModifier> {
	public ReflectiveModifierDeserializer() {
		this(null);
	}
	
	public ReflectiveModifierDeserializer(Class<?> vc) {
		super(vc);
	}
	
	@Override
	public ReflectiveModifier deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		
		if (node.get("strict") == null) {
			throw new UnsupportedOperationException("Modifier appears not to be a ReflectiveModifier when parsing json " + node);
		}
		
		return new ReflectiveModifier(node.get("fieldName").textValue(),
				node.get("value").doubleValue(),
				node.get("multiplier").booleanValue(),
				node.get("strict").booleanValue(),
				node.get("autoAdd").booleanValue()
		);
	}
}
