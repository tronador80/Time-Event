/**
 * 
 */
package pawel.sopremo.io;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.FSDirectory;

import pawel.model.sopremo.io.LuceneIndexInputSplit;
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
 * This class provides access to lucene index for sopremo/meteor jobs.
 * 
 * @author ptondryk
 * 
 */
@InputCardinality(max = 0, min = 0)
@OutputCardinality(1)
@Name(verb = "index")
public class LuceneIndexAccess extends ElementaryOperator<LuceneIndexAccess> {

	private static Logger log = Logger.getLogger(LuceneIndexAccess.class);

	/**
	 * Constant that is used to reference the lucene index variable.
	 */
	protected static final String LUCENE_INDEX_DIR = "luceneIndexDir";

	/**
	 * Index directory
	 */
	private String luceneIndexDir;

	/**
	 * Lucene index input format for sopremo/meteor jobs.
	 * 
	 * @author pawel
	 * 
	 */
	public static class LuceneInputFormat extends GenericInputFormat {

		/**
		 * This variable holds reference to the used index.
		 */
		private DirectoryReader indexReader;

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
		 * Index directory (path to directory as String).
		 */
		private String luceneIndexDir;

		/**
		 * 
		 */
		private EvaluationContext context;

		/**
		 * 
		 */
		private Schema schema;

		@Override
		public void configure(Configuration parameters) {
			super.configure(parameters);
			this.context = (EvaluationContext) SopremoUtil.getObject(
					parameters, SopremoUtil.CONTEXT, null);
			this.schema = this.context.getOutputSchema(0);
			this.luceneIndexDir = (String) SopremoUtil.getObject(parameters,
					LUCENE_INDEX_DIR, null);
			log.debug("LuceneIndexAccess.LuceneInputFormat.configure called.");
			log.debug("luceneIndexDir = " + this.luceneIndexDir);
		}

		@Override
		public boolean reachedEnd() throws IOException {
			return this.docId == -1 || this.getIndexReader() == null;
		}

		@Override
		public boolean nextRecord(PactRecord record) throws IOException {
			if (this.getIndexReader() == null) {
				return false;
			}

			ObjectNode res = new ObjectNode();
			ArrayNode<IJsonNode> annotations = new ArrayNode<IJsonNode>();

			ObjectNode text = new ObjectNode();
			Document doc = this.getIndexReader().document(this.docId++);
			if (doc != null) {
				Iterator<IndexableField> iter = doc.iterator();
				while (iter.hasNext()) {
					IndexableField field = iter.next();
					if ("text".equalsIgnoreCase(field.name())) {
						text.put("Text", new TextNode(field.stringValue()
								.replace("\"", "'")));
					} else {
						text.put(field.name(), new TextNode(field.stringValue()
								.replace("\"", "'")));
					}
				}
			}

			annotations.add(text);
			res.put("annotations", annotations);

			if (this.getIndexReader().numDocs() <= this.docId
					|| this.docId >= this.lastDocId) {
				this.docId = -1;
			}

			this.schema.jsonToRecord(res, record);
			return true;
		}

		@Override
		public Class<? extends GenericInputSplit> getInputSplitType() {
			return LuceneIndexInputSplit.class;
		}

		@Override
		public void open(final GenericInputSplit split) {
			log.debug("Open new split (splitNumber = " + split.getSplitNumber()
					+ ").");
			if (split instanceof LuceneIndexInputSplit) {
				LuceneIndexInputSplit indexSplit = (LuceneIndexInputSplit) split;
				log.debug("Split is LuceneIndexInputSplit (index = \'"
						+ indexSplit.getLuceneIndexDir() + "\', start = "
						+ indexSplit.getStartDocId() + ", end = "
						+ indexSplit.getEndDocId() + ").");

				this.docId = indexSplit.getStartDocId();
				this.lastDocId = indexSplit.getEndDocId();

				if (this.luceneIndexDir == null
						|| this.luceneIndexDir.isEmpty()) {
					this.luceneIndexDir = indexSplit.getLuceneIndexDir();
				}
			} else {
				log.warn("Received split is not LuceneIndexInputSplit... This split will be ignored.");
			}
		}

		@Override
		public void close() {
			log.debug("Closing the input format LuceneInputFormat.");
			try {
				if (this.getIndexReader() != null) {
					this.getIndexReader().close();
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
			log.debug("Lucene index closed properly.");
		}

		@Override
		public GenericInputSplit[] createInputSplits(final int minNumSplits)
				throws IOException {
			return this.createDistributedInputSplits(minNumSplits);
		}

		public DirectoryReader getIndexReader() {
			if (this.indexReader == null) {
				try {
					if (this.luceneIndexDir.startsWith("hdfs")) {
						log.error("LuceneIndexAccess can't read index from HDFS!");
						return null;
					} else if (this.luceneIndexDir.startsWith("file")) {
						this.luceneIndexDir = this.luceneIndexDir.replace(
								"file://", "");
					}
					log.info("Reading from index in directory: "
							+ this.luceneIndexDir);
					this.indexReader = DirectoryReader.open(FSDirectory
							.open(new File(this.luceneIndexDir)));
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
			return indexReader;
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
			log.debug("Creating distributed splits for index \'"
					+ this.luceneIndexDir + "\'.");

			GenericInputSplit[] splits = new LuceneIndexInputSplit[minNumSplits];
			log.debug("Creating " + minNumSplits + " splits.");

			int numberOfDocumentsToProcess = this.getIndexReader() != null ? this
					.getIndexReader().numDocs() : 0;
			log.debug("Total number of documents in index to process.");

			for (int i = 0; i < minNumSplits; i++) {
				splits[i] = new LuceneIndexInputSplit(i, this.luceneIndexDir, i
						* (numberOfDocumentsToProcess / minNumSplits),
						Math.min((i + 1)
								* (numberOfDocumentsToProcess / minNumSplits),
								numberOfDocumentsToProcess));
				log.debug("Split number " + i + " added.");
				log.debug("Start index = "
						+ i
						* (numberOfDocumentsToProcess / minNumSplits)
						+ ", end index = "
						+ Math.min((i + 1)
								* (numberOfDocumentsToProcess / minNumSplits),
								numberOfDocumentsToProcess));
			}

			return splits;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((luceneIndexDir == null) ? 0 : luceneIndexDir.hashCode());
		return result;
	}

	@Override
	public PactModule asPactModule(EvaluationContext context) {
		context.setInputsAndOutputs(0, 1);
		GenericDataSource<?> contract = new GenericDataSource<LuceneInputFormat>(
				LuceneInputFormat.class, "LuceneIndexInput");

		final PactModule pactModule = new PactModule(0, 1);
		SopremoUtil.setObject(contract.getParameters(), SopremoUtil.CONTEXT,
				context);
		SopremoUtil.setObject(contract.getParameters(), LUCENE_INDEX_DIR,
				this.luceneIndexDir);
		pactModule.getOutput(0).setInput(contract);
		return pactModule;
	}

	/**
	 * setter for lucene index directory
	 * 
	 * @param luceneIndexDir
	 *            path to directory where the lucene index files are
	 */
	@Property(flag = false, expert = true, preferred = true)
	@Name(noun = "indexdir")
	public void setLuceneIndexDir(EvaluationExpression luceneIndexDir) {
		if (luceneIndexDir == null) {
			log.warn("Lucene Index directory should not be null!");
		}
		this.luceneIndexDir = luceneIndexDir.toString();
		this.luceneIndexDir = this.luceneIndexDir.replace("'", "");
	}
}
