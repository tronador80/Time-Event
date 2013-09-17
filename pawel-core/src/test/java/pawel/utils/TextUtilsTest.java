/**
 * 
 */
package pawel.utils;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test class for class {@link TextUtils}.
 * 
 * @author ptondryk
 * @see TextUtils
 */
public class TextUtilsTest {

	@Test
	public void testCountOccurrences() {
		String word1 = "this";
		String word2 = "en";
		String word3 = "twice";
		String sentence = "In this sentence word \"This\" occures twice.";

		Assert.assertEquals(2, TextUtils.countOccurrences(word1, sentence));
		Assert.assertEquals(0, TextUtils.countOccurrences(word2, sentence));
		Assert.assertEquals(1, TextUtils.countOccurrences(word3, sentence));
	}

	@Test
	public void testRemoveDuplicateWordsFromQuery() {
		String query1 = "query how to remove duplicates from query";
		String query2 = "how how query works how";

		Assert.assertEquals("query how to remove duplicates from",
				TextUtils.removeDuplicateWordsFromQuery(query1));
		Assert.assertEquals("how query works",
				TextUtils.removeDuplicateWordsFromQuery(query2));
	}
}
