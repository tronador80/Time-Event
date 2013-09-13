/**
 * 
 */
package pawel.uima.annotator.sutime;

import static org.uimafit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.uimafit.factory.CollectionReaderFactory.createCollectionReader;
import static org.uimafit.pipeline.SimplePipeline.runPipeline;

import java.util.Random;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import pawel.sopremo.operator.PosTaggerSopremoOperator;
import pawel.uima.reader.JsonArrayReader;
import pawel.uima.writer.InMemoryOutput;
import eu.stratosphere.sopremo.type.IJsonNode;

/**
 * This class use SuTime for time tagging.
 * 
 * @author pawel
 * 
 */
public class SuTimeAnalysisComponent {

	/**
	 * 
	 */
	public SuTimeAnalysisComponent() {

	}

	/**
	 * Method tags temporal expressions using UIMA annotators.
	 * 
	 * @param inputText
	 *            json string containing output of
	 *            {@link PosTaggerSopremoOperator}
	 * @return tagged temporal expressions as json
	 * @throws Exception
	 */
	public IJsonNode tagTime(String inputText) throws Exception {
		String key = "stac_" + Math.abs((new Random()).nextLong());

		CollectionReader source = createCollectionReader(JsonArrayReader.class,
				"PARAM_INPUT", new String[] { inputText });

		TypeSystemDescriptionFactory.forceTypeDescriptorsScan();

		AnalysisEngineDescription suTime = createPrimitiveDescription(SuTimeUima.class);

		AnalysisEngineDescription dest = createPrimitiveDescription(
				InMemoryOutput.class, InMemoryOutput.PARAM_KEY, key);

		runPipeline(source, suTime, dest);

		return (IJsonNode) InMemoryOutput.getOutputMap().remove(key);
	}

}
