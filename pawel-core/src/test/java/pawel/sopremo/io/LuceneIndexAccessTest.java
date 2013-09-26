/**
 * 
 */
package pawel.sopremo.io;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import eu.stratosphere.sopremo.expressions.ConstantExpression;
import eu.stratosphere.sopremo.testing.SopremoTestPlan;
import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.ObjectNode;
import eu.stratosphere.sopremo.type.TextNode;

/**
 * Test class for class {@link LuceneIndexAccess}.
 * 
 * @author ptondryk
 * 
 */
public class LuceneIndexAccessTest {

	/**
	 * this test tests what happens when trying to open an not existing index.
	 * Expected is empty result.
	 */
	@Test
	public void testNotExistingIndex() {
		final SopremoTestPlan sopremoPlan = new SopremoTestPlan(0, 1);

		final LuceneIndexAccess luceneIndex = new LuceneIndexAccess();
		luceneIndex.setLuceneIndexDir(new ConstantExpression("test_index12"));
		sopremoPlan.getOutputOperator(0).setInputs(luceneIndex);
		sopremoPlan.getExpectedOutput(0);

		sopremoPlan.run();
	}

	/**
	 * this test tests whether the {@link LuceneIndexAccess} class can correctly
	 * read in in lucene index.
	 */
	@Test
	public void testExistingIndex() {

		final SopremoTestPlan sopremoPlan = new SopremoTestPlan(0, 1);

		final LuceneIndexAccess luceneIndex = new LuceneIndexAccess();
		luceneIndex.setLuceneIndexDir(new ConstantExpression(System
				.getProperty("user.dir") + "/src/test/resources/test_index"));
		sopremoPlan.getOutputOperator(0).setInputs(luceneIndex);

		// prepare expected output
		ObjectNode output1 = new ObjectNode();
		ArrayNode<IJsonNode> annotations1 = new ArrayNode<IJsonNode>();
		ObjectNode node1 = new ObjectNode();
		node1.put("Text", new TextNode("Pawel Example Text First"));
		node1.put("info", new TextNode("blablabla"));
		annotations1.add(node1);
		output1.put("annotations", annotations1);

		ObjectNode output2 = new ObjectNode();
		ArrayNode<IJsonNode> annotations2 = new ArrayNode<IJsonNode>();
		ObjectNode node2 = new ObjectNode();
		node2.put("Text", new TextNode("Second irrelevant text."));
		node2.put("info", new TextNode("hahahahaha"));
		annotations2.add(node2);
		output2.put("annotations", annotations2);

		sopremoPlan.getExpectedOutput(0).add(output1).add(output2);

		sopremoPlan.run();
	}
}
