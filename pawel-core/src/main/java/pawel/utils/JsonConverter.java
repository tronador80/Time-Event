/**
 * 
 */
package pawel.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import pawel.model.Sentence;
import pawel.model.Timex3;
import pawel.model.Token;
import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.MissingNode;
import eu.stratosphere.sopremo.type.ObjectNode;
import eu.stratosphere.sopremo.type.TextNode;

/**
 * Class provides functionality for some json conversions.
 * 
 * @author ptondryk
 * 
 */
public class JsonConverter {

	private static Logger log = Logger.getLogger(JsonConverter.class);

	/**
	 * Converts {@link IJsonNode} to string.
	 * 
	 * @param json
	 *            json string
	 */
	public static String json2String(IJsonNode json) {
		if (json.isArray()) {
			return JsonConverter.jsonArray2String((ArrayNode<?>) json);
		} else if (json.isObject()) {
			return JsonConverter.jsonObject2String((ObjectNode) json);
		} else if (json.isTextual()) {
			return JsonConverter.jsonText2String((TextNode) json);
		} else if (json.isMissing()) {
			log.warn("Received missing node as input. Ignoring...");
			return null;
		} else {
			log.warn("Received unknown node as input. Ignoring...");
			return null;
		}
	}

	/**
	 * Convert {@link ObjectNode} to json string.
	 * 
	 * @param objectNode
	 *            object node that should be converted into string
	 * @return json string
	 */
	private static String jsonObject2String(ObjectNode objectNode) {
		String res = "";

		for (String field : objectNode.getFieldNames()) {
			if (res.isEmpty()) {
				res += "{";
			} else {
				res += ",";
			}
			res += "\"" + field + "\" : ";
			if (objectNode.get(field) == null) {
				res += "\"\"";
			} else if (objectNode.get(field).isArray()) {
				res += JsonConverter.jsonArray2String((ArrayNode<?>) objectNode
						.get(field));
			} else if (objectNode.get(field).isObject()) {
				res += JsonConverter.jsonObject2String((ObjectNode) objectNode
						.get(field));
			} else if (objectNode.get(field).isTextual()) {
				res += JsonConverter.jsonText2String((TextNode) objectNode
						.get(field));
			} else if (objectNode.get(field).isMissing()) {
				log.warn("Received missing node as input. Ignoring...");
				res = res.substring(0, res.lastIndexOf(","));
			} else {
				log.warn("Received unknown node as input. Ignoring...");
				res = res.substring(0, res.lastIndexOf(","));
			}
		}

		return res + "}";
	}

	/**
	 * Convert {@link TextNode} to json string.
	 * 
	 * @param textNode
	 *            text node that should be converted into string
	 * @return json string
	 */
	private static String jsonText2String(TextNode textNode) {
		return "\"" + textNode.getTextValue() + "\"";
	}

	/**
	 * 
	 * Convert {@link ArrayNode} to json string.
	 * 
	 * @param arrayNode
	 *            array node that should be converted into string
	 * @return json string
	 */
	private static String jsonArray2String(ArrayNode<?> arrayNode) {
		String res = "[";

		for (int i = 0; i < arrayNode.size(); i++) {
			if (res.length() > 1) {
				res += ",";
			}
			IJsonNode node = arrayNode.get(i);
			if (node == null) {
				res += "\"\"";
			} else if (node.isArray()) {
				res += JsonConverter.jsonArray2String((ArrayNode<?>) node);
			} else if (node.isObject()) {
				res += JsonConverter.jsonObject2String((ObjectNode) node);
			} else if (node.isTextual()) {
				res += JsonConverter.jsonText2String((TextNode) node);
			} else if (node.isMissing()) {
				log.warn("Received missing node as input. Ignoring...");
				res = res.substring(0, res.lastIndexOf(","));
			} else {
				log.warn("Received unknown node as input. Ignoring...");
				res = res.substring(0, res.lastIndexOf(","));
			}
		}
		return res + "]";
	}

