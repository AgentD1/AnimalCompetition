package tech.jaboc.animalcompetition.environment;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import java.util.stream.*;

public class Environment {
	public EnvironmentalFactor timeFactor;
	public EnvironmentalFactor terrainFactor;
	public EnvironmentalFactor temperatureFactor;
	public EnvironmentalFactor weatherFactor;
	public List<EnvironmentalFactor> features;
	
	@JsonIgnore
	public List<EnvironmentalFactor> getAllEnvironmentalFactors() {
		List<EnvironmentalFactor> returnValue = new ArrayList<>(features);
		returnValue.add(timeFactor);
		returnValue.add(terrainFactor);
		returnValue.add(temperatureFactor);
		returnValue.add(weatherFactor);
		return returnValue;
	}
	
	@JsonCreator
	public Environment(@JsonProperty("timeFactor") EnvironmentalFactor timeFactor, @JsonProperty("terrainFactor") EnvironmentalFactor terrainFactor, @JsonProperty("temperatureFactor") EnvironmentalFactor temperatureFactor, @JsonProperty("weatherFactor") EnvironmentalFactor weatherFactor, @JsonProperty("features") List<EnvironmentalFactor> features) {
		this.timeFactor = timeFactor;
		this.weatherFactor = weatherFactor;
		this.temperatureFactor = temperatureFactor;
		this.terrainFactor = terrainFactor;
		this.features = features;
	}
	
	public static Environment generateEnvironment(JsonEnvironmentalFactorList factorList) {
		System.out.println("Making a new environment");
		EnvironmentalFactor timeFactor, terrainFactor, temperatureFactor, weatherFactor;
		List<EnvironmentalFactor> features = new ArrayList<>();
		
		List<EnvironmentalFactor> timeOptions = Arrays.asList(factorList.times());
		defaultFactorProbabilities(timeOptions);
		timeFactor = chooseWeightedRandom(timeOptions);
		
		List<EnvironmentalFactor> terrainOptions = Arrays.asList(factorList.terrains());
		defaultFactorProbabilities(terrainOptions);
		processFactorList(terrainOptions, timeFactor);
		terrainFactor = chooseWeightedRandom(terrainOptions);
		
		List<EnvironmentalFactor> temperatureOptions = Arrays.asList(factorList.temperatures());
		defaultFactorProbabilities(temperatureOptions);
		processFactorList(temperatureOptions, timeFactor, terrainFactor);
		temperatureFactor = chooseWeightedRandom(temperatureOptions);
		
		List<EnvironmentalFactor> weatherOptions = Arrays.asList(factorList.weathers());
		defaultFactorProbabilities(weatherOptions);
		processFactorList(weatherOptions, timeFactor, terrainFactor, temperatureFactor);
		weatherFactor = chooseWeightedRandom(weatherOptions);
		
		int numTraits = (int) (Math.random() * 4.0);
		
		for (int traitNum = 0; traitNum < numTraits; traitNum++) {
			List<EnvironmentalFactor> featureOptions = new ArrayList<>(Arrays.asList(factorList.features()));
			defaultFactorProbabilities(featureOptions);
			EnvironmentalFactor[] allFactors = Stream.concat(Arrays.stream(
									new EnvironmentalFactor[] { weatherFactor, timeFactor, terrainFactor, temperatureFactor }),
							features.stream())
					.toArray(EnvironmentalFactor[]::new);
			processFactorList(featureOptions, allFactors);
			featureOptions.removeAll(features);
			
			features.add(chooseWeightedRandom(featureOptions));
			
			double randomSum = featureOptions.stream().mapToDouble(x -> x.currentProbability).sum();
			if (randomSum == 0) { // There are no more valid features to add
				break;
			}
		}
		
		return new Environment(timeFactor, terrainFactor, temperatureFactor, weatherFactor, features);
	}
	
	static void defaultFactorProbabilities(List<EnvironmentalFactor> factorList) {
		factorList.forEach(x -> x.currentProbability = x.defaultProbability == -1.0 ? 1.0 / factorList.size() : x.defaultProbability);
	}
	
	static void processFactorList(List<EnvironmentalFactor> factorList, EnvironmentalFactor... existingFactors) {
		for (EnvironmentalFactor f : factorList) {
			for (EnvironmentalFactor existingFactor : existingFactors) {
				for (EnvironmentalFactor.FactorModifier modifier : existingFactor.factorModifiers) {
					if (f.name.equals(modifier.factorName())) {
						f.currentProbability *= modifier.multiplier();
					}
				}
			}
		}
	}
	
	static EnvironmentalFactor chooseWeightedRandom(List<EnvironmentalFactor> environmentalFactors) {
		double randomSum = environmentalFactors.stream().mapToDouble(x -> x.currentProbability).sum();
		double randomNumber = Math.random() * randomSum;
		for (EnvironmentalFactor e : environmentalFactors) {
			randomNumber -= e.currentProbability;
			if (randomNumber <= 0) return e;
		}
		throw new RuntimeException("Reached the end of the weighted random without selecting anything (This will never happen)");
	}
	
	public record JsonEnvironmentalFactorList(EnvironmentalFactor[] times, EnvironmentalFactor[] terrains,
	                                          EnvironmentalFactor[] temperatures, EnvironmentalFactor[] weathers,
	                                          EnvironmentalFactor[] features) { }
}
