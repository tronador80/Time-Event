package pawel.model.sopremo.io;

import java.util.List;

import eu.stratosphere.nephele.template.GenericInputSplit;

/**
 * @author Jochen Adamek
 * 
 */

public class TextNewsInputSplit extends GenericInputSplit {
	
	private List<String> filesToProcess;
	private List<String> newsInside;
	
	public TextNewsInputSplit(){
		super();
	}
	
	public TextNewsInputSplit(int splitId, List<String> filesToProcess, List<String> newsInside){
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
	
	public List<String> getNewsInside() {
		return newsInside;
	}

	public void setNewsInside(List<String> newsInside) {
		this.newsInside = newsInside;
	}

}
