/**
 * 
 */
package pawel.sopremo.operator;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pawel.algorithms.TemporalSummaries;
import pawel.model.Sentence2;
import pawel.utils.JsonConverter;
import pawel.utils.SentencesSelector;
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
import eu.stratosphere.sopremo.type.ObjectNode;
import eu.stratosphere.sopremo.type.TextNode;

/**
 * This class implements other algorithm for temporal summaries generation then
 * TSnippets that is additionally based on event analysis (content analysis). <br>
 * <br>
 * For every sentence it calculate its usefulness and novelty using formulas
 * from "Temporal Summaries of News Topics" paper.
 * 
 * @author pawel
 * 
 */
@InputCardinality(1)
@OutputCardinality(1)
@Name(verb = "t_summary")
public class TemporalSummariesSopremoOperator extends
		ElementaryOperator<TemporalSummariesSopremoOperator> {

	public static String SENTENCE_NUM = "sentence_num";

	/**
	 * amount of sentences that should be contained in summary
	 */
	private Integer sentenceNum = 4;

	private static Logger log = Logger
			.getLogger(TemporalSummariesSopremoOperator.class);

	public static class Implementation extends SopremoMap {

		private Integer sentenceNum;

		public void open(Configuration parameters) {
			super.open(parameters);
			this.sentenceNum = SopremoUtil.getObject(parameters, SENTENCE_NUM,
					null);
		}

		protected void map(final IJsonNode value, final JsonCollector out) {
			if (value instanceof ObjectNode) {
				ObjectNode object = (ObjectNode) value;

				TextNode tn = (TextNode) object.get("analyzedText");
				ArrayNode<?> array = (ArrayNode<?>) object.get("events");
				List<Sentence2> sentences = new ArrayList<Sentence2>();

				String completeText = "";

				ObjectNode res = new ObjectNode();
				res.put("text", tn);

				for (int i = 0; i < array.size(); i++) {
					IJsonNode node = array.get(i);
					if (node instanceof ObjectNode) {
						ObjectNode eventNode = (ObjectNode) node;
						Sentence2 sentence = JsonConverter
								.eventNode2Sentence(eventNode);

						if (sentence != null) {
							sentences.add(sentence);
							completeText += " " + sentence.getSentenceText();
						}
					}
				}

				String summary = SentencesSelector
						.listToTimeSortedString(SentencesSelector
								.selectSentencesWithHighestRank(
										TemporalSummaries.rankSentences(
												sentences, completeText),
										sentenceNum));

				res.put("summary", new TextNode(summary));
				out.collect(res);
			} else {
				log.error("Error: Root element must be object!");
			}
		}

	}

	@Override
	public PactModule asPactModule(EvaluationContext context) {
		context.setInputsAndOutputs(this.getNumInputs(), this.getNumOutputs());
		PactModule module = new PactModule(1, 1);
		MapContract.Builder builder = MapContract.builder(Implementation.class);
		builder.name(this.toString());
		builder.input(module.getInput(0));
		MapContract mapcontract = builder.build();

		SopremoUtil.setObject(mapcontract.getParameters(), SopremoUtil.CONTEXT,
				context);
		SopremoUtil.setObject(mapcontract.getParameters(), SENTENCE_NUM,
				this.sentenceNum);

		module.getOutput(0).setInput(mapcontract);
		return module;
	}

	/**
	 * 
	 * @param sentenceNum
	 */
	@Property(preferred = true)
	@Name(preposition = "sentences")
	public void setSentenceNum(EvaluationExpression sentenceNum) {
		this.sentenceNum = Integer.parseInt(sentenceNum.toString());
	}
}
