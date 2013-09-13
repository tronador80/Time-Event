/**
 * 
 */
package pawel.sopremo.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import pawel.model.sopremo.io.ReutersNewsInputSplit;
import pawel.sopremo.io.reutersnews.ReutersUnmarshaller;
import de.tu_berlin.dima.mia.reuters_model.jaxb2.Newsitem;
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
 * This class provides access to reuters news stored as xml files.
 * 
 * @author ptondryk
 * 
 */
@InputCardinality(max = 0, min = 0)
@OutputCardinality(1)
@Name(verb = "access_reuters_news")
public class ReutersNewsAccess extends ElementaryOperator<LuceneIndexAccess> {

	private static Logger log = Logger.getLogger(ReutersNewsAccess.class);

	protected static final String DOCUMENT_NAME = "document_id";
	protected static final String HDFS_CONF_PATH = "hdfs_conf_path";
	protected static final String BIG = "big";

	/**
	 * name containing substring XYZ that will be replaced with
	 * news-document-id.
	 */
	private String documentName;

	/**
	 * path to hdfs configuration files
	 */
	private String hdfsConfPath;

	/**
	 * this variable indicates whether the input files are composition of many
	 * news (true) or single news (false)
	 */
	private boolean big;

	/**
	 * 
	 * @author ptondryk
	 * 
	 */
	public static class ReutersNewsInputFormat extends GenericInputFormat {

		/**
		 * This constant represents the split marker that is used to mark where
		 * one news ends and next starts.
		 */
		private static final String SPLIT_MARKER = "ABCDEFGHIJKSPLIT";

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
		private EvaluationContext context;

		/**
		 * 
		 */
		private Schema schema;

		/**
		 * 
		 */
		private org.apache.hadoop.conf.Configuration conf;

		/**
		 * path to hdfs configuration files
		 */
		private String hdfsConfPath;

		/**
		 * this list contains names of all files that should be processed
		 */
		private List<String> filesToProcess;

		/**
		 * this variable indicates whether the input files are composition of
		 * many news (true) or single news (false)
		 */
		private boolean big;

		/**
		 * this list contains content of all news that belong to currently
		 * processed file
		 */
		private List<String> newsToProcess;

		@Override
		public void configure(
				eu.stratosphere.nephele.configuration.Configuration parameters) {
			super.configure(parameters);
			this.context = (EvaluationContext) SopremoUtil.getObject(
					parameters, SopremoUtil.CONTEXT, null);
			this.schema = this.context.getOutputSchema(0);
			this.docName = (String) SopremoUtil.getObject(parameters,
					DOCUMENT_NAME, null);
			this.big = (Boolean) SopremoUtil.getObject(parameters, BIG, true);

			// set the hadoop installation path
			this.hdfsConfPath = (String) SopremoUtil.getObject(parameters,
					HDFS_CONF_PATH, null);
			if (this.hdfsConfPath == null || this.hdfsConfPath.isEmpty()) {
				this.hdfsConfPath = System.getenv().get("HADOOP_HOME");
			}

			if (big) {
				this.newsToProcess = new ArrayList<String>();
			}
		}

		@Override
		public boolean reachedEnd() throws IOException {
			return (this.docId >= this.getFilesToPorcess().size() && (this.newsToProcess == null || this.newsToProcess
					.isEmpty()));
		}

		@Override
		public boolean nextRecord(PactRecord record) throws IOException {
			IJsonNode newNode = null;
			if (this.big) {
				if (this.newsToProcess.isEmpty()) {
					String contentOfNextBigFile = this.getFileContent(this
							.nextName());
					String[] splitsOfBigFile = contentOfNextBigFile
							.split(ReutersNewsInputFormat.SPLIT_MARKER);
					for (String contentOfSingleNews : splitsOfBigFile) {
						if (contentOfSingleNews != null
								&& !contentOfSingleNews.isEmpty()) {
							this.newsToProcess.add(contentOfSingleNews);
						}
					}
				}
				newNode = this
						.xmlReutersNewsFile2jsonObjectNode(this.newsToProcess
								.remove(0));
			} else {
				newNode = this.xmlReutersNewsFile2jsonObjectNode(this
						.getFileContent(this.nextName()));
			}

			if (newNode != null) {
				this.schema.jsonToRecord(newNode, record);
				return true;
			}
			return false;
		}

