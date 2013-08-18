package de.dima.textmining.shallow.lemmatizer;

import java.util.List;

public interface Lemmatizer {

	public List<String> lemmatize(List<String> tokenizedSentence);

}
