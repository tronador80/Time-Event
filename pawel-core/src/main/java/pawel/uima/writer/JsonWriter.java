/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package pawel.uima.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import de.dima.textmining.conll.CoNLLNode;
import de.dima.textmining.events.EventExtractor;
import de.dima.textmining.timeindex.TimelineResult;
import de.dima.textmining.types.GigawordMetadata;
import de.dima.textmining.types.Sentence;
import de.dima.textmining.types.Timespan;
import de.unihd.dbs.uima.types.heideltime.Timex3;

/**
 * A simple CAS consumer that writes the CAS to XMI format.
 * <p>
 * This CAS Consumer takes one parameter:
 * <ul>
 * <li><code>OutputDirectory</code> - path to directory into which output files
 * will be written</li>
 * </ul>
 * 
 * @author Andreas Wolf
 * @author pawel
 * 
 */
public class JsonWriter extends org.uimafit.component.JCasAnnotator_ImplBase {

	private static Logger log = Logger.getLogger(JsonWriter.class);
	/**
	 * Name of configuration parameter that must be set to the path of a
	 * directory into which the output files will be written.
	 */

	public static final String PARAM_OUTFILE = "OutputFile";

	private String outputFile;

	private EventExtractor eventExtractor;

	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {

		// extractor for time events // don't save dot trees
		eventExtractor = new EventExtractor(false);

		this.outputFile = (String) aContext
				.getConfigParameterValue(PARAM_OUTFILE);

	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		/**
		 * get all
		 */
		AnnotationIndex<Annotation> timeSpanIndex = jcas
				.getAnnotationIndex(Timespan.type);
		AnnotationIndex<Annotation> timeIndex = jcas
				.getAnnotationIndex(Timex3.type);
		AnnotationIndex<Annotation> sentIndex = jcas
				.getAnnotationIndex(Sentence.type);
		AnnotationIndex<Annotation> MetaIndex = jcas
				.getAnnotationIndex(GigawordMetadata.type);

		// TODO set correct meta data...
		GigawordMetadata meta = null;
		try {
			meta = (GigawordMetadata) MetaIndex.iterator().next();
		} catch (NoSuchElementException nsee) {
			log.warn(nsee.getMessage());
		}
		String metaDay = null;
		String metaMonth = null;
		String metaYear = null;
		String metaSourceID = null;
		String metaCity = null;
		String metaPublisher = null;
		if (meta != null) {
			metaDay = Short.toString(meta.getDayPublished());
			metaMonth = Short.toString(meta.getMonthPublished());
			metaYear = Short.toString(meta.getYearPublished());
			metaSourceID = meta.getGigawordId();
			metaCity = meta.getCity();
			metaPublisher = meta.getPublisher();
		} else {
			metaDay = "14";
			metaMonth = "03";
			metaYear = "2013";
			metaSourceID = "input";
			metaCity = "Berlin";
			metaPublisher = "pawel";
		}

		HashMap<String, Integer> week = initWeek();

		for (Annotation an : sentIndex) {
			Sentence sent = (Sentence) an;
			// ChunkDeepParser deep = (ChunkDeepParser) an;
			// System.out.println(deep.getCoveredText()+" ::: "+sent.getCoveredText());

			// check if sentence has a parse
			CoNLLNode root = null;
			String conll = sent.getCoNLLParse();
			if (conll != null) {
				try {
					root = CoNLLNode.parseCoNLL(conll);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}

			} else {
				// ignore sentence without parse
				continue;
			}

			FSIterator<Annotation> timeSpanIter = timeSpanIndex.subiterator(sent);
			FSIterator<Annotation> timeIter = timeIndex.subiterator(sent);

			// map found all found timex to tokens
			Map<String, List<Integer>> timexPositions = eventExtractor
					.mapTimexToTokens(jcas, sent);

			while (timeIter.hasNext() || timeSpanIter.hasNext()) {
				String startTime = "";
				String endTime = "";
				Annotation time;
				if (timeIter.hasNext()) {
					time = (Timex3) timeIter.next();
					startTime = ((Timex3) time).getTimexValue();
				} else {
					time = (Timespan) timeSpanIter.next();
					startTime = ((Timespan) time).getStartTime()
							.getTimexValue();
					endTime = ((Timespan) time).getEndTime().getTimexValue();
				}

				if (time.getCoveredText().matches(
						"(Sa)|(Su)|(Mo)|(Tu)|(We)|(Th)|(Fr).*")) {
					metaDay = calcMetaDay(metaDay, metaMonth, metaYear, time,
							week);
				}

				boolean timeSpan = false;
				String personalTime = "false";

				String[] startDate = new String[4];
				String[] endDate = new String[4];
				for (int i = 0; i < 4; i++) {
					startDate[i] = "";
					endDate[i] = "";
				}

				startDate = this.calcDate(startTime, startDate, metaYear,
						metaMonth, metaDay);
				String startYear = startDate[0];
				String endYear = startYear;
				String startMonth = startDate[1];
				String endMonth = startMonth;
				if (startMonth == "") {
					startMonth = "01";
					endMonth = "12";
				}
				String startDay = startDate[2];
				String endDay = startDay;
				if (startDay == "") {
					startDay = "01";
					endDay = "31";
				}
				String startHours = startDate[3];
				String endHours = startHours;
				if (startHours == "") {
					startHours = "00,00,00";
					endHours = "23,59,59";
				}

				if (endTime != "") {
					endDate = this.calcDate(endTime, endDate, metaYear,
							metaMonth, metaDay);
					endYear = endDate[0];
					endMonth = endDate[1];
					if (endMonth == "")
						endMonth = "12";
					endDay = endDate[2];
					if (endDay == "")
						endDay = "31";
					endHours = endDate[3];
					if (endHours == "")
						endHours = "23,59,59";
					timeSpan = true;
				}

				if (startTime
						.matches("(FUTURE_REF)|(PAST_REF)|(PRESENT_REF)|(UNDEF-this-day)|(UNDEF-next-day)"
								+ "|(UNDEF_last_day)|(UNDEF_this_day_MINUS_2)")
				// == "FUTURE_REF" || startTime == "PAST_REF" || startTime ==
				// "PRESENT_REF"
				) {
					// System.out.println("XX PERSONAL XX :: " + startTime);
					personalTime = startTime;
					startHours = "00,00,00";
					endHours = "00,00,00";
					startYear = metaYear;
					endYear = metaYear;
					startMonth = metaMonth;
					endMonth = metaMonth;
					startDay = metaDay;
					endDay = metaDay;
					if (startTime == "FUTURE_REF" && endMonth != "12") {
						endMonth = Integer
								.toString(Integer.parseInt(endMonth) + 1);
						endDay = "28";
					} else {
						endMonth = "01";
						endDay = "28";
					}
					if (startTime == "PAST_REF" && endMonth != "01") {
						endMonth = Integer
								.toString(Integer.parseInt(endMonth) - 1);
						endDay = "01";
					} else {
						endMonth = "12";
						endDay = "01";
					}
				}

				if (personalTime != "false" || timeSpan == true
						|| startYear != "") {

					// event generation
					String event = null;
					List<Integer> timexPos = timexPositions.get(time
							.getCoveredText()); // timeSpan.getCoveredText
					// check for mapping of timex to token (ignores durations)
					if (timexPos != null) {
						// get event for this timex
						event = eventExtractor.makeEvent(root, timexPos);
					}
					// ignore timex for which no valid event could be generated
					if (event == null) {
						continue;
					}

					String startMnth = Integer.toString(Integer
							.parseInt(startMonth) - 1); // javascript srsly
														// codes months as 0-11
					String endMnth = Integer.toString(Integer
							.parseInt(endMonth) - 1);
					String timeText = "{ \"start\": \"" + startYear + "-"
							+ startMnth + "-" + startDay + " " + startHours
							+ "\", \"end\": \"" + endYear + "-" + endMnth + "-"
							+ endDay + " " + endHours + "\", \"timeSpan\": \""
							+ timeSpan + "\", \"personalTime\": \""
							+ personalTime + "\", \"content\": \""
							+ event.replaceAll("\"", "") + "\", \"text\": \""
							+ sent.getCoveredText().replaceAll("\"", "")
							+ "\" }";
					String output = timeText.replaceAll("\\n|\\r|\\t", " ")
							.replaceAll(" +", " ");
					event = event.replaceAll("\"", "")
							.replaceAll("\\n|\\r|\\t", " ")
							.replaceAll(" +", " ");
					/**
					 * Add to Index
					 */
					final Calendar CAL = new GregorianCalendar(Locale.ENGLISH);
					CAL.set(Integer.parseInt(startYear),
							Integer.parseInt(startMnth),
							Integer.parseInt(startDay));
					Date startDat = CAL.getTime();
					CAL.set(Integer.parseInt(endYear),
							Integer.parseInt(endMnth), Integer.parseInt(endDay));
					Date endDat = CAL.getTime();
					this.addIndex(startDat, endDat, event, output, metaCity,
							metaPublisher, metaSourceID, sent);
				}
			}
		}
	}

