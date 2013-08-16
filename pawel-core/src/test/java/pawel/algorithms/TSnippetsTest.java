/**
 * 
 */
package pawel.algorithms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import pawel.model.Sentence2;
import pawel.model.Timex3;
import pawel.model.Token;

/**
 * Test class for class {@link TSnippets}.
 * 
 * @author pawel
 * @see TSnippets
 */
public class TSnippetsTest {

	static List<Sentence2> sentences;

	@BeforeClass
	public static void initSentences() {
		Sentence2 s1 = new Sentence2();
		s1.setSentenceText("Today is monday.");
		Timex3 t1 = new Timex3();
		t1.setDate(new Date());
		s1.getTimexs().add(t1);
		s1.getTokens().add(new Token());
		s1.getTokens().add(new Token());
		s1.getTokens().add(new Token());

		Sentence2 s2 = new Sentence2();
		s2.setSentenceText("I don't like monday.");
		s2.getTokens().add(new Token());
		s2.getTokens().add(new Token());
		s2.getTokens().add(new Token());
		s2.getTokens().add(new Token());

		Sentence2 s3 = new Sentence2();
		s3.setSentenceText("Yesterday was sunday.");
		Timex3 t2 = new Timex3();
		t2.setDate(new Date((new Date()).getTime() - (1000l * 60 * 60 * 24)));
		s3.getTimexs().add(t2);
		s3.getTokens().add(new Token());
		s3.getTokens().add(new Token());
		s3.getTokens().add(new Token());

		TSnippetsTest.sentences = new ArrayList<Sentence2>();
		TSnippetsTest.sentences.add(s1);
		TSnippetsTest.sentences.add(s2);
		TSnippetsTest.sentences.add(s3);
	}

	@Test
	public void testGenerateSummary() {
		String summary1 = TSnippets.generateSummary(TSnippetsTest.sentences,
				0.1d, 0.1d, 0.1d, 0.1d, 0.1d, null, 2);
		String summary2 = TSnippets.generateSummary(TSnippetsTest.sentences,
				0.1d, 0.1d, 0.1d, 0.1d, 0.1d, "Yesterday was sunday", 1);

		Assert.assertNotNull(summary1);
		Assert.assertNotNull(summary2);
		Assert.assertTrue(summary1.split("\\.").length == 2); // summary should
																// contain two
																// sentences
		Assert.assertTrue(summary2.contains("Yesterday was sunday")); // exact
																		// query
																		// matching
	}

	@Test
	public void testCalculateSentencesRank() {

		Map<Sentence2, Double> rank = TSnippets.calculateSentencesRank(
				TSnippetsTest.sentences, 0.1d, 0.1d, 0.1d, 0.1d, 0.1d,
				"Yesterday was sunday", 1);

		Assert.assertTrue(rank.size() == 3);
		Assert.assertTrue(rank.get(TSnippetsTest.sentences.get(0)) > 0);
		Assert.assertTrue(rank.get(TSnippetsTest.sentences.get(1)) == 0);
		Assert.assertTrue(rank.get(TSnippetsTest.sentences.get(2)) > 0);
	}

	@Test
	public void testCalculateSentenceRank1() {

		double rank1 = TSnippets.calculateSentenceRank(
				TSnippetsTest.sentences.get(0), 2, 1, 0.1d, 0.1d, 0.1d, 0.1d,
				0.1d, "Yesterday was sunday", 1);
		double rank2 = TSnippets.calculateSentenceRank(
				TSnippetsTest.sentences.get(2), 2, 1, 0.1d, 0.1d, 0.1d, 0.1d,
				0.1d, "Yesterday was sunday", 1);

		Assert.assertTrue(rank1 < rank2);
	}

	@Test
	public void testCalculateSentenceRank2() {
		double rank1 = TSnippets.calculateSentenceRank(
				TSnippetsTest.sentences.get(0), 2, 0, 0.1d, 0.1d, 0.1d, 0.1d,
				0.1d, null, 1);
		double rank2 = TSnippets.calculateSentenceRank(
				TSnippetsTest.sentences.get(2), 2, 2, 0.1d, 0.1d, 0.1d, 0.1d,
				0.1d, null, 1);

		Assert.assertTrue(rank1 > rank2);
	}

}
