package de.dima.textmining.shallow.tokenizer;

import java.util.List;

public interface Tokenizer {

	List<String> tokenize(String sentence);

}
