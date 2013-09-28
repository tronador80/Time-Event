/**
 * 
 */
package pawel.model.sopremo.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	 * this list contains names of all files that should be processed<br>
	 * MAP key: (name of file); value: (array with two values: id of first and
	 * last news to process)
	 */
	private Map<String, Long[]> newsIdsByFileToProcess;

	/**
	 * 
	 */
	private Boolean big;

	/**
	 * This variable indicates whether the news that contain tables should be
	 * skipped.
	 */
	private Boolean tablesOut;

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
	 * @param tablesOut
	 * @param startDocId
	 * @param endDocId
	 * @param docName
	 */
	public ReutersNewsInputSplit(int splitId, List<String> filesToProcess,
			Boolean big, boolean tablesOut) {
		super(splitId);
		this.setFilesToProcess(filesToProcess);
		this.setBig(big);
		this.setTablesOut(tablesOut);
	}

	/**
	 * constructor
	 * 
	 * @param splitId
	 * @param tablesOut
	 * @param startDocId
	 * @param endDocId
	 * @param docName
	 */
	public ReutersNewsInputSplit(int splitId,
			Map<String, Long[]> newsIdsByFileToProcess, Boolean big,
			boolean tablesOut) {
		super(splitId);
		this.newsIdsByFileToProcess = newsIdsByFileToProcess;
		this.filesToProcess = new ArrayList<String>(
				newsIdsByFileToProcess.keySet());
		this.setBig(big);
		this.setTablesOut(tablesOut);
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

	/**
	 * @return the tablesOut
	 */
	public Boolean getTablesOut() {
		return tablesOut;
	}

	/**
	 * @param tablesOut
	 *            the tablesOut to set
	 */
	public void setTablesOut(Boolean tablesOut) {
		this.tablesOut = tablesOut;
	}

	/**
	 * @return the newsIdsByFileToProcess
	 */
	public Map<String, Long[]> getNewsIdsByFileToProcess() {
		return newsIdsByFileToProcess;
	}

	/**
	 * @param newsIdsByFileToProcess
	 *            the newsIdsByFileToProcess to set
	 */
	public void setNewsIdsByFileToProcess(
			Map<String, Long[]> newsIdsByFileToProcess) {
		this.newsIdsByFileToProcess = newsIdsByFileToProcess;
	}

	@Override
	public String toString() {
		String string = "ReutersNewsInputSplit [filesToProcess="
				+ filesToProcess + ", big=" + big + ", tablesOut=" + tablesOut
				+ ", newsIdsByFileToProcess={";

		if (big) {
			for (String fileName : this.newsIdsByFileToProcess.keySet()) {
				string += "[ " + fileName + "; "
						+ this.newsIdsByFileToProcess.get(fileName)[0] + "; "
						+ this.newsIdsByFileToProcess.get(fileName)[1] + "]";
			}
		}

		return string + "}]";
	}

}
