/**
 * 
 */
package pawel.sopremo.operator;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.uima.UIMAException;

import pawel.uima.annotator.sentencesplit.SentenceSplitterAnalysisComponent;
import pawel.utils.JsonConverter;
import pawel.utils.Xmi2Json;
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
 * Sopremo operator that allows sentence splitting.
 * 
 * @author pawel
 * 
 */
@InputCardinality(1)
@OutputCardinality(1)
@Name(verb = "split_sentences")
public class SentenceSplitterSopremoOperator extends
		ElementaryOperator<SentenceSplitterSopremoOperator> {

	private static Logger log = Logger
			.getLogger(SentenceSplitterSopremoOperator.class);

	public static class Implementation extends SopremoMap {

		public void open(Configuration parameters) {
			super.open(parameters);
		}

		protected void map(final IJsonNode value, final JsonCollector out) {
			if (value instanceof ObjectNode) {
				ObjectNode object = (ObjectNode) value;
				IJsonNode node = object.get("text");
				if (node != null && node.isTextual()) {
					SentenceSplitterAnalysisComponent mt = new SentenceSplitterAnalysisComponent();
					try {
						out.collect(Xmi2Json.xmi2Json(mt.tokenize(JsonConverter
								.json2String(object))));
					} catch (UIMAException e) {
						log.error(e.getMessage());
					} catch (IOException e) {
						log.error(e.getMessage());
					}
				} else {
					log.error("Error: No \'text\' property, or \'text\' is not of type textual!");
				}

			} else {
				log.error("Error: Root must be an object!");
			}
		}
	}
}
