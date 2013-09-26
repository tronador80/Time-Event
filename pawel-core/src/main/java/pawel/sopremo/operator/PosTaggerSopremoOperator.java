/**
 * 
 */
package pawel.sopremo.operator;

import org.apache.log4j.Logger;

import pawel.uima.annotator.pos.PosTaggerAnalysisComponent;
import pawel.utils.JsonConverter;
import eu.stratosphere.nephele.configuration.Configuration;
import eu.stratosphere.pact.common.contract.MapContract;
import eu.stratosphere.pact.common.contract.ReduceContract;
import eu.stratosphere.pact.common.plan.PactModule;
import eu.stratosphere.sopremo.EvaluationContext;
import eu.stratosphere.sopremo.operator.ElementaryOperator;
import eu.stratosphere.sopremo.operator.InputCardinality;
import eu.stratosphere.sopremo.operator.Name;
import eu.stratosphere.sopremo.operator.OutputCardinality;
import eu.stratosphere.sopremo.pact.JsonCollector;
import eu.stratosphere.sopremo.pact.SopremoMap;
import eu.stratosphere.sopremo.pact.SopremoReduce;
import eu.stratosphere.sopremo.pact.SopremoUtil;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.IStreamNode;
import eu.stratosphere.sopremo.type.ObjectNode;

/**
 * Sopremo operator that is responsible for POS tagging. Input of this operator
 * is {@link SentenceSplitterSopremoOperator}.
 * 
 * @author ptondryk
 * 
 */
@InputCardinality(1)
@OutputCardinality(1)
@Name(verb = "tag_pos")
public class PosTaggerSopremoOperator extends
		ElementaryOperator<PosTaggerSopremoOperator> {

	private static Logger log = Logger
			.getLogger(PosTaggerSopremoOperator.class);

	public static class Implementation extends SopremoMap {

		public void open(Configuration parameters) {
			super.open(parameters);
		}

		protected void map(final IJsonNode value, final JsonCollector out) {
			if (value instanceof ObjectNode) {
				ObjectNode object = (ObjectNode) value;

				PosTaggerAnalysisComponent ptac = new PosTaggerAnalysisComponent();

				try {
					out.collect(ptac.tagPos(JsonConverter.json2String(object)));
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
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
		MapContract.Builder builder = MapContract.builder(Implementation.class);
		builder.name("PosOperator.mapper");
		builder.input(module.getInput(0));
		MapContract mapcontract = builder.build();

		SopremoUtil.setObject(mapcontract.getParameters(), SopremoUtil.CONTEXT,
				context);

		ReduceContract.Builder reduceBuilder = ReduceContract
				.builder(Reducer.class);
		reduceBuilder.name("PosOperator.reducer");
		reduceBuilder.input(mapcontract);
		ReduceContract reduceContract = reduceBuilder.build();

		SopremoUtil.setObject(reduceContract.getParameters(),
				SopremoUtil.CONTEXT, context);

		module.getOutput(0).addInput(reduceContract);
		return module;
	}

}
