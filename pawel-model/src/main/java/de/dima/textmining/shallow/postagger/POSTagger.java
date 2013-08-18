package de.dima.textmining.shallow.postagger;

import java.util.List;

/**
 * The Interface POSTagger.
 */
public interface POSTagger{

	/**
	 * Postag.
	 *
	 * @param sentence the sentence
	 * @return the list
	 */
	public List<String> postag (List<String> tokenizedSentence);
	
}
