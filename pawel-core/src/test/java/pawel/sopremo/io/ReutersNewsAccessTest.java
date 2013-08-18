/**
 * 
 */
package pawel.sopremo.io;

import org.junit.Test;

import pawel.sopremo.io.reutersnews.ReutersUnmarshaller;
import eu.stratosphere.sopremo.expressions.ConstantExpression;
import eu.stratosphere.sopremo.testing.SopremoTestPlan;
import eu.stratosphere.sopremo.type.ObjectNode;
import eu.stratosphere.sopremo.type.TextNode;

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
		reutersNews.setDocumentName(new ConstantExpression(System
				.getProperty("user.dir")
				+ "/src/test/resources/test_reuters_news/XYZnewsML.xml"));

		reutersNews.setIdOfFirstDocumentToProcess(new ConstantExpression(
				"10000"));
		reutersNews
				.setIdOfLastDocuemtnToProcess(new ConstantExpression("10003"));

		sopremoPlan.getOutputOperator(0).setInputs(reutersNews);

		sopremoPlan.run();
	}
}
