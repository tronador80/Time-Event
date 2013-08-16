/**
 * 
 */
package pawel.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test class for {@link OutputHandler}.
 * 
 * @author pawel
 * 
 */
public class OutputHandlerTest {

	@Test
	public void testReadOutputFromDefaultTmpDirectory() {
		String exampleText = "My name is Pawel.";
		File exampleFile = new File(OutputHandler.DEFAULT_TMP_DIR
				+ "/tmp123.out");

		try {
			File dir = new File(OutputHandler.DEFAULT_TMP_DIR);
			dir.mkdir();
			FileWriter fw = new FileWriter(exampleFile);
			fw.append(exampleText);
			fw.close();
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}

		String foundText = OutputHandler.readOutputFromDefaultTmpDirectory();

		Assert.assertEquals(exampleText, foundText);
		Assert.assertTrue(!exampleFile.exists());
	}
}
