/**
 * 
 */
package pawel.uima.annotator.sutime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UIMAException;
import org.junit.Assert;
import org.junit.Test;

import pawel.uima.annotator.sutime.SuTimeAnalysisComponent;
import pawel.utils.Xmi2Json;
import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.MissingNode;
import eu.stratosphere.sopremo.type.ObjectNode;

/**
 * @author ptondryk
 * 
 */
public class SuTimeAnalysisComponentTest {

	@Test
	public void testSuTime() {
		SuTimeAnalysisComponent htac = new SuTimeAnalysisComponent();

		String exampleInput = "{\"annotations\" : [{\"Text\" : \"Pawel was born on 07/13/1988. On 09/29/2013 Pawel completed his Master's thesis.\",\"begin\" : \"0\",\"date\" : \"20070413100000\",\"end\" : \"0\"},{\"Sentence\" : \"Pawel was born on 07/13/1988.\",\"begin\" : \"0\",\"end\" : \"29\",\"sentenceIndex\" : \"0\",\"tokenBegin\" : \"0\",\"tokenEnd\" : \"6\"},{\"Sentence\" : \"On 09/29/2013 Pawel completed his Master's thesis.\",\"begin\" : \"30\",\"end\" : \"80\",\"sentenceIndex\" : \"1\",\"tokenBegin\" : \"6\",\"tokenEnd\" : \"15\"},{\"Token\" : \"Pawel\",\"afterToken\" : \" \",\"beforeToken\" : \"\",\"begin\" : \"0\",\"end\" : \"5\",\"originalText\" : \"Pawel\",\"pos\" : \"NNP\",\"value\" : \"Pawel\"},{\"Token\" : \"was\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"6\",\"end\" : \"9\",\"originalText\" : \"was\",\"pos\" : \"VBD\",\"value\" : \"was\"},{\"Token\" : \"born\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"10\",\"end\" : \"14\",\"originalText\" : \"born\",\"pos\" : \"VBN\",\"value\" : \"born\"},{\"Token\" : \"on\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"15\",\"end\" : \"17\",\"originalText\" : \"on\",\"pos\" : \"IN\",\"value\" : \"on\"},{\"Token\" : \"07/13/1988\",\"afterToken\" : \"\",\"beforeToken\" : \" \",\"begin\" : \"18\",\"end\" : \"28\",\"originalText\" : \"07/13/1988\",\"pos\" : \"CD\",\"value\" : \"07/13/1988\"},{\"Token\" : \".\",\"afterToken\" : \" \",\"beforeToken\" : \"\",\"begin\" : \"28\",\"end\" : \"29\",\"originalText\" : \".\",\"pos\" : \".\",\"value\" : \".\"},{\"Token\" : \"On\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"30\",\"end\" : \"32\",\"originalText\" : \"On\",\"pos\" : \"IN\",\"value\" : \"On\"},{\"Token\" : \"09/29/2013\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"33\",\"end\" : \"43\",\"originalText\" : \"09/29/2013\",\"pos\" : \"CD\",\"value\" : \"09/29/2013\"},{\"Token\" : \"Pawel\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"44\",\"end\" : \"49\",\"originalText\" : \"Pawel\",\"pos\" : \"NN\",\"value\" : \"Pawel\"},{\"Token\" : \"completed\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"50\",\"end\" : \"59\",\"originalText\" : \"completed\",\"pos\" : \"VBD\",\"value\" : \"completed\"},{\"Token\" : \"his\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"60\",\"end\" : \"63\",\"originalText\" : \"his\",\"pos\" : \"PRP$\",\"value\" : \"his\"},{\"Token\" : \"Master\",\"afterToken\" : \"\",\"beforeToken\" : \" \",\"begin\" : \"64\",\"end\" : \"70\",\"originalText\" : \"Master\",\"pos\" : \"NN\",\"value\" : \"Master\"},{\"Token\" : \"'s\",\"afterToken\" : \" \",\"beforeToken\" : \"\",\"begin\" : \"70\",\"end\" : \"72\",\"originalText\" : \"'s\",\"pos\" : \"POS\",\"value\" : \"'s\"},{\"Token\" : \"thesis\",\"afterToken\" : \"\",\"beforeToken\" : \" \",\"begin\" : \"73\",\"end\" : \"79\",\"originalText\" : \"thesis\",\"pos\" : \"NN\",\"value\" : \"thesis\"},{\"Token\" : \".\",\"afterToken\" : \"\",\"beforeToken\" : \"\",\"begin\" : \"79\",\"end\" : \"80\",\"originalText\" : \".\",\"pos\" : \".\",\"value\" : \".\"}]}";

		String tokensAsXml = null;
		try {
			tokensAsXml = htac.tagTime(exampleInput);
		} catch (UIMAException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertNotNull(tokensAsXml);

		IJsonNode result = Xmi2Json.xmi2Json(tokensAsXml);

		Assert.assertNotNull(result);

		Assert.assertTrue(result instanceof ObjectNode);
		ObjectNode resultObject = (ObjectNode) result;

		Assert.assertTrue(resultObject.get("annotations") instanceof ArrayNode<?>);

		ArrayNode<?> annotationsArray = (ArrayNode<?>) resultObject
				.get("annotations");

		int expectedNumberOfAnnotations = 20;
		Assert.assertEquals(expectedNumberOfAnnotations,
				annotationsArray.size());
		String text = "";
		String sentencesText = "";
		String tokenAnnotationsText = "";

		List<String> expectedTokenAnnotations = new ArrayList<String>();
		expectedTokenAnnotations.add("Pawel");
		expectedTokenAnnotations.add("was");
		expectedTokenAnnotations.add("born");
		expectedTokenAnnotations.add("on");
		expectedTokenAnnotations.add("07/13/1988");
		expectedTokenAnnotations.add(".");
		expectedTokenAnnotations.add("On");
		expectedTokenAnnotations.add("09/29/2013");
		expectedTokenAnnotations.add("Pawel");
		expectedTokenAnnotations.add("completed");
		expectedTokenAnnotations.add("his");
		expectedTokenAnnotations.add("Master");
		expectedTokenAnnotations.add("'s");
		expectedTokenAnnotations.add("thesis");
		expectedTokenAnnotations.add(".");

		List<String> expectedTimexAnnotations = new ArrayList<String>();
		expectedTimexAnnotations.add("1988-07-13");
		expectedTimexAnnotations.add("2013-09-29");

		for (int i = 0; i < expectedNumberOfAnnotations; i++) {
			Assert.assertTrue(annotationsArray.get(i) instanceof ObjectNode);
			ObjectNode annotationObject = (ObjectNode) annotationsArray.get(i);

			if (!(annotationObject.get("Text") instanceof MissingNode)
					&& annotationObject.get("Text").isTextual()) {
				text += annotationObject.get("Text").toString();
			} else if (!(annotationObject.get("Sentence") instanceof MissingNode)
					&& annotationObject.get("Sentence").isTextual()) {
				sentencesText += annotationObject.get("Sentence").toString();
			} else if (!(annotationObject.get("Token") instanceof MissingNode)
					&& annotationObject.get("Token").isTextual()) {
				String tokenAnnotation = annotationObject.get("Token")
						.toString();
				tokenAnnotationsText += tokenAnnotation;
				Assert.assertTrue(expectedTokenAnnotations
						.remove(tokenAnnotation));
			} else if (!(annotationObject.get("Timex3") instanceof MissingNode)
					&& annotationObject.get("Timex3").isTextual()) {
				Assert.assertTrue(expectedTimexAnnotations
						.remove(annotationObject.get("timexValue").toString()));
			} else {
				Assert.fail("Unexpected annotations");
			}
		}
		Assert.assertTrue(expectedTokenAnnotations.isEmpty());
		Assert.assertTrue(expectedTimexAnnotations.isEmpty());

		Assert.assertEquals(text.replace(" ", ""),
				sentencesText.replace(" ", ""));
		Assert.assertEquals(text.replace(" ", ""), tokenAnnotationsText);

	}
}
