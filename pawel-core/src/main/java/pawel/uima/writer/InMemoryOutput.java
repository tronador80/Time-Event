/**
 * 
 */
package pawel.uima.writer;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.JCasConsumer_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.factory.ConfigurationParameterFactory;

import pawel.paweltypes.Sentence;
import pawel.paweltypes.Text;
import pawel.paweltypes.Timex3;
import pawel.paweltypes.Token;
import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.ObjectNode;
import eu.stratosphere.sopremo.type.TextNode;

/**
 * This class implements an UIMA JCas consumer that's task is to write results
 * to map.
 * 
 * @author ptondryk
 * 
 */
public class InMemoryOutput extends JCasConsumer_ImplBase {

	private static Map<String, Object> outputMap = new HashMap<String, Object>();

	/**
	 * 
	 */
	public static final String PARAM_KEY = ConfigurationParameterFactory
			.createConfigurationParameterName(InMemoryOutput.class, "key");
	@ConfigurationParameter(mandatory = true, description = "the key in output map")
	private String key;

	/**
	 * 
	 * @return
	 */
	public static Map<String, Object> getOutputMap() {
		return InMemoryOutput.outputMap;
	}

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		ObjectNode res = new ObjectNode();
		ArrayNode<IJsonNode> array = new ArrayNode<IJsonNode>();

		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> texts = jcas
				.getAnnotationIndex(Text.type);
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> sentences = jcas
				.getAnnotationIndex(Sentence.type);
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> tokens = jcas
				.getAnnotationIndex(Token.type);
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> timexs = jcas
				.getAnnotationIndex(Timex3.type);

		for (org.apache.uima.jcas.tcas.Annotation annotation : texts) {
			if (annotation instanceof Text) {
				Text textAnnotation = (Text) annotation;
				array.add(text2ObjectNode(textAnnotation));
			}
		}

		for (org.apache.uima.jcas.tcas.Annotation annotation : sentences) {
			if (annotation instanceof Sentence) {
				Sentence sentenceAnnotation = (Sentence) annotation;
				array.add(sentence2ObjectNode(sentenceAnnotation));
			}
		}

		for (org.apache.uima.jcas.tcas.Annotation annotation : tokens) {
			if (annotation instanceof Token) {
				Token tokenAnnotation = (Token) annotation;
				array.add(token2ObjectNode(tokenAnnotation));
			}
		}

		for (org.apache.uima.jcas.tcas.Annotation annotation : timexs) {
			if (annotation instanceof Timex3) {
				Timex3 timexAnnotation = (Timex3) annotation;
				array.add(timex2ObjectNode(timexAnnotation));
			}
		}

		res.put("annotations", array);
		InMemoryOutput.outputMap.put(this.key, res);
	}

	/**
	 * 
	 * @param textAnnotation
	 * @return
	 */
	private IJsonNode text2ObjectNode(Text textAnnotation) {
		ObjectNode res = new ObjectNode();

		res.put("Text", new TextNode(textAnnotation.getText()));
		res.put("begin",
				new TextNode((new Integer(textAnnotation.getBegin()))
						.toString()));
		res.put("end",
				new TextNode((new Integer(textAnnotation.getEnd())).toString()));
		res.put("date", new TextNode(textAnnotation.getDate()));

		return res;
	}

	/**
	 * 
	 * @param sentenceAnnotation
	 * @return
	 */
	private IJsonNode sentence2ObjectNode(Sentence sentenceAnnotation) {
		ObjectNode res = new ObjectNode();

		res.put("Sentence", new TextNode(sentenceAnnotation.getSentence()));
		res.put("begin",
				new TextNode((new Integer(sentenceAnnotation.getBegin()))
						.toString()));
		res.put("end",
				new TextNode((new Integer(sentenceAnnotation.getEnd()))
						.toString()));
		res.put("sentenceIndex",
				new TextNode(
						(new Integer(sentenceAnnotation.getSentenceIndex()))
								.toString()));

		res.put("tokenBegin",
				new TextNode((new Integer(sentenceAnnotation.getTokenBegin()))
						.toString()));
		res.put("tokenEnd",
				new TextNode((new Integer(sentenceAnnotation.getTokenEnd()))
						.toString()));

		return res;
	}

	/**
	 * 
	 * @param tokenAnnotation
	 * @return
	 */
	private IJsonNode token2ObjectNode(Token tokenAnnotation) {
		ObjectNode res = new ObjectNode();

		res.put("value", new TextNode(tokenAnnotation.getValue()));
		res.put("Token", new TextNode(tokenAnnotation.getToken()));
		res.put("originalText", new TextNode(tokenAnnotation.getOriginalText()));

		res.put("begin",
				new TextNode((new Integer(tokenAnnotation.getBegin()))
						.toString()));
		res.put("end",
				new TextNode((new Integer(tokenAnnotation.getEnd())).toString()));

		res.put("beforeToken", new TextNode(tokenAnnotation.getBeforeToken()));
		res.put("afterToken", new TextNode(tokenAnnotation.getAfterToken()));

		if (tokenAnnotation.getPos() != null
				&& !tokenAnnotation.getPos().isEmpty()) {
			res.put("pos", new TextNode(tokenAnnotation.getPos()));
		}

		return res;
	}

	/**
	 * 
	 * @param timexAnnotation
	 * @return
	 */
	private IJsonNode timex2ObjectNode(Timex3 timexAnnotation) {
		ObjectNode res = new ObjectNode();

		if (timexAnnotation.getAllTokIds() != null) {
			res.put("allTokIds", new TextNode(timexAnnotation.getAllTokIds()));
		}

		res.put("begin",
				new TextNode((new Integer(timexAnnotation.getBegin()))
						.toString()));
		res.put("end",
				new TextNode((new Integer(timexAnnotation.getEnd())).toString()));

		if (timexAnnotation.getFilename() != null) {
			res.put("filename", new TextNode(timexAnnotation.getFilename()));
		}

		res.put("firstTokId",
				new TextNode((new Integer(timexAnnotation.getFirstTokId()))
						.toString()));

		if (timexAnnotation.getFoundByRule() != null) {
			res.put("foundByRule",
					new TextNode(timexAnnotation.getFoundByRule()));
		}

		res.put("sentId", new TextNode(
				(new Integer(timexAnnotation.getSentId())).toString()));

		if (timexAnnotation.getTimex3() != null) {
			res.put("Timex3", new TextNode(timexAnnotation.getTimex3()));
		}

		if (timexAnnotation.getTimexFreq() != null) {
			res.put("timexFreq", new TextNode(timexAnnotation.getTimexFreq()));
		}

		if (timexAnnotation.getTimexId() != null) {
			res.put("timexId", new TextNode(timexAnnotation.getTimexId()));
		}

		res.put("timexInstance",
				new TextNode((new Integer(timexAnnotation.getTimexInstance()))
						.toString()));

		if (timexAnnotation.getTimexMod() != null) {
			res.put("timexMod", new TextNode(timexAnnotation.getTimexMod()));
		}

		if (timexAnnotation.getTimexQuant() != null) {
			res.put("timexQuant", new TextNode(timexAnnotation.getTimexQuant()));
		}

		if (timexAnnotation.getTimexType() != null) {
			res.put("timexType", new TextNode(timexAnnotation.getTimexType()));
		}

		if (timexAnnotation.getTimexValue() != null) {
			res.put("timexValue", new TextNode(timexAnnotation.getTimexValue()));
		}

		return res;
	}
}
