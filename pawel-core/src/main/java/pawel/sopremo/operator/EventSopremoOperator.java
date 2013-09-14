/**
 * 
 */
package pawel.sopremo.operator;

import org.apache.log4j.Logger;

import pawel.uima.annotator.event.EventAnalysisComponent;
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
import eu.stratosphere.sopremo.type.TextNode;

/**
 * Sopremo operator that allows events tagging. Input of this operator is result
 * of {@link TimeTaggerSopremoOperator}.
 * 
 * @author pawel
 * 
 */
@InputCardinality(1)
@OutputCardinality(1)
@Name(verb = "tag_event")
public class EventSopremoOperator extends
		ElementaryOperator<EventSopremoOperator> {

	private static Logger log = Logger.getLogger(EventSopremoOperator.class);

	private static String MAX_SENTENCE_LENGTH = "maxSentenceLength";
	private static String MIN_SENTENCE_LENGTH = "minSentenceLength";

	private String maxSentenceLength;
	private String minSentenceLength;

	public static class Implementation extends SopremoMap {

		private String maxSentenceLength;
		private String minSentenceLength;

		public void open(Configuration parameters) {
			super.open(parameters);
			this.maxSentenceLength = SopremoUtil.getObject(parameters,
					MAX_SENTENCE_LENGTH, "10");
			this.minSentenceLength = SopremoUtil.getObject(parameters,
					MIN_SENTENCE_LENGTH, "230");

		}

		protected void map(final IJsonNode value, final JsonCollector out) {
			if (value instanceof ObjectNode) {
				ObjectNode object = (ObjectNode) value;

				if (!(object.get("annotations") instanceof ArrayNode<?>)) {
					log.error("Given object has no annotations attribute.");
					return;
				}

				// find annotated text
				String analyzedText = "";
				String timestamp = "";
				if (object.get("annotations") instanceof ArrayNode<?>)
					for (Object annotation : ((ArrayNode<?>) object
							.get("annotations"))) {
						if (annotation instanceof ObjectNode
								&& !(((ObjectNode) annotation).get("Text") instanceof MissingNode)) {
							analyzedText = ((ObjectNode) annotation)
									.get("Text").toString();
							timestamp = ((ObjectNode) annotation).get("date")
									.toString();
						}
					}

				EventAnalysisComponent eac = new EventAnalysisComponent();

				int maxLength = 230;
				try {
					maxLength = Integer.parseInt(maxSentenceLength);
				} catch (NumberFormatException nfe) {

				}
				int minLength = 10;
				try {
					minLength = Integer.parseInt(minSentenceLength);
				} catch (NumberFormatException nfe) {

				}

				try {
					ObjectNode res = (ObjectNode) eac.tagEvent(
							JsonConverter.json2String(object), maxLength,
							minLength);
					res.put("analyzedText", new TextNode(analyzedText));
					res.put("timestamp", new TextNode(timestamp));
					out.collect(res);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			} else {
				log.error("Root element must be object!");
			}
		}
	}

	@Override
	public PactModule asPactModule(EvaluationContext context) {
		context.setInputsAndOutputs(this.getNumInputs(), this.getNumOutputs());
		PactModule module = new PactModule(1, 1);
		MapContract.Builder builder = MapContract.builder(Implementation.class);
		builder.name("EventsOperator");
		builder.input(module.getInput(0));
		MapContract mapcontract = builder.build();

		SopremoUtil.setObject(mapcontract.getParameters(), SopremoUtil.CONTEXT,
				context);
		SopremoUtil.setObject(mapcontract.getParameters(), MAX_SENTENCE_LENGTH,
				this.maxSentenceLength);
		SopremoUtil.setObject(mapcontract.getParameters(), MIN_SENTENCE_LENGTH,
				this.minSentenceLength);
		module.getOutput(0).setInput(mapcontract);

		return module;
	}

	@Property(flag = false, expert = true, preferred = true)
	@Name(noun = "maxSentenceLength")
	public void setMaxSentenceLength(EvaluationExpression maxSentenceLength) {
		this.maxSentenceLength = maxSentenceLength.toString().replace("\"", "")
				.replace("\'", "");
	}

	@Property(flag = false, expert = true, preferred = true)
	@Name(noun = "minSentenceLength")
	public void setMinSentenceLength(EvaluationExpression minSentenceLength) {
		this.minSentenceLength = minSentenceLength.toString().replace("\"", "")
				.replace("\'", "");
	}

}
