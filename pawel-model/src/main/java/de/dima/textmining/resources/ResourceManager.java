package de.dima.textmining.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceManager {

	private static boolean initialized = false;
	private static String tmpDir;
	private static String extractedResourceDir;

	private static void init() {
		if (initialized)
			return;
		initialized = true;
		tmpDir = System.getProperty("java.io.tmpdir");
		extractedResourceDir = tmpDir + File.separator + "tmp_models_location";
		File dir = new File(extractedResourceDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	public static String getResourcePath(String resource) {
		init();

		String[] path = resource.split(File.separator);
		String fileName = path[path.length - 1];

		File r = new File(extractedResourceDir + File.separator + fileName);

		// TODO: beim Entpacken Pfad von resource mit einbeziehen, dass es auch
		// moeglich ist Dateien mit dem selben Namen zu haben?

		// copy data
		try {
			InputStream is = ResourceManager.class
					.getResourceAsStream(resource);
			FileOutputStream fos = new FileOutputStream(r.getAbsolutePath());
			byte[] buffer = new byte[4096];
			int bytesRead = 0;
			while ((bytesRead = is.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return r.getAbsolutePath();
	}

	public static String getWorkingDir() {
		init();
		return extractedResourceDir;
	}

}
