package de.dima.textmining.resources;

import java.io.File;
import java.io.IOException;

public class ResourceManager {

	private static boolean initialized = false;
	private static String tmpDir;
	private static String extractedResourceDir;

	private static void init() {
		if (initialized)
			return;
		initialized = true;
		tmpDir = System.getProperty("user.dir");
		extractedResourceDir = tmpDir + File.separator + "src" + File.separator
				+ "main" + File.separator + "resources";
		File dir = new File(extractedResourceDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	public static String getResourcePath(String resource) throws IOException {
		init();

		File r = new File(extractedResourceDir + File.separator + resource);

		// TODO: beim Entpacken Pfad von resource mit einbeziehen, dass es auch
		// moeglich ist Dateien mit dem selben Namen zu haben?

		// copy data TODO pawel
		// InputStream is = ResourceManager.class.getResourceAsStream(resource);
		// FileOutputStream fos = new FileOutputStream(r.getAbsolutePath());
		// byte[] buffer = new byte[4096];
		// int bytesRead = 0;
		// while ((bytesRead = is.read(buffer)) != -1) {
		// fos.write(buffer, 0, bytesRead);
		// }
		//
		// fos.close();
		return r.getAbsolutePath();
	}

	public static String getWorkingDir() {
		init();
		return extractedResourceDir;
	}

}
