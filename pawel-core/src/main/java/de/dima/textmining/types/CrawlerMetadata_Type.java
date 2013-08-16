/* First created by JCasGen Mon Dec 10 10:45:26 CET 2012 */
package de.dima.textmining.types;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.tcas.Annotation_Type;

public class CrawlerMetadata_Type extends Annotation_Type {
	/** @generated */
	@Override
	protected FSGenerator getFSGenerator() {
		return fsGenerator;
	}

	/** @generated */
	private final FSGenerator fsGenerator = new FSGenerator() {
		public FeatureStructure createFS(int addr, CASImpl cas) {
			if (CrawlerMetadata_Type.this.useExistingInstance) {
				// Return eq fs instance if already created
				FeatureStructure fs = CrawlerMetadata_Type.this.jcas
						.getJfsFromCaddr(addr);
				if (null == fs) {
					fs = new CrawlerMetadata(addr, CrawlerMetadata_Type.this);
					CrawlerMetadata_Type.this.jcas.putJfsFromCaddr(addr, fs);
					return fs;
				}
				return fs;
			} else
				return new CrawlerMetadata(addr, CrawlerMetadata_Type.this);
		}
	};
	/** @generated */
	@SuppressWarnings("hiding")
	public final static int typeIndexID = CrawlerMetadata.typeIndexID;
	/**
	 * @generated
	 * @modifiable
	 */
	@SuppressWarnings("hiding")
	public final static boolean featOkTst = JCasRegistry
			.getFeatOkTst("de.dima.textmining.types.CrawlerMetadata");

	/** @generated */
	final Feature casFeat_year;
	/** @generated */
	final int casFeatCode_year;

	/** @generated */
	public short getYear(int addr) {
		if (featOkTst && casFeat_year == null)
			jcas.throwFeatMissing("year",
					"de.dima.textmining.types.CrawlerMetadata");
		return ll_cas.ll_getShortValue(addr, casFeatCode_year);
	}

	/** @generated */
	public void setYear(int addr, short v) {
		if (featOkTst && casFeat_year == null)
			jcas.throwFeatMissing("year",
					"de.dima.textmining.types.CrawlerMetadata");
		ll_cas.ll_setShortValue(addr, casFeatCode_year, v);
	}

	/** @generated */
	final Feature casFeat_month;
	/** @generated */
	final int casFeatCode_month;

	/** @generated */
	public short getMonth(int addr) {
		if (featOkTst && casFeat_month == null)
			jcas.throwFeatMissing("month",
					"de.dima.textmining.types.CrawlerMetadata");
		return ll_cas.ll_getShortValue(addr, casFeatCode_month);
	}

	/** @generated */
	public void setMonth(int addr, short v) {
		if (featOkTst && casFeat_month == null)
			jcas.throwFeatMissing("month",
					"de.dima.textmining.types.CrawlerMetadata");
		ll_cas.ll_setShortValue(addr, casFeatCode_month, v);
	}

	/** @generated */
	final Feature casFeat_day;
	/** @generated */
	final int casFeatCode_day;

	/** @generated */
	public short getDay(int addr) {
		if (featOkTst && casFeat_day == null)
			jcas.throwFeatMissing("day",
					"de.dima.textmining.types.CrawlerMetadata");
		return ll_cas.ll_getShortValue(addr, casFeatCode_day);
	}

	/** @generated */
	public void setDay(int addr, short v) {
		if (featOkTst && casFeat_day == null)
			jcas.throwFeatMissing("day",
					"de.dima.textmining.types.CrawlerMetadata");
		ll_cas.ll_setShortValue(addr, casFeatCode_day, v);
	}

	/** @generated */
	final Feature casFeat_title;
	/** @generated */
	final int casFeatCode_title;

	/** @generated */
	public String getTitle(int addr) {
		if (featOkTst && casFeat_title == null)
			jcas.throwFeatMissing("title",
					"de.dima.textmining.types.CrawlerMetadata");
		return ll_cas.ll_getStringValue(addr, casFeatCode_title);
	}

	/** @generated */
	public void setTitle(int addr, String v) {
		if (featOkTst && casFeat_title == null)
			jcas.throwFeatMissing("title",
					"de.dima.textmining.types.CrawlerMetadata");
		ll_cas.ll_setStringValue(addr, casFeatCode_title, v);
	}

	/** @generated */
	final Feature casFeat_url;
	/** @generated */
	final int casFeatCode_url;

	/** @generated */
	public String getUrl(int addr) {
		if (featOkTst && casFeat_url == null)
			jcas.throwFeatMissing("url",
					"de.dima.textmining.types.CrawlerMetadata");
		return ll_cas.ll_getStringValue(addr, casFeatCode_url);
	}

	/** @generated */
	public void setUrl(int addr, String v) {
		if (featOkTst && casFeat_url == null)
			jcas.throwFeatMissing("url",
					"de.dima.textmining.types.CrawlerMetadata");
		ll_cas.ll_setStringValue(addr, casFeatCode_url, v);
	}

	/**
	 * initialize variables to correspond with Cas Type and Features
	 * 
	 * @generated
	 */
	public CrawlerMetadata_Type(JCas jcas, Type casType) {
		super(jcas, casType);
		casImpl.getFSClassRegistry().addGeneratorForType(
				(TypeImpl) this.casType, getFSGenerator());

		casFeat_year = jcas.getRequiredFeatureDE(casType, "year",
				"uima.cas.Short", featOkTst);
		casFeatCode_year = (null == casFeat_year) ? JCas.INVALID_FEATURE_CODE
				: ((FeatureImpl) casFeat_year).getCode();

		casFeat_month = jcas.getRequiredFeatureDE(casType, "month",
				"uima.cas.Short", featOkTst);
		casFeatCode_month = (null == casFeat_month) ? JCas.INVALID_FEATURE_CODE
				: ((FeatureImpl) casFeat_month).getCode();

		casFeat_day = jcas.getRequiredFeatureDE(casType, "day",
				"uima.cas.Short", featOkTst);
		casFeatCode_day = (null == casFeat_day) ? JCas.INVALID_FEATURE_CODE
				: ((FeatureImpl) casFeat_day).getCode();

		casFeat_title = jcas.getRequiredFeatureDE(casType, "title",
				"uima.cas.String", featOkTst);
		casFeatCode_title = (null == casFeat_title) ? JCas.INVALID_FEATURE_CODE
				: ((FeatureImpl) casFeat_title).getCode();

		casFeat_url = jcas.getRequiredFeatureDE(casType, "url",
				"uima.cas.String", featOkTst);
		casFeatCode_url = (null == casFeat_url) ? JCas.INVALID_FEATURE_CODE
				: ((FeatureImpl) casFeat_url).getCode();

	}
}
