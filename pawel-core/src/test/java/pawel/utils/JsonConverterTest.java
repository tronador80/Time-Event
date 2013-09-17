/**
 * 
 */
package pawel.utils;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import pawel.model.Sentence2;
import pawel.model.Timex3;
import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.MissingNode;
import eu.stratosphere.sopremo.type.ObjectNode;
import eu.stratosphere.sopremo.type.TextNode;

/**
 * Test class for class {@link JsonConverter}.
 * 
 * @author ptondryk
 * 
 */
public class JsonConverterTest {

	@Test
	public void testJson2String() {

		String exampleInput = "[{ \"start\": \"2013-2-14 00,00,00\", \"end\": \"2013-2-14 23,59,59\", \"timeSpan\": \"false\", \"personalTime\": \"false\", \"content\": \"Today is\", \"text\": \"Today is Monday. \" }]";

		IJsonNode json = JsonConverter.string2Json(exampleInput);

		Assert.assertNotNull(json);
		Assert.assertTrue(json instanceof ObjectNode);
		ObjectNode jsonObject = (ObjectNode) json;

		Assert.assertTrue(jsonObject.get("events") instanceof ArrayNode);

		ArrayNode<?> jsonArray = (ArrayNode<?>) jsonObject.get("events");

		Assert.assertTrue(jsonArray.size() == 1);

		Assert.assertTrue(jsonArray.get(0) instanceof ObjectNode);
		ObjectNode eventNode = (ObjectNode) jsonArray.get(0);

		Assert.assertFalse(eventNode.get("content") instanceof MissingNode);
		Assert.assertEquals("Today is", eventNode.get("content").toString());

		Assert.assertFalse(eventNode.get("start") instanceof MissingNode);
		Assert.assertEquals("2013-2-14 00,00,00", eventNode.get("start")
				.toString());

		Assert.assertFalse(eventNode.get("end") instanceof MissingNode);
		Assert.assertEquals("2013-2-14 23,59,59", eventNode.get("end")
				.toString());

		Assert.assertFalse(eventNode.get("personalTime") instanceof MissingNode);
		Assert.assertEquals("false", eventNode.get("personalTime").toString());

		Assert.assertFalse(eventNode.get("text") instanceof MissingNode);
		Assert.assertEquals("Today is Monday. ", eventNode.get("text")
				.toString());

		Assert.assertFalse(eventNode.get("timeSpan") instanceof MissingNode);
		Assert.assertEquals("false", eventNode.get("timeSpan").toString());

		String resultString = JsonConverter.json2String(eventNode);
		Assert.assertEquals(
				"{\"content\" : \"Today is\",\"end\" : \"2013-2-14 23,59,59\",\"personalTime\" : \"false\",\"start\" : \"2013-2-14 00,00,00\",\"text\" : \"Today is Monday. \",\"timeSpan\" : \"false\"}",
				resultString);
	}

	@Test
	public void testJson2StringWithMissingNodes() {
		ObjectNode testNode = new ObjectNode();
		ArrayNode<IJsonNode> testArrayNode = new ArrayNode<IJsonNode>();

		ObjectNode testNode1 = new ObjectNode();
		testNode1.put("key1", new TextNode("value1"));
		ObjectNode testNode2 = new ObjectNode();
		testNode2.put("key2", new TextNode("value2"));
		ObjectNode testNode3 = new ObjectNode();
		testNode3.put("key3", new TextNode("value3"));
		IJsonNode testNode4 = new MissingNode();

		testArrayNode.add(testNode1);
		testArrayNode.add(testNode2);
		testArrayNode.add(testNode4);
		testArrayNode.add(testNode3);

		testNode.put("annotations", testArrayNode);

		Assert.assertEquals(
				"{\"annotations\" : [{\"key1\" : \"value1\"},{\"key2\" : \"value2\"},{\"key3\" : \"value3\"}]}",
				JsonConverter.json2String(testNode));

	}

	@Test
	public void testString2Json() {
		String exampleInput = "[{ \"start\": \"2013-2-14 00,00,00\", \"end\": \"2013-2-14 23,59,59\", \"timeSpan\": \"false\", \"personalTime\": \"false\", \"content\": \"Today is\", \"text\": \"Today is Monday. \" }]";

		IJsonNode json = JsonConverter.string2Json(exampleInput);

		Assert.assertNotNull(json);
		Assert.assertTrue(json instanceof ObjectNode);
		ObjectNode jsonObject = (ObjectNode) json;

		Assert.assertTrue(jsonObject.get("events") instanceof ArrayNode);

		ArrayNode<?> jsonArray = (ArrayNode<?>) jsonObject.get("events");

		Assert.assertTrue(jsonArray.size() == 1);

		Assert.assertTrue(jsonArray.get(0) instanceof ObjectNode);
		ObjectNode eventNode = (ObjectNode) jsonArray.get(0);

		Assert.assertFalse(eventNode.get("content") instanceof MissingNode);
		Assert.assertEquals("Today is", eventNode.get("content").toString());

		Assert.assertFalse(eventNode.get("start") instanceof MissingNode);
		Assert.assertEquals("2013-2-14 00,00,00", eventNode.get("start")
				.toString());

		Assert.assertFalse(eventNode.get("end") instanceof MissingNode);
		Assert.assertEquals("2013-2-14 23,59,59", eventNode.get("end")
				.toString());

		Assert.assertFalse(eventNode.get("personalTime") instanceof MissingNode);
		Assert.assertEquals("false", eventNode.get("personalTime").toString());

		Assert.assertFalse(eventNode.get("text") instanceof MissingNode);
		Assert.assertEquals("Today is Monday. ", eventNode.get("text")
				.toString());

		Assert.assertFalse(eventNode.get("timeSpan") instanceof MissingNode);
		Assert.assertEquals("false", eventNode.get("timeSpan").toString());

	}