	/**
	 * This method converts json object from <b>jsonObject</b> to annotation
	 * classes and binds tokens an timexs with sentences to which they belong.
	 * 
	 * @param jsonObject
	 *            json object representing annotations (sentence, token, timex)
	 * @return list of sentences
	 */
	public static List<Sentence> parseAnnotations(ObjectNode jsonObject) {
		List<Sentence> res = new ArrayList<Sentence>();
		Object o = null;

		o = jsonObject.get("annotations");

		if (o instanceof ArrayNode) {
			ArrayNode<?> jsonArray = (ArrayNode<?>) o;

			List<Token> tmpTokens = new ArrayList<Token>();
			List<Timex3> tmpTimexs = new ArrayList<Timex3>();
			for (int i = 0; i < jsonArray.size(); i++) {
				IJsonNode arrayElem = jsonArray.get(i);
				if (arrayElem instanceof ObjectNode) {
					ObjectNode annotation = (ObjectNode) arrayElem;
					if (!(annotation.get("Sentence") instanceof MissingNode)) {
						Sentence s = new Sentence();

						IJsonNode begin = annotation.get("begin");
						if (begin instanceof TextNode) {
							TextNode beginAsText = (TextNode) begin;
							s.setBegin(Integer.parseInt(beginAsText
									.getTextValue().toString()));
						}
						IJsonNode end = annotation.get("end");
						if (end instanceof TextNode) {
							TextNode endAsText = (TextNode) end;
							s.setEnd(Integer.parseInt(endAsText.getTextValue()
									.toString()));
						}
						IJsonNode sentence = annotation.get("Sentence");
						if (end instanceof TextNode) {
							TextNode sentenceAsText = (TextNode) sentence;
							s.setSentenceText(sentenceAsText.getTextValue()
									.toString());
						}

						res.add(s);
					} else if (!(annotation.get("Timex3") instanceof MissingNode)) {
						Timex3 t = new Timex3();

						IJsonNode begin = annotation.get("begin");
						if (begin instanceof TextNode) {
							TextNode beginAsText = (TextNode) begin;
							t.setBegin(Integer.parseInt(beginAsText
									.getTextValue().toString()));
						}

						// for example <... type="DATE"
						// value="2003-06-07">
						IJsonNode timexType = annotation.get("timexType");
						if (timexType instanceof TextNode) {
							TextNode timexTypeAsText = (TextNode) timexType;
							if (timexTypeAsText.getTextValue().toString()
									.equalsIgnoreCase("DATE")) {
								SimpleDateFormat fullDate = new SimpleDateFormat(
										"yyyy-MM-dd");
								SimpleDateFormat yearMonth = new SimpleDateFormat(
										"yyyy-MM");
								SimpleDateFormat year = new SimpleDateFormat(
										"yyyy");
								try {

									IJsonNode date = annotation
											.get("timexValue");
									if (date instanceof TextNode) {
										TextNode dateAsText = (TextNode) date;
										String dateAsString = dateAsText
												.getTextValue().toString();
										if (dateAsString
												.matches("\\d\\d\\d\\d")) {
											t.setDate(year.parse(dateAsString));
										} else if (dateAsString
												.matches("\\d\\d\\d\\d-\\d\\d")) {
											t.setDate(yearMonth
													.parse(dateAsString));
										} else if (dateAsString
												.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d")) {
											t.setDate(fullDate
													.parse(dateAsString));
										}

									}

								} catch (ParseException e) {
									log.error(e.getMessage(), e);
									continue;
								}
							} else {
								continue;
							}
						}

						// find to which sentence this timex3 belongs
						boolean foundSentenceWhereBelong = false;
						for (Sentence s : res) {
							if (s.getBegin() <= t.getBegin()
									&& s.getEnd() > t.getBegin()) {
								s.getTimexs().add(t);
								foundSentenceWhereBelong = true;
								break;
							}
						}
						if (!foundSentenceWhereBelong) {
							tmpTimexs.add(t);
						}
					} else if (!(annotation.get("Token") instanceof MissingNode)) {
						Token t = new Token();
						IJsonNode begin = annotation.get("begin");
						if (begin instanceof TextNode) {
							TextNode beginAsText = (TextNode) begin;
							t.setBegin(Integer.parseInt(beginAsText
									.getTextValue().toString()));
						}

						// find to which sentence this token belongs
						boolean foundSentenceWhereBelong = false;
						for (Sentence s : res) {
							if (s.getBegin() <= t.getBegin()
									&& s.getEnd() > t.getBegin()) {
								s.getTokens().add(t);
								foundSentenceWhereBelong = true;
								break;
							}
						}
						if (!foundSentenceWhereBelong) {
							tmpTokens.add(t);
						}
					}
				}
			}

			if (!tmpTokens.isEmpty()) {
				for (Token t : tmpTokens) {
					for (Sentence s : res) {
						if (s.getBegin() <= t.getBegin()
								&& s.getEnd() > t.getBegin()) {
							s.getTokens().add(t);
							break;
						}
					}
				}
			}

			if (!tmpTimexs.isEmpty()) {
				for (Timex3 t : tmpTimexs) {
					for (Sentence s : res) {
						if (s.getBegin() <= t.getBegin()
								&& s.getEnd() > t.getBegin()) {
							s.getTimexs().add(t);
							break;
						}
					}
				}
			}
		}
		return res;
	}

