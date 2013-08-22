/**
 * 
 */
package pawel.sopremo.io;

import org.junit.Test;

import eu.stratosphere.sopremo.expressions.ConstantExpression;
import eu.stratosphere.sopremo.testing.SopremoTestPlan;

/**
 * Test class for {@link ReutersNewsAccess}.
 * 
 * @author ptondryk
 * 
 */
public class ReutersNewsAccessTest {

	@Test
	public void testExistingFileSystemReutersNewsFile() {

		final SopremoTestPlan sopremoPlan = new SopremoTestPlan(0, 1);

		final ReutersNewsAccess reutersNews = new ReutersNewsAccess();
		reutersNews.setDocumentName(new ConstantExpression(
				"hdfs://localhost:9000/selected_news/")); // /home/ptondryk/Development/DATA/test_reuters_news/

		reutersNews.setHdfsConfPath(new ConstantExpression(
				"/home/ptondryk/Development/hadoop-2.0.5-alpha/etc/hadoop/"));

		sopremoPlan.getOutputOperator(0).setInputs(reutersNews);

		sopremoPlan.run();
	}
}
