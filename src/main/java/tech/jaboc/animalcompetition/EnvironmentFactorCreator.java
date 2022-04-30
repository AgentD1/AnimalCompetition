package tech.jaboc.animalcompetition;

import com.fasterxml.jackson.databind.ObjectMapper;
import tech.jaboc.animalcompetition.animal.Modifier;
import tech.jaboc.animalcompetition.environment.*;

import java.io.IOException;
import java.nio.file.Paths;

public class EnvironmentFactorCreator {
	public static void writeEnvironmentalFactors() {
		//TODO: make water
		
		EnvironmentalFactor[] features = new EnvironmentalFactor[] {
				new EnvironmentalFactor("Forested", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Jungle", 0.0)
				}),
				new EnvironmentalFactor("Jungle", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Forested", 0.0)
				}),
				new EnvironmentalFactor("Large Rocks", new Modifier[0], new EnvironmentalFactor.FactorModifier[0]),
				new EnvironmentalFactor("Caves", new Modifier[0], new EnvironmentalFactor.FactorModifier[0]),
				new EnvironmentalFactor("Barren", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Lush", 0.0)
				}),
				new EnvironmentalFactor("Lush", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Barren", 0.0)
				}),
				new EnvironmentalFactor("Windy", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Sheltered", 0.0)
				}),
				new EnvironmentalFactor("Sheltered", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Windy", 0.0)
				}),
		};
		EnvironmentalFactor[] weathers = new EnvironmentalFactor[] { // TODO: add lush and barren to weathers and temperatures
				new EnvironmentalFactor("Sunny", new Modifier[0], new EnvironmentalFactor.FactorModifier[] { // Jungles and forests are typically darker
						new EnvironmentalFactor.FactorModifier("Jungle", 0.8),
						new EnvironmentalFactor.FactorModifier("Forested", 0.8)
				}),
				new EnvironmentalFactor("Full Moon", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Jungle", 0.8),
						new EnvironmentalFactor.FactorModifier("Forested", 0.8)
				}),
				new EnvironmentalFactor("Rainy", new Modifier[0], new EnvironmentalFactor.FactorModifier[] { // Jungles and forests are typically wetter
						new EnvironmentalFactor.FactorModifier("Jungle", 1.5),
						new EnvironmentalFactor.FactorModifier("Forested", 1.2)
				}),
				new EnvironmentalFactor("Foggy", new Modifier[0], new EnvironmentalFactor.FactorModifier[] { // Fog is usually still
						new EnvironmentalFactor.FactorModifier("Windy", 0.8),
						new EnvironmentalFactor.FactorModifier("Sheltered", 1.2)
				}),
				new EnvironmentalFactor("Thunderstorm", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Windy", 2.0),
				}),
				new EnvironmentalFactor("Snowy", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Windy", 1.2),
						new EnvironmentalFactor.FactorModifier("Jungle", 0.5),
						new EnvironmentalFactor.FactorModifier("Forested", 0.7)
				}),
				new EnvironmentalFactor("Snowstorm", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Windy", 2.0),
						new EnvironmentalFactor.FactorModifier("Sheltered", 0.5),
						new EnvironmentalFactor.FactorModifier("Jungle", 0.2),
						new EnvironmentalFactor.FactorModifier("Forested", 0.4)
				}),
		};
		EnvironmentalFactor[] temperatures = new EnvironmentalFactor[] {
				new EnvironmentalFactor("Freezing", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Jungle", 0.0),
						new EnvironmentalFactor.FactorModifier("Forested", 0.5),
						new EnvironmentalFactor.FactorModifier("Snowy", 2.0),
						new EnvironmentalFactor.FactorModifier("Snowstorm", 2.0),
						new EnvironmentalFactor.FactorModifier("Rainy", 0.0),
						new EnvironmentalFactor.FactorModifier("Thunderstorm", 0.1),
						new EnvironmentalFactor.FactorModifier("Sunny", 0.2),
				}),
				new EnvironmentalFactor("Cold", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Jungle", 0.2),
						new EnvironmentalFactor.FactorModifier("Forested", 0.8),
						new EnvironmentalFactor.FactorModifier("Snowy", 1.5),
						new EnvironmentalFactor.FactorModifier("Snowstorm", 1.5),
						new EnvironmentalFactor.FactorModifier("Rainy", 0.2),
						new EnvironmentalFactor.FactorModifier("Sunny", 0.5),
				}),
				new EnvironmentalFactor("Neutral", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Jungle", 0.5),
						new EnvironmentalFactor.FactorModifier("Snowy", 0.2),
						new EnvironmentalFactor.FactorModifier("Snowstorm", 0.1),
						new EnvironmentalFactor.FactorModifier("Rainy", 1.5),
				}),
				new EnvironmentalFactor("Warm", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Snowy", 0.0),
						new EnvironmentalFactor.FactorModifier("Snowstorm", 0.0),
						new EnvironmentalFactor.FactorModifier("Forested", 1.2),
						new EnvironmentalFactor.FactorModifier("Jungle", 0.8),
				}),
				new EnvironmentalFactor("Hot", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Snowy", 0.0),
						new EnvironmentalFactor.FactorModifier("Snowstorm", 0.0),
						new EnvironmentalFactor.FactorModifier("Rainy", 0.3),
						new EnvironmentalFactor.FactorModifier("Thunderstorm", 0.5),
						new EnvironmentalFactor.FactorModifier("Jungle", 2.0),
						new EnvironmentalFactor.FactorModifier("Sunny", 2.0),
						new EnvironmentalFactor.FactorModifier("Full Moon", 2.0),
				}),
		};
		EnvironmentalFactor[] terrains = new EnvironmentalFactor[] {
				new EnvironmentalFactor("Mountain", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Freezing", 3.0),
						new EnvironmentalFactor.FactorModifier("Cold", 1.5),
						new EnvironmentalFactor.FactorModifier("Neutral", 0.25),
						new EnvironmentalFactor.FactorModifier("Warm", 0.0),
						new EnvironmentalFactor.FactorModifier("Hot", 0.0),
				}),
				new EnvironmentalFactor("Hills", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
				
				}),
				new EnvironmentalFactor("Grassland", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Freezing", 0.5),
						new EnvironmentalFactor.FactorModifier("Cold", 0.8),
						new EnvironmentalFactor.FactorModifier("Neutral", 1.5),
						new EnvironmentalFactor.FactorModifier("Warm", 0.8),
						new EnvironmentalFactor.FactorModifier("Hot", 0.5),
				}),
				new EnvironmentalFactor("Desert", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Freezing", 0.0),
						new EnvironmentalFactor.FactorModifier("Cold", 0.0),
						new EnvironmentalFactor.FactorModifier("Neutral", 0.1),
						new EnvironmentalFactor.FactorModifier("Warm", 1.0),
						new EnvironmentalFactor.FactorModifier("Hot", 1.5),
						new EnvironmentalFactor.FactorModifier("Rainy", 0.1),
						new EnvironmentalFactor.FactorModifier("Thunderstorm", 0.1),
				}),
		};
		
		
		EnvironmentalFactor[] times = new EnvironmentalFactor[] {
				new EnvironmentalFactor("Day", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Full Moon", 0.0),
				}),
				new EnvironmentalFactor("Twilight", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Sunny", 0.0),
						new EnvironmentalFactor.FactorModifier("Full Moon", 0.0),
				}),
				new EnvironmentalFactor("Night", new Modifier[0], new EnvironmentalFactor.FactorModifier[] {
						new EnvironmentalFactor.FactorModifier("Sunny", 0.0),
				}),
		};
		
		
		Environment.JsonEnvironmentalFactorList list;
		
		
		ObjectMapper om = new ObjectMapper();
		
		list = new Environment.JsonEnvironmentalFactorList(times, terrains, temperatures, weathers, features);
		
		
		try {
			om.writerWithDefaultPrettyPrinter().writeValue(Paths.get("src/main/resources/environmentalFactors.json").toFile(), list);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		try {
			list = om.readValue(Paths.get("src/main/resources/environmentalFactors.json").toFile(), Environment.JsonEnvironmentalFactorList.class);
			//om.writerWithDefaultPrettyPrinter().writeValue(Paths.get("src/main/resources/copyOfEnvironmentalFactors.json").toFile(), list);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
