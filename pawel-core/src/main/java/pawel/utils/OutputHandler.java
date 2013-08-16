/**
 * 
 */
package pawel.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

/**
 * 
 * Class helps me to handle output of UIMA pipelines (that are running as parts
 * of Sopremo operators) until I found better solutions...
 * 
 * @author pawel
 * 
 */
public class OutputHandler {

	private static Logger log = Logger.getLogger(OutputHandler.class);

	public static final String DEFAULT_TMP_DIR = System
			.getProperty("user.home") + "/default_tmp_dir";

	/**
	 * 
	 * @return output of last started jobs
	 */
	public static String readOutputFromDefaultTmpDirectory() {
		return OutputHandler.readOutputFromExtendedDefaultTmpDirectory(null);
	}

	public static String readOutputFromExtendedDefaultTmpDirectory(
			String additionalPath) {
		String res = "";
		try {
			File folder = null;
			if (additionalPath == null) {
				folder = new File(DEFAULT_TMP_DIR);
			} else {
				folder = new File(DEFAULT_TMP_DIR + additionalPath);
			}

			res = OutputHandler.readAllFilesFromFolderAndSubfolders(folder);

			OutputHandler.delete(folder);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return res;
	}

	/**
	 * TODO comments...
	 * 
	 * @param folder
	 * @return
	 * @throws IOException
	 */
	private static String readAllFilesFromFolderAndSubfolders(File folder)
			throws IOException {
		return OutputHandler.readAllFilesFromFolderAndSubfolders(folder, null);
	}

	/**
	 * TODO comments...
	 * 
	 * @param folder
	 * @param folderPrefix
	 * @return
	 * @throws IOException
	 */
	private static String readAllFilesFromFolderAndSubfolders(File folder,
			String folderPrefix) throws IOException {
		File[] listOfFiles = folder.listFiles();
		String res = "";

		if (listOfFiles != null) {
			for (File file : listOfFiles) {
				if (file != null && file.isFile()) {
					res += OutputHandler.readFile(file);
				} else if (file != null && file.isDirectory()
						&& folderPrefix != null
						&& file.getName().startsWith(folderPrefix)) {
					res += OutputHandler.readAllFilesFromFolderAndSubfolders(
							folder, folderPrefix);
				}
			}
		} else {
			log.warn("No output in folder: " + folder);
		}

		return res;

	}

	/**
	 * Method reads <b>file</b>.
	 * 
	 * @param file
	 *            file to read
	 * @return content of <b>file</b> as string
	 * @throws IOException
	 */
	private static String readFile(File file) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());

			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}

	/**
	 * Method deletes <b>f</b>. If <b>f</b> is directory delete also all
	 * subfiles.
	 * 
	 * @param f
	 *            file to delte
	 * @throws IOException
	 */
	private static void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete())
			;
	}
}
