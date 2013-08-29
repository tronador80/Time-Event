/**
 * 
 */
package pawel.uima.reader;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.uimafit.component.JCasCollectionReader_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.descriptor.TypeCapability;

import pawel.types.pawel.Text;
import pawel.types.pawel.Sentence;
import de.dima.textmining.types.ShallowAnnotation;
import de.unihd.dbs.uima.types.heideltime.Dct;
import de.unihd.dbs.uima.types.heideltime.Timex3;
import pawel.types.pawel.Token;

/**
 * CollectionReader that takes an array of XMI formatted strings as input.
 * 
 * @author ptondryk
 */
@TypeCapability(inputs = "String", outputs = "de.dima.textmining.types.Sentence")
public class JsonArrayReader extends JCasCollectionReader_ImplBase {

	private static Logger log = Logger.getLogger(JsonArrayReader.class);

	public static final String COMPONENT_ID = JsonArrayReader.class.getName();

	@ConfigurationParameter(name = "PARAM_INPUT", mandatory = true, description = "a array of string, to be processed")
	private String[] inputArray;

	private int docId;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		docId = 0;
	}

	public void getNext(JCas jcas) throws IOException, CollectionException {
		String jsonString = inputArray[docId++];
		String documentText = "";

		if (jsonString != null) {
			try {
				JSONObject jsonObject = null;
				if (jsonString.startsWith("[")) {
					// if input is json array (use first object)
					JSONArray jsonArray = new JSONArray(jsonString);
					if (jsonArray.length() > 0) {
						jsonObject = (JSONObject) jsonArray.get(0);
					}
				} else {
					// if input is json object
					jsonObject = new JSONObject(jsonString);
				}

				Object o = jsonObject.get("annotations");

				if (o instanceof JSONArray) {
					JSONArray jsonArray = (JSONArray) o;

					for (int i = 0; i < jsonArray.length(); i++) {
						Object arrayElem = jsonArray.get(i);

						if (arrayElem instanceof JSONObject) {
							JSONObject annotation = (JSONObject) arrayElem;
							if (annotation.has("Text")) {
								// TODO add sopremo parameter for this ??
								// (textName, dateName, datePattern)
								Text text = this.fromJSONObject(jcas,
										annotation, "Text", "date",
										"yyyyMMddHHmm");
								text.addToIndexes();
								documentText = annotation.getString("Text");

							} else if (annotation.has("Sentence")) {
								Sentence s = new Sentence(jcas);
								s.setBegin(annotation.getInt("begin"));
								s.setEnd(annotation.getInt("end"));
								s.setTokenBegin(annotation.getInt("tokenBegin"));
								s.setTokenEnd(annotation.getInt("tokenEnd"));
								s.setSentence(annotation.getString("Sentence"));
								s.setSentenceIndex(annotation
										.getInt("sentenceIndex"));
								s.addToIndexes();

							} else if (annotation.has("ShallowAnnotation")) {
								ShallowAnnotation sa = new ShallowAnnotation(
										jcas);
								sa.setBegin(annotation.getInt("begin"));
								sa.setEnd(annotation.getInt("end"));
								sa.setLemma(annotation.getString("Lemma"));
								sa.setPosTag(annotation.getString("PosTag"));
								sa.addToIndexes();

							} else if (annotation.has("Timex3")) {
								Timex3 t = new Timex3(jcas);
								if (annotation.has("begin")) {
									t.setBegin(annotation.getInt("begin"));
								}
								if (annotation.has("end")) {
									t.setEnd(annotation.getInt("end"));
								}
								if (annotation.has("allTokIds")) {
									t.setAllTokIds(annotation
											.getString("allTokIds"));
								}
								if (annotation.has("firstTokId")) {
									t.setFilename("");
									t.setFirstTokId(annotation
											.getInt("firstTokId"));
								}
								if (annotation.has("foundByRule")) {
									t.setFoundByRule(annotation
											.getString("foundByRule"));
								}
								if (annotation.has("sentId")) {
									t.setSentId(annotation.getInt("sentId"));
								}
								if (annotation.has("timexFreq")) {
									t.setTimexFreq(annotation
											.getString("timexFreq"));
								}
								if (annotation.has("timexId")) {
									t.setTimexId(annotation
											.getString("timexId"));
								}
								if (annotation.has("timexInstance")) {
									t.setTimexInstance(annotation
											.getInt("timexInstance"));
								}
								if (annotation.has("timexMod")) {
									t.setTimexMod(annotation
											.getString("timexMod"));
								}
								if (annotation.has("timexQuant")) {
									t.setTimexQuant(annotation
											.getString("timexQuant"));
								}
								if (annotation.has("timexType")) {
									t.setTimexType(annotation
											.getString("timexType"));
								}
								if (annotation.has("timexValue")) {
									t.setTimexValue(annotation
											.getString("timexValue"));
								}
								t.addToIndexes();

							} else if (annotation.has("Token")) {
								Token token = new Token(jcas);

								if (annotation.has("begin")) {
									token.setBegin(annotation.getInt("begin"));
								}
								if (annotation.has("end")) {
									token.setEnd(annotation.getInt("end"));
								}
								if (annotation.has("value")) {
									token.setValue(annotation
											.getString("value"));
								}
								if (annotation.has("Token")) {
									token.setToken(annotation
											.getString("Token"));
								}
								if (annotation.has("originalText")) {
									token.setOriginalText(annotation
											.getString("originalText"));
								}
								if (annotation.has("beforeToken")) {
									token.setBeforeToken(annotation
											.getString("beforeToken"));
								}
								if (annotation.has("afterToken")) {
									token.setAfterToken(annotation
											.getString("afterToken"));
								}

								if (annotation.has("pos")) {
									token.setPos(annotation.getString("pos"));
								}

								token.addToIndexes();

							} else if (annotation.has("Dct")) {
								// 199406121734 <- date
								Dct dct = new Dct(jcas);
								dct.setValue(annotation.getString("Dct"));
								dct.addToIndexes();

							}
						}
					}
				}
			} catch (JSONException e) {
				log.error(e.getMessage(), e);
			}
		}

		jcas.setDocumentText(documentText);
	}

	public boolean hasNext() throws IOException, CollectionException {
		return docId < inputArray.length;
	}

	public Progress[] getProgress() {
		return null;
	}

	/**
	 * 
	 * Generates {@link Text} instance from given json node. If textName,
	 * dateName or datePattern is null then following default value will be
	 * used: <code>"Text", "date", "yyyyMMddHHmmss"</code>.
	 * 
	 * @param node
	 *            json node from which a new {@link Text} instance should be
	 *            created
	 * @param textName
	 *            name of json element holding the content of text
	 * @param dateName
	 *            name of json element holding the timestamp
	 * @param datePattern
	 *            pattern how is the timestamp formatted (for example
	 *            yyyyMMddHHmmss)
	 * @return new {@link Text} instance
	 */
	public Text fromJSONObject(JCas jcas, JSONObject node, String textName,
			String dateName, String datePattern) {

		// set default values if not set
		if (textName == null) {
			textName = "Text";
		}
		if (dateName == null) {
			dateName = "date";
		}
		if (datePattern == null) {
			datePattern = "yyyyMMddHHmmss";
		}

		if (node == null || !node.has(textName) || !node.has(dateName)) {
			return null;
		}

		Text newText = new Text(jcas);

		try {
			newText.setText(node.getString(textName));

			if (datePattern.equals("yyyyMMddHHmmss")) {
				newText.setDate(node.getString(dateName));

			} else {
				// parse the date using given pattern
				SimpleDateFormat sdf1 = new SimpleDateFormat(datePattern);
				Date timestamp = sdf1.parse(node.getString(dateName));

				// format the date to standard format
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
				newText.setDate(sdf2.format(timestamp));

			}
		} catch (ParseException e) {
			return null;
		} catch (JSONException e) {
			return null;
		}

		return newText;
	}

}
