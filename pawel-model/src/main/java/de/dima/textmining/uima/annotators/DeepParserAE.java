package de.dima.textmining.uima.annotators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.annotator.AnnotatorContext;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.descriptor.ConfigurationParameter;

import de.dima.textmining.conll.CoNLLNode;
import de.dima.textmining.languages.LanguageType;
import de.dima.textmining.parser.MateParser;
import de.dima.textmining.parser.Parser;
import de.dima.textmining.shallow.ShallowToken;
import de.dima.textmining.types.Sentence;
import de.dima.textmining.types.ShallowAnnotation;
import de.dima.textmining.types.Timespan;
import de.unihd.dbs.uima.types.heideltime.Timex3;

/**
 * This class expands pagelink annotations. Given a term with a pagelink in a
 * wikipedia article, all other occurrences of this term are annotated with the
 * same pagelink. The class also performs a database lookup of the term to find
 * synonyms. If synonyms appear in the text, they are also marked with the
 * pagelink.
 */
public class DeepParserAE extends org.uimafit.component.JCasAnnotator_ImplBase {

	@ConfigurationParameter(name = "MAX_SENTENCE_LENGTH")
	public int maxSentenceLength;

	@ConfigurationParameter(name = "MIN_SENTENCE_LENGTH")
	public int minSentenceLength;

	@ConfigurationParameter(name = "WriteCoNLL")
	public Boolean writeCoNLL;

	@ConfigurationParameter(name = "OutputFile")
	public String outputFileName;

	/** The logger. */
	private static Logger logger = Logger.getLogger(DeepParserAE.class);

	private Parser parser;

	/**
	 * key for the output filename parameter
	 */
	private PrintStream output;

	/**
	 * Performs initialization logic. This implementation just reads values for
	 * the configuration parameters.
	 * 
	 * @param aContext
	 *            the context
	 * @throws ResourceInitializationException
	 *             the resource initialization exception
	 * @see org.apache.uima.analysis_engine.annotator.BaseAnnotator#initialize(AnnotatorContext)
	 */
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);

		parser = new MateParser(LanguageType.ENGLISH);

		if (writeCoNLL) {
			try {
				this.openOutputFiles(this.outputFileName);
			} catch (final FileNotFoundException e) {
				throw new ResourceInitializationException(e);
			}
		}
	}

	private void openOutputFiles(String fileNameBase)
			throws FileNotFoundException {
		// open text outputfile for sentences
		final File outputFile = new File(fileNameBase);

		logger.debug("file: " + outputFile);

		final File parent = outputFile.getParentFile();
		if (parent != null) {
			parent.mkdirs();
		}
		this.output = new PrintStream(new FileOutputStream(outputFile));

	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		/**
		 * get iterators over sentences and tokens
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		AnnotationIndex<Annotation> sentenceIndex = (AnnotationIndex) aJCas
				.getJFSIndexRepository().getAnnotationIndex(Sentence.type);

		@SuppressWarnings({ "rawtypes", "unchecked" })
		AnnotationIndex<Annotation> tokenIndex = (AnnotationIndex) aJCas
				.getJFSIndexRepository().getAnnotationIndex(
						ShallowAnnotation.type);
		// to test if sentence contains timex
		AnnotationIndex<Annotation> timeIndex = aJCas
				.getAnnotationIndex(Timex3.type);
		// to test if sentence contains timex
		AnnotationIndex<Annotation> timespanIndex = aJCas
				.getAnnotationIndex(Timespan.type);

		Vector<ShallowToken> tokenVector;
		Map<Integer, ShallowAnnotation> sentenceTokens;
		CoNLLNode parse;

		StringBuilder sb = new StringBuilder();

		/**
		 * iterate over each sentence of text
		 */
		for (Annotation an : sentenceIndex) {

			try {
				Sentence sent = (Sentence) an;
				// ignore sentence with " this causes parser errors
				if (sent.getCoveredText().indexOf('"') != -1) {
					continue;
				}

				if (sent.getCoveredText().length() > this.maxSentenceLength
						|| sent.getCoveredText().length() < this.minSentenceLength) {
					continue;
				}

				// only parse if sentence contains a time expression
				FSIterator timeIter = timeIndex.subiterator(sent);
				FSIterator timespanIter = timespanIndex.subiterator(sent);
				if (!(timeIter.hasNext() || timespanIter.hasNext())) {
					// skip sentence without time annotation
					continue;
				}

				/*
				 * init new vector of Shallow token
				 */
				tokenVector = new Vector<ShallowToken>();

				sentenceTokens = new HashMap<Integer, ShallowAnnotation>();
				/*
				 * iterate over ShallowAnnotation
				 */
				int count = 0;

				@SuppressWarnings("rawtypes")
				FSIterator tokenIterator = tokenIndex.subiterator(sent);
				while (tokenIterator.hasNext()) {
					ShallowAnnotation token = (ShallowAnnotation) tokenIterator
							.next();
					count++;

					sentenceTokens.put(count, token);

					tokenVector.add(new ShallowToken(token.getCoveredText(),
							token.getPosTag(), token.getLemma()));
				}

				/**
				 * parse sentence and add to sentence annotation
				 */
				parse = this.parser.parse(tokenVector);

				sent.setCoNLLParse(parse.getCompleteSentenceAsCoNLL());

				/**
				 * write as conll file
				 */
				if (writeCoNLL) {
					sb.append(parse.getCompleteSentenceAsCoNLL() + "\n");
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		/**
		 * write as conll file
		 */
		if (writeCoNLL && sb.length() > 10) {
			this.output.println(sb);
		}
	}

	/**
	 * @see org.apache.uima.analysis_component.AnalysisComponent_ImplBase#collectionProcessComplete()
	 */
	@Override
	public void collectionProcessComplete() {
		this.closeOutputFiles();
	}

	private void closeOutputFiles() {
		if (this.output != null) {
			this.output.close();
		}
	}
}