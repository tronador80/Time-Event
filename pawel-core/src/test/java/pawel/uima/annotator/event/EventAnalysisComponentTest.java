/**
 * 
 */
package pawel.uima.annotator.event;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.MissingNode;
import eu.stratosphere.sopremo.type.ObjectNode;

/**
 * Test class for class {@link EventAnalysisComponent}.
 * 
 * @author ptondryk
 * 
 */
public class EventAnalysisComponentTest {

	@Test
	public void testTagEvent1() {
		EventAnalysisComponent eac = new EventAnalysisComponent();

		String exampleInput = "{\"annotations\" : [{\"Text\" : \"Pawel was born on 07/13/1988. On 09/29/2013 Pawel completed his Master's thesis.\",\"begin\" : \"0\",\"date\" : \"20070413100000\",\"end\" : \"0\"},{\"Sentence\" : \"Pawel was born on 07/13/1988.\",\"begin\" : \"0\",\"end\" : \"29\",\"sentenceIndex\" : \"0\",\"tokenBegin\" : \"0\",\"tokenEnd\" : \"6\"},{\"Sentence\" : \"On 09/29/2013 Pawel completed his Master's thesis.\",\"begin\" : \"30\",\"end\" : \"80\",\"sentenceIndex\" : \"1\",\"tokenBegin\" : \"6\",\"tokenEnd\" : \"15\"},{\"Token\" : \"Pawel\",\"afterToken\" : \" \",\"beforeToken\" : \"\",\"begin\" : \"0\",\"end\" : \"5\",\"originalText\" : \"Pawel\",\"pos\" : \"NNP\",\"value\" : \"Pawel\"},{\"Token\" : \"was\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"6\",\"end\" : \"9\",\"originalText\" : \"was\",\"pos\" : \"VBD\",\"value\" : \"was\"},{\"Token\" : \"born\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"10\",\"end\" : \"14\",\"originalText\" : \"born\",\"pos\" : \"VBN\",\"value\" : \"born\"},{\"Token\" : \"on\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"15\",\"end\" : \"17\",\"originalText\" : \"on\",\"pos\" : \"IN\",\"value\" : \"on\"},{\"Token\" : \"07/13/1988\",\"afterToken\" : \"\",\"beforeToken\" : \" \",\"begin\" : \"18\",\"end\" : \"28\",\"originalText\" : \"07/13/1988\",\"pos\" : \"CD\",\"value\" : \"07/13/1988\"},{\"Token\" : \".\",\"afterToken\" : \" \",\"beforeToken\" : \"\",\"begin\" : \"28\",\"end\" : \"29\",\"originalText\" : \".\",\"pos\" : \".\",\"value\" : \".\"},{\"Token\" : \"On\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"30\",\"end\" : \"32\",\"originalText\" : \"On\",\"pos\" : \"IN\",\"value\" : \"On\"},{\"Token\" : \"09/29/2013\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"33\",\"end\" : \"43\",\"originalText\" : \"09/29/2013\",\"pos\" : \"CD\",\"value\" : \"09/29/2013\"},{\"Token\" : \"Pawel\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"44\",\"end\" : \"49\",\"originalText\" : \"Pawel\",\"pos\" : \"NNP\",\"value\" : \"Pawel\"},{\"Token\" : \"completed\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"50\",\"end\" : \"59\",\"originalText\" : \"completed\",\"pos\" : \"VBD\",\"value\" : \"completed\"},{\"Token\" : \"his\",\"afterToken\" : \" \",\"beforeToken\" : \" \",\"begin\" : \"60\",\"end\" : \"63\",\"originalText\" : \"his\",\"pos\" : \"PRP$\",\"value\" : \"his\"},{\"Token\" : \"Master\",\"afterToken\" : \"\",\"beforeToken\" : \" \",\"begin\" : \"64\",\"end\" : \"70\",\"originalText\" : \"Master\",\"pos\" : \"NNP\",\"value\" : \"Master\"},{\"Token\" : \"'s\",\"afterToken\" : \" \",\"beforeToken\" : \"\",\"begin\" : \"70\",\"end\" : \"72\",\"originalText\" : \"'s\",\"pos\" : \"POS\",\"value\" : \"'s\"},{\"Token\" : \"thesis\",\"afterToken\" : \"\",\"beforeToken\" : \" \",\"begin\" : \"73\",\"end\" : \"79\",\"originalText\" : \"thesis\",\"pos\" : \"NN\",\"value\" : \"thesis\"},{\"Token\" : \".\",\"afterToken\" : \"\",\"beforeToken\" : \"\",\"begin\" : \"79\",\"end\" : \"80\",\"originalText\" : \".\",\"pos\" : \".\",\"value\" : \".\"},{\"Timex3\" : \"07/13/1988\",\"allTokIds\" : \"BEGIN<-->0\",\"begin\" : \"18\",\"end\" : \"28\",\"firstTokId\" : \"0\",\"foundByRule\" : \"date_r0c-explicit\",\"sentId\" : \"0\",\"timexFreq\" : \"\",\"timexId\" : \"t1\",\"timexInstance\" : \"0\",\"timexMod\" : \"\",\"timexQuant\" : \"\",\"timexType\" : \"DATE\",\"timexValue\" : \"1988-07-13\"},{\"Timex3\" : \"09/29/2013\",\"allTokIds\" : \"BEGIN<-->0\",\"begin\" : \"33\",\"end\" : \"43\",\"firstTokId\" : \"0\",\"foundByRule\" : \"date_r0c-explicit\",\"sentId\" : \"1\",\"timexFreq\" : \"\",\"timexId\" : \"t3\",\"timexInstance\" : \"0\",\"timexMod\" : \"\",\"timexQuant\" : \"\",\"timexType\" : \"DATE\",\"timexValue\" : \"2013-09-29\"}]}";

		IJsonNode result = null;
		try {
			result = eac.tagEvent(exampleInput, 250, 10);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		Assert.assertNotNull(result);

		Assert.assertTrue(result instanceof ObjectNode);
		ObjectNode resultObject = (ObjectNode) result;

		Assert.assertFalse(resultObject.get("events") instanceof MissingNode);

		Assert.assertTrue(resultObject.get("events") instanceof ArrayNode<?>);

		ArrayNode<?> annotationsArray = (ArrayNode<?>) resultObject
				.get("events");

		int expectedNumberOfAnnotations = 2;
		Assert.assertEquals(expectedNumberOfAnnotations,
				annotationsArray.size());

		List<String> expectedEventContents = new ArrayList<String>();
		expectedEventContents.add("Pawel completed his Master 's thesis");
		expectedEventContents.add("Pawel was born");

		for (int i = 0; i < expectedNumberOfAnnotations; i++) {
			Assert.assertTrue(annotationsArray.get(i) instanceof ObjectNode);
			ObjectNode eventObject = (ObjectNode) annotationsArray.get(i);

			Assert.assertFalse(eventObject.get("start") instanceof MissingNode);
			Assert.assertFalse(eventObject.get("end") instanceof MissingNode);
			Assert.assertFalse(eventObject.get("timeSpan") instanceof MissingNode);
			Assert.assertFalse(eventObject.get("personalTime") instanceof MissingNode);
			Assert.assertFalse(eventObject.get("content") instanceof MissingNode);
			Assert.assertFalse(eventObject.get("text") instanceof MissingNode);
			Assert.assertFalse(eventObject.get("start") instanceof MissingNode);

			Assert.assertTrue(expectedEventContents.remove(eventObject.get(
					"content").toString()));
		}

		Assert.assertTrue(expectedEventContents.isEmpty());
	}

	@Test
	public void testTagEventWithTimespan() {
		EventAnalysisComponent eac = new EventAnalysisComponent();

		String exampleInput = "{\"annotations\":[{\"Text\":\"Pawel was in Berlin from 02/07/2013 to 02/09/2013.\",\"begin\":\"0\",\"date\":\"20131009020000\",\"end\":\"0\"},{\"Sentence\":\"Pawel was in Berlin from 02/07/2013 to 02/09/2013.\",\"begin\":\"0\",\"end\":\"50\",\"sentenceIndex\":\"0\",\"tokenBegin\":\"0\",\"tokenEnd\":\"9\"},{\"Token\":\"Pawel\",\"afterToken\":\" \",\"beforeToken\":\"\",\"begin\":\"0\",\"end\":\"5\",\"originalText\":\"Pawel\",\"pos\":\"NNP\",\"value\":\"Pawel\"},{\"Token\":\"was\",\"afterToken\":\" \",\"beforeToken\":\" \",\"begin\":\"6\",\"end\":\"9\",\"originalText\":\"was\",\"pos\":\"VBD\",\"value\":\"was\"},{\"Token\":\"in\",\"afterToken\":\" \",\"beforeToken\":\" \",\"begin\":\"10\",\"end\":\"12\",\"originalText\":\"in\",\"pos\":\"IN\",\"value\":\"in\"},{\"Token\":\"Berlin\",\"afterToken\":\" \",\"beforeToken\":\" \",\"begin\":\"13\",\"end\":\"19\",\"originalText\":\"Berlin\",\"pos\":\"NNP\",\"value\":\"Berlin\"},{\"Token\":\"from\",\"afterToken\":\" \",\"beforeToken\":\" \",\"begin\":\"20\",\"end\":\"24\",\"originalText\":\"from\",\"pos\":\"IN\",\"value\":\"from\"},{\"Token\":\"02/07/2013\",\"afterToken\":\" \",\"beforeToken\":\" \",\"begin\":\"25\",\"end\":\"35\",\"originalText\":\"02/07/2013\",\"pos\":\"CD\",\"value\":\"02/07/2013\"},{\"Token\":\"to\",\"afterToken\":\" \",\"beforeToken\":\" \",\"begin\":\"36\",\"end\":\"38\",\"originalText\":\"to\",\"pos\":\"TO\",\"value\":\"to\"},{\"Token\":\"02/09/2013\",\"afterToken\":\"\",\"beforeToken\":\" \",\"begin\":\"39\",\"end\":\"49\",\"originalText\":\"02/09/2013\",\"pos\":\"CD\",\"value\":\"02/09/2013\"},{\"Token\":\".\",\"afterToken\":\"\",\"beforeToken\":\"\",\"begin\":\"49\",\"end\":\"50\",\"originalText\":\".\",\"pos\":\".\",\"value\":\".\"},{\"Timex3\":\"02/07/2013\",\"begin\":\"25\",\"end\":\"35\",\"firstTokId\":\"0\",\"sentId\":\"0\",\"timexId\":\"t1\",\"timexInstance\":\"0\",\"timexType\":\"DATE\",\"timexValue\":\"2013-02-07\"},{\"Timex3\":\"02/09/2013\",\"begin\":\"39\",\"end\":\"49\",\"firstTokId\":\"0\",\"sentId\":\"0\",\"timexId\":\"t2\",\"timexInstance\":\"0\",\"timexType\":\"DATE\",\"timexValue\":\"2013-02-09\"}]}";

		IJsonNode result = null;
		try {
			result = eac.tagEvent(exampleInput, 250, 10);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		Assert.assertNotNull(result);

		Assert.assertTrue(result instanceof ObjectNode);
		ObjectNode resultObject = (ObjectNode) result;

		Assert.assertFalse(resultObject.get("events") instanceof MissingNode);

		Assert.assertTrue(resultObject.get("events") instanceof ArrayNode<?>);

		ArrayNode<?> annotationsArray = (ArrayNode<?>) resultObject
				.get("events");

		int expectedNumberOfAnnotations = 1;
		Assert.assertEquals(expectedNumberOfAnnotations,
				annotationsArray.size());

		List<String> expectedEventContents = new ArrayList<String>();
		expectedEventContents.add("Pawel was");

		Assert.assertTrue(annotationsArray.get(0) instanceof ObjectNode);
		ObjectNode eventObject = (ObjectNode) annotationsArray.get(0);

		Assert.assertFalse(eventObject.get("start") instanceof MissingNode);
		Assert.assertFalse(eventObject.get("end") instanceof MissingNode);
		Assert.assertFalse(eventObject.get("timeSpan") instanceof MissingNode);
		Assert.assertFalse(eventObject.get("personalTime") instanceof MissingNode);
		Assert.assertFalse(eventObject.get("content") instanceof MissingNode);
		Assert.assertFalse(eventObject.get("text") instanceof MissingNode);
		Assert.assertFalse(eventObject.get("start") instanceof MissingNode);

		Assert.assertTrue(expectedEventContents.remove(eventObject.get(
				"content").toString()));
	}
}
