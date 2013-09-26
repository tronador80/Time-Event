package pawel.uima.annotator.sentencesplit;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;

import pawel.paweltypes.Text;
import pawel.paweltypes.Sentence;
import pawel.paweltypes.Token;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.PTBTokenizerAnnotator;
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;
import edu.stanford.nlp.util.CoreMap;

/**
 * UIMA annotator responsible for sentences splitting.
 * 
 * @author ptondryk
 * 
 */
public class SentenceSplitter extends
		org.uimafit.component.JCasAnnotator_ImplBase {

	private static PTBTokenizerAnnotator ptbta;
	private static WordsToSentencesAnnotator wtsa;

	/**
	 * empty constructor
	 */
	public SentenceSplitter() {
		super();
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String text = jcas.getDocumentText();

		if (SentenceSplitter.ptbta == null) {
			SentenceSplitter.ptbta = new PTBTokenizerAnnotator(false);
		}
		if (SentenceSplitter.wtsa == null) {
			SentenceSplitter.wtsa = new WordsToSentencesAnnotator(false);
		}

		AnnotationPipeline pipeline = new AnnotationPipeline();
		pipeline.addAnnotator(SentenceSplitter.ptbta);
		pipeline.addAnnotator(SentenceSplitter.wtsa);

		Annotation annotations = new Annotation(text);

		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> texts = jcas
				.getAnnotationIndex(Text.type);

		for (org.apache.uima.jcas.tcas.Annotation annotation : texts) {
			if (annotation instanceof Text) {
				Text textAnnotation = (Text) annotation;
				annotations.set(CoreAnnotations.TextAnnotation.class,
						textAnnotation.getText());
			}
		}

		// split sentences using the configured pipeline
		pipeline.annotate(annotations);

		for (CoreMap sentenceAnnotation : annotations
				.get(CoreAnnotations.SentencesAnnotation.class)) {
			Sentence res = this.sentenceAnnotationToSentence(
					sentenceAnnotation, jcas);
			res.addToIndexes();

		}

		for (CoreMap tokenAnnotation : annotations
				.get(CoreAnnotations.TokensAnnotation.class)) {
			Token res = this.tokenAnnotationToToken(tokenAnnotation, jcas);
			res.addToIndexes();

		}

	}

	/**
	 * This method converts sentence annotation represented as {@link CoreMap}
	 * (used by stanford NLP) into {@link Sentence}.
	 * 
	 * @param sentenceAnnotation
	 *            {@link CoreMap}
	 * @param jcas
	 *            JCas necessary to create new uima annotation
	 * @return {@link Sentence}
	 */
	private Sentence sentenceAnnotationToSentence(CoreMap sentenceAnnotation,
			JCas jcas) {
		Sentence res = new Sentence(jcas);

		res.setSentence(sentenceAnnotation
				.get(CoreAnnotations.TextAnnotation.class));
		res.setBegin(sentenceAnnotation
				.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class));
		res.setEnd(sentenceAnnotation
				.get(CoreAnnotations.CharacterOffsetEndAnnotation.class));

		res.setSentenceIndex(sentenceAnnotation
				.get(CoreAnnotations.SentenceIndexAnnotation.class));

		res.setTokenBegin(sentenceAnnotation
				.get(CoreAnnotations.TokenBeginAnnotation.class));
		res.setTokenEnd(sentenceAnnotation
				.get(CoreAnnotations.TokenEndAnnotation.class));

		return res;
	}

	/**
	 * This method converts token annotation represented as {@link CoreMap}
	 * (used by stanford NLP) into {@link Token}.
	 * 
	 * @param tokenAnnotation
	 *            {@link CoreMap}
	 * @param jcas
	 *            JCas necessary to create new uima annotation
	 * @return {@link Token}
	 */
	private Token tokenAnnotationToToken(CoreMap tokenAnnotation, JCas jcas) {
		Token res = new Token(jcas);

		res.setValue(tokenAnnotation.get(CoreAnnotations.ValueAnnotation.class));
		res.setToken(tokenAnnotation.get(CoreAnnotations.TextAnnotation.class));
		res.setOriginalText(tokenAnnotation
				.get(CoreAnnotations.OriginalTextAnnotation.class));

		res.setBegin(tokenAnnotation
				.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class));
		res.setEnd(tokenAnnotation
				.get(CoreAnnotations.CharacterOffsetEndAnnotation.class));

		res.setBeforeToken(tokenAnnotation
				.get(CoreAnnotations.BeforeAnnotation.class));
		res.setAfterToken(tokenAnnotation
				.get(CoreAnnotations.AfterAnnotation.class));

		return res;
	}
}
