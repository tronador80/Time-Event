package pawel.model.sopremo.io;

import java.util.List;

import eu.stratosphere.nephele.template.GenericInputSplit;

public class ClusterNewsInputSplit extends GenericInputSplit {

	private List<String> filesToProcess;
	private List<List<String>> newsInside;

	public ClusterNewsInputSplit() {
		super();
	}

	public ClusterNewsInputSplit(int splitId, List<String> filesToProcess,
			List<List<String>> newsInside) {
		super(splitId);
		this.setFilesToProcess(filesToProcess);
		this.setNewsInside(newsInside);

	}

	public List<String> getFilesToProcess() {
		return filesToProcess;
	}

	public void setFilesToProcess(List<String> filesToProcess) {
		this.filesToProcess = filesToProcess;
	}

	public List<List<String>> getNewsInside() {
		return newsInside;
	}

	public void setNewsInside(List<List<String>> newsInside) {
		this.newsInside = newsInside;
	}
}
