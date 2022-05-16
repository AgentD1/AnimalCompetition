package tech.jaboc.animalcompetition;

import java.util.Scanner;

/**
 * A static class containing Scanner helper functions.
 */
public class UserInputUtils {
	/**
	 * Get a single constrained character as input from the user
	 * @param in The scanner to read from
	 * @param allowedCharacters A list of allowed characters (must be lowercase)
	 * @param repeatMessage The message to print if the user enters an illegal character
	 * @return The user's character
	 */
	public static char getUserCharSelection(Scanner in, String allowedCharacters, String repeatMessage) {
		while (true) {
			String nextLine = in.nextLine();
			if (nextLine.length() != 0) {
				char character = Character.toLowerCase(nextLine.charAt(0));
				if (allowedCharacters.contains(Character.toString(character))) {
					return character;
				}
			}
			System.out.println(repeatMessage);
		}
	}
	
	/**
	 * Get a single constrained character as input from the user
	 * @param in The scanner to read from
	 * @param allowedCharacters A list of allowed characters (case-sensitive)
	 * @param repeatMessage The message to print if the user enters an illegal character
	 * @return The user's character
	 */
	public static char getUserCharSelectionCaseSensitive(Scanner in, String allowedCharacters, String repeatMessage) {
		while (true) {
			String nextLine = in.nextLine();
			if (nextLine.length() != 0) {
				char character = nextLine.charAt(0);
				if (allowedCharacters.contains(Character.toString(character))) {
					return character;
				}
			}
			System.out.println(repeatMessage);
		}
	}
	
	/**
	 * Gets a constrained int from the user
	 * @param in The scanner to read from
	 * @param lowerBound The lowest integer allowed
	 * @param upperBound The highest integer allowed
	 * @return The user's selection
	 */
	public static int getUserIntSelection(Scanner in, int lowerBound, int upperBound) {
		return getUserIntSelection(in, lowerBound, upperBound, String.format("Please enter a number between %d and %d (inclusive)", lowerBound, upperBound));
	}
	
	/**
	 * Gets a constrained int from the user
	 * @param in The scanner to read from
	 * @param lowerBound The lowest integer allowed
	 * @param upperBound The highest integer allowed
	 * @param repeatMessage The message to print if the user enters an illegal character
	 * @return The user's selection
	 */
	public static int getUserIntSelection(Scanner in, int lowerBound, int upperBound, String repeatMessage) {
		while (true) {
			String nextLine = in.nextLine();
			try {
				int parsed = Integer.parseInt(nextLine);
				if (upperBound >= parsed && parsed >= lowerBound) {
					return parsed;
				}
			} catch (NumberFormatException ignored) { }
			System.out.println(repeatMessage);
		}
	}
	
	
	private UserInputUtils() {
		throw new UnsupportedOperationException();
	}
}
