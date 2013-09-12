package pawel.uima.annotator.sentencesplit;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pawel.utils.OutputHandler;
import pawel.utils.Xmi2Json;
import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.MissingNode;
import eu.stratosphere.sopremo.type.ObjectNode;

/**
 * Test class for class {@link SentenceSplitterAnalysisComponent}
 * 
 * @author pawel
 * @see SentenceSplitterAnalysisComponent
 */
public class SentenceSplitterAnalysisComponentTest {

	/**
	 * this method checks whether the temporal directory is empty after each
	 * operator call
	 */
	@After
	public void testIsTmpDirEmpty() {
		File tmpDir = new File(OutputHandler.DEFAULT_TMP_DIR);
		if (tmpDir.exists() && tmpDir.isDirectory()) {
			Assert.assertEquals(0, tmpDir.list().length);
		}
	}

	@Test
	public void testSingleSentence() {
		SentenceSplitterAnalysisComponent tac = new SentenceSplitterAnalysisComponent();

		String exampleSentence = "{\"annotations\": [{\"Text\": \"This is an example.\", \"date\": \"200704121412\"}]}";
		String tokensAsXml = null;
		try {
			tokensAsXml = tac.tokenize(exampleSentence);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

		Assert.assertNotNull(tokensAsXml);

		IJsonNode result = Xmi2Json.xmi2Json(tokensAsXml);
		Assert.assertTrue(result instanceof ObjectNode);

		ObjectNode resultObject = (ObjectNode) result;
		Assert.assertFalse(resultObject.get("annotations") instanceof MissingNode);

		Assert.assertTrue(resultObject.get("annotations") instanceof ArrayNode<?>);

		ArrayNode<?> annotationsArray = (ArrayNode<?>) resultObject
				.get("annotations");

		Assert.assertTrue(annotationsArray.get(0) instanceof ObjectNode);
		Assert.assertTrue(annotationsArray.get(1) instanceof ObjectNode);

		Assert.assertFalse(((ObjectNode) annotationsArray.get(0)).get("Text") instanceof MissingNode);

		ObjectNode sentenceObject = (ObjectNode) annotationsArray.get(1);
		Assert.assertFalse(sentenceObject.get("Sentence") instanceof MissingNode);
		Assert.assertFalse(sentenceObject.get("begin") instanceof MissingNode);
		Assert.assertFalse(sentenceObject.get("end") instanceof MissingNode);
		Assert.assertTrue(sentenceObject.get("start") instanceof MissingNode);

		Assert.assertEquals("This is an example.",
				sentenceObject.get("Sentence").toString());
		Assert.assertEquals(0,
				Integer.parseInt(sentenceObject.get("begin").toString()));
		Assert.assertEquals("This is an example.".length(),
				Integer.parseInt(sentenceObject.get("end").toString()));
	}

	@Test
	public void testTwoSentences() {
		SentenceSplitterAnalysisComponent tac = new SentenceSplitterAnalysisComponent();

		String exampleText = "{\"annotations\": [{\"Text\": \"Pawel was born on 07/13/1988. "
				+ "On 09/29/2013 Pawel completed his Master's thesis.\", \"date\": \"200704121412\"}]}";
		String tokensAsXml = null;
		try {
			tokensAsXml = tac.tokenize(exampleText);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertNotNull(tokensAsXml);

		IJsonNode result = Xmi2Json.xmi2Json(tokensAsXml);
		Assert.assertTrue(result instanceof ObjectNode);

		ObjectNode resultObject = (ObjectNode) result;
		Assert.assertFalse(resultObject.get("annotations") instanceof MissingNode);
		Assert.assertTrue(resultObject.get("annotations") instanceof ArrayNode<?>);

		ArrayNode<?> annotationsArray = (ArrayNode<?>) resultObject
				.get("annotations");

		Assert.assertTrue(annotationsArray.get(1) instanceof ObjectNode);
		ObjectNode sentence0Object = (ObjectNode) annotationsArray.get(1);
		Assert.assertNotNull(sentence0Object.get("Sentence"));
		Assert.assertFalse(sentence0Object.get("Sentence") instanceof MissingNode);
		Assert.assertFalse(sentence0Object.get("begin") instanceof MissingNode);
		Assert.assertFalse(sentence0Object.get("end") instanceof MissingNode);
		Assert.assertEquals("Pawel was born on 07/13/1988.", sentence0Object
				.get("Sentence").toString());
		Assert.assertEquals(0,
				Integer.parseInt(sentence0Object.get("begin").toString()));
		Assert.assertEquals("Pawel was born on 07/13/1988.".length(),
				Integer.parseInt(sentence0Object.get("end").toString()));

		Assert.assertTrue(annotationsArray.get(2) instanceof ObjectNode);
		ObjectNode sentence1Object = (ObjectNode) annotationsArray.get(2);
		Assert.assertNotNull(sentence1Object.get("Sentence"));
		Assert.assertFalse(sentence1Object.get("Sentence") instanceof MissingNode);
		Assert.assertFalse(sentence1Object.get("begin") instanceof MissingNode);
		Assert.assertFalse(sentence1Object.get("end") instanceof MissingNode);
		Assert.assertEquals(
				"On 09/29/2013 Pawel completed his Master's thesis.",
				sentence1Object.get("Sentence").toString());
		Assert.assertEquals("Pawel was born on 07/13/1988. ".length(),
				Integer.parseInt(sentence1Object.get("begin").toString()));
		Assert.assertEquals(
				"Pawel was born on 07/13/1988. On 09/29/2013 Pawel completed his Master's thesis."
						.length(), Integer.parseInt(sentence1Object.get("end")
						.toString()));

	}

	@Test
	public void testManySenteces() {
		SentenceSplitterAnalysisComponent tac = new SentenceSplitterAnalysisComponent();

		String exampleText = "{\"annotations\": [{\"Text\": \"In order to start/stop the remote processes, "
				+ "the master node requires access via ssh to the worker nodes. "
				+ "It is most convenient to use ssh's public key authentication "
				+ "for this. To setup public key authentication, log on to the master "
				+ "as the user who will later execute all the Nephele components. "
				+ "The same user (i.e. a user with the same user name) must also "
				+ "exist on all worker nodes. For the remainder of this instruction "
				+ "we will refer to this user as nephele. Using the super user root "
				+ "is highly discouraged for security reasons.\", \"date\": \"200704121412\"}]} ";
		String tokensAsXml = null;

		try {
			tokensAsXml = tac.tokenize(exampleText);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertNotNull(tokensAsXml);

		IJsonNode result = Xmi2Json.xmi2Json(tokensAsXml);
		Assert.assertTrue(result instanceof ObjectNode);

		ObjectNode resultObject = (ObjectNode) result;
		Assert.assertFalse(resultObject.get("annotations") instanceof MissingNode);
		Assert.assertTrue(resultObject.get("annotations") instanceof ArrayNode<?>);

		ArrayNode<?> annotationsArray = (ArrayNode<?>) resultObject
				.get("annotations");

		int endOfPreviousSentence = 0;
		for (int i = 1; i < 7; i++) {
			Assert.assertTrue(annotationsArray.get(i) instanceof ObjectNode);

			ObjectNode sentenceObject = (ObjectNode) annotationsArray.get(i);
			Assert.assertNotNull(sentenceObject.get("Sentence"));
			Assert.assertFalse(sentenceObject.get("Sentence") instanceof MissingNode);
			Assert.assertFalse(sentenceObject.get("begin") instanceof MissingNode);
			Assert.assertFalse(sentenceObject.get("end") instanceof MissingNode);
			Assert.assertTrue(sentenceObject.get("start") instanceof MissingNode);

			int begin = Integer
					.parseInt(sentenceObject.get("begin").toString());
			int end = Integer.parseInt(sentenceObject.get("end").toString());
			Assert.assertEquals(endOfPreviousSentence, begin);
			endOfPreviousSentence = end + 1;
		}
	}
}
