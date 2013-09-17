/**
 * 
 */
package pawel.uima.annotator.event;

import static org.uimafit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.uimafit.factory.CollectionReaderFactory.createCollectionReader;
import static org.uimafit.pipeline.SimplePipeline.runPipeline;

import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import pawel.sopremo.operator.TimeTaggerSopremoOperator;
import pawel.uima.reader.JsonArrayReader;
import pawel.uima.writer.InMemoryOutput;
import pawel.uima.writer.JsonWriter;
import de.dima.textmining.uima.annotators.DeepParserAE;
import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;

/**
 * Class allows to tag event expressions.
 * 
 * @author ptondryk
 * 
 */
public class EventAnalysisComponent {

	private static Logger log = Logger.getLogger(EventAnalysisComponent.class);

	/**
	 * 
	 */
	public EventAnalysisComponent() {

	}

	/**
	 * Method tags events using UIMA annotators.
	 * 
	 * @param inputText
	 *            json string containing output of
	 *            {@link TimeTaggerSopremoOperator}.
	 * @param maxSentenceLength
	 * @param minSentenceLength
	 * @return json string containing tagged event expressions
	 * @throws Exception
	 */
	public IJsonNode tagEvent(String inputText, int maxSentenceLength,
			int minSentenceLength) throws Exception {
		String key = "eac_" + Math.abs((new Random()).nextLong());

		try {
			CollectionReader source = createCollectionReader(
					JsonArrayReader.class, "PARAM_INPUT",
					new String[] { inputText });

			TypeSystemDescriptionFactory.forceTypeDescriptorsScan();

			AnalysisEngineDescription eventTranslator = createPrimitiveDescription(
					EventTranslator.class,
					TypeSystemDescriptionFactory.createTypeSystemDescription());

			AnalysisEngineDescription deepParse = createPrimitiveDescription(
					DeepParserAE.class, "MAX_SENTENCE_LENGTH",
					maxSentenceLength, "MIN_SENTENCE_LENGTH",
					minSentenceLength, "WriteCoNLL", false);

			AnalysisEngineDescription dest = createPrimitiveDescription(
					JsonWriter.class, JsonWriter.PARAM_KEY, key);

			runPipeline(source, eventTranslator, deepParse, dest);

		} catch (OutOfMemoryError ome) {
			log.error(ome.getMessage(), ome);
			return new ArrayNode<IJsonNode>();
		}

		return (IJsonNode) InMemoryOutput.getOutputMap().remove(key);
	}
}
