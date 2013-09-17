/**
 * 
 */
package pawel.sopremo.operator;

import org.junit.Test;

import eu.stratosphere.sopremo.io.Source;
import eu.stratosphere.sopremo.testing.SopremoTestPlan;
import eu.stratosphere.sopremo.type.ObjectNode;
import eu.stratosphere.sopremo.type.TextNode;

/**
 * Test class for {@link AnnotationsCounterSopremoOperator}.
 * 
 * @author ptondryk
 * @see AnnotationsCounterSopremoOperator
 */
public class AnnotationsCounterSopremoOperatorTest {

	@Test
	public void testCountSentences() {

		final SopremoTestPlan sopremoPlan = new SopremoTestPlan(0, 1);

		final AnnotationsCounterSopremoOperator annotationsCounter = new AnnotationsCounterSopremoOperator();
		annotationsCounter
				.setInputs(new Source(
						"file://"
								+ System.getProperty("user.dir")
								+ "/src/test/resources/test_operators/sentencesplit_output.json"));

		sopremoPlan.getOutputOperator(0).setInputs(annotationsCounter);

		ObjectNode sentences = new ObjectNode();
		sentences.put("sentences", new TextNode("16"));
		ObjectNode text = new ObjectNode();
		text.put("text", new TextNode("2"));
		ObjectNode tokens = new ObjectNode();
		tokens.put("tokens", new TextNode("364"));
		ObjectNode timexs = new ObjectNode();
		timexs.put("timexs", new TextNode("0"));

		sopremoPlan.getExpectedOutput(0).add(text).add(sentences).add(tokens)
				.add(timexs);

		sopremoPlan.run();
	}

	@Test
	public void testCountEvents() {

		final SopremoTestPlan sopremoPlan = new SopremoTestPlan(0, 1);

		final AnnotationsCounterSopremoOperator annotationsCounter = new AnnotationsCounterSopremoOperator();
		annotationsCounter.setInputs(new Source("file://"
				+ System.getProperty("user.dir")
				+ "/src/test/resources/test_operators/event_output.json"));

		sopremoPlan.getOutputOperator(0).setInputs(annotationsCounter);

		ObjectNode text = new ObjectNode();
		text.put("text", new TextNode("2"));
		ObjectNode events = new ObjectNode();
		events.put("event", new TextNode("2"));

		sopremoPlan.getExpectedOutput(0).add(text).add(events);

		sopremoPlan.run();
	}
}
