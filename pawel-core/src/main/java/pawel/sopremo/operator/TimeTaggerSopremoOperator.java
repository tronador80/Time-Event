/**
 * 
 */
package pawel.sopremo.operator;

import org.apache.log4j.Logger;

import pawel.uima.annotator.heideltime.HeidelTimeAnalysisComponent;
import pawel.uima.annotator.sutime.SuTimeAnalysisComponent;
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
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.IStreamNode;
import eu.stratosphere.sopremo.type.ObjectNode;

/**
 * This class realize Sopremo operator that can tag temporal expressions. It is
 * parameterizable. Engine can be choosed (sutime or heideltime). Heideltime
 * additionally has one more parameter: type (news or narrative) of text to
 * process.
 * 
 * @author ptondryk
 * 
 */
@InputCardinality(1)
@OutputCardinality(1)
@Name(verb = "tag_time")
public class TimeTaggerSopremoOperator extends
		ElementaryOperator<TimeTaggerSopremoOperator> {

	private static Logger log = Logger
			.getLogger(TimeTaggerSopremoOperator.class);

	public static final String ENGINE = "engine";
	public static final String TYPE_TO_PROCESS = "type_to_process";
	public static final String LANGUAGE = "language";

	/**
	 * engine used by time tagger (heideltime or sutime)
	 */
	private String engine;

	/**
	 * type of document to process (only for heideltime)
	 */
	private String typeToProcess;

	/**
	 * language of the input documents (only heideltime)
	 */
	private String language;

	/**
	 * This class is responsible for
	 * 
	 * @author ptondryk
	 * 
	 */
	public static class Implementation extends SopremoMap {

		private String typeToProcess;
		private String engine;
		private String language;

		public void open(Configuration parameters) {
			super.open(parameters);
			this.typeToProcess = SopremoUtil.getObject(parameters,
					TYPE_TO_PROCESS, "news");
			this.engine = SopremoUtil.getObject(parameters, ENGINE,
					"heideltime");
			this.language = SopremoUtil.getObject(parameters, LANGUAGE,
					"english");
		}

		protected void map(final IJsonNode value, final JsonCollector out) {
			if (value instanceof ObjectNode) {
				ObjectNode object = (ObjectNode) value;

				try {

					if (this.engine.contains("sutime")) {
						SuTimeAnalysisComponent stac = new SuTimeAnalysisComponent();
						out.collect(stac.tagTime(JsonConverter
								.json2String(object)));

					} else if (this.engine.contains("heideltime")) {
						HeidelTimeAnalysisComponent htac = new HeidelTimeAnalysisComponent();

						if (this.language == null) {
							this.language = "english";
						}
						if (this.typeToProcess == null) {
							this.typeToProcess = "news";
						}

						out.collect(htac.tagTime(
								JsonConverter.json2String(object),
								this.typeToProcess, this.language));
					} else {
						log.error("Unknown time tagger! Please select \"sutime\" or \"heideltime\".");
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			} else {
				log.error("Error: Root element mst be object!");
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
		builder.name("TimeOperator.mapper");
		builder.input(module.getInput(0));
		MapContract mapcontract = builder.build();

		SopremoUtil.setObject(mapcontract.getParameters(), SopremoUtil.CONTEXT,
				context);
		SopremoUtil.setObject(mapcontract.getParameters(), TYPE_TO_PROCESS,
				this.typeToProcess);
		SopremoUtil.setObject(mapcontract.getParameters(), ENGINE, this.engine);
		SopremoUtil.setObject(mapcontract.getParameters(), LANGUAGE,
				this.language);

		ReduceContract.Builder reduceBuilder = ReduceContract
				.builder(Reducer.class);
		reduceBuilder.name("TimeOperator.reducer");
		reduceBuilder.input(mapcontract);
		ReduceContract reduceContract = reduceBuilder.build();

		SopremoUtil.setObject(reduceContract.getParameters(),
				SopremoUtil.CONTEXT, context);

		module.getOutput(0).addInput(reduceContract);
		return module;
	}

	@Property(preferred = true)
	@Name(preposition = "engine")
	public void setEngine(EvaluationExpression engine) {
		this.engine = engine.toString().replaceAll("\"", "")
				.replaceAll("'", "");
	}

	@Property(preferred = true)
	@Name(preposition = "input_type")
	public void setType(EvaluationExpression typeToProcess) {
		this.typeToProcess = typeToProcess.toString().replaceAll("\"", "")
				.replaceAll("'", "");
	}

	@Property(preferred = true)
	@Name(preposition = "language")
	public void setLanguage(EvaluationExpression language) {
		this.language = language.toString().replaceAll("\"", "")
				.replaceAll("'", "");
	}

}
