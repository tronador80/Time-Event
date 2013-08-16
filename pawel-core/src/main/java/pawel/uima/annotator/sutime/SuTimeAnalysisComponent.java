/**
 * 
 */
package pawel.uima.annotator.sutime;

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
	 * @return tagged temporal expressions as json string
	 * @throws UIMAException
	 * @throws IOException
	 */
	public String tagTime(String inputText) throws UIMAException, IOException {

		String outputDirName = "/sutime_tagger_"
				+ Math.abs((new Random()).nextLong());

		CollectionReader source = createCollectionReader(JsonArrayReader.class,
				"PARAM_INPUT", new String[] { inputText });

		TypeSystemDescriptionFactory.forceTypeDescriptorsScan();

		AnalysisEngineDescription suTime = createPrimitiveDescription(SuTimeUima.class);

		AnalysisEngineDescription dest = createPrimitiveDescription(
				XWriter.class, XWriter.PARAM_OUTPUT_DIRECTORY_NAME,
				OutputHandler.DEFAULT_TMP_DIR + outputDirName);

		runPipeline(source, suTime, dest);

		return OutputHandler
				.readOutputFromExtendedDefaultTmpDirectory(outputDirName);
	}

}
