/**
 * 
 */
package pawel.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.jcas.JCasRegistry;

/**
 * Class represents single sentence.
 * 
 * @author ptondryk
 * 
 */
public class Sentence2 {

	private int begin;
	private int end;
	private String sentenceText;
	private List<Token> tokens;
	private List<Timex3> timexs;

	public final static int typeIndexID = JCasRegistry
			.register(Sentence2.class);

	public final static int type = typeIndexID;

	public int getTypeIndexID() {
		return typeIndexID;
	}

	/**
	 * empty constructor
	 */
	public Sentence2() {
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getSentenceText() {
		return sentenceText;
	}

	public void setSentenceText(String sentenceText) {
		this.sentenceText = sentenceText;
	}

	/**
	 * 
	 * @return list of all tokens that build this sentence
	 */
	public List<Token> getTokens() {
		if (this.tokens == null) {
			this.tokens = new ArrayList<Token>();
		}
		return tokens;
	}

	/**
	 * 
	 * @return list of all timexs that are part of this sentence
	 */
	public List<Timex3> getTimexs() {
		if (this.timexs == null) {
			this.timexs = new ArrayList<Timex3>();
		}
		return timexs;
	}

	@Override
	public String toString() {
		return "Sentence [begin=" + begin + ", end=" + end + ", sentenceText="
				+ sentenceText + ", tokens=" + tokens + ", timexs=" + timexs
				+ "]";
	}

}
