package tech.jaboc.animalcompetition;

public class Main {
	/**
	 * An old remnant from the old ways of making main methods.
	 * Go to the Start class in the gui package.
	 *
	 * @param args nothing
	 * @see tech.jaboc.animalcompetition.gui.Start
	 */
	public static void main(String[] args) {
		System.out.println("""
				If you're reading this, you didn't start the program with JavaFX!\s
				The JavaFX application class is tech.jaboc.animalcompetition.gui.AnimalMain,\s
				and the real main function is in the tech.jaboc.animalcompetition.gui.Start class.""");
		throw new RuntimeException("Bruh moment");
	}
}
