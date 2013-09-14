/**
 * 
 */
package pawel.sopremo.operator;

import org.apache.log4j.Logger;

import pawel.uima.annotator.sentencesplit.SentenceSplitterAnalysisComponent;
import pawel.utils.JsonConverter;
import eu.stratosphere.nephele.configuration.Configuration;
import eu.stratosphere.pact.common.contract.MapContract;
import eu.stratosphere.pact.common.plan.PactModule;
import eu.stratosphere.sopremo.EvaluationContext;
import eu.stratosphere.sopremo.expressions.EvaluationExpression;
import eu.stratosphere.sopremo.operator.ElementaryOperator;
import eu.stratosphere.sopremo.operator.InputCardinality;
import eu.stratosphere.sopremo.operator.Name;
import eu.stratosphere.sopremo.operator.OutputCardinality;
import eu.stratosphere.sopremo.operator.Property;
import eu.stratosphere.sopremo.pact.JsonCollector;
import eu.stratosphere.sopremo.pact.SopremoMap;
import eu.stratosphere.sopremo.pact.SopremoUtil;
import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.MissingNode;
import eu.stratosphere.sopremo.type.ObjectNode;

/**
 * Sopremo operator that allows sentence splitting.
 * 
 * @author pawel
 * 
 */
@InputCardinality(1)
@OutputCardinality(1)
@Name(verb = "split_sentences")
public class SentenceSplitterSopremoOperator extends
		ElementaryOperator<SentenceSplitterSopremoOperator> {

	private static Logger log = Logger
			.getLogger(SentenceSplitterSopremoOperator.class);

	private static String MAX_TOKENS_NUMBER = "maxTokensNumber";
	private static String MIN_TOKENS_NUMBER = "minTokensNumber";
	private static String MAX_SENTENCES_NUMBER = "maxSentencesNumber";
	private static String MIN_SENTENCES_NUMBER = "minSentencesNumber";

	private Integer maxTokensNumber = null;
	private Integer minTokensNumber = null;
	private Integer maxSentencesNumber = null;
	private Integer minSentencesNumber = null;

	/**
	 * 
	 * @author ptondryk
	 * 
	 */
	public static class Implementation extends SopremoMap {

		private Integer maxTokensNumber = null;
		private Integer minTokensNumber = null;
		private Integer maxSentencesNumber = null;
		private Integer minSentencesNumber = null;

		public void open(Configuration parameters) {
			super.open(parameters);

			this.maxTokensNumber = (Integer) SopremoUtil.getObject(parameters,
					MAX_TOKENS_NUMBER, null);
			this.minTokensNumber = (Integer) SopremoUtil.getObject(parameters,
					MIN_TOKENS_NUMBER, null);
			this.maxSentencesNumber = (Integer) SopremoUtil.getObject(
					parameters, MAX_SENTENCES_NUMBER, null);
			this.minSentencesNumber = (Integer) SopremoUtil.getObject(
					parameters, MIN_SENTENCES_NUMBER, null);
		}

		/**
		 * 
		 * @param value
		 * @param out
		 */
		protected void map(final IJsonNode value, final JsonCollector out) {
			if (value instanceof ObjectNode) {
				ObjectNode object = (ObjectNode) value;
				SentenceSplitterAnalysisComponent ssac = new SentenceSplitterAnalysisComponent();

				try {
					ObjectNode result = (ObjectNode) ssac
							.tokenize(JsonConverter.json2String(object));

					if (this.amountOfTokensOk(result)
							&& this.amountOfSentencesOk(result)) {
						out.collect(result);
					}
				} catch (Exception e) {
					log.error(e.getMessage());
				}

			} else {
				log.error("Error: Root must be an object!");
			}
		}

		/**
		 * 
		 * @param result
		 * @return
		 */
		private boolean amountOfTokensOk(ObjectNode result) {
			int tokensCounter = 0;

			IJsonNode shouldBeAnnotationsArray = result.get("annotations");
			if (shouldBeAnnotationsArray instanceof ArrayNode<?>) {
				ArrayNode<IJsonNode> annotations = (ArrayNode<IJsonNode>) shouldBeAnnotationsArray;
				for (int i = 0; i < annotations.size(); i++) {
					IJsonNode shouldBeAnnotation = annotations.get(i);
					if (shouldBeAnnotation instanceof ObjectNode) {
						ObjectNode annotation = (ObjectNode) shouldBeAnnotation;
						if (!(annotation.get("Token") instanceof MissingNode)) {
							tokensCounter++;
						}
					}
				}
			}

			return (this.minTokensNumber == null || tokensCounter >= this.minTokensNumber)
					&& (this.maxTokensNumber == null || tokensCounter <= this.maxTokensNumber);
		}

		/**
		 * 
		 * @param result
		 * @return
		 */
		private boolean amountOfSentencesOk(ObjectNode result) {
			int sentencesCounter = 0;

			IJsonNode shouldBeAnnotationsArray = result.get("annotations");
			if (shouldBeAnnotationsArray instanceof ArrayNode<?>) {
				ArrayNode<IJsonNode> annotations = (ArrayNode<IJsonNode>) shouldBeAnnotationsArray;
				for (int i = 0; i < annotations.size(); i++) {
					IJsonNode shouldBeAnnotation = annotations.get(i);
					if (shouldBeAnnotation instanceof ObjectNode) {
						ObjectNode annotation = (ObjectNode) shouldBeAnnotation;
						if (!(annotation.get("Sentence") instanceof MissingNode)) {
							sentencesCounter++;
						}
					}
				}
			}

			return (this.minSentencesNumber == null || sentencesCounter >= this.minSentencesNumber)
					&& (this.maxSentencesNumber == null || sentencesCounter <= this.maxSentencesNumber);
		}
	}

	@Override
	public PactModule asPactModule(EvaluationContext context) {
		context.setInputsAndOutputs(this.getNumInputs(), this.getNumOutputs());
		PactModule module = new PactModule(1, 1);
		MapContract.Builder builder = MapContract.builder(Implementation.class);
		builder.name("SentenceOperator");
		builder.input(module.getInput(0));
		MapContract mapcontract = builder.build();

		SopremoUtil.setObject(mapcontract.getParameters(), SopremoUtil.CONTEXT,
				context);

		SopremoUtil.setObject(mapcontract.getParameters(), MAX_TOKENS_NUMBER,
				this.maxTokensNumber);
		SopremoUtil.setObject(mapcontract.getParameters(), MIN_TOKENS_NUMBER,
				this.minTokensNumber);
		SopremoUtil.setObject(mapcontract.getParameters(),
				MAX_SENTENCES_NUMBER, this.maxSentencesNumber);
		SopremoUtil.setObject(mapcontract.getParameters(),
				MIN_SENTENCES_NUMBER, this.minSentencesNumber);
		module.getOutput(0).setInput(mapcontract);

		return module;
	}

	@Property(flag = false, expert = true, preferred = true)
	@Name(noun = "maxTokensNumber")
	public void setMaxTokensNumber(EvaluationExpression maxTokensNumber) {
		try {
			this.maxTokensNumber = Integer.parseInt(maxTokensNumber.toString()
					.replace("\"", "").replace("\'", ""));
		} catch (NumberFormatException nfe) {

		}
	}

	@Property(flag = false, expert = true, preferred = true)
	@Name(noun = "minTokensNumber")
	public void setMinTokensNumber(EvaluationExpression minTokensNumber) {
		try {
			this.minTokensNumber = Integer.parseInt(minTokensNumber.toString()
					.replace("\"", "").replace("\'", ""));
		} catch (NumberFormatException nfe) {

		}
	}

	@Property(flag = false, expert = true, preferred = true)
	@Name(noun = "maxSentencesNumber")
	public void setMaxSentencesNumber(EvaluationExpression maxSentencesNumber) {
		try {
			this.maxSentencesNumber = Integer.parseInt(maxSentencesNumber
					.toString().replace("\"", "").replace("\'", ""));
		} catch (NumberFormatException nfe) {

		}
	}

	@Property(flag = false, expert = true, preferred = true)
	@Name(noun = "minSentencesNumber")
	public void setMinSentencesNumber(EvaluationExpression minSentencesNumber) {
		try {
			this.minSentencesNumber = Integer.parseInt(minSentencesNumber
					.toString().replace("\"", "").replace("\'", ""));
		} catch (NumberFormatException nfe) {

		}
	}
}
