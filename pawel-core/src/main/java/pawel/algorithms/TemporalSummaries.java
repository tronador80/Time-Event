/**
 * 
 */
package pawel.algorithms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pawel.model.Sentence;
import pawel.utils.TextUtils;

/**
 * Method calculates temporal summaries based on algorithm from
 * "Temporal Summaries of News Topics" paper.
 * 
 * @author ptondryk
 * 
 */
public class TemporalSummaries {

	/**
	 * Method calculates rank for every sentences.
	 * 
	 * @param sentences
	 *            list of all sentences
	 * @param completeText
	 *            text where the sentences come from
	 * @return map<Sentence, rank>
	 */
	public static Map<Sentence, Double> rankSentences(
			List<Sentence> sentences, String completeText) {

		Map<Sentence, Double> sentenceRanking = new HashMap<Sentence, Double>();

		int completeTextLength = completeText.split(" ").length;
		for (int k = 0; k < sentences.size(); k++) {
			Sentence sentence = sentences.get(k);
			String[] sentenceWords = sentence.getSentenceText().split(" ");
			// calculate usefulness
			double usefulness = 1.0d;
			for (String word : sentenceWords) {
				usefulness *= ((TextUtils.countOccurrences(word, completeText) + 0.01) / (1.01d * completeTextLength));
			}
			usefulness = Math.pow(usefulness,
					1.0 / Math.max(sentenceWords.length, 1));

			// calculate novelty
			double novelty = 1.0d;
			for (int i = 0; i < k; i++) {
				String previousSentence = sentences.get(i).getSentenceText();
				double tmp = 1.0d;
				for (String word : previousSentence.split(" ")) {
					tmp *= ((TextUtils.countOccurrences(word,
							sentence.getSentenceText()) + 0.01) / (1.01d * completeTextLength));
				}
				tmp = Math.pow(tmp, 1.0 / Math.max(sentenceWords.length, 1));
				tmp = 1.0d - tmp;
				tmp = Math.pow(tmp, 1.0 / Math.max(k - 1, 1));
				novelty *= tmp;
			}
			sentenceRanking.put(sentence, usefulness * novelty);
		}

		return sentenceRanking;
	}
}
