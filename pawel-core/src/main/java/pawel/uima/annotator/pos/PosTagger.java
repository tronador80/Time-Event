/**
 * 
 */
package pawel.uima.annotator.pos;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.uimafit.component.JCasAnnotator_ImplBase;

import de.dima.textmining.resources.ResourceManager;
import pawel.types.pawel.Sentence;
import pawel.types.pawel.Token;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.POSTaggerAnnotator;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.util.CoreMap;

/**
 * UIMA Annotator for Part-of-speech tagging based on stanford NLP annotator.
 * 
 * @author ptondryk
 * 
 */
public class PosTagger extends JCasAnnotator_ImplBase {

	/**
	 * maxent tagger (created here to avoid loading model every time when
	 * calling the PosTagger)
	 */
	private static MaxentTagger mt;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.uima.analysis_component.JCasAnnotator_ImplBase#process(org
	 * .apache.uima.jcas.JCas)
	 */
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String text = jcas.getDocumentText();

		if (PosTagger.mt == null) {
			PosTagger.mt = new MaxentTagger(
					ResourceManager
							.getResourcePath("/edu/stanford/nlp/models/pos-tagger/wsj-left3words/wsj-0-18-left3words-distsim.tagger"));
		}

		// prepare the pipeline for part-of-speech tagging
		AnnotationPipeline pipeline = new AnnotationPipeline();
		pipeline.addAnnotator(new POSTaggerAnnotator(PosTagger.mt));
		Annotation annotations = new Annotation(text);

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

		// tag part-of-speech forms using the configured pipeline
		pipeline.annotate(annotations);

		// remove old token annotation from jcas
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> existingTokens = jcas
				.getAnnotationIndex(Token.type);
		List<Token> tokensToRemove = new ArrayList<Token>();
		for (org.apache.uima.jcas.tcas.Annotation annotation : existingTokens) {
			if (annotation instanceof Token) {
				tokensToRemove.add((Token) annotation);
			}
		}
		for (Token tokenToRemove : tokensToRemove) {
			tokenToRemove.removeFromIndexes();
		}

		// add new updated (with POS) token annotation to jcas
		for (CoreMap tokenAnnotation : annotations
				.get(CoreAnnotations.TokensAnnotation.class)) {
			Token res = this.tokenAnnotationToToken(tokenAnnotation, jcas);
			res.addToIndexes();

		}

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

		return tokenAnnotation;
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

		res.setPos(tokenAnnotation
				.get(CoreAnnotations.PartOfSpeechAnnotation.class));

		return res;
	}
}
