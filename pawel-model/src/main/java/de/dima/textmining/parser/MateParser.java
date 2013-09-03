package de.dima.textmining.parser;

import is2.data.SentenceData09;
import is2.parser.Options;
import is2.parser.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.dima.textmining.conll.CoNLLNode;
import de.dima.textmining.languages.LanguageType;
import de.dima.textmining.resources.ResourceManager;
import de.dima.textmining.shallow.ShallowToken;

public class MateParser implements de.dima.textmining.parser.Parser {

	private Parser parser;

	Logger logger = Logger.getLogger(MateParser.class);

	public MateParser() {
		initilize(LanguageType.DEFAULT);
	}

	public MateParser(LanguageType language) {
		initilize(language);
	}

	public MateParser(String modelFile) {
		initilize(modelFile);
	}

	public MateParser(Options options) {
		initilize(options);
	}

	private void initilize(Options options) {

		parser = new Parser(options);
	}

	private void initilize(String modelFile) {

		Options optsParser = new Options(new String[] { "-model", modelFile });

		parser = new Parser(optsParser);
	}

	private void initilize(LanguageType language) {

		String resourcePath = "";

		if (language.equals(LanguageType.GERMAN)
				|| language.equals(LanguageType.DEFAULT)) {
			logger.debug("initializing german mate parser");
			resourcePath = "/models/mate/prs-ger.model";
		} else if (language.equals(LanguageType.ENGLISH)) {
			logger.debug("initializing english mate parser");
			resourcePath = "/models/mate/prs-eng.model";
		} else {
			logger.error("Unsupported language requested for this POSTagger: "
					+ language);
			return;
		}

		String parserModelPath = ResourceManager.getResourcePath(resourcePath);

		Options optsParser = new Options(new String[] { "-model",
				parserModelPath });

		// TODO: in manchen Situationen treten hier Probleme auf, weil er das
		// Model nicht Laden kann.
		// Evt. wird es manchen Situationen bei Hadoop zu schnell geloescht.

		parser = new Parser(optsParser);
	}

	@Override
	public CoNLLNode parse(List<ShallowToken> shallowSentence) {

		List<String> tokens = new ArrayList<String>();
		List<String> postags = new ArrayList<String>();

		for (ShallowToken shallowToken : shallowSentence) {

			tokens.add(shallowToken.getText());
			postags.add(shallowToken.getTag());

		}

		return this.parse(tokens, postags);
	}

	@Override
	public CoNLLNode parse(List<String> tokens, List<String> postags) {
		logger.debug("parsing tokens: " + tokens + " postags: " + postags);

		if (tokens.isEmpty() || postags.isEmpty()) {
			// if sentence was empty, return null
			logger.info("parser got empty sentence. returning null.\n");
			return null;
		}

		List<String> tokensN = new ArrayList<String>();
		tokensN.add("<root>");
		tokensN.addAll(tokens);

		List<String> postagsN = new ArrayList<String>();
		postagsN.add("<root-POS>");
		postagsN.addAll(postags);

		SentenceData09 sen = new SentenceData09();

		// Provide the sentence
		sen.init(tokensN.toArray(new String[0]));
		sen.setPPos(postagsN.toArray(new String[0]));
		// System.out.println(sen.printSem());

		sen = parser.parse(sen);

		List<String> conllLines = new ArrayList<String>();

		for (int i = 0; i < sen.forms.length; i++) {

			String line = "" + (i + 1) + "\t" + sen.forms[i] + "\t" + "_"
					+ "\t" + sen.ppos[i] + "\t" + sen.ppos[i] + "\t"
					+ sen.pfeats[i] + "\t" + sen.phead[i] + "\t"
					+ sen.labels[i] + "\t" + sen.phead[i] + "\t"
					+ sen.labels[i];

			conllLines.add(line);

		}

		CoNLLNode node = null;

		try {
			node = CoNLLNode.parseCoNLL(conllLines);
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.debug("parsing result:\n" + node);

		return node;
	}
}
