/**
 * 
 */
package pawel.uima.annotator.pos;

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

import pawel.sopremo.operator.SentenceSplitterSopremoOperator;
import pawel.uima.reader.JsonArrayReader;
import pawel.utils.OutputHandler;

/**
 * Class provides method for pos tagging.
 * 
 * @author pawel
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
	 * @return json string containing pos tags
	 * @throws UIMAException
	 * @throws IOException
	 */
	public String tagPos(String inputText) throws UIMAException, IOException {

		String outputDirName = "/pos_tagger_"
				+ Math.abs((new Random()).nextLong());

		CollectionReader source = createCollectionReader(JsonArrayReader.class,
				"PARAM_INPUT", new String[] { inputText });

		AnalysisEngineDescription tokens = createPrimitiveDescription(
				PosTagger.class,
				TypeSystemDescriptionFactory.createTypeSystemDescription());

		AnalysisEngineDescription dest = createPrimitiveDescription(
				XWriter.class, XWriter.PARAM_OUTPUT_DIRECTORY_NAME,
				OutputHandler.DEFAULT_TMP_DIR + outputDirName);

		runPipeline(source, tokens, dest);

		return OutputHandler
				.readOutputFromExtendedDefaultTmpDirectory(outputDirName);
	}
}
