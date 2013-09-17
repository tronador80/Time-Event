/**
 * 
 */
package pawel.algorithms;

import junit.framework.Assert;

import org.junit.Test;

import pawel.algorithms.CosineSimilarity;

/**
 * Test case class for <code>CosineSimilarity</code> class.
 * 
 * @author ptondryk
 * @see CosineSimilarity
 */
public class CosineSimilarityTest {

	/**
	 * This test checks whether method <code>calculateCosineSimilarity</code>
	 * from class <code>CosineSimilarity</code> correctly calculates cosine
	 * similarity by checking what similarity value it returns for different
	 * sentences (similar and not similar).
	 */
	@Test
	public void testCalculateCosineSimilarity() {

		String s1 = "This is a foo bar sentence.";
		String s2 = "This sentence is similar to a foo bar sentence.";
		String s3 = "What is this string ? Totally not related to the other two lines.";

		double similarity12 = CosineSimilarity
				.calculateCosineSimilarity(s1, s2);
		double similarity13 = CosineSimilarity
				.calculateCosineSimilarity(s1, s3);
		double similarity23 = CosineSimilarity
				.calculateCosineSimilarity(s2, s3);

		Assert.assertTrue(similarity12 > similarity13);
		Assert.assertTrue(similarity12 > similarity23);

	}
}
