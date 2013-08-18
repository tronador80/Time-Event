/**
 * 
 */
package pawel.model.sopremo.io;

import eu.stratosphere.nephele.template.GenericInputSplit;

/**
 * @author ptondryk
 * 
 */
public class ReutersNewsInputSplit extends GenericInputSplit {

	/**
	 * 
	 */
	private int startDocId;

	/**
	 * 
	 */
	private int endDocId;

	/**
	 * 
	 */
	private String docName;

	/**
	 * empty construtor
	 */
	public ReutersNewsInputSplit() {
		super();
	}

	/**
	 * constructor
	 * 
	 * @param splitId
	 * @param startDocId
	 * @param endDocId
	 * @param docName
	 */
	public ReutersNewsInputSplit(int splitId, String docName, int startDocId,
			int endDocId) {
		super(splitId);
		this.startDocId = startDocId;
		this.endDocId = endDocId;
		this.docName = docName;
	}

	/**
	 * @return the startDocId
	 */
	public int getStartDocId() {
		return startDocId;
	}

	/**
	 * @param startDocId
	 *            the startDocId to set
	 */
	public void setStartDocId(int startDocId) {
		this.startDocId = startDocId;
	}

	/**
	 * @return the endDocId
	 */
	public int getEndDocId() {
		return endDocId;
	}

	/**
	 * @param endDocId
	 *            the endDocId to set
	 */
	public void setEndDocId(int endDocId) {
		this.endDocId = endDocId;
	}

	/**
	 * @return the docName
	 */
	public String getDocName() {
		return docName;
	}

	/**
	 * @param docName
	 *            the docName to set
	 */
	public void setDocName(String docName) {
		this.docName = docName;
	}

}
