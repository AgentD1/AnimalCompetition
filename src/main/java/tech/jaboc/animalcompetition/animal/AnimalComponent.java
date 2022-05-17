package tech.jaboc.animalcompetition.animal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is an annotation which should be used to tag all variables in modules that can be accessed by ReflectiveModifiers
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AnimalComponent {
	String name();

	boolean multiplier();
}
