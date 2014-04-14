package pawel.sopremo.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import pawel.model.sopremo.io.ClusterNewsInputSplit;

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

@InputCardinality(max = 0, min = 0)
@OutputCardinality(1)
@Name(verb = "cluster_input")
public class ClusterNewsAccess extends ElementaryOperator<ClusterNewsAccess> {

	private static Logger log = Logger.getLogger(ClusterNewsAccess.class);

	protected static final String DOCUMENT_NAME = "document_id";
	protected static final String TIME_STAMP = "time_st";

	private String documentName;

	public static class ClusterInputFormat extends GenericInputFormat {

		/**
		 * Reading from tsv-file text import stream
		 */
		private BufferedReader tsvBuffReader;

		/**
		 * Additional context for evaluation
		 */
		private EvaluationContext context;

		/**
		 * id of document that is currently processed (it refers to the list
		 * <code>filesToProcess</code>)
		 */
		private int docId;

		/**
		* functionality for conversion between PactRecords and IJsonNodes
		*/
		private Schema schema;
		
		/**
		 * Timestamp of news documents
		 */
		private String timeSt;

		/**
		 * String of file path
		 */
		private String docName;

		/**
		 * this list contains names of all files that should be processed
		 */
		private List<String> filesToProcess;

		/**
		 * this list contains names of all files that should be processed
		 */
		private List<List<String>> newsInside;

		/**
		 * this list contains content of all news that belong to currently
		 * processed file
		 */
		private List<List<String>> newsToProcess;

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
			if (this.newsToProcess == null) {
				this.newsToProcess = new ArrayList<List<String>>();
			}
		}

		@Override
		public boolean reachedEnd() throws IOException {
			return (this.newsToProcess.isEmpty() || this.newsToProcess == null)
					&& (this.filesToProcess.isEmpty() || this.filesToProcess == null);
		}

		@Override
		public boolean nextRecord(PactRecord record) throws IOException {
			IJsonNode newNode = null;

			if (this.newsToProcess.isEmpty()) {
				this.newsToProcess = this.getTSVBuffReader(this
						.getFilesToProcess().get(this.docId));
			}

			newNode = this.clusterNewsFile2jsonObjectNode(this.newsToProcess
					.remove(0));
			if (newNode != null) {
				this.schema.jsonToRecord(newNode, record);
			}

			if (this.newsToProcess.isEmpty()) {
				this.filesToProcess.remove(0);
			}
			// System.out.println("end of nextRecord");

			return true;
		}
		
		public void open(final GenericInputSplit split) {
			log.debug("Open new split (splitNumber = " + split.getSplitNumber()
					+ ").");
			if (split instanceof ClusterNewsInputSplit) {
				ClusterNewsInputSplit textNewsSplit = (ClusterNewsInputSplit) split;

				this.docId = 0;
				this.getFilesToProcess().addAll(
						textNewsSplit.getFilesToProcess());
				this.getNewsInside().addAll(textNewsSplit.getNewsInside());
				if (this.newsToProcess == null) {
					this.newsToProcess = new ArrayList<List<String>>();
				}
			} else {
				log.warn("Received split is not TextNewsInputSplit... This split will be ignored.");
			}
		}

		public void close() {
			log.debug("The input format TextAccess closed.");
		}