	/**
	 * Add to Index
	 */
	private void addIndex(Date startDate, Date endDate, String event,
			String output, String metaCity, String metaPublisher,
			String metaSourceID, Sentence sent) {
		TimelineResult timeResult = new TimelineResult(event, output, metaCity,
				metaPublisher, metaSourceID, startDate, endDate,
				sent.getBegin(), sent.getEnd());

		log.debug("ADD INDEX");
		log.debug(timeResult.getJson());
		log.debug(this.outputFile + "/tmpL.out");

		try {
			File dir = new File(this.outputFile);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			FileWriter fw = new FileWriter(this.outputFile + "/tmp"
					+ (new Random()).nextLong() + ".out", true);
			BufferedWriter out = new BufferedWriter(fw);
			out.append(timeResult.getJson());
			out.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private String calcMetaDay(String metaDay, String metaMonth,
			String metaYear, Annotation time, HashMap<String, Integer> week) {
		String metaWeekDay = this.getWeekDay(metaDay, metaMonth, metaYear);
		String weekDay = time.getCoveredText().substring(0, 2);
		int weekDayNum = week.get(weekDay);
		int metaWeekDayNum = week.get(metaWeekDay);
		int diff = weekDayNum - metaWeekDayNum;
		if (weekDayNum < metaWeekDayNum) {
			diff = 7 - metaWeekDayNum + weekDayNum;
		}
		return (Integer.toString(metaWeekDayNum + diff));
	}

	private String[] calcDate(String pointTime, String[] date, String metaYear,
			String metaMonth, String metaDay) {
		if (pointTime.length() == 16
				&& pointTime.matches("[0-9X]{4}-[0-9X]{2}-[0-9X]{2}.*")) {
			date[0] = pointTime.substring(0, 4);
			if (date[0].contains("X")) {
				date[0] = metaYear;
			}
			date[1] = pointTime.substring(5, 7);
			if (date[1].contains("X")) {
				date[1] = metaMonth;
			}
			date[2] = pointTime.substring(8, 10);
			if (date[2].contains("X")) {
				date[2] = metaDay;
			}
			date[3] = pointTime.substring(11, 16);

		} else if (pointTime.length() == 10
				&& pointTime.matches("[0-9X]{4}-[0-9X]{2}-[0-9X]{2}")) {
			date[0] = pointTime.substring(0, 4);
			if (date[0].contains("X")) {
				date[0] = metaYear;
			}
			date[1] = pointTime.substring(5, 7);
			if (date[1].contains("X")) {
				date[1] = metaMonth;
			}
			date[2] = pointTime.substring(8, 10);
			if (date[2].contains("X")) {
				date[2] = metaDay;
			}
		} else if (pointTime.length() == 7
				&& pointTime.matches("[0-9X]{4}-[0-9X]{2}")) {
			date[0] = pointTime.substring(0, 4);
			if (date[0].contains("X")) {
				date[0] = metaYear;
			}
			date[1] = pointTime.substring(5, 7);
			if (date[1].contains("X")) {
				date[1] = metaMonth;
			}
		} else if (pointTime.length() == 4 && pointTime.matches("[0-9X]{4}")) {
			date[0] = pointTime.substring(0, 4);
			if (date[0].contains("X")) {
				date[0] = metaYear;
			}
		}
		return date;
	}

	private HashMap<String, Integer> initWeek() {

		HashMap<String, Integer> week = new HashMap<String, Integer>();
		week.put("Sa", 0);
		week.put("Su", 1);
		week.put("Mo", 2);
		week.put("Tu", 3);
		week.put("We", 4);
		week.put("Th", 5);
		week.put("Fr", 6);
		return week;
	}

	private String getWeekDay(String tag, String monat, String jahr) {

		// Referenztag ist Samstag, der 01.01.0001.
		// Nun werden alle Tage bis zum Eingabe-Datum gezlt.
		// Alle-Vergangenen-Tage modulo 7 ergibt dann eine Zahl
		// zwischen 0 und 6, die einen Wochentag
		// zwischen Samstag und Freitag repr�sentiert.

		// grobe Z�hlung: Ganze Jahre +
		// Tage im aktuellen Monat + ganze Monate im aktuellen Jahr
		String month = "312831303130313130313031";
		int yyyy = Integer.parseInt(jahr);
		int mm = Integer.parseInt(monat);
		int dd = Integer.parseInt(tag);

		int days = (yyyy - 1) * 365 + (dd - 1);
		for (int i = 0; i < mm - 1; i++)
			days += Integer.parseInt(month.substring(i * 2, i * 2 + 2)) * 1;

		// Kalenderreform: Sprung vom 04.10.1582 zum 15.10.1582
		if (yyyy > 1582 || yyyy == 1582 && (mm > 10 || mm == 10 && dd > 4))
			days -= 10;

		// Schaltjahre bis 1599: alle Jahre, die durch 4 teilbar sind.
		// Ab 1600: alle Jahre, die durch 4 teilbar sind,
		// außer den vollen Jahrhunderten,
		// es sei denn, sie sind durch 400 teilbar.
		double leapyears = Math.floor(yyyy / 4);
		if (yyyy % 4 == 0 && mm < 3)
			leapyears--;
		if (yyyy >= 1600) {
			leapyears -= Math.floor((yyyy - 1600) / 100);
			leapyears += Math.floor((yyyy - 1600) / 400);
			if (yyyy % 100 == 0 && mm < 3) {
				leapyears++;
				if (yyyy % 400 == 0)
					leapyears--;
			}
		}
		days += leapyears;

		// Ergebnis anzeigen
		String week = "SaSuMoTuWeThFr";
		return (week.substring(days % 7 * 2, days % 7 * 2 + 2));
	}
}