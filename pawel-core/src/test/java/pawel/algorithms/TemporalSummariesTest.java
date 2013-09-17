/**
 * 
 */
package pawel.algorithms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import pawel.model.Sentence2;
import pawel.model.Timex3;

/**
 * Test class for class {@link TemporalSummaries}.
 * 
 * @author ptondryk
 * @see TemporalSummaries
 * 
 */
public class TemporalSummariesTest {

	@Test
	public void testRankSentences() {
		Sentence2 s1 = new Sentence2();
		s1.setSentenceText("Today is monday.");
		Timex3 t1 = new Timex3();
		t1.setDate(new Date());
		s1.getTimexs().add(t1);

		Sentence2 s2 = new Sentence2();
		s2.setSentenceText("Today is monday.");

		Sentence2 s3 = new Sentence2();
		s3.setSentenceText("Today is my birthday.");
		Timex3 t2 = new Timex3();
		t2.setDate(new Date((new Date()).getTime() - (1000l * 60 * 60 * 24)));
		s3.getTimexs().add(t2);

		List<Sentence2> sentences = new ArrayList<Sentence2>();
		sentences.add(s1);
		sentences.add(s2);
		sentences.add(s3);

		Map<Sentence2, Double> rank = TemporalSummaries.rankSentences(
				sentences,
				"Today is monday. Today is monday. Today is my birthday.");

		Assert.assertEquals(3, rank.size());

		double rankSentence1 = rank.get(s1);
		double rankSentence2 = rank.get(s2);
		double rankSentence3 = rank.get(s3);

		Assert.assertTrue(rankSentence1 >= 0.0 && rankSentence2 >= 0.0
				&& rankSentence3 >= 0.0);

		// 1st and 2nd sentence are equal - that means same usefulness but
		// because sentence1 is before sentence2, sentence1 has higher novelty,
		// and therefore higher rank
		Assert.assertTrue(rankSentence1 > rankSentence2);

		// 3rd sentence contains words that occur also in other sentences and at
		// the same time it should have high novelty - novelty of 2nd sentence
		// should be low, therefore 3rd sentence should have higher rank then
		// 2nd sentence
		Assert.assertTrue(rankSentence3 > rankSentence2);
	}
}