		@Override
		public Class<? extends GenericInputSplit> getInputSplitType() {
			return ReutersNewsInputSplit.class;
		}

		@Override
		public void open(final GenericInputSplit split) {
			log.debug("Open new split (splitNumber = " + split.getSplitNumber()
					+ ").");
			if (split instanceof ReutersNewsInputSplit) {
				ReutersNewsInputSplit reutersNewsSplit = (ReutersNewsInputSplit) split;

				this.docId = 0;
				this.getFilesToPorcess().addAll(
						reutersNewsSplit.getFilesToProcess());
				this.big = reutersNewsSplit.getBig();
				if (big && this.newsToProcess == null) {
					this.newsToProcess = new ArrayList<String>();
				}

			} else {
				log.warn("Received split is not ReutersNewsInputSplit... This split will be ignored.");
			}
		}

		@Override
		public void close() {
			log.debug("The input format ReutersNewAccess closed.");

		}

		@Override
		public GenericInputSplit[] createInputSplits(final int minNumSplits)
				throws IOException {
			return this.createDistributedInputSplits(minNumSplits);
		}

		/**
		 * getter for HDFS configuration object
		 * 
		 * @return hdfs configuration object
		 */
		private org.apache.hadoop.conf.Configuration getConfiguration() {
			if (this.conf == null) {
				this.conf = new org.apache.hadoop.conf.Configuration();

				conf.addResource(new Path(hdfsConfPath
						+ "/etc/hadoop/core-site.xml"));
				conf.addResource(new Path(hdfsConfPath
						+ "/etc/hadoop/hdfs-site.xml"));
				conf.addResource(new Path(hdfsConfPath + "/conf/core-site.xml"));
				conf.addResource(new Path(hdfsConfPath + "/conf/hdfs-site.xml"));
			}
			return conf;
		}

		/**
		 * this method returns the names of files that should be processed
		 * 
		 * @return list of file names
		 */
		private List<String> getFilesToPorcess() {
			if (this.filesToProcess == null) {
				this.filesToProcess = new ArrayList<String>();
			}
			return this.filesToProcess;
		}

