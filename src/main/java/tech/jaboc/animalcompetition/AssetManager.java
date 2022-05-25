package tech.jaboc.animalcompetition;

import java.io.*;
import java.util.Objects;

public class AssetManager {
	static boolean isJarFile;
	
	static {
		isJarFile = Objects.requireNonNull(AssetManager.class.getResource("AssetManager.class")).getProtocol().equals("jar");
	}
	
	public static boolean isJarFile() {
		return isJarFile;
	}
	
	public static InputStream getResourceStream(String name) {
		System.out.println(new File("/" + name).getAbsolutePath());
		InputStream input = AssetManager.class.getResourceAsStream("/" + name);
		if (input == null) {
			input = AssetManager.class.getClassLoader().getResourceAsStream(name);
		}
		
		return input;
	}
	
	private AssetManager() {
		throw new UnsupportedOperationException();
	}
}