	@Test
	public void testParseAnnotations() {
		ObjectNode json = new ObjectNode();
		ArrayNode<ObjectNode> annotations = new ArrayNode<ObjectNode>();
		json.put("annotations", annotations);

		ObjectNode sentence1 = new ObjectNode();
		sentence1.put("Sentence", new TextNode("My name is Pawel. "));
		sentence1.put("begin", new TextNode("0"));
		sentence1.put("end", new TextNode("18"));

		ObjectNode sentence2 = new ObjectNode();
		sentence2.put("Sentence", new TextNode("Today is 07/13/2013. "));
		sentence2.put("begin", new TextNode("18"));
		sentence2.put("end", new TextNode("39"));

		ObjectNode token1 = new ObjectNode();
		token1.put("Token", new TextNode("My"));
		token1.put("begin", new TextNode("0"));

		ObjectNode token2 = new ObjectNode();
		token2.put("Token", new TextNode("name"));
		token2.put("begin", new TextNode("3"));

		ObjectNode token3 = new ObjectNode();
		token3.put("Token", new TextNode("is"));
		token3.put("begin", new TextNode("8"));

		ObjectNode token4 = new ObjectNode();
		token4.put("Token", new TextNode("Pawel"));
		token4.put("begin", new TextNode("11"));

		ObjectNode token5 = new ObjectNode();
		token5.put("Token", new TextNode("."));
		token5.put("begin", new TextNode("16"));

		ObjectNode token6 = new ObjectNode();
		token6.put("Token", new TextNode("Today"));
		token6.put("begin", new TextNode("18"));

		ObjectNode token7 = new ObjectNode();
		token7.put("Token", new TextNode("is"));
		token7.put("begin", new TextNode("24"));

		ObjectNode token8 = new ObjectNode();
		token8.put("Token", new TextNode("07/13/2013"));
		token8.put("begin", new TextNode("27"));

		ObjectNode token9 = new ObjectNode();
		token9.put("Token", new TextNode("."));
		token9.put("begin", new TextNode("37"));

		ObjectNode timex = new ObjectNode();
		timex.put("Timex3", new TextNode("07/13/2013"));
		timex.put("begin", new TextNode("27"));
		timex.put("timexValue", new TextNode("13-07-2013"));

		annotations.add(sentence1);
		annotations.add(sentence2);
		annotations.add(token1);
		annotations.add(token2);
		annotations.add(token3);
		annotations.add(token4);
		annotations.add(token5);
		annotations.add(token6);
		annotations.add(token7);
		annotations.add(token8);
		annotations.add(timex);
		annotations.add(token9);

		List<Sentence2> sentences = JsonConverter.parseAnnotations(json);

		Assert.assertTrue(sentences.size() == 2);
		Sentence2 s1 = sentences.get(0);
		Sentence2 s2 = sentences.get(1);
		Assert.assertEquals("My name is Pawel. ", s1.getSentenceText());
		Assert.assertEquals(5, s1.getTokens().size());
		Assert.assertEquals(0, s1.getTimexs().size());

		Assert.assertEquals("Today is 07/13/2013. ", s2.getSentenceText());
		Assert.assertEquals(4, s2.getTokens().size());
		Assert.assertEquals(1, s2.getTimexs().size());
	}

	@Test
	public void testEventNode2Sentence() {
		String exampleInput = "[{ \"start\": \"2013-2-14 00,00,00\", \"end\": \"2013-2-14 23,59,59\", \"timeSpan\": \"false\", \"personalTime\": \"false\", \"content\": \"Today is\", \"text\": \"Today is Monday. \" }]";

		IJsonNode json = JsonConverter.string2Json(exampleInput);
		Assert.assertNotNull(json);
		Assert.assertTrue(json instanceof ObjectNode);
		ObjectNode jsonObject = (ObjectNode) json;
		Assert.assertTrue(jsonObject.get("events") instanceof ArrayNode);
		ArrayNode<?> jsonArray = (ArrayNode<?>) jsonObject.get("events");
		Assert.assertTrue(jsonArray.size() == 1);
		Assert.assertTrue(jsonArray.get(0) instanceof ObjectNode);
		ObjectNode eventNode = (ObjectNode) jsonArray.get(0);

		Sentence2 s = JsonConverter.eventNode2Sentence(eventNode);

		Assert.assertEquals("Today is", s.getSentenceText());
		Assert.assertTrue(s.getTimexs() != null && s.getTimexs().size() == 1);
		Timex3 t = s.getTimexs().get(0);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Assert.assertEquals("20130214235959", sdf.format(t.getDate()));
	}

	@Test
	public void testString2JsonNode() {
		String input = "[{\"key11\":\"value11\", \"key12\":\"value12\"},"
				+ "{\"key21\":\"value21\", \"key22\":\"value22\"}]";
		ArrayNode<IJsonNode> expectedResult = new ArrayNode<IJsonNode>();
		ObjectNode node1 = new ObjectNode();
		node1.put("key11", new TextNode("value11"));
		node1.put("key12", new TextNode("value12"));
		ObjectNode node2 = new ObjectNode();
		node2.put("key21", new TextNode("value21"));
		node2.put("key22", new TextNode("value22"));
		expectedResult.add(node1).add(node2);

		Assert.assertEquals(expectedResult,
				JsonConverter.string2JsonNode(input));
	}
}
