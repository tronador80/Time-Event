/**
 * 
 */
package pawel.uima.annotator.pos;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.MissingNode;
import eu.stratosphere.sopremo.type.ObjectNode;

/**
 * @author pawel
 * 
 */
public class PosTaggerAnalysisComponentTest {

	@Test
	public void testPosTaggingShortText() {
		PosTaggerAnalysisComponent ptac = new PosTaggerAnalysisComponent();

		String exampleInput = "{\"annotations\" : [{\"Text\" : \"Pawel was born on 07/13/1988. On 09/29/2013 Pawel completed his Master's thesis.\",\"begin\" : \"0\",\"date\" : \"20070412141200\",\"end\" : \"0\"},{\"Sentence\" : \"Pawel was born on 07/13/1988.\",\"begin\" : \"0\",\"end\" : \"29\",\"sentenceIndex\" : \"0\",\"tokenBegin\" : \"0\",\"tokenEnd\" : \"6\"},{\"Sentence\" : \"On 09/29/2013 Pawel completed his Master's thesis.\",\"begin\" : \"30\",\"end\" : \"80\",\"sentenceIndex\" : \"1\",\"tokenBegin\" : \"6\",\"tokenEnd\" : \"15\"},{\"Token\" : \"Pawel\",\"afterToken\" : \" \",\"beforeToken\" : \"\",\"begin\" : \"0\",\"end\" : \"5\",\"originalText\" : \"Pawel\",\"value\" : \"Pawel\"},{\"Token\" : \"was\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"6\",\"end\" : \"9\",\"originalText\" : \"was\",\"value\" : \"was\"},{\"Token\" : \"born\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"10\",\"end\" : \"14\",\"originalText\" : \"born\",\"value\" : \"born\"},{\"Token\" : \"on\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"15\",\"end\" : \"17\",\"originalText\" : \"on\",\"value\" : \"on\"},{\"Token\" : \"07/13/1988\",\"afterToken\" : \"\",\"beforeToken\" : \" \",\"begin\" : \"18\",\"end\" : \"28\",\"originalText\" : \"07/13/1988\",\"value\" : \"07/13/1988\"},{\"Token\" : \".\",\"afterToken\" : \" \",\"beforeToken\" : \"\",\"begin\" : \"28\",\"end\" : \"29\",\"originalText\" : \".\",\"value\" : \".\"},{\"Token\" : \"On\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"30\",\"end\" : \"32\",\"originalText\" : \"On\",\"value\" : \"On\"},{\"Token\" : \"09/29/2013\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"33\",\"end\" : \"43\",\"originalText\" : \"09/29/2013\",\"value\" : \"09/29/2013\"},{\"Token\" : \"Pawel\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"44\",\"end\" : \"49\",\"originalText\" : \"Pawel\",\"value\" : \"Pawel\"},{\"Token\" : \"completed\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"50\",\"end\" : \"59\",\"originalText\" : \"completed\",\"value\" : \"completed\"},{\"Token\" : \"his\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"60\",\"end\" : \"63\",\"originalText\" : \"his\",\"value\" : \"his\"},{\"Token\" : \"Master\",\"afterToken\" : \"\",\"beforeToken\" : \" \",\"begin\" : \"64\",\"end\" : \"70\",\"originalText\" : \"Master\",\"value\" : \"Master\"},{\"Token\" : \"'s\",\"afterToken\" : \" \",\"beforeToken\" : \"\",\"begin\" : \"70\",\"end\" : \"72\",\"originalText\" : \"'s\",\"value\" : \"'s\"},{\"Token\" : \"thesis\",\"afterToken\" : \"\",\"beforeToken\" : \" \",\"begin\" : \"73\",\"end\" : \"79\",\"originalText\" : \"thesis\",\"value\" : \"thesis\"},{\"Token\" : \".\",\"afterToken\" : \"\",\"beforeToken\" : \"\",\"begin\" : \"79\",\"end\" : \"80\",\"originalText\" : \".\",\"value\" : \".\"}]}";

		IJsonNode result = null;
		try {
			result = ptac.tagPos(exampleInput);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		Assert.assertNotNull(result);

		Assert.assertNotNull(result);

		Assert.assertTrue(result instanceof ObjectNode);
		ObjectNode resultObject = (ObjectNode) result;

		Assert.assertTrue(resultObject.get("annotations") instanceof ArrayNode<?>);

		ArrayNode<?> annotationsArray = (ArrayNode<?>) resultObject
				.get("annotations");

		String text = "";
		String sentencesText = "";
		String tokensText = "";

		List<String> expectedAnnotations = new ArrayList<String>();
		expectedAnnotations.add("Pawel");
		expectedAnnotations.add("was");
		expectedAnnotations.add("born");
		expectedAnnotations.add("on");
		expectedAnnotations.add("07/13/1988");
		expectedAnnotations.add(".");
		expectedAnnotations.add("On");
		expectedAnnotations.add("09/29/2013");
		expectedAnnotations.add("Pawel");
		expectedAnnotations.add("completed");
		expectedAnnotations.add("his");
		expectedAnnotations.add("Master");
		expectedAnnotations.add("'s");
		expectedAnnotations.add("thesis");
		expectedAnnotations.add(".");

		for (int i = 0; i < annotationsArray.size(); i++) {
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
				tokensText += tokenAnnotation;
				Assert.assertTrue(expectedAnnotations.remove(tokenAnnotation));

			} else {
				Assert.fail("Unexpected annotations");

			}
		}
		Assert.assertTrue(expectedAnnotations.isEmpty());

		Assert.assertEquals(text.replace(" ", ""),
				sentencesText.replace(" ", ""));
		Assert.assertEquals(text.replace(" ", ""), tokensText);

	}
}
