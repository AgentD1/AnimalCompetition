module AnimalCompetition {
	requires javafx.controls;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.annotation;
	requires org.jetbrains.annotations;
	
	exports tech.jaboc.animalcompetition.animal;
	exports tech.jaboc.animalcompetition.environment;
	exports tech.jaboc.animalcompetition;
	exports tech.jaboc.animalcompetition.gui to javafx.graphics;
}