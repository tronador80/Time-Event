/**
 * 
 */
package pawel.sopremo.operator;

import org.apache.log4j.Logger;

import eu.stratosphere.nephele.configuration.Configuration;
import eu.stratosphere.pact.common.contract.MapContract;
import eu.stratosphere.pact.common.contract.ReduceContract;
import eu.stratosphere.pact.common.plan.PactModule;
import eu.stratosphere.sopremo.EvaluationContext;
import eu.stratosphere.sopremo.expressions.ObjectAccess;
import eu.stratosphere.sopremo.operator.ElementaryOperator;
import eu.stratosphere.sopremo.operator.InputCardinality;
import eu.stratosphere.sopremo.operator.Name;
import eu.stratosphere.sopremo.operator.OutputCardinality;
import eu.stratosphere.sopremo.operator.PactBuilderUtil;
import eu.stratosphere.sopremo.pact.JsonCollector;
import eu.stratosphere.sopremo.pact.SopremoMap;
import eu.stratosphere.sopremo.pact.SopremoReduce;
import eu.stratosphere.sopremo.pact.SopremoUtil;
import eu.stratosphere.sopremo.serialization.Schema;
import eu.stratosphere.sopremo.type.AbstractObjectNode;
import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.IStreamNode;
import eu.stratosphere.sopremo.type.MissingNode;
import eu.stratosphere.sopremo.type.ObjectNode;
import eu.stratosphere.sopremo.type.TextNode;

/**
 * This class contains implementation of an Sopremo operator that is able to
 * count amount of annotation in json file (json file in format that is output
 * from other operators).
 * 
 * @author ptondryk
 * 
 */
@InputCardinality(max = 1, min = 1)
@OutputCardinality(1)
@Name(verb = "count_annotations")
public class AnnotationsCounterSopremoOperator extends
		ElementaryOperator<AnnotationsCounterSopremoOperator> {

	private static Logger log = Logger
			.getLogger(AnnotationsCounterSopremoOperator.class);

	public AnnotationsCounterSopremoOperator() {
		this.setKeyExpressions(0, new ObjectAccess("annotation"));
	}

	/**
	 * 
	 * @author ptondryk
	 * 
	 */
	public static class Mapper extends SopremoMap {

		public void open(Configuration parameters) {
			super.open(parameters);
		}

		protected void map(final IJsonNode value, final JsonCollector out) {
			if (value.isObject()) {
				AbstractObjectNode inputNode = (AbstractObjectNode) value;
				IJsonNode annotationsNode = inputNode.get("annotations");
				IJsonNode eventsNode = inputNode.get("events");

				if (!(annotationsNode instanceof MissingNode)
						&& annotationsNode instanceof ArrayNode) {
					@SuppressWarnings("unchecked")
					ArrayNode<IJsonNode> annotations = (ArrayNode<IJsonNode>) annotationsNode;

					int text = 0;
					int sentences = 0;
					int tokens = 0;
					int timexs = 0;
					for (int i = 0; i < annotations.size(); i++) {
						IJsonNode node = annotations.get(i);

						if (node instanceof ObjectNode) {
							ObjectNode annotation = (ObjectNode) node;

							if (annotation.getFieldNames().contains("Text")) {
								text++;
							} else if (annotation.getFieldNames().contains(
									"Sentence")) {
								sentences++;
							} else if (annotation.getFieldNames().contains(
									"Token")) {
								tokens++;
							} else if (annotation.getFieldNames().contains(
									"Timex")) {
								timexs++;
							}
						}
					}

					ObjectNode textCounter = new ObjectNode();
					textCounter.put("annotation", new TextNode("text"));
					textCounter.put("amount",
							new TextNode((new Integer(text)).toString()));
					out.collect(textCounter);

					ObjectNode sentencesCounter = new ObjectNode();
					sentencesCounter.put("annotation",
							new TextNode("sentences"));
					sentencesCounter.put("amount", new TextNode((new Integer(
							sentences)).toString()));
					out.collect(sentencesCounter);

					ObjectNode tokensCounter = new ObjectNode();
					tokensCounter.put("annotation", new TextNode("tokens"));
					tokensCounter.put("amount", new TextNode((new Integer(
							tokens)).toString()));
					out.collect(tokensCounter);

					ObjectNode timexsCounter = new ObjectNode();
					timexsCounter.put("annotation", new TextNode("timexs"));
					timexsCounter.put("amount", new TextNode((new Integer(
							timexs)).toString()));
					out.collect(timexsCounter);

				}

				if (!(eventsNode instanceof MissingNode)
						&& eventsNode instanceof ArrayNode) {
					@SuppressWarnings("unchecked")
					ArrayNode<IJsonNode> events = (ArrayNode<IJsonNode>) eventsNode;
					ObjectNode result = new ObjectNode();
					result.put("annotation", new TextNode("event"));
					result.put(
							"amount",
							new TextNode((new Integer(events.size()))
									.toString()));
					out.collect(result);

					ObjectNode textCounter = new ObjectNode();
					textCounter.put("annotation", new TextNode("text"));
					textCounter.put("amount",
							new TextNode((new Integer(1)).toString()));
					out.collect(textCounter);
				}

			} else {
				log.error("Error: Root element must be object!");
			}
		}
	}

	/**
	 * 
	 * @author ptondryk
	 * 
	 */
	public static class Reducer extends SopremoReduce {

		public void open(Configuration parameters) throws Exception {
			super.open(parameters);
		}

		protected void reduce(IStreamNode<IJsonNode> values, JsonCollector out) {
			int counter = 0;
			String annotationType = null;
			for (IJsonNode node : values) {
				if (node.isObject()) {
					AbstractObjectNode annotationCounter = (AbstractObjectNode) node;
					if (annotationType == null) {
						annotationType = annotationCounter.get("annotation")
								.toString();
					}

					if (annotationCounter.get("amount") instanceof TextNode) {
						TextNode amountNode = (TextNode) annotationCounter
								.get("amount");

						try {
							counter += Integer.parseInt(amountNode.toString());
						} catch (NumberFormatException nfe) {

						}
					}
				}
			}

			ObjectNode result = new ObjectNode();
			result.put(annotationType,
					new TextNode((new Integer(counter)).toString()));
			out.collect(result);
		}
	}

	@Override
	public PactModule asPactModule(EvaluationContext context) {
		context.setInputsAndOutputs(this.getNumInputs(), this.getNumOutputs());

		PactModule module = new PactModule(1, 1);
		Schema globalSchema = context.getSchema();

		MapContract.Builder mapBuilder = MapContract.builder(Mapper.class);
		mapBuilder.name("AnnotationsCounter.mapper");
		mapBuilder.input(module.getInput(0));
		MapContract mapContract = mapBuilder.build();

		SopremoUtil.setObject(mapContract.getParameters(), SopremoUtil.CONTEXT,
				context);

		ReduceContract.Builder reduceBuilder = ReduceContract
				.builder(Reducer.class);
		reduceBuilder.name("AnnotationsCounter.reducer");
		reduceBuilder.input(mapContract);
		int[] keyIndices = this.getKeyIndices(globalSchema,
				this.getKeyExpressions(0));
		PactBuilderUtil.addKeys(reduceBuilder,
				this.getKeyClasses(globalSchema, keyIndices), keyIndices);
		ReduceContract reduceContract = reduceBuilder.build();

		SopremoUtil.setObject(reduceContract.getParameters(),
				SopremoUtil.CONTEXT, context);

		module.getOutput(0).addInput(reduceContract);
		return module;

	}
}
