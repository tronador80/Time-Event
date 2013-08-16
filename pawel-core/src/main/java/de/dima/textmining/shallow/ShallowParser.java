package de.dima.textmining.shallow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.dima.textmining.shallow.ShallowToken;
import de.dima.textmining.shallow.lemmatizer.Lemmatizer;
import de.dima.textmining.shallow.postagger.POSTagger;
import de.dima.textmining.shallow.tokenizer.Tokenizer;

/**
 * The Class ShallowParser.
 */
public class ShallowParser {

	/** The tokenizer. */
	Tokenizer tokenizer;

	/** The postagger. */
	POSTagger postagger;

	/** The lemmatizer. */
	Lemmatizer lemmatizer;

	public ShallowParser() {

	}

	public ShallowParser(Tokenizer tokenizer, POSTagger postagger) {
		super();
		this.tokenizer = tokenizer;
		this.postagger = postagger;
		this.lemmatizer = null;
	}

	/**
	 * Instantiates a new shallow parser.
	 * 
	 * @param tokenizer
	 *            the tokenizer
	 * @param postagger
	 *            the postagger
	 * @param lemmatizer
	 *            the lemmatizer
	 */
	public ShallowParser(Tokenizer tokenizer, POSTagger postagger,
			Lemmatizer lemmatizer) {
		super();
		this.tokenizer = tokenizer;
		this.postagger = postagger;
		this.lemmatizer = lemmatizer;
	}

	public Vector<Vector<ShallowToken>> parseSentences(Vector<String> sentences) {

		Vector<Vector<ShallowToken>> parsedSentences = new Vector<Vector<ShallowToken>>();

		String sentence;
		Vector<ShallowToken> parsedSentence;
		for (int i = 0; i < sentences.size(); i++) {

			sentence = sentences.get(i);
			parsedSentence = this.parseSentence(sentence);
			if (i % 10 == 0) {
				System.out.println(i + " : " + parsedSentence);
			}
			parsedSentences.add(parsedSentence);

		}

		return parsedSentences;

	}

	/**
	 * Parses the sentence.
	 * 
	 * @param sentence
	 *            the sentence
	 * @return the vector
	 */
	public Vector<ShallowToken> parseSentence(String sentence) {

		List<String> tokens = tokenizer.tokenize(sentence);
		List<String> tags = postagger.postag(tokens);
		List<String> lemmas = null;

		if (this.lemmatizer != null) {
			lemmas = lemmatizer.lemmatize(tokens);
		}

		Vector<ShallowToken> parsedSentence = new Vector<ShallowToken>();

		for (int i = 0; i < tokens.size(); i++) {

			if (this.lemmatizer != null)
				parsedSentence.add(new ShallowToken(tokens.get(i), tags.get(i),
						lemmas.get(i)));
			else
				parsedSentence
						.add(new ShallowToken(tokens.get(i), tags.get(i)));
		}

		return parsedSentence;

	}

	public void setTokenizer(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	public void setPostagger(POSTagger postagger) {
		this.postagger = postagger;
	}

	public void setLemmatizer(Lemmatizer lemmatizer) {
		this.lemmatizer = lemmatizer;
	}

	public String writeJSON(Vector<String> sentences) {

		StringBuilder sb = new StringBuilder();

		for (String sentence : sentences) {

			ShallowSentence parsed = new ShallowSentence(
					this.parseSentence(sentence));

			sb.append(parsed.toJson() + "\n");

		}
		return sb.toString();
	}

	public static void main(String[] args) {

		File file = new File("src/main/resources/corpus/corpus-json");

		// open/read the application context file
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:english-context.xml");

		// instantiate our spring dao object from the application context
		ShallowParser parser = (ShallowParser) ctx
				.getBean("englishShallowParser");

		FileWriter fw;
		try {
			fw = new FileWriter(file);

			Vector<String> sentences = new Vector<String>();

			Scanner scan = new Scanner(new File(
					"src/main/resources/corpus/corpus.txt"));

			String line;

			while (scan.hasNextLine()) {

				line = scan.nextLine();

				if (line.trim().length() < 10) {
					continue;
				}

				sentences.add(line);

			}

			System.out.println(sentences.size());

			fw.write(parser.writeJSON(sentences));
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
