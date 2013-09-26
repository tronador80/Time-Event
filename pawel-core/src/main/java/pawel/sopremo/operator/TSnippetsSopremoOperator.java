/**
 * 
 */
package pawel.sopremo.operator;

import org.apache.log4j.Logger;

import pawel.algorithms.TSnippets;
import pawel.utils.JsonConverter;
import eu.stratosphere.nephele.configuration.Configuration;
import eu.stratosphere.pact.common.contract.MapContract;
import eu.stratosphere.pact.common.contract.ReduceContract;
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
import eu.stratosphere.sopremo.pact.SopremoReduce;
import eu.stratosphere.sopremo.pact.SopremoUtil;
import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.IStreamNode;
import eu.stratosphere.sopremo.type.MissingNode;
import eu.stratosphere.sopremo.type.ObjectNode;
import eu.stratosphere.sopremo.type.TextNode;

/**
 * Implementation of TSnippets algorithm. <br>
 * <br>
 * <b>IMPORTANT</b>: TSnippets is not depending on events contents, therefore
 * its input is result of TimeTaggerSopremoOperator and NOT result of
 * EventSopremoOperator
 * 
 * @author ptondryk
 * 
 */
@InputCardinality(1)
@OutputCardinality(1)
@Name(verb = "tsnippets")
public class TSnippetsSopremoOperator extends
		ElementaryOperator<TSnippetsSopremoOperator> {

	public static String ALPHA = "ALPHA";
	public static String BETA = "BETA";
	public static String GAMMA = "GAMMA";
	public static String EPSILON = "EPSILON";
	public static String THETA = "THETA";
	public static String QUERY = "query";
	public static String SENTENCE_NUM = "sentence_num";

	private Double alpha = 1.0;
	private Double beta = 1.0;
	private Double gamma = 1.0;
	private Double epsilon = 1.0;
	private Double theta = 1.0;
	private String query = null;

	/**
	 * amount of sentences that should be contained in summary
	 */
	private Integer sentenceNum = 4;

	private static Logger log = Logger
			.getLogger(TSnippetsSopremoOperator.class);

	public static class Mapper extends SopremoMap {

		private Double alpha;
		private Double beta;
		private Double gamma;
		private Double epsilon;
		private Double theta;
		private String query;
		private Integer sentenceNum;

		public void open(Configuration parameters) {
			super.open(parameters);
			this.alpha = SopremoUtil.getObject(parameters, ALPHA, null);
			this.beta = SopremoUtil.getObject(parameters, BETA, null);
			this.gamma = SopremoUtil.getObject(parameters, GAMMA, null);
			this.epsilon = SopremoUtil.getObject(parameters, EPSILON, null);
			this.theta = SopremoUtil.getObject(parameters, THETA, null);
			this.query = SopremoUtil.getObject(parameters, QUERY, null);
			this.sentenceNum = SopremoUtil.getObject(parameters, SENTENCE_NUM,
					null);
		}

		protected void map(final IJsonNode value, final JsonCollector out) {
			if (value instanceof ObjectNode) {
				ObjectNode object = (ObjectNode) value;

				String analyzedText = "";
				if (object.get("annotations") instanceof ArrayNode<?>)
					for (Object annotation : ((ArrayNode<?>) object
							.get("annotations"))) {
						if (annotation instanceof ObjectNode
								&& !(((ObjectNode) annotation).get("Text") instanceof MissingNode)) {
							analyzedText = ((ObjectNode) annotation)
									.get("Text").toString();
						}
					}

				ObjectNode res = new ObjectNode();
				res.put("analyzedText", new TextNode(analyzedText));

				res.put("summary",
						new TextNode(
								TSnippets.generateSummary(
										JsonConverter.parseAnnotations(object),
										alpha, beta, gamma, epsilon, theta,
										query, sentenceNum)));
				out.collect(res);

			} else {
				log.error("Error: Root element must be object!");
			}
		}

	}

	public static class Reducer extends SopremoReduce {

		public void open(Configuration parameters) throws Exception {
			super.open(parameters);
		}

		protected void reduce(IStreamNode<IJsonNode> values, JsonCollector out) {
			for (IJsonNode value : values) {
				out.collect(value);
			}
		}
	}

	@Override
	public PactModule asPactModule(EvaluationContext context) {
		context.setInputsAndOutputs(this.getNumInputs(), this.getNumOutputs());
		PactModule module = new PactModule(1, 1);
		MapContract.Builder builder = MapContract.builder(Mapper.class);
		builder.name("TSnippets.mapper");
		builder.input(module.getInput(0));
		MapContract mapcontract = builder.build();

		SopremoUtil.setObject(mapcontract.getParameters(), SopremoUtil.CONTEXT,
				context);
		SopremoUtil.setObject(mapcontract.getParameters(), ALPHA, this.alpha);
		SopremoUtil.setObject(mapcontract.getParameters(), BETA, this.beta);
		SopremoUtil.setObject(mapcontract.getParameters(), GAMMA, this.gamma);
		SopremoUtil.setObject(mapcontract.getParameters(), EPSILON,
				this.epsilon);
		SopremoUtil.setObject(mapcontract.getParameters(), THETA, this.theta);
		SopremoUtil.setObject(mapcontract.getParameters(), QUERY, this.query);
		SopremoUtil.setObject(mapcontract.getParameters(), SENTENCE_NUM,
				this.sentenceNum);

		ReduceContract.Builder reduceBuilder = ReduceContract
				.builder(Reducer.class);
		reduceBuilder.name("TSnippets.reducer");
		reduceBuilder.input(mapcontract);
		ReduceContract reduceContract = reduceBuilder.build();

		SopremoUtil.setObject(reduceContract.getParameters(),
				SopremoUtil.CONTEXT, context);

		module.getOutput(0).addInput(reduceContract);
		return module;
	}

	@Property(preferred = true)
	@Name(preposition = "alpha")
	public void setAlpha(EvaluationExpression alpha) {
		this.alpha = Double.parseDouble(alpha.toString());
	}

	@Property(preferred = true)
	@Name(preposition = "beta")
	public void setBeta(EvaluationExpression beta) {
		this.beta = Double.parseDouble(beta.toString());
	}

	@Property(preferred = true)
	@Name(preposition = "gamma")
	public void setGamma(EvaluationExpression gamma) {
		this.gamma = Double.parseDouble(gamma.toString());
	}

	@Property(preferred = true)
	@Name(preposition = "epsilon")
	public void setEpsilon(EvaluationExpression epsilon) {
		this.epsilon = Double.parseDouble(epsilon.toString());
	}

	@Property(preferred = true)
	@Name(preposition = "theta")
	public void setTheta(EvaluationExpression theta) {
		this.theta = Double.parseDouble(theta.toString());
	}

	@Property(preferred = true)
	@Name(preposition = "query")
	public void setQuery(EvaluationExpression query) {
		this.query = query.toString();
		log.debug("TSnippets query set to \"" + this.query + "\"");
	}

	@Property(preferred = true)
	@Name(preposition = "sentences")
	public void setSentenceNum(EvaluationExpression sentenceNum) {
		this.sentenceNum = Integer.parseInt(sentenceNum.toString());
	}
}
