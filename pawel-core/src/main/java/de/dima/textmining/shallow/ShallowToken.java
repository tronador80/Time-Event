package de.dima.textmining.shallow;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Class Token.
 */
public class ShallowToken implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The text. */
	private String text;

	/** The tag. */
	private String tag;

	/** The lemma. */
	private String lemma;

	/**
	 * Instantiates a new token.
	 * 
	 * @param text
	 *            the text
	 * @param tag
	 *            the tag
	 * @param lemma
	 *            the lemma
	 */
	public ShallowToken(String text, String tag, String lemma) {
		super();
		this.text = text;
		this.tag = tag;
		this.lemma = lemma;
	}

	/**
	 * Instantiates a new token.
	 * 
	 * @param text
	 *            the text
	 * @param tag
	 *            the tag
	 */
	public ShallowToken(String text, String tag) {
		super();
		this.text = text;
		this.tag = tag;
		this.lemma = text;
	}

	/**
	 * Instantiates a new token.
	 * 
	 * @param text
	 *            the text
	 */
	public ShallowToken(String text) {
		super();
		this.text = text;
		this.tag = null;
		this.lemma = null;
	}

	public String toString() {

		String result = this.text;

		if (this.tag != null && this.lemma != null) {

			result += " (" + this.tag + " " + this.lemma + ")";

		} else if (this.tag != null) {
			result += " (" + this.tag + ")";

		} else if (this.lemma != null) {
			result += " (" + this.lemma + ")";

		}
		return result;
	}

	public JSONObject toJson() {

		JSONObject job = new JSONObject();
		try {
			job.put("text", text);
			job.put("tag", tag);
			job.put("lemma", lemma);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return job;
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the tag.
	 * 
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Sets the tag.
	 * 
	 * @param tag
	 *            the new tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Gets the lemma.
	 * 
	 * @return the lemma
	 */
	public String getLemma() {
		return lemma;
	}

	/**
	 * Sets the lemma.
	 * 
	 * @param lemma
	 *            the new lemma
	 */
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.hashCode() == (obj.hashCode());
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.getText().hashCode() + this.getTag().hashCode()
				+ this.getLemma().hashCode();
	}

}
