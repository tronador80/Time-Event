/**
 * 
 */
package pawel.uima.annotator.heideltime;

import static org.uimafit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.uimafit.factory.CollectionReaderFactory.createCollectionReader;
import static org.uimafit.pipeline.SimplePipeline.runPipeline;

import java.io.IOException;
import java.util.Random;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.uimafit.component.xwriter.XWriter;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import pawel.sopremo.operator.PosTaggerSopremoOperator;
import pawel.uima.reader.JsonArrayReader;
import pawel.utils.OutputHandler;
import de.unihd.dbs.uima.annotator.heideltime.HeidelTime;

/**
 * Class provides functionality for temporal expressions tagging.
 * 
 * @author pawel
 * 
 */
public class HeidelTimeAnalysisComponent {

	/**
	 * 
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
	 *            news/narratives
	 * @return tagged temporal expressions as json string
	 * @throws UIMAException
	 * @throws IOException
	 */
	public String tagTime(String inputText, String typeToProcess)
			throws UIMAException, IOException {

		String outputDirName = "/heideltime_tagger_"
				+ Math.abs((new Random()).nextLong());

		CollectionReader source = createCollectionReader(JsonArrayReader.class,
				"PARAM_INPUT", new String[] { inputText });

		TypeSystemDescriptionFactory.forceTypeDescriptorsScan();

		AnalysisEngineDescription heidelTimeTranslatorBefore = createPrimitiveDescription(
				HeidelTranslatorBefore.class,
				TypeSystemDescriptionFactory.createTypeSystemDescription());

		AnalysisEngineDescription heidelTime = createPrimitiveDescription(
				HeidelTime.class, HeidelTime.PARAM_DATE, true,
				HeidelTime.PARAM_DURATION, true, HeidelTime.PARAM_TIME, true,
				HeidelTime.PARAM_SET, true, HeidelTime.PARAM_LANGUAGE,
				"english", HeidelTime.PARAM_TYPE_TO_PROCESS, typeToProcess);

		AnalysisEngineDescription heidelTimeTranslatorAfter = createPrimitiveDescription(
				HeidelTranslatorAfter.class,
				TypeSystemDescriptionFactory.createTypeSystemDescription());

		AnalysisEngineDescription dest = createPrimitiveDescription(
				XWriter.class, XWriter.PARAM_OUTPUT_DIRECTORY_NAME,
				OutputHandler.DEFAULT_TMP_DIR + outputDirName);

		runPipeline(source, heidelTimeTranslatorBefore, heidelTime,
				heidelTimeTranslatorAfter, dest);

		return OutputHandler
				.readOutputFromExtendedDefaultTmpDirectory(outputDirName);
	}
}