		/**
		 * this method reads content of file given by <b>fileName</b>.
		 * 
		 * @param fileName
		 * @return content of file as string
		 */
		private String getFileContent(String fileName) {
			String fileContent = null;

			if (fileName.startsWith("hdfs")) {
				FileSystem fileSystem;
				try {
					fileSystem = FileSystem.get(this.getConfiguration());

					Path path = new Path(fileName);
					if (!fileSystem.exists(path)) {
						return null;
					}

					FSDataInputStream in = fileSystem.open(path);
					fileContent = IOUtils.toString(in);

				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			} else {
				try {
					FileInputStream fos = new FileInputStream(fileName);
					fileContent = IOUtils.toString(fos);
					fos.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
			return fileContent;
		}

		/**
		 * this method returns name of next reuters news file that should be
		 * processed
		 * 
		 * @return name of file
		 */
		private String nextName() {
			return this.getFilesToPorcess().get(this.docId++);
		}

		/**
		 * this method parse the reuters news content given in
		 * <b>reutersNewsFileName</b> and creates an json object from it
		 * 
		 * @param reutersNewsFileContent
		 * @return {@link IJsonNode} instance
		 */
		private IJsonNode xmlReutersNewsFile2jsonObjectNode(
				String reutersNewsFileContent) {

			if (reutersNewsFileContent == null) {
				return null;
			}

			ObjectNode res = new ObjectNode();
			ArrayNode<IJsonNode> annotations = new ArrayNode<IJsonNode>();

			try {
				ObjectNode text = new ObjectNode();

				ReutersUnmarshaller ru = ReutersUnmarshaller.getInstance();
				Newsitem news = ru.unmarshall(IOUtils
						.toInputStream(reutersNewsFileContent));

				if (news == null) {
					return text;
				}

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

				text.put("title", this.string2TextNode(news.getTitle()));
				text.put("byline", this.string2TextNode(news.getByline()));
				text.put("copyright", this.string2TextNode(news.getCopyright()));
				text.put("dateline", this.string2TextNode(news.getDateline()));
				text.put("headline", this.string2TextNode(news.getHeadline()));
				text.put("id", this.string2TextNode(news.getId()));
				text.put("lang", this.string2TextNode(news.getLang()));
				text.put(
						"date",
						this.string2TextNode(sdf.format(news.getDate()
								.toGregorianCalendar().getTime())));
				text.put("itemId",
						this.string2TextNode(news.getItemid().toString()));

				StringBuilder textBuilder = new StringBuilder();
				for (int i = 0; i < news.getText().getP().size(); i++) {
					String p = news.getText().getP().get(i);
					if (i == (news.getText().getP().size() - 1)
							&& p.contains("--")) {
						continue;
					}
					if (textBuilder.length() > 0) {
						textBuilder.append(" ");
					}
					textBuilder.append(p);
				}
				text.put("Text", this.string2TextNode(textBuilder.toString()));

				annotations.add(text);

			} catch (JAXBException e) {
				log.error(e.getMessage(), e);
			}

			res.put("annotations", annotations);

			return res;
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
		 * Method creates input splits that represent part of lucene index (each
		 * split contains start and end document index - all document within
		 * this split should be processed).
		 * 
		 * @param minNumSplits
		 *            minimal number of splits that should be created
		 * @return splits that should be processed
		 */
		private GenericInputSplit[] createDistributedInputSplits(
				int minNumSplits) {

			GenericInputSplit[] splits = new ReutersNewsInputSplit[minNumSplits];

			List<String> allFilesToProcess = new ArrayList<String>();
			if (this.docName.startsWith("hdfs")) {
				FileSystem fileSystem;
				try {
					fileSystem = FileSystem.get(this.getConfiguration());
					FileStatus[] fileStates = fileSystem.listStatus(new Path(
							this.docName));
					for (FileStatus fileStatus : fileStates) {
						allFilesToProcess.add(fileStatus.getPath().toString());
					}

				} catch (IOException e) {
					log.error(e.getMessage(), e);
					return null;
				}

			} else {
				File[] files = (new File(this.docName)).listFiles();
				for (File file : files) {
					allFilesToProcess.add(file.getAbsolutePath());
				}
			}

			int numberOfDocumentsToProcess = allFilesToProcess.size();

			float minNumSplitsDouble = (float) minNumSplits;
			for (int i = 0; i < minNumSplits; i++) {
				List<String> filesToProcessWithinThisSplit = new ArrayList<String>(
						allFilesToProcess.subList(
								Math.round(i
										* (numberOfDocumentsToProcess / minNumSplitsDouble)),
								Math.min(
										Math.round((i + 1)
												* (numberOfDocumentsToProcess / minNumSplitsDouble)),
										numberOfDocumentsToProcess)));
				splits[i] = new ReutersNewsInputSplit(i,
						filesToProcessWithinThisSplit, this.big);

			}

			return splits;
		}
	}

	@Override
	public PactModule asPactModule(EvaluationContext context) {
		context.setInputsAndOutputs(0, 1);
		GenericDataSource<?> contract = new GenericDataSource<ReutersNewsInputFormat>(
				ReutersNewsInputFormat.class, "Reuters News Index Input");

		final PactModule pactModule = new PactModule(0, 1);
		SopremoUtil.setObject(contract.getParameters(), SopremoUtil.CONTEXT,
				context);
		SopremoUtil.setObject(contract.getParameters(), DOCUMENT_NAME,
				this.documentName);
		SopremoUtil.setObject(contract.getParameters(), HDFS_CONF_PATH,
				this.hdfsConfPath);
		SopremoUtil.setObject(contract.getParameters(), BIG, this.big);
		pactModule.getOutput(0).setInput(contract);
		return pactModule;
	}

	@Property(flag = false, expert = true, preferred = true)
	@Name(noun = "name")
	public void setDocumentName(EvaluationExpression documentName) {
		this.documentName = documentName.toString().replace("\"", "")
				.replace("\'", "");
	}

	@Property(flag = false, expert = true, preferred = true)
	@Name(noun = "hadoop_home")
	public void setHdfsConfPath(EvaluationExpression hdfsConfPath) {
		this.hdfsConfPath = hdfsConfPath.toString().replace("\"", "")
				.replace("\'", "");
	}

	@Property(flag = true)
	@Name(noun = "big")
	public void setBig(EvaluationExpression big) {
		String tmpBig = big.toString().replace("\"", "").replace("\'", "");
		if (tmpBig.contains("false")) {
			this.big = false;
		} else {
			this.big = true;
		}
	}
}
