/**
 * 
 */
package pawel.sopremo.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBException;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import pawel.model.sopremo.io.ReutersNewsInputSplit;
import pawel.sopremo.io.reutersnews.ReutersUnmarshaller;
import de.tu_berlin.dima.mia.reuters_model.jaxb2.Newsitem;
import eu.stratosphere.nephele.configuration.Configuration;
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
	protected static final String ID_FIRST_DOCUMENT = "id_first_document";
	protected static final String ID_LAST_DOCUMENT = "id_last_document";
	protected static final String HDFS_CONF_PATH = "hdfs_conf_path";

	/**
	 * name containing substring XYZ that will be replaced with
	 * news-document-id.
	 */
	private String documentName;

	/**
	 * 
	 */
	private String idOfFirstDocumentToProcess;

	/**
	 * 
	 */
	private String idOfLastDocuemtnToProcess;

	/**
	 * path to hdfs configuration files
	 */
	private String hdfsConfPath;

	/**
	 * 
	 * @author ptondryk
	 * 
	 */
	public static class ReutersNewsInputFormat extends GenericInputFormat {

		/**
		 * name of the files containing reuters news (file name should contain
		 * XYZ string at position where the document id should be inserted)
		 * starting with: file:// or hdfs://
		 */
		private String docName;

		/**
		 * Id of document that should be read from index in next
		 * <code>nextRecord</code> call.
		 */
		private int docId;

		/**
		 * Id of last document that should be processed within currently
		 * processed split.
		 */
		private int lastDocId;

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

		@Override
		public void configure(Configuration parameters) {
			super.configure(parameters);
			this.context = (EvaluationContext) SopremoUtil.getObject(
					parameters, SopremoUtil.CONTEXT, null);
			this.schema = this.context.getOutputSchema(0);
			this.docName = (String) SopremoUtil.getObject(parameters,
					DOCUMENT_NAME, null);
			this.docId = Integer.parseInt((String) SopremoUtil.getObject(
					parameters, ID_FIRST_DOCUMENT, "-1"));
			this.lastDocId = Integer.parseInt((String) SopremoUtil.getObject(
					parameters, ID_LAST_DOCUMENT, "-1"));
			this.hdfsConfPath = (String) SopremoUtil.getObject(parameters,
					HDFS_CONF_PATH, null);

			// if not set which files read -> read all
			if (this.docId == -1 || this.lastDocId == -1) {
				this.docId = 0;
				this.lastDocId = -1;
			}
		}

		@Override
		public boolean reachedEnd() throws IOException {
			if (lastDocId != -1) {
				return this.docId > lastDocId;
			} else {
				return this.docId > (new File(this.docName)).list().length;
			}
		}

		@Override
		public boolean nextRecord(PactRecord record) throws IOException {
			this.schema.jsonToRecord(
					this.xmlReutersNewsFile2jsonObjectNode(this.nextName()),
					record);
			return true;
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
				ReutersNewsInputSplit indexSplit = (ReutersNewsInputSplit) split;

				this.docId = indexSplit.getStartDocId();
				this.lastDocId = indexSplit.getEndDocId();

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
		 * this method returns name of next reuters news file that should be
		 * processed
		 * 
		 * @return name of file
		 */
		private String nextName() {
			if (this.lastDocId != -1) {
				return this.docName.replace("XYZ",
						(new Integer(this.docId++).toString()));
			} else {
				return (new File(this.docName)).list()[this.docId++];
			}
		}

		/**
		 * getter for HDFS configuration object
		 * 
		 * @return hdfs configuration object
		 */
		private org.apache.hadoop.conf.Configuration getConfiguration() {
			if (this.conf == null) {
				this.conf = new org.apache.hadoop.conf.Configuration();

				conf.addResource(new Path(hdfsConfPath + "core-site.xml"));
				conf.addResource(new Path(hdfsConfPath + "hdfs-site.xml"));
			}
			return conf;
		}

		/**
		 * this method parse the reuters news file given by
		 * <b>reutersNewsFileName</b> and creates an json object from it
		 * 
		 * @param reutersNewsFileName
		 * @return {@link IJsonNode} instance
		 */
		private IJsonNode xmlReutersNewsFile2jsonObjectNode(
				String reutersNewsFileName) {

			ObjectNode res = new ObjectNode();
			ArrayNode<IJsonNode> annotations = new ArrayNode<IJsonNode>();

			try {

				ReutersUnmarshaller ru = ReutersUnmarshaller.getInstance();
				Newsitem news = null;

				ObjectNode text = new ObjectNode();

				if (reutersNewsFileName.startsWith("hdfs")) {
					FileSystem fileSystem;
					try {
						fileSystem = FileSystem.get(this.getConfiguration());

						Path path = new Path(reutersNewsFileName);
						if (!fileSystem.exists(path)) {
							return text;
						}

						FSDataInputStream in = fileSystem.open(path);

						news = ru.unmarshall(in);
					} catch (IOException e) {
						log.error(e.getMessage(), e);
						return text;
					}

				} else {
					news = ru.unmarshall(new FileInputStream(new File(
							reutersNewsFileName)));
				}

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
				for (String p : news.getText().getP()) {
					if(textBuilder.length() > 0) {
						textBuilder.append(" ");
					}
					textBuilder.append(p);
				}
				text.put("Text", this.string2TextNode(textBuilder.toString()));

				annotations.add(text);

			} catch (JAXBException e) {
				log.error(e.getMessage(), e);
			} catch (FileNotFoundException e) {
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

			int numberOfDocumentsToProcess = lastDocId - docId;

			for (int i = 0; i < minNumSplits; i++) {
				splits[i] = new ReutersNewsInputSplit(
						i,
						this.docName,
						this.docId + i
								* (numberOfDocumentsToProcess / minNumSplits),
						this.docId
								+ Math.min(
										(i + 1)
												* (numberOfDocumentsToProcess / minNumSplits),
										numberOfDocumentsToProcess));
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
		SopremoUtil.setObject(contract.getParameters(), ID_FIRST_DOCUMENT,
				this.idOfFirstDocumentToProcess);
		SopremoUtil.setObject(contract.getParameters(), ID_LAST_DOCUMENT,
				this.idOfLastDocuemtnToProcess);
		SopremoUtil.setObject(contract.getParameters(), HDFS_CONF_PATH,
				this.hdfsConfPath);
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
	@Name(noun = "start")
	public void setIdOfFirstDocumentToProcess(
			EvaluationExpression idOfFirstDocumentToProcess) {
		this.idOfFirstDocumentToProcess = idOfFirstDocumentToProcess.toString()
				.replace("\"", "").replace("\'", "");
	}

	@Property(flag = false, expert = true, preferred = true)
	@Name(noun = "end")
	public void setIdOfLastDocuemtnToProcess(
			EvaluationExpression idOfLastDocuemtnToProcess) {
		this.idOfLastDocuemtnToProcess = idOfLastDocuemtnToProcess.toString()
				.replace("\"", "").replace("\'", "");
	}

	@Property(flag = false, expert = true, preferred = true)
	@Name(noun = "hdfsConfPath")
	public void setHdfsConfPath(EvaluationExpression hdfsConfPath) {
		this.hdfsConfPath = hdfsConfPath.toString().replace("\"", "")
				.replace("\'", "");
	}

}
