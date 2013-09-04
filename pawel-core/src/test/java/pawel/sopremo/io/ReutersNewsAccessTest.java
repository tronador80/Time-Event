/**
 * 
 */
package pawel.sopremo.io;

import org.junit.Test;

import eu.stratosphere.sopremo.expressions.ConstantExpression;
import eu.stratosphere.sopremo.testing.SopremoTestPlan;
import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.ObjectNode;
import eu.stratosphere.sopremo.type.TextNode;

/**
 * Test class for {@link ReutersNewsAccess}.
 * 
 * @author ptondryk
 * 
 */
public class ReutersNewsAccessTest {

	/**
	 * checks whether Reuters News are read correctly from xml files
	 */
	@Test
	public void testFileSystemReutersNewsFile() {

		final SopremoTestPlan sopremoPlan = new SopremoTestPlan(0, 1);

		final ReutersNewsAccess reutersNews = new ReutersNewsAccess();
		reutersNews.setDocumentName(new ConstantExpression(System
				.getProperty("user.dir")
				+ "/src/test/resources/test_reuters_news"));

		sopremoPlan.getOutputOperator(0).setInputs(reutersNews);

		// prepare expected output
		// first expected JSON node
		ObjectNode out1 = new ObjectNode();
		ArrayNode<ObjectNode> annotationsArray1 = new ArrayNode<ObjectNode>();
		ObjectNode textAnnotation1 = new ObjectNode();

		textAnnotation1
				.put("Text",
						new TextNode(
								"China said on Thursday it was hopeful a global nuclear test ban treaty could be approved by the U.N. General Assembly before the end of 1996, despite India's move this week to block the pact. 'China hopes that the treaty could be open for signature by the end of the year and that there would be universal accession to and compliance with it by all countries,' a Foreign Ministry spokesman said. The spokesman declined to comment directly on India's decision on Tuesday to block the Comprehensive Test Ban Treaty (CTBT) at the Conference on Disarmament in Geneva. New Delhi's stance, which was seen as effectively blocking 2-1/2 years of negotiations at the Conference on Disarmament, drew widespread but generally muted criticism. India has also pledged to oppose any forwarding of the draft treaty to the General Assembly. China has pledged support for the pact. 'The draft of the CTBT basically reflects the objective way the negotiations were conducted and although there are still some places in the draft which are unsatisfactory, its overall content is balanced in general,' the spokesman said. 'China can agree that the conference adopt this draft and submit it to the U.N. General Assembly for consideration,' he said. On July 29 China held what it said would be its last nuclear test before a self-imposed moratorium that took effect the following day. China was the last declared nuclear power to announce a halt to testing. 'The conclusion of the CTBT will be conducive to nuclear disarmament and nuclear non-proliferation,' the spokesman said."));
		textAnnotation1.put("byline", new TextNode(""));
		textAnnotation1.put("copyright", new TextNode(
				"(c) Reuters Limited 1996"));
		textAnnotation1.put("date", new TextNode("19960822000000"));
		textAnnotation1.put("dateline", new TextNode("BEIJING 1996-08-22"));
		textAnnotation1.put("headline", new TextNode(
				"China says hopeful on global nuclear test ban."));
		textAnnotation1.put("id", new TextNode("root"));
		textAnnotation1.put("itemId", new TextNode("10000"));
		textAnnotation1.put("lang", new TextNode("en"));
		textAnnotation1.put("title", new TextNode(
				"CHINA: China says hopeful on global nuclear test ban."));

		annotationsArray1.add(textAnnotation1);
		out1.put("annotations", annotationsArray1);

		// second expected JSON node
		ObjectNode out2 = new ObjectNode();
		ArrayNode<ObjectNode> annotationsArray2 = new ArrayNode<ObjectNode>();
		ObjectNode textAnnotation2 = new ObjectNode();

		textAnnotation2
				.put("Text",
						new TextNode(
								"Ringgit ended lower in Kuala Lumpur, but the losses were limited by technical support at 2.4950 per dollar level, dealers said on Thursday. They said the ringgit was likely to remain weak on covering of short dollar positions in the near term. Unwinding of ringgit/Sing was likely to depress sentiment further. 'Sentiment is generally weak for the ringgit. People believe it has become expensive to swap into ringgit, though its yields are still higher,' a local bank dealer said. He said there was also some cross play against yen, which contributed to the ringgit's weakening.   Dealers said ringgit was also affected by fear that the U.S. could impose sanctions against Malaysia because Petronas, the Malaysian oil company, has bought into an Iranian oil field. 'There is reaction to the possibility of sanctions against Malaysia,' said a dealer with a U.S. bank in Singapore. He said there was support for the ringgit at 2.4950 per dollar, though cross play against Sing and yen was threatening to break that support level. At 0900 GMT, the ringgit was at 2.4945/55 per dollar against 2.4926/31 on Wednesday. The Singapore dollar was at 0.5649/55 against 0.5661/68. - Madhav Reddy (603-230 8911)"));
		textAnnotation2.put("byline", new TextNode(""));
		textAnnotation2.put("copyright", new TextNode(
				"(c) Reuters Limited 1996"));
		textAnnotation2.put("date", new TextNode("19960822000000"));
		textAnnotation2
				.put("dateline", new TextNode("KUALA LUMPUR 1996-08-22"));
		textAnnotation2.put("headline", new TextNode(
				"Ringgit losses limited by technicals, seen down."));
		textAnnotation2.put("id", new TextNode("root"));
		textAnnotation2.put("itemId", new TextNode("10002"));
		textAnnotation2.put("lang", new TextNode("en"));
		textAnnotation2.put("title", new TextNode(
				"MALAYSIA: Ringgit losses limited by technicals, seen down."));

		annotationsArray2.add(textAnnotation2);
		out2.put("annotations", annotationsArray2);

		sopremoPlan.getExpectedOutput(0).add(out1).add(out2);

		sopremoPlan.run();
	}

