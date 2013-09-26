/**
 * 
 */
package pawel.uima.annotator.heideltime;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.uimafit.component.JCasAnnotator_ImplBase;

import pawel.paweltypes.Sentence;
import pawel.paweltypes.Text;
import pawel.paweltypes.Token;
import de.unihd.dbs.uima.types.heideltime.Dct;

/**
 * This annotator prepares necessary annotations for HeidelTime annotator.
 * 
 * @author ptondryk
 * 
 */
public class HeidelTranslatorBefore extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> text = jcas
				.getAnnotationIndex(Text.type);

		for (org.apache.uima.jcas.tcas.Annotation annotation : text) {
			if (annotation instanceof Text) {
				Text textAnnotation = (Text) annotation;

				Dct dct = new Dct(jcas);
				dct.setValue(textAnnotation.getDate().substring(0, 8));

				dct.addToIndexes();
			}
		}

		// create Heideltime Token annotations
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> tokens = jcas
				.getAnnotationIndex(Token.type);

		for (org.apache.uima.jcas.tcas.Annotation annotation : tokens) {
			if (annotation instanceof Token) {
				de.unihd.dbs.uima.types.heideltime.Token heidelToken = this
						.tokenToHeideltimeToken((Token) annotation, jcas);
				heidelToken.addToIndexes();
			}
		}

		// create Heideltime Sentence annotations
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> sentences = jcas
				.getAnnotationIndex(Sentence.type);

		for (org.apache.uima.jcas.tcas.Annotation annotation : sentences) {
			if (annotation instanceof Sentence) {
				de.unihd.dbs.uima.types.heideltime.Sentence heidelSentence = this
						.sentenceToHeideltimeSentence((Sentence) annotation,
								jcas);
				heidelSentence.addToIndexes();
			}
		}
	}

	/**
	 * 
	 * @param sentence
	 * @param jcas
	 * @return
	 */
	private de.unihd.dbs.uima.types.heideltime.Sentence sentenceToHeideltimeSentence(
			Sentence sentence, JCas jcas) {
		de.unihd.dbs.uima.types.heideltime.Sentence res = new de.unihd.dbs.uima.types.heideltime.Sentence(
				jcas);

		res.setBegin(sentence.getBegin());
		res.setEnd(sentence.getEnd());
		res.setSentenceId(sentence.getSentenceIndex());

		return res;
	}

	/**
	 * 
	 * @param token
	 * @param jcas
	 * @return
	 */
	private de.unihd.dbs.uima.types.heideltime.Token tokenToHeideltimeToken(
			Token token, JCas jcas) {
		de.unihd.dbs.uima.types.heideltime.Token res = new de.unihd.dbs.uima.types.heideltime.Token(
				jcas);

		res.setBegin(token.getBegin());
		res.setEnd(token.getEnd());
		res.setPos(token.getPos());

		return res;
	}

}
