/**
 * 
 */
package pawel.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import pawel.model.Sentence;
import pawel.model.Timex3;

/**
 * Test class for {@link SentencesSelector}
 * 
 * @author ptondryk
 * @see SentencesSelector
 */
public class SentenceSelectorTest {

	@Test
	public void testSelectSentencesWithHighestRank() {
		Sentence s1 = new Sentence();
		s1.setSentenceText("Today is monday.");
		Timex3 t1 = new Timex3();
		t1.setDate(new Date());
		s1.getTimexs().add(t1);

		Sentence s2 = new Sentence();
		s2.setSentenceText("Yesterday was sunday.");
		Timex3 t2 = new Timex3();
		t2.setDate(new Date((new Date()).getTime() - (1000l * 60 * 60 * 24)));
		s2.getTimexs().add(t2);

		Map<Sentence, Double> sentenceRanking = new HashMap<Sentence, Double>();
		sentenceRanking.put(s1, 0.5);
		sentenceRanking.put(s2, 1.0);

		List<Sentence> sentences = SentencesSelector
				.selectSentencesWithHighestRank(sentenceRanking, 1);

		Assert.assertNotNull(sentences);
		Assert.assertTrue(sentences.size() == 1);
		Assert.assertEquals(s2, sentences.get(0));
	}

	@Test
	public void testListToTimeSortedString() {
		Sentence s1 = new Sentence();
		s1.setSentenceText("Today is monday.");
		Timex3 t1 = new Timex3();
		t1.setDate(new Date());
		s1.getTimexs().add(t1);

		Sentence s2 = new Sentence();
		s2.setSentenceText("Yesterday was sunday.");
		Timex3 t2 = new Timex3();
		t2.setDate(new Date((new Date()).getTime() - (1000l * 60 * 60 * 24)));
		s2.getTimexs().add(t2);

		List<Sentence> sentences = new ArrayList<Sentence>();
		sentences.add(s1);
		sentences.add(s2);

		String summary = SentencesSelector.listToTimeSortedString(sentences);

		Assert.assertNotNull(summary);
		Assert.assertTrue(summary.contains(s1.getSentenceText()));
		Assert.assertTrue(summary.contains(s2.getSentenceText()));
		Assert.assertEquals("Yesterday was sunday. Today is monday.", summary);

	}
}
