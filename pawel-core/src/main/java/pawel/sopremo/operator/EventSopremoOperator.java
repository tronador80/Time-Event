/**
 * 
 */
package pawel.sopremo.operator;

import org.apache.log4j.Logger;

import pawel.uima.annotator.event.EventAnalysisComponent;
import pawel.utils.JsonConverter;
import eu.stratosphere.nephele.configuration.Configuration;
import eu.stratosphere.sopremo.operator.ElementaryOperator;
import eu.stratosphere.sopremo.operator.InputCardinality;
import eu.stratosphere.sopremo.operator.Name;
import eu.stratosphere.sopremo.operator.OutputCardinality;
import eu.stratosphere.sopremo.pact.JsonCollector;
import eu.stratosphere.sopremo.pact.SopremoMap;
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

	public static class Implementation extends SopremoMap {

		public void open(Configuration parameters) {
			super.open(parameters);
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
				for (Object annotation : ((ArrayNode) object.get("annotations"))) {
					if (annotation instanceof ObjectNode
							&& !(((ObjectNode) annotation).get("Text") instanceof MissingNode)) {
						analyzedText = ((ObjectNode) annotation).get("Text")
								.toString();
						timestamp = ((ObjectNode) annotation).get("date")
								.toString();
					}
				}

				EventAnalysisComponent eac = new EventAnalysisComponent();

				try {
					ObjectNode res = (ObjectNode) JsonConverter.string2Json(eac
							.tagEvent(JsonConverter.json2String(object)));
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

}
