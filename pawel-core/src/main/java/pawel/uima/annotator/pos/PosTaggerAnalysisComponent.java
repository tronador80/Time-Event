/**
 * 
 */
package pawel.uima.annotator.pos;

import static org.uimafit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.uimafit.factory.CollectionReaderFactory.createCollectionReader;
import static org.uimafit.pipeline.SimplePipeline.runPipeline;

import java.util.Random;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import pawel.sopremo.operator.SentenceSplitterSopremoOperator;
import pawel.uima.reader.JsonArrayReader;
import pawel.uima.writer.InMemoryOutput;
import eu.stratosphere.sopremo.type.IJsonNode;

/**
 * Class provides method for pos tagging.
 * 
 * @author ptondryk
 * 
 */
public class PosTaggerAnalysisComponent {

	/**
	 * 
	 */
	public PosTaggerAnalysisComponent() {

	}

	/**
	 * Method tags POS using UIMA annotators.
	 * 
	 * @param inputText
	 *            json string containing output of
	 *            {@link SentenceSplitterSopremoOperator}.
	 * @return json containing pos tags
	 * @throws Exception
	 */
	public IJsonNode tagPos(String inputText) throws Exception {
		String key = "ptac_" + Math.abs((new Random()).nextLong());

		CollectionReader source = createCollectionReader(JsonArrayReader.class,
				"PARAM_INPUT", new String[] { inputText });

		AnalysisEngineDescription tokens = createPrimitiveDescription(
				PosTagger.class,
				TypeSystemDescriptionFactory.createTypeSystemDescription());

		AnalysisEngineDescription dest = createPrimitiveDescription(
				InMemoryOutput.class, InMemoryOutput.PARAM_KEY, key);

		runPipeline(source, tokens, dest);

		return (IJsonNode) InMemoryOutput.getOutputMap().remove(key);
	}
}
