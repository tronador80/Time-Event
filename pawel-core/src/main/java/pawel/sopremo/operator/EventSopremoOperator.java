/**
 * 
 */
package pawel.sopremo.operator;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.uima.UIMAException;

import pawel.uima.annotator.event.EventAnalysisComponent;
import pawel.utils.JsonConverter;
import eu.stratosphere.nephele.configuration.Configuration;
import eu.stratosphere.sopremo.operator.ElementaryOperator;
import eu.stratosphere.sopremo.operator.InputCardinality;
import eu.stratosphere.sopremo.operator.Name;
import eu.stratosphere.sopremo.operator.OutputCardinality;
import eu.stratosphere.sopremo.pact.JsonCollector;
import eu.stratosphere.sopremo.pact.SopremoMap;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.ObjectNode;

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

	public static class Implementation extends SopremoMap {

		public void open(Configuration parameters) {
			super.open(parameters);
		}

		protected void map(final IJsonNode value, final JsonCollector out) {
			if (value instanceof ObjectNode) {
				ObjectNode object = (ObjectNode) value;

				EventAnalysisComponent eac = new EventAnalysisComponent();

				try {
					out.collect(JsonConverter.string2Json(eac
							.tagEvent(JsonConverter.json2String(object))));
				} catch (UIMAException e) {
					log.error(e.getMessage(), e);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			} else {
				log.error("Root element must be object!");
			}
		}

	}

}
