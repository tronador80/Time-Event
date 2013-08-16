/**
 * 
 */
package pawel.uima.annotator.heideltime;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.uimafit.component.JCasAnnotator_ImplBase;

import de.unihd.dbs.uima.types.heideltime.Dct;
import de.unihd.dbs.uima.types.heideltime.Sentence;
import de.unihd.dbs.uima.types.heideltime.Timex3;
import de.unihd.dbs.uima.types.heideltime.Token;

/**
 * This annotator removes annotations that was required for HeidelTime annotator
 * but are no more required. This class also adjusts the Timex3 annotations to
 * necessary format.
 * 
 * @author ptondryk
 * @see Timex3
 */
public class HeidelTranslatorAfter extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		List<org.apache.uima.jcas.tcas.Annotation> annotationsToRemove = new ArrayList<org.apache.uima.jcas.tcas.Annotation>();

		// find the heideltime.Dct
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> timexs = jcas
				.getAnnotationIndex(Timex3.type);
		for (org.apache.uima.jcas.tcas.Annotation annotation : timexs) {
			if (annotation instanceof Timex3) {
				Timex3 heidelTimex = (Timex3) annotation;
				annotationsToRemove.add(heidelTimex);

				pawel.types.pawel.Timex3 timex = this.heidelTimex3ToTimex3(
						heidelTimex, jcas);
				timex.addToIndexes();
			}
		}

		// traverse the heideltime.Tokens
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> tokens = jcas
				.getAnnotationIndex(Token.type);
		for (org.apache.uima.jcas.tcas.Annotation annotation : tokens) {
			if (annotation instanceof Token) {
				annotationsToRemove.add(annotation);
			}
		}

		// traverse the heideltime.Sentences
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> sentences = jcas
				.getAnnotationIndex(Sentence.type);
		for (org.apache.uima.jcas.tcas.Annotation annotation : sentences) {
			if (annotation instanceof Sentence) {
				annotationsToRemove.add(annotation);
			}
		}

		// find the heideltime.Dct
		AnnotationIndex<org.apache.uima.jcas.tcas.Annotation> dct = jcas
				.getAnnotationIndex(Dct.type);
		for (org.apache.uima.jcas.tcas.Annotation annotation : dct) {
			if (annotation instanceof Dct) {
				annotationsToRemove.add(annotation);
			}
		}

		// remove unnecessary annotation from jcas
		for (org.apache.uima.jcas.tcas.Annotation annotation : annotationsToRemove) {
			annotation.removeFromIndexes();
		}

	}

	/**
	 * 
	 * @param heidelTimex
	 * @param jcas
	 * @return
	 */
	private pawel.types.pawel.Timex3 heidelTimex3ToTimex3(Timex3 heidelTimex,
			JCas jcas) {

		pawel.types.pawel.Timex3 res = new pawel.types.pawel.Timex3(jcas);

		res.setAllTokIds(heidelTimex.getAllTokIds());
		res.setBegin(heidelTimex.getBegin());
		res.setEnd(heidelTimex.getEnd());
		res.setFilename(heidelTimex.getFilename());
		res.setFirstTokId(heidelTimex.getFirstTokId());
		res.setFoundByRule(heidelTimex.getFoundByRule());
		res.setSentId(heidelTimex.getSentId());
		res.setTimex3(jcas.getDocumentText().substring(heidelTimex.getBegin(),
				heidelTimex.getEnd()));
		res.setTimexFreq(heidelTimex.getTimexFreq());
		res.setTimexId(heidelTimex.getTimexId());
		res.setTimexInstance(heidelTimex.getTimexInstance());
		res.setTimexMod(heidelTimex.getTimexMod());
		res.setTimexQuant(heidelTimex.getTimexQuant());
		res.setTimexType(heidelTimex.getTimexType());
		res.setTimexValue(heidelTimex.getTimexValue());

		return res;
	}

}
