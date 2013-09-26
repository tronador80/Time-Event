/**
 * 
 */
package pawel.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import pawel.model.Sentence;
import pawel.model.Timex3;

/**
 * @author ptondryk
 * 
 */
public class SentencesSelector {

	/**
	 * This method selects from <b>sentenceRanking</b> <b>sentenceNum</b>
	 * sentences with highest ranking.
	 * 
	 * @param sentenceRanking
	 *            map<Sentence, rank>
	 * @param sentenceNum
	 *            amount of senteces to select
	 * @return list of <b>sentenceNum</b> sentences from <b>sentenceRanking</b>
	 *         with highest ranking
	 */
	public static List<Sentence> selectSentencesWithHighestRank(
			Map<Sentence, Double> sentenceRanking, Integer sentenceNum) {
		List<Sentence> summary = new ArrayList<Sentence>();

		Sentence currentSentence = null;
		int amountSentences = sentenceRanking.size();
		for (int i = 0; i < Math.min(sentenceNum, amountSentences); i++) {
			double rankingLimit = 0.0d;

			for (Sentence sentence : sentenceRanking.keySet()) {
				double rank = sentenceRanking.get(sentence);

				if (currentSentence == null || rank >= rankingLimit) {
					rankingLimit = rank;
					currentSentence = sentence;
				}
			}

			// check if sentence with same text is already contained in summary
			boolean isContained = false;
			for (Sentence sent : summary) {
				if (sent.getSentenceText().equals(
						currentSentence.getSentenceText())) {
					isContained = true;
					break;
				}
			}
			if (isContained) {
				i--;
				continue;
			}

			if (sentenceRanking.remove(currentSentence) == 0.0) {
				break; // if rank = 0 break because rank = 0 have sentences
						// without temporal expressions
			}
			summary.add(currentSentence);
		}

		return summary;

	}

	/**
	 * Method changes given single sentences into one string. Sentences in this
	 * string are time sorted. That means they are sorted according to Timex
	 * contained in this sentences.
	 * 
	 * @param sentencesWithHighestRank
	 *            sentences to connect into one string.
	 * @return sentences connected into one string
	 */
	public static String listToTimeSortedString(
			List<Sentence> sentencesWithHighestRank) {
		Collections.sort(sentencesWithHighestRank, new Comparator<Sentence>() {

			@Override
			public int compare(Sentence s1, Sentence s2) {
				Timex3 sentence1FirstTimex = null;
				for (Timex3 timex : s1.getTimexs()) {
					if (sentence1FirstTimex == null
							|| (sentence1FirstTimex.getDate() != null && sentence1FirstTimex
									.getDate().compareTo(timex.getDate()) < 0)) {
						sentence1FirstTimex = timex;
					}
				}
				Timex3 sentence2FirstTimex = null;
				for (Timex3 timex : s2.getTimexs()) {
					if (sentence2FirstTimex == null
							|| (sentence2FirstTimex.getDate() != null && sentence2FirstTimex
									.getDate().compareTo(timex.getDate()) < 0)) {
						sentence2FirstTimex = timex;
					}
				}

				if ((sentence1FirstTimex == null || sentence1FirstTimex
						.getDate() == null)
						&& (sentence2FirstTimex != null && sentence2FirstTimex
								.getDate() != null)) {
					return 1;
				} else if ((sentence1FirstTimex != null && sentence1FirstTimex
						.getDate() != null)
						&& (sentence2FirstTimex == null || sentence2FirstTimex
								.getDate() == null)) {
					return -1;
				} else if ((sentence1FirstTimex == null || sentence1FirstTimex
						.getDate() == null)
						&& (sentence2FirstTimex == null || sentence2FirstTimex
								.getDate() == null)) {
					return 0;
				} else {
					return sentence1FirstTimex.getDate().compareTo(
							sentence2FirstTimex.getDate());
				}
			}
		});

		String res = "";
		for (Sentence sentence : sentencesWithHighestRank) {
			if (!res.isEmpty()) {
				res += " ";
			}
			res += sentence.getSentenceText();
		}
		return res;
	}
}
