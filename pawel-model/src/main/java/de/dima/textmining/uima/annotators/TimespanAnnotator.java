package de.dima.textmining.uima.annotators;

import java.util.ArrayList;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.dima.textmining.types.Timespan;
import de.unihd.dbs.uima.types.heideltime.Sentence;
import de.unihd.dbs.uima.types.heideltime.Timex3;
import de.unihd.dbs.uima.types.heideltime.Token;

// import edu.stanford.nlp.ling.CoreAnnotations.PossibleAnswersAnnotation;

public class TimespanAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		// for every sentence

		// get first timex token (if any)

		// save start and content of token

		// look for next timex token
		// ////
		@SuppressWarnings({ "rawtypes", "unchecked" })
		AnnotationIndex<Annotation> sentIndex = jcas
				.getAnnotationIndex(Sentence.type);
		AnnotationIndex<Annotation> timeIndex = jcas
				.getAnnotationIndex(Timex3.type);
		AnnotationIndex<Annotation> tokenIndex = jcas
				.getAnnotationIndex(Token.type);

		boolean addTimespan = false;

		// for every sentence
		for (Annotation an : sentIndex) {
			Sentence sent = (Sentence) an;
			int sentBegin = sent.getBegin();
			int sentEnd = sent.getEnd();

			FSIterator<Annotation> timeIter = timeIndex.subiterator(sent);
			// save begins and ends of all timex tokens adjust index to sentence
			ArrayList<Integer> begins = new ArrayList<Integer>();
			ArrayList<Integer> ends = new ArrayList<Integer>();
			ArrayList<Timex3> timexs = new ArrayList<Timex3>();
			// get all timex annot. that are no durations like 6 minutes
			while (timeIter.hasNext()) {
				Timex3 time = (Timex3) timeIter.next();
				if (!time.getTimexType().equals("DURATION")) {
					timexs.add(time);
					// begins.add(time.getBegin());// - sentBegin);
					// ends.add(time.getEnd());// - sentBegin);

				}
				// //System.out.println(sent.getCoveredText());
			}

			// if at least 2 found check text between timex expressions
			int timexCount = timexs.size();
			int interval;
			int begin1, end1, begin2, end2;
			if (timexCount >= 2) {
				// TODO change if direct access to sents of jcas per id is
				// possible
				String docText = jcas.getDocumentText(); // .getCoveredText();
				for (int i = 0; i < timexCount - 1; i++) {
					// get postitons of timexs and scale to sentence
					begin1 = timexs.get(i).getBegin() - sentBegin;
					begin2 = timexs.get(i + 1).getBegin() - sentBegin;
					;
					end1 = timexs.get(i).getEnd() - sentBegin;
					;
					end2 = timexs.get(i + 1).getEnd() - sentBegin;
					;
					// //System.out.println(begins.get(i) + " " + ends.get(i +
					// 1) + " " +
					// sentBegin + " "+ sent.getEnd());
					// interval = begins.get(i + 1) - ends.get(i);
					// only cut text if interval under max length
					interval = begin2 - end1;
					if (interval < 10 && interval > 0) {
						String sentText = sent.getCoveredText();
						// String betweenText = docText.substring(ends.get(i),
						// begins.get(i
						// + 1));
						// get text between timex expr. and removes surrounding
						// whitespace
						// String betweenText = docText.substring(end1,
						// begin2).trim();
						String betweenText = sentText.substring(end1, begin2)
								.trim();

						// System.out.println(betweenText);
						// String output = "";
						// output += "|#" + betweenText + "#" +
						// sentText.substring(begin1, end2) + " ##"
						// + sentText.substring(begin1, end1) + "|" +
						// sentText.substring(begin2, end2) + "||";
						// TODO make check nicer not or but set
						if (betweenText.equals("-") || betweenText.equals("to")
								|| betweenText.equals("through")) {
							addTimespan = true;
							// System.out.println("Found to or - ");
						} else if (betweenText.equals("and")) {
							// for 'and' check if 'between' is before first
							// timex
							try {
								// System.out.println((begin1 - 8));
								if (begin1 - 8 >= 0) {
									String preText = sentText
											.substring(begin1 - 8, begin1)
											.trim().toLowerCase();
									// System.out.println("Pre: " + preText);
									if (preText.equals("between")) {
										addTimespan = true;
										// System.out.println("Found between and");
									} else {
										// System.out.println("not Found and: "
										// + preText);
									}
								}
							} catch (Exception e) {
								// System.err.println("Cut error" +
								// sent.getCoveredText());

							}
						}
						// add new annotation
						if (addTimespan) {
							Timespan timespan = new Timespan(jcas);
							// set text span
							timespan.setBegin(timexs.get(i).getBegin());
							timespan.setEnd(timexs.get(i + 1).getEnd());
							// set times
							timespan.setStartTime(timexs.get(i));
							timespan.setEndTime(timexs.get(i + 1));
							// System.out.println("ADDED");
							timespan.addToIndexes(jcas);
							// remove timex annotations form jcas
							timexs.get(i).removeFromIndexes(jcas);
							timexs.get(i + 1).removeFromIndexes(jcas);
							addTimespan = false;
						}

						// output += sentText;
						// System.out.println(output.replace("\n", " "));
					}

				}
			}

		}
	}

}
