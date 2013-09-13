/**
 * 
 */
package pawel.uima.annotator.event;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.uimafit.component.JCasAnnotator_ImplBase;

import de.dima.textmining.types.ShallowAnnotation;
import de.dima.textmining.types.Sentence;
import de.unihd.dbs.uima.types.heideltime.Timex3;
import de.unihd.dbs.uima.types.heideltime.Token;

/**
 * @author ptondryk
 * 
 */
public class EventTranslator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		List<org.apache.uima.jcas.tcas.Annotation> annotationsToAdd = new ArrayList<org.apache.uima.jcas.tcas.Annotation>();
		List<org.apache.uima.jcas.tcas.Annotation> annotationsToRemove = new ArrayList<org.apache.uima.jcas.tcas.Annotation>();

		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> timexs = jcas
				.getAnnotationIndex(pawel.types.pawel.Timex3.type);
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> tokens = jcas
				.getAnnotationIndex(pawel.types.pawel.Token.type);
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> sentences = jcas
				.getAnnotationIndex(pawel.types.pawel.Sentence.type);

		for (org.apache.uima.jcas.tcas.Annotation annotation : tokens) {
			if (annotation instanceof pawel.types.pawel.Token) {
				annotationsToAdd.add(this.tokenToShallowAnnotation(
						(pawel.types.pawel.Token) annotation, jcas));
				annotationsToAdd.add(this.tokenToHeidelToken(
						(pawel.types.pawel.Token) annotation, jcas));
				annotationsToRemove.add(annotation);
			}
		}

		for (org.apache.uima.jcas.tcas.Annotation annotation : timexs) {
			if (annotation instanceof pawel.types.pawel.Timex3) {
				annotationsToAdd.add(this.timex3ToHeidelTimex3(
						(pawel.types.pawel.Timex3) annotation, jcas));
				annotationsToRemove.add(annotation);
			}
		}

		for (org.apache.uima.jcas.tcas.Annotation annotation : sentences) {
			if (annotation instanceof pawel.types.pawel.Sentence) {
				annotationsToAdd.add(this.sentenceToHeidelSentence(
						(pawel.types.pawel.Sentence) annotation, jcas));
				annotationsToRemove.add(annotation);
			}
		}

		for (org.apache.uima.jcas.tcas.Annotation annotation : annotationsToAdd) {
			annotation.addToIndexes();
		}

		for (org.apache.uima.jcas.tcas.Annotation annotation : annotationsToRemove) {
			annotation.removeFromIndexes();
		}
	}

	/**
	 * 
	 * @param token
	 * @param jcas
	 * @return
	 */
	private Token tokenToHeidelToken(pawel.types.pawel.Token token, JCas jcas) {
		Token res = new Token(jcas);

		res.setBegin(token.getBegin());
		res.setEnd(token.getEnd());
		res.setPos(token.getPos());
		res.setTokenId(token.getTypeIndexID());

		return res;
	}

	/**
	 * 
	 * @param sentence
	 * @param jcas
	 * @return
	 */
	private Sentence sentenceToHeidelSentence(
			pawel.types.pawel.Sentence sentence, JCas jcas) {
		Sentence res = new Sentence(jcas);

		res.setBegin(sentence.getBegin());
		res.setEnd(sentence.getEnd());

		return res;
	}

	/**
	 * 
	 * @param token
	 * @param jcas
	 * @return
	 */
	private ShallowAnnotation tokenToShallowAnnotation(
			pawel.types.pawel.Token token, JCas jcas) {
		ShallowAnnotation res = new ShallowAnnotation(jcas);

		res.setBegin(token.getBegin());
		res.setEnd(token.getEnd());
		res.setLemma(token.getToken());
		res.setPosTag(token.getPos());

		return res;
	}

	/**
	 * 
	 * @param timex
	 * @param jcas
	 * @return
	 */
	private Timex3 timex3ToHeidelTimex3(pawel.types.pawel.Timex3 timex,
			JCas jcas) {

		Timex3 res = new Timex3(jcas);

		res.setAllTokIds(timex.getAllTokIds());
		res.setBegin(timex.getBegin());
		res.setEnd(timex.getEnd());
		res.setFilename(timex.getFilename());
		res.setFirstTokId(timex.getFirstTokId());
		res.setFoundByRule(timex.getFoundByRule());
		res.setSentId(timex.getSentId());
		res.setTimexFreq(timex.getTimexFreq());
		res.setTimexId(timex.getTimexId());
		res.setTimexInstance(timex.getTimexInstance());
		res.setTimexMod(timex.getTimexMod());
		res.setTimexQuant(timex.getTimexQuant());
		res.setTimexType(timex.getTimexType());
		res.setTimexValue(timex.getTimexValue());

		return res;
	}
}
