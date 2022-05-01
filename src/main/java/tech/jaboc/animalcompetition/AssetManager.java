package tech.jaboc.animalcompetition;

import java.io.InputStream;
import java.util.Objects;

public class AssetManager {
	static boolean isJarFile;
	
	static {
		isJarFile = Objects.requireNonNull(AssetManager.class.getResource("AssetManager.class")).getProtocol().equals("jar");
		System.out.println(isJarFile);
	}
	
	public static boolean isJarFile() {
		return isJarFile;
	}
	
	public static InputStream getResourceStream(String name) {
		InputStream input = AssetManager.class.getResourceAsStream("/resources/" + name);
		if (input == null) {
			System.out.println("Errors");
			input = AssetManager.class.getClassLoader().getResourceAsStream(name);
		}
		
		return input;
	}
	
	private AssetManager() {
		throw new UnsupportedOperationException();
	}
}
