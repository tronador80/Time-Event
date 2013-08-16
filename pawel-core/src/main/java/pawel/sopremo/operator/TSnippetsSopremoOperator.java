/**
 * 
 */
package pawel.sopremo.operator;

import org.apache.log4j.Logger;

import pawel.algorithms.TSnippets;
import pawel.utils.JsonConverter;
import eu.stratosphere.nephele.configuration.Configuration;
import eu.stratosphere.pact.common.contract.MapContract;
import eu.stratosphere.pact.common.plan.PactModule;
import eu.stratosphere.sopremo.EvaluationContext;
import eu.stratosphere.sopremo.expressions.ConstantExpression;
import eu.stratosphere.sopremo.expressions.EvaluationExpression;
import eu.stratosphere.sopremo.operator.ElementaryOperator;
import eu.stratosphere.sopremo.operator.InputCardinality;
import eu.stratosphere.sopremo.operator.Name;
import eu.stratosphere.sopremo.operator.OutputCardinality;
import eu.stratosphere.sopremo.operator.Property;
import eu.stratosphere.sopremo.pact.JsonCollector;
import eu.stratosphere.sopremo.pact.SopremoMap;
import eu.stratosphere.sopremo.pact.SopremoUtil;
import eu.stratosphere.sopremo.type.DecimalNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.ObjectNode;
import eu.stratosphere.sopremo.type.TextNode;

/**
 * Implementation of TSnippets algorithm. <br>
 * <br>
 * <b>IMPORTANT</b>: TSnippets is not depending on events contents, therefore
 * its input is result of TimeTaggerSopremoOperator and NOT result of
 * EventSopremoOperator
 * 
 * @author pawel
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

	public static class Implementation extends SopremoMap {

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
				TextNode tn = (TextNode) object.get("analyzedText");

				ObjectNode res = new ObjectNode();
				res.put("text", tn);

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
		SopremoUtil.setObject(mapcontract.getParameters(), ALPHA, this.alpha);
		SopremoUtil.setObject(mapcontract.getParameters(), BETA, this.beta);
		SopremoUtil.setObject(mapcontract.getParameters(), GAMMA, this.gamma);
		SopremoUtil.setObject(mapcontract.getParameters(), EPSILON,
				this.epsilon);
		SopremoUtil.setObject(mapcontract.getParameters(), THETA, this.theta);
		SopremoUtil.setObject(mapcontract.getParameters(), QUERY, this.query);
		SopremoUtil.setObject(mapcontract.getParameters(), SENTENCE_NUM,
				this.sentenceNum);

		module.getOutput(0).setInput(mapcontract);
		return module;
	}

	@Property(preferred = true)
	@Name(preposition = "alpha")
	public void setAlpha(EvaluationExpression alpha) {
		if (alpha instanceof ConstantExpression) {
			ConstantExpression constant = (ConstantExpression) alpha;
			DecimalNode dn = (DecimalNode) constant.getConstant();
			this.alpha = dn.getDoubleValue();
		}
	}

	@Property(preferred = true)
	@Name(preposition = "beta")
	public void setBeta(EvaluationExpression beta) {
		if (beta instanceof ConstantExpression) {
			ConstantExpression constant = (ConstantExpression) beta;
			DecimalNode dn = (DecimalNode) constant.getConstant();
			this.beta = dn.getDoubleValue();
		}
	}

	@Property(preferred = true)
	@Name(preposition = "gamma")
	public void setGamma(EvaluationExpression gamma) {
		if (gamma instanceof ConstantExpression) {
			ConstantExpression constant = (ConstantExpression) gamma;
			DecimalNode dn = (DecimalNode) constant.getConstant();
			this.gamma = dn.getDoubleValue();
		}
	}

	@Property(preferred = true)
	@Name(preposition = "epsilon")
	public void setEpsilon(EvaluationExpression epsilon) {
		if (epsilon instanceof ConstantExpression) {
			ConstantExpression constant = (ConstantExpression) epsilon;
			DecimalNode dn = (DecimalNode) constant.getConstant();
			this.epsilon = dn.getDoubleValue();
		}
	}

	@Property(preferred = true)
	@Name(preposition = "theta")
	public void setTheta(EvaluationExpression theta) {
		if (theta instanceof ConstantExpression) {
			ConstantExpression constant = (ConstantExpression) theta;
			DecimalNode dn = (DecimalNode) constant.getConstant();
			this.theta = dn.getDoubleValue();
		}
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
