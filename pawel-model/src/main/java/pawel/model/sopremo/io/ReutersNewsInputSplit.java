/**
 * 
 */
package pawel.model.sopremo.io;

import java.util.List;

import eu.stratosphere.nephele.template.GenericInputSplit;

/**
 * @author ptondryk
 * 
 */
public class ReutersNewsInputSplit extends GenericInputSplit {

	/**
	 * this list contains names of files that should be processed within this
	 * inputsplit
	 */
	private List<String> filesToProcess;

	/**
	 * 
	 */
	private Boolean big;

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
	public ReutersNewsInputSplit(int splitId, List<String> filesToProcess,
			Boolean big) {
		super(splitId);
		this.setFilesToProcess(filesToProcess);
		this.setBig(big);
	}

	/**
	 * @return the filesToProcess
	 */
	public List<String> getFilesToProcess() {
		return filesToProcess;
	}

	/**
	 * @param filesToProcess
	 *            the filesToProcess to set
	 */
	public void setFilesToProcess(List<String> filesToProcess) {
		this.filesToProcess = filesToProcess;
	}

	/**
	 * @return the big
	 */
	public Boolean getBig() {
		return big;
	}

	/**
	 * @param big
	 *            the big to set
	 */
	public void setBig(Boolean big) {
		this.big = big;
	}

}
