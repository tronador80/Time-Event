

/* First created by JCasGen Mon Dec 10 10:45:26 CET 2012 */
package de.dima.textmining.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** Metadata from Gigaword Article
 * Updated by JCasGen Mon Dec 10 10:45:41 CET 2012
 * XML source: /home/robert/Projekte/timeline/time/src/dima-uima/src/main/resources/desc/typesystem/compoundTypeSystem.xml
 * @generated */
public class GigawordMetadata extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(GigawordMetadata.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected GigawordMetadata() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public GigawordMetadata(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public GigawordMetadata(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public GigawordMetadata(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: gigawordId

  /** getter for gigawordId - gets Article id from gigaword corpus.
   * @generated */
  public String getGigawordId() {
    if (GigawordMetadata_Type.featOkTst && ((GigawordMetadata_Type)jcasType).casFeat_gigawordId == null)
      jcasType.jcas.throwFeatMissing("gigawordId", "de.dima.textmining.types.GigawordMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((GigawordMetadata_Type)jcasType).casFeatCode_gigawordId);}
    
  /** setter for gigawordId - sets Article id from gigaword corpus. 
   * @generated */
  public void setGigawordId(String v) {
    if (GigawordMetadata_Type.featOkTst && ((GigawordMetadata_Type)jcasType).casFeat_gigawordId == null)
      jcasType.jcas.throwFeatMissing("gigawordId", "de.dima.textmining.types.GigawordMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((GigawordMetadata_Type)jcasType).casFeatCode_gigawordId, v);}    
   
    
  //*--------------*
  //* Feature: yearPublished

  /** getter for yearPublished - gets 
   * @generated */
  public short getYearPublished() {
    if (GigawordMetadata_Type.featOkTst && ((GigawordMetadata_Type)jcasType).casFeat_yearPublished == null)
      jcasType.jcas.throwFeatMissing("yearPublished", "de.dima.textmining.types.GigawordMetadata");
    return jcasType.ll_cas.ll_getShortValue(addr, ((GigawordMetadata_Type)jcasType).casFeatCode_yearPublished);}
    
  /** setter for yearPublished - sets  
   * @generated */
  public void setYearPublished(short v) {
    if (GigawordMetadata_Type.featOkTst && ((GigawordMetadata_Type)jcasType).casFeat_yearPublished == null)
      jcasType.jcas.throwFeatMissing("yearPublished", "de.dima.textmining.types.GigawordMetadata");
    jcasType.ll_cas.ll_setShortValue(addr, ((GigawordMetadata_Type)jcasType).casFeatCode_yearPublished, v);}    
   
    
  //*--------------*
  //* Feature: monthPublished

  /** getter for monthPublished - gets 
   * @generated */
  public short getMonthPublished() {
    if (GigawordMetadata_Type.featOkTst && ((GigawordMetadata_Type)jcasType).casFeat_monthPublished == null)
      jcasType.jcas.throwFeatMissing("monthPublished", "de.dima.textmining.types.GigawordMetadata");
    return jcasType.ll_cas.ll_getShortValue(addr, ((GigawordMetadata_Type)jcasType).casFeatCode_monthPublished);}
    
  /** setter for monthPublished - sets  
   * @generated */
  public void setMonthPublished(short v) {
    if (GigawordMetadata_Type.featOkTst && ((GigawordMetadata_Type)jcasType).casFeat_monthPublished == null)
      jcasType.jcas.throwFeatMissing("monthPublished", "de.dima.textmining.types.GigawordMetadata");
    jcasType.ll_cas.ll_setShortValue(addr, ((GigawordMetadata_Type)jcasType).casFeatCode_monthPublished, v);}    
   
    
  //*--------------*
  //* Feature: dayPublished

  /** getter for dayPublished - gets 
   * @generated */
  public short getDayPublished() {
    if (GigawordMetadata_Type.featOkTst && ((GigawordMetadata_Type)jcasType).casFeat_dayPublished == null)
      jcasType.jcas.throwFeatMissing("dayPublished", "de.dima.textmining.types.GigawordMetadata");
    return jcasType.ll_cas.ll_getShortValue(addr, ((GigawordMetadata_Type)jcasType).casFeatCode_dayPublished);}
    
  /** setter for dayPublished - sets  
   * @generated */
  public void setDayPublished(short v) {
    if (GigawordMetadata_Type.featOkTst && ((GigawordMetadata_Type)jcasType).casFeat_dayPublished == null)
      jcasType.jcas.throwFeatMissing("dayPublished", "de.dima.textmining.types.GigawordMetadata");
    jcasType.ll_cas.ll_setShortValue(addr, ((GigawordMetadata_Type)jcasType).casFeatCode_dayPublished, v);}    
   
    
  //*--------------*
  //* Feature: city

  /** getter for city - gets 
   * @generated */
  public String getCity() {
    if (GigawordMetadata_Type.featOkTst && ((GigawordMetadata_Type)jcasType).casFeat_city == null)
      jcasType.jcas.throwFeatMissing("city", "de.dima.textmining.types.GigawordMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((GigawordMetadata_Type)jcasType).casFeatCode_city);}
    
  /** setter for city - sets  
   * @generated */
  public void setCity(String v) {
    if (GigawordMetadata_Type.featOkTst && ((GigawordMetadata_Type)jcasType).casFeat_city == null)
      jcasType.jcas.throwFeatMissing("city", "de.dima.textmining.types.GigawordMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((GigawordMetadata_Type)jcasType).casFeatCode_city, v);}    
   
    
  //*--------------*
  //* Feature: publisher

  /** getter for publisher - gets Publisher of the article
   * @generated */
  public String getPublisher() {
    if (GigawordMetadata_Type.featOkTst && ((GigawordMetadata_Type)jcasType).casFeat_publisher == null)
      jcasType.jcas.throwFeatMissing("publisher", "de.dima.textmining.types.GigawordMetadata");
    return jcasType.ll_cas.ll_getStringValue(addr, ((GigawordMetadata_Type)jcasType).casFeatCode_publisher);}
    
  /** setter for publisher - sets Publisher of the article 
   * @generated */
  public void setPublisher(String v) {
    if (GigawordMetadata_Type.featOkTst && ((GigawordMetadata_Type)jcasType).casFeat_publisher == null)
      jcasType.jcas.throwFeatMissing("publisher", "de.dima.textmining.types.GigawordMetadata");
    jcasType.ll_cas.ll_setStringValue(addr, ((GigawordMetadata_Type)jcasType).casFeatCode_publisher, v);}    
  }

    