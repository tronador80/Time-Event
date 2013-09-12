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
import org.uimafit.component.xwriter.XWriter;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import pawel.sopremo.operator.PosTaggerSopremoOperator;
import pawel.uima.reader.JsonArrayReader;
import pawel.utils.OutputHandler;
import de.unihd.dbs.uima.annotator.heideltime.HeidelTime;

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
	 * @return tagged temporal expressions as json string
	 * @throws Exception
	 */
	public String tagTime(String inputText, String typeToProcess,
			String language) throws Exception {

		String outputDirName = "/heideltime_tagger_"
				+ Math.abs((new Random()).nextLong());

		// prepare and run UIMA pipeline
		try {
			CollectionReader source = createCollectionReader(
					JsonArrayReader.class, "PARAM_INPUT",
					new String[] { inputText });

			TypeSystemDescriptionFactory.forceTypeDescriptorsScan();

			AnalysisEngineDescription heidelTimeTranslatorBefore = createPrimitiveDescription(
					HeidelTranslatorBefore.class,
					TypeSystemDescriptionFactory.createTypeSystemDescription());

			AnalysisEngineDescription heidelTime = createPrimitiveDescription(
					HeidelTime.class, HeidelTime.PARAM_DATE, true,
					HeidelTime.PARAM_DURATION, true, HeidelTime.PARAM_TIME,
					true, HeidelTime.PARAM_SET, true,
					HeidelTime.PARAM_TYPE_TO_PROCESS, typeToProcess,
					HeidelTime.PARAM_LANGUAGE, language);

			AnalysisEngineDescription heidelTimeTranslatorAfter = createPrimitiveDescription(
					HeidelTranslatorAfter.class,
					TypeSystemDescriptionFactory.createTypeSystemDescription());

			AnalysisEngineDescription dest = createPrimitiveDescription(
					XWriter.class, XWriter.PARAM_OUTPUT_DIRECTORY_NAME,
					OutputHandler.DEFAULT_TMP_DIR + outputDirName);

			runPipeline(source, heidelTimeTranslatorBefore, heidelTime,
					heidelTimeTranslatorAfter, dest);
		} catch (Exception e) {
			OutputHandler.removeOutputDirectory(outputDirName);
			throw e;
		}

		return OutputHandler
				.readOutputFromExtendedDefaultTmpDirectory(outputDirName);
	}
}
