/**
 * 
 */
package pawel.algorithms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pawel.model.Sentence2;
import pawel.utils.SentencesSelector;

/**
 * Implementation of TSnippets algorithm.
 * 
 * @author pawel
 * 
 */
public class TSnippets {

	/**
	 * Method generates (given parameters) the summary text.
	 * 
	 * @param anotations
	 *            sentences to build summary from
	 * @param sentenceNum
	 *            amount of sentences that should be used to build summary
	 * @param theta
	 * @param epsilon
	 * @param gamma
	 * @param beta
	 * @param alpha
	 * @param query
	 *            query (if using query dependent TSnippet approche, query
	 *            should be <code>null</code> if not generating query dependent
	 *            snippet)
	 * @return summary as string
	 */
	public static String generateSummary(List<Sentence2> anotations,
			Double alpha, Double beta, Double gamma, Double epsilon,
			Double theta, String query, Integer sentenceNum) {
		Map<Sentence2, Double> sentenceRanking = TSnippets
				.calculateSentencesRank(anotations, alpha, beta, gamma,
						epsilon, theta, query, sentenceNum);
		return SentencesSelector.listToTimeSortedString(SentencesSelector
				.selectSentencesWithHighestRank(sentenceRanking, sentenceNum));

	}

	/**
	 * 
	 * @param sentences
	 *            sentences to build summary from
	 * @param sentenceNum
	 *            amount of sentences that should be used to build summary
	 * @param theta
	 * @param epsilon
	 * @param gamma
	 * @param beta
	 * @param alpha
	 * @param query
	 *            query (if using query dependent TSnippet approche, query
	 *            should be <code>null</code> if not generating query dependent
	 *            snippet)
	 * @return map<Sentence, rank>
	 */
	public static Map<Sentence2, Double> calculateSentencesRank(
			List<Sentence2> sentences, Double alpha, Double beta, Double gamma,
			Double epsilon, Double theta, String query, Integer sentenceNum) {
		Map<Sentence2, Double> sentenceRanking = new HashMap<Sentence2, Double>();

		int countTimexInDocument = 0;
		for (int sentencePosition = 0; sentencePosition < sentences.size(); sentencePosition++) {
			Sentence2 sentence = sentences.get(sentencePosition);
			countTimexInDocument += sentence.getTimexs().size();
		}

		for (int sentencePosition = 0; sentencePosition < sentences.size(); sentencePosition++) {
			Sentence2 sentence = sentences.get(sentencePosition);

			sentenceRanking.put(sentence, TSnippets.calculateSentenceRank(
					sentence, countTimexInDocument, sentencePosition, alpha,
					beta, gamma, epsilon, theta, query, sentenceNum));
		}

		return sentenceRanking;
	}

	/**
	 * Method calculates rank for single given <b>sentence</b>.
	 * 
	 * @param sentence
	 * @param countTimexInDocument
	 *            amount of timex in document
	 * @param sentencePosition
	 *            position at which given <b>sentence</b> occures in text
	 * @param sentenceNum
	 *            amount of sentences that should be used to build summary
	 * @param query
	 *            query (if using query dependent TSnippet approche, query
	 *            should be <code>null</code> if not generating query dependent
	 *            snippet)
	 * @param theta
	 * @param epsilon
	 * @param gamma
	 * @param beta
	 * @param alpha
	 * @return rank of given <b>sentence</b>
	 */
	public static double calculateSentenceRank(Sentence2 sentence,
			int countTimexInDocument, int sentencePosition, Double alpha,
			Double beta, Double gamma, Double epsilon, Double theta,
			String query, Integer sentenceNum) {
		double rank = 0.0d;

		if (!sentence.getTimexs().isEmpty() && !sentence.getTokens().isEmpty()) {
			rank = alpha * sentence.getTimexs().size()
					/ (double) sentence.getTokens().size() - beta
					* (double) sentencePosition + gamma
					* (double) sentence.getTokens().size() + epsilon
					* (double) sentence.getTimexs().size() + theta
					* (double) countTimexInDocument;
			if (query != null && !query.isEmpty()) {
				rank += CosineSimilarity.calculateCosineSimilarity(
						sentence.getSentenceText(), query);
			}
		}

		return rank;
	}

}
