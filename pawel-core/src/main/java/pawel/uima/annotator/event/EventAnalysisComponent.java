/**
 * 
 */
package pawel.uima.annotator.event;

import static org.uimafit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.uimafit.factory.CollectionReaderFactory.createCollectionReader;
import static org.uimafit.pipeline.SimplePipeline.runPipeline;

import java.io.IOException;
import java.util.Random;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import pawel.sopremo.operator.TimeTaggerSopremoOperator;
import pawel.uima.reader.JsonArrayReader;
import pawel.uima.writer.JsonWriter;
import pawel.utils.OutputHandler;
import de.dima.textmining.uima.annotators.DeepParserAE;

/**
 * Class allows to tag event expressions.
 * 
 * @author pawel
 * 
 */
public class EventAnalysisComponent {

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
	 * @return json string containing tagged event expressions
	 * @throws UIMAException
	 * @throws IOException
	 */
	public String tagEvent(String inputText) throws UIMAException, IOException {

		String outputDirName = "/event_tagger_"
				+ Math.abs((new Random()).nextLong());

		CollectionReader source = createCollectionReader(JsonArrayReader.class,
				"PARAM_INPUT", new String[] { inputText });

		TypeSystemDescriptionFactory.forceTypeDescriptorsScan();

		AnalysisEngineDescription eventTranslator = createPrimitiveDescription(
				EventTranslator.class,
				TypeSystemDescriptionFactory.createTypeSystemDescription());

		AnalysisEngineDescription deepParse = createPrimitiveDescription(
				DeepParserAE.class, "Max_sentence_length", 300, "WriteCoNLL",
				true, "OutputFile", "test.txt");

		AnalysisEngineDescription dest = createPrimitiveDescription(
				JsonWriter.class, JsonWriter.PARAM_OUTFILE,
				OutputHandler.DEFAULT_TMP_DIR + outputDirName);

		try {
			runPipeline(source, eventTranslator, deepParse, dest);
		} catch (OutOfMemoryError ome) {
			System.err.println(ome.getMessage());
			return "[]";
		}

		String res = OutputHandler
				.readOutputFromExtendedDefaultTmpDirectory(outputDirName);

		if (res == null) {
			res = "[]";
		} else {
			res = res.replace("}{", "},{");
			res = "[" + res + "]";
		}
		return res;
	}
}
