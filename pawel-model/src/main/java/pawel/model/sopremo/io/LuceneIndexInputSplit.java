package pawel.model.sopremo.io;

import eu.stratosphere.nephele.template.GenericInputSplit;

/**
 * Extension of {@link GenericInputSplit} class that holds information about
 * lucene index input splits. <br>
 * <br>
 * <b>IMPORTANT:</b> This class should be register with Nepeheles kryo instances
 * (this class should be added to types-list in nephele-server/
 * eu.stratosphere.nephele.rpc.ServerTypeUtils.getRPCTypesToRegister method).
 * 
 * @author pawel
 * 
 */
public class LuceneIndexInputSplit extends GenericInputSplit {

	/**
	 * directory where the lucene index lies
	 */
	private String luceneIndexDir;

	/**
	 * id of first document that should be processed within this split
	 */
	private int startDocId;

	/**
	 * id of last document that should be processed within this split
	 */
	private int endDocId;

	/**
	 * empty constructor
	 */
	public LuceneIndexInputSplit() {
		super();
	}

	/**
	 * constructor
	 * 
	 * @param number
	 *            split number (= identifier)
	 * @param luceneIndexDir
	 *            directory where the lucene index lies
	 * @param startDocId
	 *            id of first document that should be processed within this
	 *            split
	 * @param endDocId
	 *            id of last document that should be processed within this split
	 */
	public LuceneIndexInputSplit(int number, String luceneIndexDir,
			int startDocId, int endDocId) {
		super(number);
		this.luceneIndexDir = luceneIndexDir;
		this.startDocId = startDocId;
		this.endDocId = endDocId;
	}

	/**
	 * @return the luceneIndexDir
	 */
	public String getLuceneIndexDir() {
		return luceneIndexDir;
	}

	/**
	 * @return the startDocId
	 */
	public int getStartDocId() {
		return startDocId;
	}

	/**
	 * @return the endDocId
	 */
	public int getEndDocId() {
		return endDocId;
	}

}