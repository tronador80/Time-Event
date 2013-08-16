package de.dima.textmining.shallow.tokenizer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import org.apache.log4j.Logger;

import de.dima.textmining.languages.LanguageType;
import de.dima.textmining.resources.ResourceManager;

public class MaxEntTokenizer implements Tokenizer {

	/** The tokenizer. */
	private TokenizerME tokenizer;

	/** The logger. */
	Logger logger = Logger.getLogger(MaxEntTokenizer.class);

	public MaxEntTokenizer() {
		initialize(LanguageType.DEFAULT);
	}

	public MaxEntTokenizer(LanguageType language) {
		initialize(language);
	}
	
	public MaxEntTokenizer(String modelFile) {
		initialize(modelFile);
	}

	private void initialize(String modelFile) {
		
		InputStream modelIn_token;

		TokenizerModel token_model;

		try {
			modelIn_token = new FileInputStream(modelFile);

			token_model = new TokenizerModel(modelIn_token);

			tokenizer = new TokenizerME(token_model);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initialize(LanguageType language) {

		String ressourcePath = "";

		if (language.equals(LanguageType.GERMAN)
				|| language.equals(LanguageType.DEFAULT)) {
			logger.debug("initializing german opennlp tokenizer");
			ressourcePath = "/models/opennlp/de-token.bin";
			
		} else if (language.equals(LanguageType.ENGLISH)) {
			logger.debug("initializing german opennlp tokenizer");
			ressourcePath = "/models/opennlp/en-token.bin";
			
		} else {
			logger.error("Unsupported language requested for this POSTagger: "
					+ language);
			return;
		}

		InputStream modelIn_token;

		TokenizerModel token_model;

		try {
			modelIn_token = new FileInputStream(
					ResourceManager.getResourcePath(ressourcePath));

			token_model = new TokenizerModel(modelIn_token);

			tokenizer = new TokenizerME(token_model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> tokenize(String sentence) {

		logger.debug("tokenizing sentence: " + sentence);

		String[] tokens = tokenizer.tokenize(sentence);

		logger.debug("tokenizing result: " + tokens);

		return Arrays.asList(tokens);
	}

}
