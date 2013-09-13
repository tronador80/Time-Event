package pawel.uima.annotator.sutime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;

import pawel.types.pawel.Text;
import pawel.types.pawel.Sentence;
import pawel.types.pawel.Timex3;
import pawel.types.pawel.Token;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.time.SUTimeMain;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.Timex;
import edu.stanford.nlp.util.CoreMap;

/**
 * Class from Stanford CoreNLP - {@link TimeAnnotator} - adapted to UIMA.
 * 
 * @author ptondryk
 * 
 * @see TimeAnnotator
 * @see SUTimeMain
 */
public class SuTimeUima extends org.uimafit.component.JCasAnnotator_ImplBase {

	/**
	 * TimeAnnotator created as static to avoid creating it for every jcas...
	 */
	private static TimeAnnotator ta = new TimeAnnotator("sutime",
			System.getProperties());

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		AnnotationPipeline pipeline = new AnnotationPipeline();
		pipeline.addAnnotator(SuTimeUima.ta);

		Text text = null;
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> texts = jcas
				.getAnnotationIndex(Text.type);
		for (org.apache.uima.jcas.tcas.Annotation annotation : texts) {
			if (annotation instanceof Text) {
				text = (Text) annotation;
				break;
			}
		}

		SimpleDateFormat sutimeDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd:hh:mm:ss");
		Annotation annotations = null;
		if (text == null) {
			annotations = new Annotation(jcas.getDocumentText());

			annotations.set(CoreAnnotations.DocDateAnnotation.class,
					sutimeDateFormat.format(new Date()));
		} else {
			annotations = new Annotation(text.getText());

			SimpleDateFormat myDateFormat = new SimpleDateFormat(
					"yyyyMMddHHmmss");
			Date documentDate;
			try {
				documentDate = myDateFormat.parse(text.getDate());

				annotations.set(CoreAnnotations.DocDateAnnotation.class,
						sutimeDateFormat.format(documentDate));
			} catch (ParseException e) {
				annotations.set(CoreAnnotations.DocDateAnnotation.class,
						sutimeDateFormat.format(new Date()));
			}

		}

		// create Token annotations
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> tokens = jcas
				.getAnnotationIndex(Token.type);
		List<CoreLabel> tokenAnnotations = new ArrayList<CoreLabel>();
		for (org.apache.uima.jcas.tcas.Annotation annotation : tokens) {
			if (annotation instanceof Token) {
				tokenAnnotations.add(this
						.tokenToTokenAnnotation((Token) annotation));
			}
		}

		// create Sentence annotations
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> sentences = jcas
				.getAnnotationIndex(Sentence.type);
		List<CoreMap> sentenceAnnotations = new ArrayList<CoreMap>();
		for (org.apache.uima.jcas.tcas.Annotation annotation : sentences) {
			if (annotation instanceof Sentence) {
				sentenceAnnotations.add(this.sentenceToSentenceAnnotation(
						(Sentence) annotation, tokenAnnotations));
			}
		}

		// add annotations to stanford NLP annotation
		annotations.set(CoreAnnotations.TokensAnnotation.class,
				tokenAnnotations);
		annotations.set(CoreAnnotations.SentencesAnnotation.class,
				sentenceAnnotations);

		// call the sutime pipeline
		pipeline.annotate(annotations);