	/**
	 * Method converts event node (json) to <code>Sentence</code>. (TODO now
	 * method fills only this sentence fields that are relevant for temporal
	 * summaries generation in class
	 * <code>TemporalSummariesSopremoOperator</code>)
	 * 
	 * @param eventNode
	 *            event node to convert to sentence
	 * @return {@link Sentence} object
	 */
	public static Sentence eventNode2Sentence(ObjectNode eventNode) {
		Sentence res = null;

		IJsonNode eventText = eventNode.get("text");
		IJsonNode eventTime = eventNode.get("end");

		if (eventNode != null && eventText instanceof TextNode
				&& eventTime != null && eventTime instanceof TextNode) {
			res = new Sentence();

			TextNode event = (TextNode) eventText;
			String sentenceContent = event.getTextValue().toString();

			TextNode time = (TextNode) eventTime;
			Timex3 timex = new Timex3();
			SimpleDateFormat dateParser = new SimpleDateFormat(
					"yyyy-MM-dd hh,mm,ss");
			try {
				timex.setDate(dateParser.parse(time.toString()));
			} catch (ParseException e) {
				log.error(e.getMessage(), e);
			}
			res.getTimexs().add(timex);

			res.setSentenceText(sentenceContent);
		} else {
			log.error("Could not parse given node to sentence. "
					+ "The given node is not well formatted event node.");
		}

		return res;
	}

	/**
	 * Method converts json string to {@link IJsonNode} (used in event tagging).
	 * 
	 * @param eventsAsString
	 *            json-array of events (json-objects) as string
	 * @param analyzedText
	 *            text of type {@link TextNode} that will be attached as
	 *            analyzedText to result
	 * @return events as {@link IJsonNode}
	 */
	public static IJsonNode string2Json(String eventsAsString) {
		ObjectNode res = new ObjectNode();

		ArrayNode<ObjectNode> array = new ArrayNode<ObjectNode>();
		res.put("events", array);

		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = null;
		Object object = null;
		try {
			object = jsonParser.parse(eventsAsString);
		} catch (org.json.simple.parser.ParseException e) {
			log.error(e.getMessage(), e);
			return null;
		}
		if (object instanceof JSONArray) {
			jsonArray = (JSONArray) object;
		} else {
			log.error("Given string does not json array!");
			return null;
		}

		if (jsonArray != null) {
			for (Object o : jsonArray) {
				if (o != null && o instanceof JSONObject) {
					JSONObject jsonObject = (JSONObject) o;
					ObjectNode resultObject = new ObjectNode();
					resultObject.put("start",
							new TextNode(jsonObject.get("start").toString()));
					resultObject.put("end", new TextNode(jsonObject.get("end")
							.toString()));
					resultObject.put("is_timespan", new TextNode(jsonObject
							.get("is_timespan").toString()));
					resultObject.put("is_personal", new TextNode(jsonObject
							.get("is_personal").toString()));
					resultObject.put("event",
							new TextNode(jsonObject.get("event").toString()));
					resultObject.put("text", new TextNode(jsonObject
							.get("text").toString()));

					if (!resultObject.isMissing()) {
						array.add(resultObject);
					}
				}
			}
		}

		return res;
	}

	/**
	 * This method parse (json-) string into stratosphere json object.
	 * 
	 * @param jsonAsString
	 *            string to parse
	 * @return {@link IJsonNode}
	 */
	public static IJsonNode string2JsonNode(String jsonAsString) {
		JSONParser jsonParser = new JSONParser();
		Object o = null;

		try {
			o = jsonParser.parse(jsonAsString);
		} catch (org.json.simple.parser.ParseException e) {
			log.error(e.getMessage(), e);
			return null;
		}

		return JsonConverter.parseJson(o);
	}

	/**
	 * 
	 * This method converts given json object given as {@link org.json.simple}
	 * object into {@link eu.stratosphere.sopremo.type.IJsonNode}.
	 * 
	 * @param jsonObject
	 * @return
	 */
	private static IJsonNode parseJson(Object jsonObject) {
		IJsonNode res = null;

		if (jsonObject instanceof JSONObject) {
			res = JsonConverter.parseJsonObject((JSONObject) jsonObject);

		} else if (jsonObject instanceof JSONArray) {
			res = JsonConverter.parseJsonArray((JSONArray) jsonObject);

		} else if (jsonObject instanceof JSONValue
				|| jsonObject instanceof String) {
			res = new TextNode(jsonObject.toString());
		}

		return res;
	}

	/**
	 * This method converts given {@link org.json.simple.JSONObject} into
	 * {@link eu.stratosphere.sopremo.type.ObjectNode}.
	 * 
	 * @param jsonObject
	 * @return
	 */
	private static ObjectNode parseJsonObject(JSONObject jsonObject) {
		ObjectNode res = new ObjectNode();

		for (Object key : jsonObject.keySet()) {
			res.put(key.toString(),
					JsonConverter.parseJson(jsonObject.get(key)));
		}

		return res;
	}

	/**
	 * This method converts given {@link org.json.simple.JSONArray} into
	 * {@link eu.stratosphere.sopremo.type.ArrayNode}.
	 * 
	 * @param jsonArray
	 * @return
	 */
	private static IJsonNode parseJsonArray(JSONArray jsonArray) {
		ArrayNode<IJsonNode> res = new ArrayNode<IJsonNode>();

		for (Object arrayElement : jsonArray) {
			res.add(JsonConverter.parseJson(arrayElement));
		}

		return res;
	}

}