		/**
		 * Extracts the timestamp and news content from Google News cluster:
		 * http://www.textmining.tu-berlin.de/gnewscrawler/
		 * 
		 * @param file
		 * @return List<List <String>> containing the timestamp and news content
		 */
		public List<List<String>> getTSVBuffReader(String file) {
			// System.out.println("inside getBuffer");
			List<List<String>> outerL = new ArrayList<List<String>>();
			List<String> tsvList = new ArrayList<String>();
			if (this.tsvBuffReader == null) {
				try {
					this.tsvBuffReader = new BufferedReader(
							new InputStreamReader(new FileInputStream(file),
									"utf-8"));
					try {
						String line = this.tsvBuffReader.readLine();
						while (line != null) {
							tsvList = new ArrayList<>();
							// Dirty hack since crawls are not in standardised
							// format
							String splitarray[] = line.split("\\t");
							if (splitarray.length == 7) {
								tsvList.add(splitarray[5]);
								tsvList.add(splitarray[6]);
							} else if (splitarray.length == 8) {
								tsvList.add(splitarray[5]);
								tsvList.add(splitarray[7]);
							} else if (splitarray.length == 9) {
								tsvList.add(splitarray[6]);
								tsvList.add(splitarray[8]);
							} else if (splitarray.length == 10) {
								tsvList.add(splitarray[6]);
								tsvList.add(splitarray[9]);
							} else if (splitarray.length == 11) {
								tsvList.add(splitarray[7]);
								tsvList.add(splitarray[10]);
							} else if (splitarray.length == 13) {
								tsvList.add(splitarray[9]);
								tsvList.add(splitarray[12]);
							} else if (splitarray.length >= 13) {
								tsvList.add(splitarray[6]);
								tsvList.add(splitarray[13]);
							}

							outerL.add(tsvList);
							line = this.tsvBuffReader.readLine();
						}
					} finally {
						this.tsvBuffReader.close();
					}
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
			return outerL;
		}

		/**
		 * this method returns the names of files that should be processed
		 * 
		 * @return list of file names
		 */
		private List<String> getFilesToProcess() {

			// System.out.println("inside getFilesProcess");

			if (this.filesToProcess == null) {
				this.filesToProcess = new ArrayList<String>();
			}
			// System.out.println("inside getFilesToProcess2");
			return this.filesToProcess;
		}

		private List<List<String>> getNewsInside() {

			// System.out.println("inside getNewsInside");

			if (this.newsInside == null) {
				this.newsInside = new ArrayList<List<String>>();
			}
			// System.out.println("inside getFilesToProcess2");
			return this.newsInside;
		}

		/**
		 * 
		 * Converts string information to TextNode
		 * 
		 * @param string
		 * @return TextNode covering String information
		 */
		private TextNode string2TextNode(String string) {
			return string != null ? new TextNode(string.replace("\"", "'"))
					: new TextNode("");
		}
		
		/**
		 * 
		 * normalizes timestamp value, but source format needs to be consistent
		 * 
		 * @param from describes source format
		 * @param to describes target format
		 * @param date value to be normalized
		 * @return normalized timestamp value
		 */

		private String convertFormat(SimpleDateFormat from,
				SimpleDateFormat to, String date) {

			String normalizeDate = new String();
			Date da = new Date();
			try {
				da = from.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			normalizeDate = to.format(da);
			return normalizeDate;
		}

		/**
		 * 
		 * Generates IJsonNode with timestamp and news content. Timestamp value is normalized.
		 * 
		 * @param textNewsFileContent
		 * @return IJsonNode
		 */
		private IJsonNode clusterNewsFile2jsonObjectNode(
				List<String> textNewsFileContent) {

			ObjectNode res = new ObjectNode();
			ArrayNode<IJsonNode> annotations = new ArrayNode<IJsonNode>();
			/*
			 * if(this.getBuffReader(textNewsFileContent) == null){
			 * log.error("exctraction of string from document failed"); }
			 */

			if (this.newsToProcess == null) {
				log.error("no news to process!!!!");
			}

			try {
				SimpleDateFormat sdfOld = new SimpleDateFormat(
						"EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
				SimpleDateFormat sdfNew = new SimpleDateFormat("yyyyMMddHHmmss");

				ObjectNode fullText = new ObjectNode();
				System.out
						.println("inside testNewsFile:" + textNewsFileContent);
				System.out.println("value of fullText:" + fullText.toString());
				fullText.put("Text",
						this.string2TextNode(textNewsFileContent.get(1)));

				fullText.put("byline", this.string2TextNode("")); // Test for
																	// sentence
																	// splitting

				fullText.put("copyright", this.string2TextNode("")); // Test for
																		// sentence
																		// splitting

				fullText.put("date", this.string2TextNode(convertFormat(sdfOld,
						sdfNew, textNewsFileContent.get(0)))); // Test
				// for
				// sentence
				// splitting

				fullText.put("dateline", this.string2TextNode("")); // Test for
																	// sentence
																	// splitting

				fullText.put("headline", this.string2TextNode("")); // Test for
																	// sentence
																	// splitting

				fullText.put("id", this.string2TextNode("")); // Test for
																// sentence
																// splitting

				fullText.put("itemId", this.string2TextNode("")); // Test for
																	// sentence
																	// splitting

				fullText.put("lang", this.string2TextNode("")); // Test for
																// sentence
																// splitting

				fullText.put("title", this.string2TextNode("")); // Test for
																	// sentence
																	// splitting
				annotations.add(fullText);

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

			res.put("annotations", annotations);
			// System.out.println("res is currently:" + res.toString());

			return res;
		}

		@Override
		public Class<? extends GenericInputSplit> getInputSplitType() {
			return ClusterNewsInputSplit.class;
		}

		@Override
		public GenericInputSplit[] createInputSplits(final int minNumSplits)
				throws IOException {
			return this.createDistributedInputSplits(minNumSplits);
		}

		private GenericInputSplit[] createDistributedInputSplits(
				int minNumSplits) {

			GenericInputSplit[] splits = new ClusterNewsInputSplit[minNumSplits];

			List<String> allFilesToProcess = new ArrayList<String>();

			File[] files = (new File(this.docName.replace("file://", "")))
					.listFiles();
			for (File file : files) {
				allFilesToProcess.add(file.getAbsolutePath());
			}
			List<List<String>> testNewsinside = new ArrayList<List<String>>();
			testNewsinside = this.getTSVBuffReader(allFilesToProcess.get(0));

			int numberOfDocumentsToProcess = testNewsinside.size(); // amount of
																	// docs
																	// calculated
																	// correctly
																	// System.out.println("Numbers of docs:"
																	// +
																	// numberOfDocumentsToProcess);

			float minNumSplitsDouble = (float) minNumSplits;
			for (int i = 0; i < minNumSplits; i++) {
				List<List<String>> newsToProcessWithinThisSplit = new ArrayList<List<String>>(
						testNewsinside.subList(
								Math.round(i
										* (numberOfDocumentsToProcess / minNumSplitsDouble)),
								Math.min(
										Math.round((i + 1)
												* (numberOfDocumentsToProcess / minNumSplitsDouble)),
										numberOfDocumentsToProcess)));
				splits[i] = new ClusterNewsInputSplit(i, allFilesToProcess,
						newsToProcessWithinThisSplit);
			}

			return splits;
		}
	}
	
	@Property(flag = false, expert = true, preferred = true)
	@Name(noun = "nametext")
	public void setDocumentName(EvaluationExpression documentName) {
		this.documentName = documentName.toString().replace("\"", "")
				.replace("\'", "");
	}

	@Override
	public PactModule asPactModule(EvaluationContext context) {
		context.setInputsAndOutputs(0, 1);
		GenericDataSource<?> contract = new GenericDataSource<ClusterInputFormat>(
				ClusterInputFormat.class, "TextNewsInput");

		final PactModule pactModule = new PactModule(0, 1);
		SopremoUtil.setObject(contract.getParameters(), SopremoUtil.CONTEXT,
				context);
		SopremoUtil.setObject(contract.getParameters(), DOCUMENT_NAME,
				this.documentName);

		pactModule.getOutput(0).setInput(contract);
		// System.out.println("print contract:" + contract.toString());
		return pactModule;
	}

}
