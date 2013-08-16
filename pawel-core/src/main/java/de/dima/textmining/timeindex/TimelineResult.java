package de.dima.textmining.timeindex;

import java.util.Date;
/**
 * Class to represent to result after processing with the timeline UIMA pipeline.
 * 
 * @author Michael Huelfenhaus
 *
 */
public class TimelineResult {

	private String event;

	private String json;

	private String city;
	
	private String publisher;

	private String sourceId;

	private Date startDate;

	private Date endDate;

	private int sentenceStart;

	private int sentenceEnd;

	public TimelineResult() {
		this("","","","","",null,null,0,0);
	}

	/**
	 * Create a result that represents the processing result for one found event
	 * 
	 * @param event text of the event that is displayed
	 * @param json json representation for the visualization
	 * @param city city extracted for gigaword article
	 * @param publisher publisher of the article
	 * @param sourceId gigaword id of the source text
	 * @param startDate start date of the event
	 * @param endDate end date of the event
	 * @param sentenceStart start position of the sentence from which the event was extracted
	 * @param sentenceEnd start position of the sentence from which the event was extracted
	 */
	public TimelineResult(String event, String json, String city,String publisher,
			String sourceId, Date startDate, Date endDate, int sentenceStart,
			int sentenceEnd) {
		this.event = event;
		this.json = json;
		this.city = city;
		this.publisher = publisher;
		this.sourceId = sourceId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.sentenceStart = sentenceStart;
		this.sentenceEnd = sentenceEnd;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getSentenceStart() {
		return sentenceStart;
	}

	public void setSentenceStart(int sentenceStart) {
		this.sentenceStart = sentenceStart;
	}

	public int getSentenceEnd() {
		return sentenceEnd;
	}

	public void setSentenceEnd(int sentenceEnd) {
		this.sentenceEnd = sentenceEnd;
	}

}
