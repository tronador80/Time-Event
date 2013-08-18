package de.dima.textmining.shallow.lemmatizer;

import is2.data.SentenceData09;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import de.dima.textmining.languages.LanguageType;
import de.dima.textmining.resources.ResourceManager;

public class MateLemmatizer implements Lemmatizer {

	/** The lemmatizer. */
	private static is2.lemmatizer.Lemmatizer lemmatizer;

	/** The opts lemmatizer. */
	private static is2.lemmatizer.Options optsLemmatizer;

	/** The logger. */
	Logger logger = Logger.getLogger(MateLemmatizer.class);

	public MateLemmatizer() {

		LanguageType language = LanguageType.ENGLISH;

		String taggerResourcePath;

		if (language.equals(LanguageType.GERMAN)) {
			taggerResourcePath = "/models/mate/lemmatizer.model";
		} else if (language.equals(LanguageType.ENGLISH)) {
			taggerResourcePath = "/models/mate/lemma-eng.model";
		} else {
			logger.error("Unsupported language requested for this POSTagger: "
					+ language);
			return;
		}

		logger.debug("initializing mate-tools lemmatizer");

		try {

			String lemmatizerModelPath = ResourceManager
					.getResourcePath(taggerResourcePath);

			optsLemmatizer = new is2.lemmatizer.Options(new String[] {
					"-model", lemmatizerModelPath });

			lemmatizer = new is2.lemmatizer.Lemmatizer(optsLemmatizer);

		} catch (IOException e) {

			logger.error("Problems loading lemmatizer model: " + e);
		}
	}

	public MateLemmatizer(LanguageType language) {

		String taggerResourcePath;

		if (language.equals(LanguageType.GERMAN)) {
			taggerResourcePath = "/models/mate/lemmatizer.model";
		} else if (language.equals(LanguageType.ENGLISH)) {
			taggerResourcePath = "/models/mate/lemma-eng.model";
		} else {
			logger.error("Unsupported language requested for this POSTagger: "
					+ language);
			return;
		}

		logger.debug("initializing mate-tools lemmatizer");

		try {

			String lemmatizerModelPath = ResourceManager
					.getResourcePath(taggerResourcePath);

			optsLemmatizer = new is2.lemmatizer.Options(new String[] {
					"-model", lemmatizerModelPath });

			lemmatizer = new is2.lemmatizer.Lemmatizer(optsLemmatizer);

		} catch (IOException e) {

			logger.error("Problems loading lemmatizer model: " + e);
		}
	}

	@Override
	/**
	 * Lemmatize.
	 * 
	 * @param sentence
	 *            the sentence
	 * @return the list
	 */
	public List<String> lemmatize(List<String> tokenizedSentence) {

		logger.debug("postagging sentence: " + tokenizedSentence);

		SentenceData09 sen = new SentenceData09();

		sen.init(tokenizedSentence.toArray(new String[0]));

		// lemmatizing
		lemmatizer.lemmatize(optsLemmatizer, sen);

		logger.debug("lemmatizing result: " + Arrays.asList(sen.lemmas));

		return Arrays.asList(sen.lemmas);
	}

}
