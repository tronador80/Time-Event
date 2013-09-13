/**
 * 
 */
package pawel.uima.annotator.sentencesplit;

import static org.uimafit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.uimafit.factory.CollectionReaderFactory.createCollectionReader;
import static org.uimafit.pipeline.SimplePipeline.runPipeline;

import java.util.Random;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import pawel.uima.reader.JsonArrayReader;
import pawel.uima.writer.InMemoryOutput;
import eu.stratosphere.sopremo.type.IJsonNode;

/**
 * Class provides functionality for sentence splitting.
 * 
 * @author ptondryk
 * 
 */
public class SentenceSplitterAnalysisComponent {

	/**
	 * 
	 */
	public SentenceSplitterAnalysisComponent() {

	}

	/**
	 * Method splits sentences in given <b>inputText</b> (json object node
	 * containing element <code>text</code>).
	 * 
	 * @param inputText
	 *            text that should be splitted to single sentences
	 * @return json representing single sentences and tokens of <b>inputText</b>
	 * @throws Exception
	 */
	public IJsonNode tokenize(String inputText) throws Exception {
		String key = "ssac_" + Math.abs((new Random()).nextLong());

		CollectionReader source = createCollectionReader(JsonArrayReader.class,
				"PARAM_INPUT", new String[] { inputText });

		AnalysisEngineDescription splitter = createPrimitiveDescription(
				SentenceSplitter.class,
				TypeSystemDescriptionFactory.createTypeSystemDescription());

		AnalysisEngineDescription dest = createPrimitiveDescription(
				InMemoryOutput.class, InMemoryOutput.PARAM_KEY, key);

		runPipeline(source, splitter, dest);

		return (IJsonNode) InMemoryOutput.getOutputMap().remove(key);
	}
}
