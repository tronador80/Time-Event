/**
 * 
 */
package pawel.utils;

/**
 * @author ptondryk
 * 
 */
public class TextUtils {

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
	 * Method removes duplicate words from <b>query</b>.
	 * 
	 * @param query
	 * @return
	 */
	public static String removeDuplicateWordsFromQuery(String query) {
		String queryWithoutDuplicates = "";
		String tmpQuery = query.replace(",", "").replace("-", "");
		for (String wordInQuery : tmpQuery.split(" ")) {
			String trimmedWordInQuery = wordInQuery.trim();

			if (!queryWithoutDuplicates.contains(trimmedWordInQuery)) {
				if (!queryWithoutDuplicates.isEmpty()) {
					queryWithoutDuplicates += " ";
				}
				queryWithoutDuplicates += trimmedWordInQuery;
			}
		}
		return queryWithoutDuplicates;
	}
}
