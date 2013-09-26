/**
 * 
 */
package pawel.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ptondryk
 * 
 */
public class TextUtils {

	/**
	 * English stop words (this set comes from Apache Lucene project
	 * {@link org.apache.lucene.analysis.core.StopAnalyzer})
	 */
	private static final List<String> stopWords = Arrays.asList("a", "an",
			"and", "are", "as", "at", "be", "but", "by", "for", "if", "in",
			"into", "is", "it", "no", "not", "of", "on", "or", "such", "that",
			"the", "their", "then", "there", "these", "they", "this", "to",
			"was", "will", "with");

	/**
	 * Method calculates how often <b>word</b> occures in <b>text</b>.
	 * 
	 * @param word
	 * @param text
	 *            text where should be search for occurrences of <b>word</b>
	 * @return number of occurrences of word <b>word</b> in text <b>text</b>
	 */
	public static int countOccurrences(String word, String text) {
		int counter = 0;

		// "clean" text
		String tmpText = text.replace(",", "").replace(".", "")
				.replace("-", "").replace("\"", "").replace("\'", "");
		String[] wordsInText = tmpText.split(" ");
		for (String wordInText : wordsInText) {
			String tmpWord = wordInText.trim();
			if (tmpWord.equalsIgnoreCase(word)) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Method removes duplicate words and (English) stop words from <b>text</b>.
	 * 
	 * @param text
	 *            text from which (English) stop words and duplicate words
	 *            should be removed
	 * @return same text but lower-cased and without duplicate words and
	 *         (English) stop words
	 */
	public static String removeDuplicateAndStopWords(String text) {
		String textWithoutDuplicates = "";
		List<String> wordsInTextWithoutDuplicates = new ArrayList<>();
		String tmpQuery = text.replace(",", " ").replace("-", " ")
				.replace(".", " ").replace("?", " ");
		for (String wordInQuery : tmpQuery.split(" ")) {
			if (wordInQuery.isEmpty()) {
				continue;
			}

			String trimmedWordInQuery = wordInQuery.trim().toLowerCase();

			if (!wordsInTextWithoutDuplicates.contains(trimmedWordInQuery)
					&& !TextUtils.stopWords.contains(trimmedWordInQuery)) {
				if (!textWithoutDuplicates.isEmpty()) {
					textWithoutDuplicates += " ";
				}
				textWithoutDuplicates += trimmedWordInQuery;
				wordsInTextWithoutDuplicates.add(trimmedWordInQuery);
			}
		}
		return textWithoutDuplicates;
	}
}
