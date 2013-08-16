/**
 * 
 */
package pawel.uima.reader;

import java.io.IOException;

import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.uimafit.component.JCasCollectionReader_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.descriptor.TypeCapability;

/**
 * CollectionReader that takes a simple text array as input.
 * 
 * @author renaud.richardet@epfl.ch
 */
@TypeCapability(outputs = "String")
public class TextArrayReader extends JCasCollectionReader_ImplBase {

	public static final String COMPONENT_ID = TextArrayReader.class.getName();

	@ConfigurationParameter(name = "PARAM_INPUT", mandatory = true, description = "a array of string, to be processed")
	private String[] inputArray;

	private int docId;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		docId = 0;
	}

	public void getNext(JCas jcas) throws IOException, CollectionException {

		jcas.setDocumentText(inputArray[docId++]);
	}

	public boolean hasNext() throws IOException, CollectionException {
		return docId < inputArray.length;
	}

	public Progress[] getProgress() {
		return null;
	}

}
