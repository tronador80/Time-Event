/**
 * 
 */
package pawel.sopremo.operator;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.uima.UIMAException;

import pawel.uima.annotator.heideltime.HeidelTimeAnalysisComponent;
import pawel.uima.annotator.sutime.SuTimeAnalysisComponent;
import pawel.utils.JsonConverter;
import pawel.utils.Xmi2Json;
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
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.ObjectNode;

/**
 * This class realize Sopremo operator that can tag temporal expressions. It is
 * parameterizable. Engine can be choosed (sutime or heideltime). Heideltime
 * additionally has one more parameter: type (news or narrative) of text to
 * process.
 * 
 * @author pawel
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
	private String engine;

	public static final String TYPE_TO_PROCESS = "type_to_process";
	private String typeToProcess;

	public static class Implementation extends SopremoMap {

		private String typeToProcess;
		private String engine;

		public void open(Configuration parameters) {
			super.open(parameters);
			this.typeToProcess = SopremoUtil.getObject(parameters,
					TYPE_TO_PROCESS, null);
			this.engine = SopremoUtil.getObject(parameters, ENGINE, null);

			if (this.engine == null) {
				log.info("Engine not specified, using default: heideltime.");
			} else if (this.engine.contains("heideltime")
					|| this.engine.contains("sutime")) {
				log.info("Engine specified, using time tagging engine: "
						+ this.engine + ".");
			} else {
				log.warn("Unknown time tagging engine: " + this.engine + ".");
			}
		}

		protected void map(final IJsonNode value, final JsonCollector out) {
			if (value instanceof ObjectNode) {
				ObjectNode object = (ObjectNode) value;

				try {

					if (this.getEngine().contains("sutime")) {
						SuTimeAnalysisComponent stac = new SuTimeAnalysisComponent();
						out.collect(Xmi2Json.xmi2Json(stac
								.tagTime(JsonConverter.json2String(object))));

					} else if (this.getEngine().contains("heideltime")) {
						HeidelTimeAnalysisComponent htac = new HeidelTimeAnalysisComponent();

						out.collect(Xmi2Json.xmi2Json(htac.tagTime(
								JsonConverter.json2String(object),
								this.getTypeToProcess())));
					} else {
						log.error("Unknown time tagger! Please select \"sutime\" or \"heideltime\".");
					}
				} catch (UIMAException e) {
					log.error(e.getMessage(), e);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			} else {
				log.error("Error: Root element mst be object!");
			}
		}

		public String getTypeToProcess() {
			if (typeToProcess == null) {
				return "news";
			}
			return typeToProcess;
		}

		public String getEngine() {
			if (this.engine == null) {
				return "heideltime";
			}
			return engine;
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
		SopremoUtil.setObject(mapcontract.getParameters(), TYPE_TO_PROCESS,
				this.typeToProcess);
		SopremoUtil.setObject(mapcontract.getParameters(), ENGINE, this.engine);

		module.getOutput(0).setInput(mapcontract);
		return module;
	}

	@Property(preferred = true)
	@Name(preposition = "engine")
	public void setEngine(EvaluationExpression engine) {
		this.engine = engine.toString();
	}

	@Property(preferred = true)
	@Name(preposition = "input_type")
	public void setType(EvaluationExpression typeToProcess) {
		this.typeToProcess = typeToProcess.toString();
		log.debug("Type of document to process (for heidel time) is set to \""
				+ this.typeToProcess + "\"");
	}

}