		// transform the timex annotations in sutime form into timex annotations
		// in form used by module pawel
		for (CoreMap timexAnnotation : annotations
				.get(TimeAnnotations.TimexAnnotations.class)) {
			Timex3 timex = this.timexAnnotation2timex3(timexAnnotation, jcas);
			if (timex != null) {
				timex.addToIndexes();
			}

		}
	}

	/**
	 * 
	 * @param timexAnnotation
	 * @param jcas
	 * @return
	 */
	private Timex3 timexAnnotation2timex3(CoreMap timexAnnotation, JCas jcas) {
		Timex3 res = new Timex3(jcas);

		Timex timex = timexAnnotation
				.get(TimeAnnotations.TimexAnnotation.class);

		res.setTimex3(timexAnnotation.get(CoreAnnotations.TextAnnotation.class));
		res.setBegin(timexAnnotation
				.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class));
		res.setEnd(timexAnnotation
				.get(CoreAnnotations.CharacterOffsetEndAnnotation.class));

		if (timex.value() != null && !timex.value().isEmpty()) {
			res.setTimexValue(timex.value());
		} else if (timex.altVal() != null && !timex.altVal().isEmpty()) {
			res.setTimexValue(timex.altVal());
		} else {
			return null;
		}

		res.setTimexType(timex.timexType());
		res.setTimexId(timex.tid());

		return res;
	}

	/**
	 * This method converts sentence represented as {@link Token} into
	 * {@link CoreLabel} (used by stanford NLP).
	 * 
	 * @param token
	 *            {@link Token}
	 * @return {@link CoreLabel} token annotation in stanford NLP format
	 */
	private CoreLabel tokenToTokenAnnotation(Token token) {
		CoreLabel tokenAnnotation = new CoreLabel();

		tokenAnnotation.set(CoreAnnotations.ValueAnnotation.class,
				token.getValue());
		tokenAnnotation.set(CoreAnnotations.TextAnnotation.class,
				token.getToken());
		tokenAnnotation.set(CoreAnnotations.OriginalTextAnnotation.class,
				token.getOriginalText());

		tokenAnnotation.set(
				CoreAnnotations.CharacterOffsetBeginAnnotation.class,
				token.getBegin());
		tokenAnnotation.set(CoreAnnotations.CharacterOffsetEndAnnotation.class,
				token.getEnd());

		tokenAnnotation.set(CoreAnnotations.BeforeAnnotation.class,
				token.getBeforeToken());
		tokenAnnotation.set(CoreAnnotations.AfterAnnotation.class,
				token.getAfterToken());

		tokenAnnotation.set(CoreAnnotations.PartOfSpeechAnnotation.class,
				token.getPos());

		return tokenAnnotation;
	}

	/**
	 * This method converts sentence represented as {@link Sentence} into
	 * {@link CoreMap} (used by stanford NLP).
	 * 
	 * @param sentence
	 *            {@link Sentence}
	 * @param tokenAnnotations
	 * @param jcas
	 * @return {@link CoreMap}
	 */
	private CoreMap sentenceToSentenceAnnotation(Sentence sentence,
			List<CoreLabel> tokenAnnotations) {
		CoreMap sentenceAnnotation = new Annotation(sentence.getSentence());

		sentenceAnnotation.set(CoreAnnotations.TextAnnotation.class,
				sentence.getSentence());
		sentenceAnnotation.set(
				CoreAnnotations.CharacterOffsetBeginAnnotation.class,
				sentence.getBegin());
		sentenceAnnotation.set(
				CoreAnnotations.CharacterOffsetEndAnnotation.class,
				sentence.getEnd());

		sentenceAnnotation.set(CoreAnnotations.SentenceIndexAnnotation.class,
				sentence.getSentenceIndex());
		sentenceAnnotation.set(CoreAnnotations.TokenBeginAnnotation.class,
				sentence.getTokenBegin());
		sentenceAnnotation.set(CoreAnnotations.TokenEndAnnotation.class,
				sentence.getTokenEnd());

		List<CoreLabel> sentenceTokenAnnotations = new ArrayList<CoreLabel>();
		for (int i = sentence.getTokenBegin(); i < sentence.getTokenEnd(); i++) {
			sentenceTokenAnnotations.add(tokenAnnotations.get(i));
		}

		sentenceAnnotation.set(CoreAnnotations.TokensAnnotation.class,
				sentenceTokenAnnotations);

		return sentenceAnnotation;
	}

}