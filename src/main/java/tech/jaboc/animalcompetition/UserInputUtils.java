package tech.jaboc.animalcompetition;

import java.util.Scanner;

public class UserInputUtils {
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
	
	public static int getUserIntSelection(Scanner in, int lowerBound, int upperBound) {
		return getUserIntSelection(in, lowerBound, upperBound, String.format("Please enter a number between %d and %d (inclusive)", lowerBound, upperBound));
	}
	
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