	/**
	 * checks whether Reuters News are read correctly from big xml files (big
	 * are files that contains more then one news, it is necessary feature
	 * because HDFS can not handle big amount of small files well)
	 */
	@Test
	public void testFileSystemReutersNewsFileWithBigFiles() {

		final SopremoTestPlan sopremoPlan = new SopremoTestPlan(0, 1);

		final ReutersNewsAccess reutersNews = new ReutersNewsAccess();
		reutersNews.setDocumentName(new ConstantExpression(System
				.getProperty("user.dir")
				+ "/src/test/resources/test_reuters_news_big"));
		reutersNews.setBig();

		sopremoPlan.getOutputOperator(0).setInputs(reutersNews);

		// prepare expected output
		// first expected JSON node
		ObjectNode out1 = new ObjectNode();
		ArrayNode<ObjectNode> annotationsArray1 = new ArrayNode<ObjectNode>();
		ObjectNode textAnnotation1 = new ObjectNode();

		textAnnotation1
				.put("Text",
						new TextNode(
								"China said on Thursday it was hopeful a global nuclear test ban treaty could be approved by the U.N. General Assembly before the end of 1996, despite India's move this week to block the pact. 'China hopes that the treaty could be open for signature by the end of the year and that there would be universal accession to and compliance with it by all countries,' a Foreign Ministry spokesman said. The spokesman declined to comment directly on India's decision on Tuesday to block the Comprehensive Test Ban Treaty (CTBT) at the Conference on Disarmament in Geneva. New Delhi's stance, which was seen as effectively blocking 2-1/2 years of negotiations at the Conference on Disarmament, drew widespread but generally muted criticism. India has also pledged to oppose any forwarding of the draft treaty to the General Assembly. China has pledged support for the pact. 'The draft of the CTBT basically reflects the objective way the negotiations were conducted and although there are still some places in the draft which are unsatisfactory, its overall content is balanced in general,' the spokesman said. 'China can agree that the conference adopt this draft and submit it to the U.N. General Assembly for consideration,' he said. On July 29 China held what it said would be its last nuclear test before a self-imposed moratorium that took effect the following day. China was the last declared nuclear power to announce a halt to testing. 'The conclusion of the CTBT will be conducive to nuclear disarmament and nuclear non-proliferation,' the spokesman said."));
		textAnnotation1.put("byline", new TextNode(""));
		textAnnotation1.put("copyright", new TextNode(
				"(c) Reuters Limited 1996"));
		textAnnotation1.put("date", new TextNode("19960822000000"));
		textAnnotation1.put("dateline", new TextNode("BEIJING 1996-08-22"));
		textAnnotation1.put("headline", new TextNode(
				"China says hopeful on global nuclear test ban."));
		textAnnotation1.put("id", new TextNode("root"));
		textAnnotation1.put("itemId", new TextNode("10000"));
		textAnnotation1.put("lang", new TextNode("en"));
		textAnnotation1.put("title", new TextNode(
				"CHINA: China says hopeful on global nuclear test ban."));

		annotationsArray1.add(textAnnotation1);
		out1.put("annotations", annotationsArray1);

		// second expected JSON node
		ObjectNode out2 = new ObjectNode();
		ArrayNode<ObjectNode> annotationsArray2 = new ArrayNode<ObjectNode>();
		ObjectNode textAnnotation2 = new ObjectNode();

		textAnnotation2
				.put("Text",
						new TextNode(
								"Ringgit ended lower in Kuala Lumpur, but the losses were limited by technical support at 2.4950 per dollar level, dealers said on Thursday. They said the ringgit was likely to remain weak on covering of short dollar positions in the near term. Unwinding of ringgit/Sing was likely to depress sentiment further. 'Sentiment is generally weak for the ringgit. People believe it has become expensive to swap into ringgit, though its yields are still higher,' a local bank dealer said. He said there was also some cross play against yen, which contributed to the ringgit's weakening.   Dealers said ringgit was also affected by fear that the U.S. could impose sanctions against Malaysia because Petronas, the Malaysian oil company, has bought into an Iranian oil field. 'There is reaction to the possibility of sanctions against Malaysia,' said a dealer with a U.S. bank in Singapore. He said there was support for the ringgit at 2.4950 per dollar, though cross play against Sing and yen was threatening to break that support level. At 0900 GMT, the ringgit was at 2.4945/55 per dollar against 2.4926/31 on Wednesday. The Singapore dollar was at 0.5649/55 against 0.5661/68. - Madhav Reddy (603-230 8911)"));
		textAnnotation2.put("byline", new TextNode(""));
		textAnnotation2.put("copyright", new TextNode(
				"(c) Reuters Limited 1996"));
		textAnnotation2.put("date", new TextNode("19960822000000"));
		textAnnotation2
				.put("dateline", new TextNode("KUALA LUMPUR 1996-08-22"));
		textAnnotation2.put("headline", new TextNode(
				"Ringgit losses limited by technicals, seen down."));
		textAnnotation2.put("id", new TextNode("root"));
		textAnnotation2.put("itemId", new TextNode("10002"));
		textAnnotation2.put("lang", new TextNode("en"));
		textAnnotation2.put("title", new TextNode(
				"MALAYSIA: Ringgit losses limited by technicals, seen down."));

		annotationsArray2.add(textAnnotation2);
		out2.put("annotations", annotationsArray2);

		// third expected JSON node
		ObjectNode out3 = new ObjectNode();
		ArrayNode<ObjectNode> annotationsArray3 = new ArrayNode<ObjectNode>();
		ObjectNode textAnnotation3 = new ObjectNode();

		textAnnotation3
				.put("Text",
						new TextNode(
								"China said on Thursday it was hopeful a global nuclear test ban treaty could be approved by the U.N. General Assembly before the end of 1996, despite India's move this week to block the pact. 'China hopes that the treaty could be open for signature by the end of the year and that there would be universal accession to and compliance with it by all countries,' a Foreign Ministry spokesman said. The spokesman declined to comment directly on India's decision on Tuesday to block the Comprehensive Test Ban Treaty (CTBT) at the Conference on Disarmament in Geneva. New Delhi's stance, which was seen as effectively blocking 2-1/2 years of negotiations at the Conference on Disarmament, drew widespread but generally muted criticism. India has also pledged to oppose any forwarding of the draft treaty to the General Assembly. China has pledged support for the pact. 'The draft of the CTBT basically reflects the objective way the negotiations were conducted and although there are still some places in the draft which are unsatisfactory, its overall content is balanced in general,' the spokesman said. 'China can agree that the conference adopt this draft and submit it to the U.N. General Assembly for consideration,' he said. On July 29 China held what it said would be its last nuclear test before a self-imposed moratorium that took effect the following day. China was the last declared nuclear power to announce a halt to testing. 'The conclusion of the CTBT will be conducive to nuclear disarmament and nuclear non-proliferation,' the spokesman said."));
		textAnnotation3.put("byline", new TextNode(""));
		textAnnotation3.put("copyright", new TextNode(
				"(c) Reuters Limited 1996"));
		textAnnotation3.put("date", new TextNode("19960822000000"));
		textAnnotation3.put("dateline", new TextNode("BEIJING 1996-08-22"));
		textAnnotation3.put("headline", new TextNode(
				"China says hopeful on global nuclear test ban."));
		textAnnotation3.put("id", new TextNode("root"));
		textAnnotation3.put("itemId", new TextNode("10000"));
		textAnnotation3.put("lang", new TextNode("en"));
		textAnnotation3.put("title", new TextNode(
				"CHINA: China says hopeful on global nuclear test ban."));

		annotationsArray3.add(textAnnotation3);
		out3.put("annotations", annotationsArray3);

		// fourth expected JSON node
		ObjectNode out4 = new ObjectNode();
		ArrayNode<ObjectNode> annotationsArray4 = new ArrayNode<ObjectNode>();
		ObjectNode textAnnotation4 = new ObjectNode();

		textAnnotation4
				.put("Text",
						new TextNode(
								"Ringgit ended lower in Kuala Lumpur, but the losses were limited by technical support at 2.4950 per dollar level, dealers said on Thursday. They said the ringgit was likely to remain weak on covering of short dollar positions in the near term. Unwinding of ringgit/Sing was likely to depress sentiment further. 'Sentiment is generally weak for the ringgit. People believe it has become expensive to swap into ringgit, though its yields are still higher,' a local bank dealer said. He said there was also some cross play against yen, which contributed to the ringgit's weakening.   Dealers said ringgit was also affected by fear that the U.S. could impose sanctions against Malaysia because Petronas, the Malaysian oil company, has bought into an Iranian oil field. 'There is reaction to the possibility of sanctions against Malaysia,' said a dealer with a U.S. bank in Singapore. He said there was support for the ringgit at 2.4950 per dollar, though cross play against Sing and yen was threatening to break that support level. At 0900 GMT, the ringgit was at 2.4945/55 per dollar against 2.4926/31 on Wednesday. The Singapore dollar was at 0.5649/55 against 0.5661/68. - Madhav Reddy (603-230 8911)"));
		textAnnotation4.put("byline", new TextNode(""));
		textAnnotation4.put("copyright", new TextNode(
				"(c) Reuters Limited 1996"));
		textAnnotation4.put("date", new TextNode("19960822000000"));
		textAnnotation4
				.put("dateline", new TextNode("KUALA LUMPUR 1996-08-22"));
		textAnnotation4.put("headline", new TextNode(
				"Ringgit losses limited by technicals, seen down."));
		textAnnotation4.put("id", new TextNode("root"));
		textAnnotation4.put("itemId", new TextNode("10002"));
		textAnnotation4.put("lang", new TextNode("en"));
		textAnnotation4.put("title", new TextNode(
				"MALAYSIA: Ringgit losses limited by technicals, seen down."));

		annotationsArray4.add(textAnnotation4);
		out4.put("annotations", annotationsArray4);

		sopremoPlan.getExpectedOutput(0).add(out1).add(out2).add(out3)
				.add(out4);

		sopremoPlan.run();
	}
}
