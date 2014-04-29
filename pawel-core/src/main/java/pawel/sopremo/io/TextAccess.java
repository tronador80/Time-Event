package pawel.sopremo.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import pawel.model.sopremo.io.TextNewsInputSplit;

import eu.stratosphere.nephele.template.GenericInputSplit;
import eu.stratosphere.pact.common.contract.GenericDataSource;
import eu.stratosphere.pact.common.io.GenericInputFormat;
import eu.stratosphere.pact.common.plan.PactModule;
import eu.stratosphere.pact.common.type.PactRecord;
import eu.stratosphere.sopremo.EvaluationContext;
import eu.stratosphere.sopremo.expressions.EvaluationExpression;
import eu.stratosphere.sopremo.operator.ElementaryOperator;
import eu.stratosphere.sopremo.operator.InputCardinality;
import eu.stratosphere.sopremo.operator.Name;
import eu.stratosphere.sopremo.operator.OutputCardinality;
import eu.stratosphere.sopremo.operator.Property;
import eu.stratosphere.sopremo.pact.SopremoUtil;
import eu.stratosphere.sopremo.serialization.Schema;
import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.ObjectNode;
import eu.stratosphere.sopremo.type.TextNode;

/**
 * The class realizes the input from text files with n-news documents, seperated by hyphens. The mapper converts
 * converts the text file in records containing one news document.
 *
 * 
 * @author Jochen Adamek
 */

@InputCardinality(max = 0, min = 0)
@OutputCardinality(1)
@Name(verb = "text_input")
public class TextAccess extends ElementaryOperator<TextAccess>{
	
	private static Logger log = Logger.getLogger(TextAccess.class);
	
	protected static final String DOCUMENT_NAME = "document_id";
	protected static final String TIME_STAMP = "time_st";
	
	private String documentName;
	
	/**
	 * this String contains the current timestamp of the news cluster
	 */
	private String timeStampValue;
	
	public static class TextInputFormat extends GenericInputFormat {
		
		
		
		private EvaluationContext context;
			
		private BufferedReader buffReader;
		
		/**
		 * Name of the files containing reuters news. Starting with: file:// or
		 * hdfs://
		 */
		private String docName;
		
		/**
		 * id of document that is currently processed (it refers to the list
		 * <code>filesToProcess</code>)
		 */
		private int docId;
			
		/**
		* 
		*/
		private Schema schema;

		/**
		 * this list contains names of all files that should be processed
		 */
		private List<String> filesToProcess;
		
		/**
		 * this list contains names of all files that should be processed
		 */
		private List<String> newsInside;
		
		/**
		 * this list contains content of all news that belong to currently
		 * processed file
		 */
		private List<String> newsToProcess;
		
		/**
		 * 
		 */
		private String timeSt;
		
		@Override
		public void configure(
				eu.stratosphere.nephele.configuration.Configuration parameters) {
			super.configure(parameters);
			this.context = (EvaluationContext) SopremoUtil.getObject(
					parameters, SopremoUtil.CONTEXT, null);
			this.schema = this.context.getOutputSchema(0);
			this.docName = (String) SopremoUtil.getObject(parameters,
					DOCUMENT_NAME, null);
			this.timeSt = (String) SopremoUtil.getObject(parameters,
					TIME_STAMP, null);
			if (this.newsToProcess == null){
			this.newsToProcess = new ArrayList<String>();
			}
 
		}
		
		@Override
		public boolean reachedEnd() throws IOException {
			return (this.newsToProcess.isEmpty() || this.newsToProcess == null) && (this.filesToProcess.isEmpty() || this.filesToProcess == null);
		}

		
		@Override
		public boolean nextRecord(PactRecord record) throws IOException {
			
			IJsonNode newNode = null;

			if (this.newsToProcess.isEmpty()) {
				String contentOfNextBigFile = this.getBuffReader(this.getFilesToProcess().get(this.docId));
				String help = contentOfNextBigFile.substring(1);
				this.newsToProcess = this.splitText(help);

			}
			
			newNode = this.textNewsFile2jsonObjectNode(this.newsToProcess.remove(0));
			if (newNode != null) {
			this.schema.jsonToRecord(newNode, record);
			}			
			
			if(this.newsToProcess.isEmpty()){
				this.filesToProcess.remove(0);	
			}
			System.out.println("end of nextRecord");

			return true;
		}
		
		
		
		public void open(final GenericInputSplit split) {
			log.debug("Open new split (splitNumber = " + split.getSplitNumber()
					+ ").");
			if (split instanceof TextNewsInputSplit) {
				TextNewsInputSplit textNewsSplit = (TextNewsInputSplit) split;

				this.docId = 0;
				this.getFilesToProcess().addAll(
						textNewsSplit.getFilesToProcess());
				this.getNewsInside().addAll(
						textNewsSplit.getNewsInside());
			/*	if (this.newsToProcess == null) {
					this.newsToProcess = new ArrayList<String>();
				} */
			} else {
				log.warn("Received split is not TextNewsInputSplit... This split will be ignored.");
			}
		}
		
		public void close() {
			log.debug("The input format TextAccess closed.");

		}
		
		private List<String> splitText(String inputString){
			String line = "----";
			List<String> splitResult = new ArrayList<String>();
			splitResult = new ArrayList<String>(Arrays.asList(inputString.split(line)));			
			return splitResult;
		}
		

