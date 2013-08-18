package de.dima.textmining.parser;

import java.util.List;

import de.dima.textmining.conll.CoNLLNode;
import de.dima.textmining.shallow.ShallowToken;

public interface Parser {

	/**
	 * Parse.
	 * 
	 * @param tokens
	 * @param postags
	 * @return the parse treee
	 */
	public CoNLLNode parse(List<String> tokens, List<String> postags);
	

	public CoNLLNode parse(List<ShallowToken> tokens);

}
