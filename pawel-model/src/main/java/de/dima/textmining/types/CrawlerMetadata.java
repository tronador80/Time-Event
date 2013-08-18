/* First created by JCasGen Mon Dec 10 10:45:26 CET 2012 */
package de.dima.textmining.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class CrawlerMetadata extends Annotation {
	/**
	 * @generated
	 * @ordered
	 */
	@SuppressWarnings("hiding")
	public final static int typeIndexID = JCasRegistry
			.register(CrawlerMetadata.class);
	/**
	 * @generated
	 * @ordered
	 */
	@SuppressWarnings("hiding")
	public final static int type = typeIndexID;

	/** @generated */
	@Override
	public int getTypeIndexID() {
		return typeIndexID;
	}

	/**
	 * Never called. Disable default constructor
	 * 
	 * @generated
	 */
	protected CrawlerMetadata() {/* intentionally empty block */
	}

	/**
	 * Internal - constructor used by generator
	 * 
	 * @generated
	 */
	public CrawlerMetadata(int addr, TOP_Type type) {
		super(addr, type);
		readObject();
	}

	/** @generated */
	public CrawlerMetadata(JCas jcas) {
		super(jcas);
		readObject();
	}

	/** @generated */
	public CrawlerMetadata(JCas jcas, int begin, int end) {
		super(jcas);
		setBegin(begin);
		setEnd(end);
		readObject();
	}

	/**
	 * <!-- begin-user-doc --> Write your own initialization here <!--
	 * end-user-doc -->
	 * 
	 * @generated modifiable
	 */
	private void readObject() {/* default - does nothing empty block */
	}

	// *--------------*
	// * Feature: year

	public String getTitle() {
		if (CrawlerMetadata_Type.featOkTst
				&& ((CrawlerMetadata_Type) jcasType).casFeat_title == null)
			jcasType.jcas.throwFeatMissing("title",
					"de.dima.textmining.types.CrawlerMetadata");
		return jcasType.ll_cas.ll_getStringValue(addr,
				((CrawlerMetadata_Type) jcasType).casFeatCode_title);
	}

	public void setTitle(String v) {
		if (CrawlerMetadata_Type.featOkTst
				&& ((CrawlerMetadata_Type) jcasType).casFeat_title == null)
			jcasType.jcas.throwFeatMissing("title",
					"de.dima.textmining.types.CrawlerMetadata");
		jcasType.ll_cas.ll_setStringValue(addr,
				((CrawlerMetadata_Type) jcasType).casFeatCode_title, v);
	}

	// *--------------*
	// * Feature: year

	public short getYear() {
		if (CrawlerMetadata_Type.featOkTst
				&& ((CrawlerMetadata_Type) jcasType).casFeat_year == null)
			jcasType.jcas.throwFeatMissing("year",
					"de.dima.textmining.types.CrawlerMetadata");
		return jcasType.ll_cas.ll_getShortValue(addr,
				((CrawlerMetadata_Type) jcasType).casFeatCode_year);
	}

	public void setYear(short v) {
		if (CrawlerMetadata_Type.featOkTst
				&& ((CrawlerMetadata_Type) jcasType).casFeat_year == null)
			jcasType.jcas.throwFeatMissing("year",
					"de.dima.textmining.types.CrawlerMetadata");
		jcasType.ll_cas.ll_setShortValue(addr,
				((CrawlerMetadata_Type) jcasType).casFeatCode_year, v);
	}

	// *--------------*
	// * Feature: month

	public short getMonth() {
		if (CrawlerMetadata_Type.featOkTst
				&& ((CrawlerMetadata_Type) jcasType).casFeat_month == null)
			jcasType.jcas.throwFeatMissing("month",
					"de.dima.textmining.types.CrawlerMetadata");
		return jcasType.ll_cas.ll_getShortValue(addr,
				((CrawlerMetadata_Type) jcasType).casFeatCode_month);
	}

	public void setMonth(short v) {
		if (CrawlerMetadata_Type.featOkTst
				&& ((CrawlerMetadata_Type) jcasType).casFeat_month == null)
			jcasType.jcas.throwFeatMissing("month",
					"de.dima.textmining.types.CrawlerMetadata");
		jcasType.ll_cas.ll_setShortValue(addr,
				((CrawlerMetadata_Type) jcasType).casFeatCode_month, v);
	}

	// *--------------*
	// * Feature: day

	public short getDay() {
		if (CrawlerMetadata_Type.featOkTst
				&& ((CrawlerMetadata_Type) jcasType).casFeat_day == null)
			jcasType.jcas.throwFeatMissing("day",
					"de.dima.textmining.types.CrawlerMetadata");
		return jcasType.ll_cas.ll_getShortValue(addr,
				((CrawlerMetadata_Type) jcasType).casFeatCode_day);
	}

	public void setDay(short v) {
		if (CrawlerMetadata_Type.featOkTst
				&& ((CrawlerMetadata_Type) jcasType).casFeat_day == null)
			jcasType.jcas.throwFeatMissing("day",
					"de.dima.textmining.types.CrawlerMetadata");
		jcasType.ll_cas.ll_setShortValue(addr,
				((CrawlerMetadata_Type) jcasType).casFeatCode_day, v);
	}

	// *--------------*
	// * Feature: url

	public String getUrl() {
		if (CrawlerMetadata_Type.featOkTst
				&& ((CrawlerMetadata_Type) jcasType).casFeat_url == null)
			jcasType.jcas.throwFeatMissing("url",
					"de.dima.textmining.types.CrawlerMetadata");
		return jcasType.ll_cas.ll_getStringValue(addr,
				((CrawlerMetadata_Type) jcasType).casFeatCode_url);
	}

	public void setUrl(String v) {
		if (CrawlerMetadata_Type.featOkTst
				&& ((CrawlerMetadata_Type) jcasType).casFeat_url == null)
			jcasType.jcas.throwFeatMissing("url",
					"de.dima.textmining.types.CrawlerMetadata");
		jcasType.ll_cas.ll_setStringValue(addr,
				((CrawlerMetadata_Type) jcasType).casFeatCode_url, v);
	}
}
