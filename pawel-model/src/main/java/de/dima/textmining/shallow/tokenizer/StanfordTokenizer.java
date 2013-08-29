package de.dima.textmining.shallow.tokenizer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.dima.textmining.shallow.postagger.StanfordPOSTagger;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class StanfordTokenizer implements Tokenizer {

	/** The logger. */
	Logger logger = Logger.getLogger(StanfordPOSTagger.class);

	@Override
	public List<String> tokenize(String sentence) {

		logger.debug("tokenizing sentence: " + sentence);

		List<String> tokens = new ArrayList<String>();

		List<List<HasWord>> sentences = MaxentTagger
				.tokenizeText(new StringReader(sentence));

		/*
		 * Get only the first detected sentence; it could be a part of the
		 * sentence
		 */
		List<HasWord> firstSentence = sentences.get(0);

		for (HasWord hasWord : firstSentence) {
			tokens.add(hasWord.word());
		}

		logger.debug("tokenizing result: " + tokens);

		return tokens;
	}

}
