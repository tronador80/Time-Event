/**
 * 
 */
package pawel.uima.annotator.heideltime;

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
import de.unihd.dbs.uima.annotator.heideltime.HeidelTime;
import eu.stratosphere.sopremo.type.IJsonNode;

/**
 * Class provides functionality for temporal expressions tagging with
 * HeidelTime.
 * 
 * @author ptondryk
 * 
 */
public class HeidelTimeAnalysisComponent {

	/**
	 * empty constructor
	 */
	public HeidelTimeAnalysisComponent() {

	}

	/**
	 * Method tags temporal expressions using UIMA annotators.
	 * 
	 * @param inputText
	 *            json string containing output of
	 *            {@link PosTaggerSopremoOperator}
	 * @param typeToProcess
	 *            type of document to process
	 *            (news/narratives/colloquial/scientific)
	 * @param language
	 *            language of the input documents
	 * @return tagged temporal expressions as json
	 * @throws Exception
	 */
	public IJsonNode tagTime(String inputText, String typeToProcess,
			String language) throws Exception {
		String key = "htac_" + Math.abs((new Random()).nextLong());

		// prepare and run UIMA pipeline
		CollectionReader source = createCollectionReader(JsonArrayReader.class,
				"PARAM_INPUT", new String[] { inputText });

		TypeSystemDescriptionFactory.forceTypeDescriptorsScan();

		AnalysisEngineDescription heidelTimeTranslatorBefore = createPrimitiveDescription(
				HeidelTranslatorBefore.class,
				TypeSystemDescriptionFactory.createTypeSystemDescription());

		AnalysisEngineDescription heidelTime = createPrimitiveDescription(
				HeidelTime.class, HeidelTime.PARAM_DATE, true,
				HeidelTime.PARAM_DURATION, true, HeidelTime.PARAM_TIME, true,
				HeidelTime.PARAM_SET, true, HeidelTime.PARAM_TYPE_TO_PROCESS,
				typeToProcess, HeidelTime.PARAM_LANGUAGE, language);

		AnalysisEngineDescription heidelTimeTranslatorAfter = createPrimitiveDescription(
				HeidelTranslatorAfter.class,
				TypeSystemDescriptionFactory.createTypeSystemDescription());

		AnalysisEngineDescription dest = createPrimitiveDescription(
				InMemoryOutput.class, InMemoryOutput.PARAM_KEY, key);

		runPipeline(source, heidelTimeTranslatorBefore, heidelTime,
				heidelTimeTranslatorAfter, dest);

		return (IJsonNode) InMemoryOutput.getOutputMap().remove(key);
	}
}
