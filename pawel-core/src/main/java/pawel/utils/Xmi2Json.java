/**
 * 
 */
package pawel.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.ObjectNode;
import eu.stratosphere.sopremo.type.TextNode;

/**
 * @author pawel
 * 
 */
public class Xmi2Json {

	private static Logger log = Logger.getLogger(Xmi2Json.class);

	/**
	 * Method converts <b>xmiString</b> into {@link IJsonNode} object.
	 * 
	 * @param xmiString
	 *            xmi formatted string (containing annotations)
	 * @param analyzedText
	 *            text that have been annotated and is represented by
	 *            <b>xmiString</b>
	 * @return {@link IJsonNode} object
	 */
	public static IJsonNode xmi2Json(String xmiString) {

		ObjectNode res = new ObjectNode();
		ArrayNode<ObjectNode> array = new ArrayNode<ObjectNode>();

		res.put("annotations", array);
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new BufferedInputStream(
					new ByteArrayInputStream(xmiString.getBytes())));

			doc.getDocumentElement().normalize();

			Node node = doc.getFirstChild();

			for (int i = 0; i < node.getChildNodes().getLength(); i++) {

				Node nNode = node.getChildNodes().item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE
						&& (nNode.getNodeName().startsWith("pawel:") || (nNode
								.getNodeName().startsWith("heideltime:")))) {

					Element eElement = (Element) nNode;
					ObjectNode outputObject = new ObjectNode();

					outputObject.put("begin",
							new TextNode(eElement.getAttribute("begin")));
					outputObject.put("end",
							new TextNode(eElement.getAttribute("end")));

					for (int j = 0; j < eElement.getAttributes().getLength(); j++) {
						String attrName = eElement.getAttributes().item(j)
								.getNodeName();
						if (!attrName.equals("begin")
								&& !attrName.equals("end")
								&& !attrName.equals("xmi:id")
								&& !attrName.equals("sofa")) {
							outputObject.put(
									attrName,
									new TextNode(eElement
											.getAttribute(attrName)));
						}
					}

					if (!outputObject.isMissing()) {
						array.add(outputObject);
					}
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return res;
	}
}
