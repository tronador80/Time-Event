/**
 * 
 */
package pawel.algorithms;

import pawel.utils.TextUtils;

/**
 * Class provides methods for calculating cosine similarity.
 * 
 * @author pawel
 * 
 */
public class CosineSimilarity {

	/**
	 * Method calulates the cosine similarity. Compares <b>sentence</b> with
	 * <b>query</b>.
	 * 
	 * @param sentence
	 * @param query
	 * @return cosine similarity as double value
	 */
	public static double calculateCosineSimilarity(String sentence, String query) {
		double similarity = 0.0;
		int countWordsInQuery = 0;
		String normalizedQuery = TextUtils.removeDuplicateWordsFromQuery(query);
		for (String queryWord : normalizedQuery.split(" ")) {
			similarity += (double) TextUtils.countOccurrences(queryWord,
					sentence);
			countWordsInQuery++;
		}

		double norm = 0.0;
		String nomalizedSentence = TextUtils
				.removeDuplicateWordsFromQuery(sentence);
		for (String sentenceWord : nomalizedSentence.split(" ")) {
			norm += Math.pow(
					TextUtils.countOccurrences(sentenceWord, sentence), 2);
		}

		if (norm == 0 || countWordsInQuery == 0) {
			return 0;
		}
		return similarity
				/ (Math.sqrt(norm) * Math.sqrt((double) countWordsInQuery));
	}

}
