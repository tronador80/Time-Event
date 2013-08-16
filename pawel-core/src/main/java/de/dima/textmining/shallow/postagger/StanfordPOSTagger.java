package de.dima.textmining.shallow.postagger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.dima.textmining.languages.LanguageType;
import de.dima.textmining.resources.ResourceManager;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * The Class StanfordPOSTagger is a Wrapper for the Stanford POSTagger.
 */
public class StanfordPOSTagger implements POSTagger {

	/** The tagger */
	private MaxentTagger tagger;

	/** The logger. */
	Logger logger = Logger.getLogger(StanfordPOSTagger.class);

	/**
	 * Instantiates a new stanford pos tagger.
	 * 
	 * @param language
	 *            the used language
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public StanfordPOSTagger(LanguageType language) {
		initialize(language);
	}

	/**
	 * Instantiates a new stanford pos tagger.
	 * 
	 * @param modelFile
	 *            the modelFile for the part-of-speech tagger
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public StanfordPOSTagger(String modelFile) throws IOException,
			ClassNotFoundException {
		initialize(modelFile);
	}

	/**
	 * Instantiates a new stanford pos tagger.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public StanfordPOSTagger() throws IOException, ClassNotFoundException {
		initialize(LanguageType.DEFAULT);
	}

	/**
	 * Initialize.
	 * 
	 * @param language
	 *            the used language
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	private void initialize(LanguageType language) {

		String resourcePath = "";

		if (language.equals(LanguageType.GERMAN)
				|| language.equals(LanguageType.DEFAULT)) {
			logger.debug("initializing german stanford postagger");
			resourcePath = "/models/stanford/german-accurate.tagger";
		} else if (language.equals(LanguageType.ENGLISH)) {
			/* TODO which model should be used ? */
			logger.debug("initializing english stanford postagger");
			resourcePath = "/models/stanford/bidirectional-distsim-wsj-0-18.tagger";
			resourcePath = "/models/stanford/left3words-wsj-0-18.tagger";

		} else {
			logger.error("Unsupported language requested for this POSTagger: "
					+ language);
			return;
		}

		try {
			String taggerModelPath = ResourceManager
					.getResourcePath(resourcePath);

			tagger = new MaxentTagger(taggerModelPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize.
	 * 
	 * @param modelFile
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	private void initialize(String modelFile) throws IOException,
			ClassNotFoundException {
		tagger = new MaxentTagger(modelFile);
	}

	@Override
	public List<String> postag(List<String> tokenizedSentence) {

		List<String> postags = new ArrayList<String>();

		if (tokenizedSentence.isEmpty()) {
			// if empty: return empty list because stanford tagger has problems
			// with empty sentences.
			return postags;
		}

		ArrayList<TaggedWord> words = new ArrayList<TaggedWord>();

		for (String token : tokenizedSentence) {
			words.add(new TaggedWord(token));
		}

		/* part-of-speech tagging */
		ArrayList<TaggedWord> tSentence = tagger.tagSentence(words);

		for (TaggedWord tag : tSentence) {
			postags.add(tag.tag());
		}

		return postags;
	}

}
