/**
 * 
 */
package pawel.uima.annotator.sentencesplit;

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

import pawel.uima.reader.JsonArrayReader;
import pawel.utils.OutputHandler;

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
	 * @return json string representing single sentences of <b>inputText</b>
	 * @throws UIMAException
	 * @throws IOException
	 */
	public String tokenize(String inputText) throws UIMAException, IOException {

		String outputDirName = "/sentence_splitter_"
				+ Math.abs((new Random()).nextLong());

		try {
			CollectionReader source = createCollectionReader(
					JsonArrayReader.class, "PARAM_INPUT",
					new String[] { inputText });

			AnalysisEngineDescription splitter = createPrimitiveDescription(
					SentenceSplitter.class,
					TypeSystemDescriptionFactory.createTypeSystemDescription());

			AnalysisEngineDescription dest = createPrimitiveDescription(
					XWriter.class, XWriter.PARAM_OUTPUT_DIRECTORY_NAME,
					OutputHandler.DEFAULT_TMP_DIR + outputDirName);

			runPipeline(source, splitter, dest);
			
		} catch (Exception e) {
			OutputHandler.removeOutputDirectory(outputDirName);
			throw e;
		}

		return OutputHandler
				.readOutputFromExtendedDefaultTmpDirectory(outputDirName);
	}
}
