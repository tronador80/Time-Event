
/* First created by JCasGen Mon Dec 10 10:45:26 CET 2012 */
package de.dima.textmining.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** Metadata from Gigaword Article
 * Updated by JCasGen Mon Dec 10 10:45:41 CET 2012
 * @generated */
public class GigawordMetadata_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (GigawordMetadata_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = GigawordMetadata_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new GigawordMetadata(addr, GigawordMetadata_Type.this);
  			   GigawordMetadata_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new GigawordMetadata(addr, GigawordMetadata_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = GigawordMetadata.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.dima.textmining.types.GigawordMetadata");
 
  /** @generated */
  final Feature casFeat_gigawordId;
  /** @generated */
  final int     casFeatCode_gigawordId;
  /** @generated */ 
  public String getGigawordId(int addr) {
        if (featOkTst && casFeat_gigawordId == null)
      jcas.throwFeatMissing("gigawordId", "de.dima.textmining.types.GigawordMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_gigawordId);
  }
  /** @generated */    
  public void setGigawordId(int addr, String v) {
        if (featOkTst && casFeat_gigawordId == null)
      jcas.throwFeatMissing("gigawordId", "de.dima.textmining.types.GigawordMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_gigawordId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_yearPublished;
  /** @generated */
  final int     casFeatCode_yearPublished;
  /** @generated */ 
  public short getYearPublished(int addr) {
        if (featOkTst && casFeat_yearPublished == null)
      jcas.throwFeatMissing("yearPublished", "de.dima.textmining.types.GigawordMetadata");
    return ll_cas.ll_getShortValue(addr, casFeatCode_yearPublished);
  }
  /** @generated */    
  public void setYearPublished(int addr, short v) {
        if (featOkTst && casFeat_yearPublished == null)
      jcas.throwFeatMissing("yearPublished", "de.dima.textmining.types.GigawordMetadata");
    ll_cas.ll_setShortValue(addr, casFeatCode_yearPublished, v);}
    
  
 
  /** @generated */
  final Feature casFeat_monthPublished;
  /** @generated */
  final int     casFeatCode_monthPublished;
  /** @generated */ 
  public short getMonthPublished(int addr) {
        if (featOkTst && casFeat_monthPublished == null)
      jcas.throwFeatMissing("monthPublished", "de.dima.textmining.types.GigawordMetadata");
    return ll_cas.ll_getShortValue(addr, casFeatCode_monthPublished);
  }
  /** @generated */    
  public void setMonthPublished(int addr, short v) {
        if (featOkTst && casFeat_monthPublished == null)
      jcas.throwFeatMissing("monthPublished", "de.dima.textmining.types.GigawordMetadata");
    ll_cas.ll_setShortValue(addr, casFeatCode_monthPublished, v);}
    
  
 
  /** @generated */
  final Feature casFeat_dayPublished;
  /** @generated */
  final int     casFeatCode_dayPublished;
  /** @generated */ 
  public short getDayPublished(int addr) {
        if (featOkTst && casFeat_dayPublished == null)
      jcas.throwFeatMissing("dayPublished", "de.dima.textmining.types.GigawordMetadata");
    return ll_cas.ll_getShortValue(addr, casFeatCode_dayPublished);
  }
  /** @generated */    
  public void setDayPublished(int addr, short v) {
        if (featOkTst && casFeat_dayPublished == null)
      jcas.throwFeatMissing("dayPublished", "de.dima.textmining.types.GigawordMetadata");
    ll_cas.ll_setShortValue(addr, casFeatCode_dayPublished, v);}
    
  
 
  /** @generated */
  final Feature casFeat_city;
  /** @generated */
  final int     casFeatCode_city;
  /** @generated */ 
  public String getCity(int addr) {
        if (featOkTst && casFeat_city == null)
      jcas.throwFeatMissing("city", "de.dima.textmining.types.GigawordMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_city);
  }
  /** @generated */    
  public void setCity(int addr, String v) {
        if (featOkTst && casFeat_city == null)
      jcas.throwFeatMissing("city", "de.dima.textmining.types.GigawordMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_city, v);}
    
  
 
  /** @generated */
  final Feature casFeat_publisher;
  /** @generated */
  final int     casFeatCode_publisher;
  /** @generated */ 
  public String getPublisher(int addr) {
        if (featOkTst && casFeat_publisher == null)
      jcas.throwFeatMissing("publisher", "de.dima.textmining.types.GigawordMetadata");
    return ll_cas.ll_getStringValue(addr, casFeatCode_publisher);
  }
  /** @generated */    
  public void setPublisher(int addr, String v) {
        if (featOkTst && casFeat_publisher == null)
      jcas.throwFeatMissing("publisher", "de.dima.textmining.types.GigawordMetadata");
    ll_cas.ll_setStringValue(addr, casFeatCode_publisher, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public GigawordMetadata_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_gigawordId = jcas.getRequiredFeatureDE(casType, "gigawordId", "uima.cas.String", featOkTst);
    casFeatCode_gigawordId  = (null == casFeat_gigawordId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_gigawordId).getCode();

 
    casFeat_yearPublished = jcas.getRequiredFeatureDE(casType, "yearPublished", "uima.cas.Short", featOkTst);
    casFeatCode_yearPublished  = (null == casFeat_yearPublished) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_yearPublished).getCode();

 
    casFeat_monthPublished = jcas.getRequiredFeatureDE(casType, "monthPublished", "uima.cas.Short", featOkTst);
    casFeatCode_monthPublished  = (null == casFeat_monthPublished) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_monthPublished).getCode();

 
    casFeat_dayPublished = jcas.getRequiredFeatureDE(casType, "dayPublished", "uima.cas.Short", featOkTst);
    casFeatCode_dayPublished  = (null == casFeat_dayPublished) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_dayPublished).getCode();

 
    casFeat_city = jcas.getRequiredFeatureDE(casType, "city", "uima.cas.String", featOkTst);
    casFeatCode_city  = (null == casFeat_city) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_city).getCode();

 
    casFeat_publisher = jcas.getRequiredFeatureDE(casType, "publisher", "uima.cas.String", featOkTst);
    casFeatCode_publisher  = (null == casFeat_publisher) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_publisher).getCode();

  }
}



    