		public String getBuffReader(String file) {
			System.out.println("inside getBuffer");
			String everything = "";
			if (this.buffReader == null) {
				try {
					this.buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
					try {
						StringBuilder sb = new StringBuilder();
						String line = this.buffReader.readLine();
						
						while (line != null) {
							sb.append(line);
						//	sb.append('\n');
							line = this.buffReader.readLine();
						}
						everything = sb.toString();
						System.out.println("string inside getBuffer:" + everything);
					} finally {
						this.buffReader.close();
					}
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
			return everything;
		}
		
		
		/**
		 * this method returns the names of files that should be processed
		 * 
		 * @return list of file names
		 */
		private List<String> getFilesToProcess() {

			System.out.println("inside getFilesProcess");

			if (this.filesToProcess == null) {
				this.filesToProcess = new ArrayList<String>();
			}
			System.out.println("inside getFilesToProcess2");
			return this.filesToProcess;
		} 
		
		
		private List<String> getNewsInside() {

			System.out.println("inside getNewsInside");

			if (this.newsInside == null) {
				this.newsInside = new ArrayList<String>();
			}
			System.out.println("inside getFilesToProcess2");
			return this.newsInside;
		} 
		
		/**
		 * 
		 * @param string
		 * @return
		 */
		private TextNode string2TextNode(String string) {
			return string != null ? new TextNode(string.replace("\"", "'"))
					: new TextNode("");
		}
		
		
		/**
		 * 
		 * @param textNewsFileContent
		 * @return
		 */
    	private IJsonNode textNewsFile2jsonObjectNode(String textNewsFileContent){
			
			ObjectNode res = new ObjectNode();
			ArrayNode<IJsonNode> annotations = new ArrayNode<IJsonNode>();
			if(this.getBuffReader(textNewsFileContent) == null){
				log.error("exctraction of string from document failed");
			}
			
			if(this.newsToProcess == null){
				log.error("no news to process!!!!");
			}
		
			try {
				
					ObjectNode fullText = new ObjectNode();
					System.out.println("inside testNewsFile:" + textNewsFileContent);
					System.out.println("value of fullText:" + fullText.toString());
					fullText.put("Text", this.string2TextNode(textNewsFileContent));
//					fullText.put("Text",new TextNode(textNewsFileContent));
					
					fullText.put("byline", this.string2TextNode(""));  // Test for sentence splitting
				
					fullText.put("copyright", this.string2TextNode(""));  // Test for sentence splitting
				
					fullText.put("date", this.string2TextNode(this.timeSt));  // Test for sentence splitting
				
					fullText.put("dateline", this.string2TextNode(""));  // Test for sentence splitting
					
					fullText.put("headline", this.string2TextNode(""));  // Test for sentence splitting
				
					fullText.put("id", this.string2TextNode(""));  // Test for sentence splitting
				
					fullText.put("itemId", this.string2TextNode(""));  // Test for sentence splitting
					
					fullText.put("lang", this.string2TextNode(""));  // Test for sentence splitting
					
					fullText.put("title", this.string2TextNode(""));  // Test for sentence splitting
					annotations.add(fullText);
					
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			
			res.put("annotations", annotations);
			System.out.println("res is currently:" + res.toString());
			
			return res;		
		}
		
		
		@Override
		public Class<? extends GenericInputSplit> getInputSplitType() {
			return TextNewsInputSplit.class;
		}
		
		@Override
		public GenericInputSplit[] createInputSplits(final int minNumSplits)
				throws IOException {
			return this.createDistributedInputSplits(minNumSplits);
		}
		
		private GenericInputSplit[] createDistributedInputSplits(
				int minNumSplits) {

			GenericInputSplit[] splits = new TextNewsInputSplit[minNumSplits];
			
			List<String> allFilesToProcess = new ArrayList<String>();
			
			File[] files = (new File(this.docName.replace("file://", "")))
					.listFiles();
			for (File file : files) {
				allFilesToProcess.add(file.getAbsolutePath());
		}
			List<String> testNewsinside = new ArrayList<String>();
			testNewsinside = this.splitText(getBuffReader(allFilesToProcess.get(0)));

			int numberOfDocumentsToProcess = testNewsinside.size(); //amount of docs calculated correctly
			System.out.println("Numbers of docs:" + numberOfDocumentsToProcess);

			float minNumSplitsDouble = (float) minNumSplits;
			for (int i = 0; i < minNumSplits; i++) {
				List<String> newsToProcessWithinThisSplit = new ArrayList<String>(
						testNewsinside.subList(
								Math.round(i
										* (numberOfDocumentsToProcess / minNumSplitsDouble)),
								Math.min(
										Math.round((i + 1)
												* (numberOfDocumentsToProcess / minNumSplitsDouble)),
										numberOfDocumentsToProcess)));
				splits[i] = new TextNewsInputSplit(i,allFilesToProcess,
						newsToProcessWithinThisSplit); 
			}

			return splits;
		}
	}
		

	    @Override
		public PactModule asPactModule(EvaluationContext context){
			context.setInputsAndOutputs(0, 1);
			GenericDataSource<?> contract = new GenericDataSource<TextInputFormat>(
					TextInputFormat.class, "TextNewsInput");

			final PactModule pactModule = new PactModule(0, 1);
			SopremoUtil.setObject(contract.getParameters(), SopremoUtil.CONTEXT,
					context);
			SopremoUtil.setObject(contract.getParameters(), DOCUMENT_NAME,
					this.documentName);
			SopremoUtil.setObject(contract.getParameters(), TIME_STAMP,
					this.timeStampValue);
			pactModule.getOutput(0).setInput(contract);
			System.out.println("print contract:"+ contract.toString());
			return pactModule;	
		}
		
		@Property(flag = false, expert = true, preferred = true)
		@Name(noun = "nametext")
		public void setDocumentName(EvaluationExpression documentName) {
			this.documentName = documentName.toString().replace("\"", "")
					.replace("\'", "");
		}
		@Property(flag = true)
		@Name(noun = "timestamp")
		public void setTimestamp(EvaluationExpression timeSt) {
			this.timeStampValue = timeSt.toString();
		}
}
