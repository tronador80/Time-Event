

/* First created by JCasGen Fri Jul 26 00:15:14 CEST 2013 */
package pawel.types.pawel;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Fri Jul 26 00:15:14 CEST 2013
 * XML source: /Users/ptondryk/Dropbox/projects/Masterarbeit_git/dopa/pawel/pawel-core/src/main/resources/desc/pawel/Pawel_TypeSystem.xml
 * @generated */
public class Timex3 extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Timex3.class);
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
  protected Timex3() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Timex3(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Timex3(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Timex3(JCas jcas, int begin, int end) {
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
  //* Feature: filename

  /** getter for filename - gets 
   * @generated */
  public String getFilename() {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_filename == null)
      jcasType.jcas.throwFeatMissing("filename", "pawel.types.pawel.Timex3");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_filename);}
    
  /** setter for filename - sets  
   * @generated */
  public void setFilename(String v) {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_filename == null)
      jcasType.jcas.throwFeatMissing("filename", "pawel.types.pawel.Timex3");
    jcasType.ll_cas.ll_setStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_filename, v);}    
   
    
  //*--------------*
  //* Feature: sentId

  /** getter for sentId - gets 
   * @generated */
  public int getSentId() {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_sentId == null)
      jcasType.jcas.throwFeatMissing("sentId", "pawel.types.pawel.Timex3");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Timex3_Type)jcasType).casFeatCode_sentId);}
    
  /** setter for sentId - sets  
   * @generated */
  public void setSentId(int v) {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_sentId == null)
      jcasType.jcas.throwFeatMissing("sentId", "pawel.types.pawel.Timex3");
    jcasType.ll_cas.ll_setIntValue(addr, ((Timex3_Type)jcasType).casFeatCode_sentId, v);}    
   
    
  //*--------------*
  //* Feature: firstTokId

  /** getter for firstTokId - gets 
   * @generated */
  public int getFirstTokId() {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_firstTokId == null)
      jcasType.jcas.throwFeatMissing("firstTokId", "pawel.types.pawel.Timex3");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Timex3_Type)jcasType).casFeatCode_firstTokId);}
    
  /** setter for firstTokId - sets  
   * @generated */
  public void setFirstTokId(int v) {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_firstTokId == null)
      jcasType.jcas.throwFeatMissing("firstTokId", "pawel.types.pawel.Timex3");
    jcasType.ll_cas.ll_setIntValue(addr, ((Timex3_Type)jcasType).casFeatCode_firstTokId, v);}    
   
    
  //*--------------*
  //* Feature: allTokIds

  /** getter for allTokIds - gets 
   * @generated */
  public String getAllTokIds() {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_allTokIds == null)
      jcasType.jcas.throwFeatMissing("allTokIds", "pawel.types.pawel.Timex3");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_allTokIds);}
    
  /** setter for allTokIds - sets  
   * @generated */
  public void setAllTokIds(String v) {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_allTokIds == null)
      jcasType.jcas.throwFeatMissing("allTokIds", "pawel.types.pawel.Timex3");
    jcasType.ll_cas.ll_setStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_allTokIds, v);}    
   
    
  //*--------------*
  //* Feature: timexId

  /** getter for timexId - gets 
   * @generated */
  public String getTimexId() {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_timexId == null)
      jcasType.jcas.throwFeatMissing("timexId", "pawel.types.pawel.Timex3");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_timexId);}
    
  /** setter for timexId - sets  
   * @generated */
  public void setTimexId(String v) {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_timexId == null)
      jcasType.jcas.throwFeatMissing("timexId", "pawel.types.pawel.Timex3");
    jcasType.ll_cas.ll_setStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_timexId, v);}    
   
    
  //*--------------*
  //* Feature: timexInstance

  /** getter for timexInstance - gets 
   * @generated */
  public int getTimexInstance() {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_timexInstance == null)
      jcasType.jcas.throwFeatMissing("timexInstance", "pawel.types.pawel.Timex3");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Timex3_Type)jcasType).casFeatCode_timexInstance);}
    
  /** setter for timexInstance - sets  
   * @generated */
  public void setTimexInstance(int v) {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_timexInstance == null)
      jcasType.jcas.throwFeatMissing("timexInstance", "pawel.types.pawel.Timex3");
    jcasType.ll_cas.ll_setIntValue(addr, ((Timex3_Type)jcasType).casFeatCode_timexInstance, v);}    
   
    
  //*--------------*
  //* Feature: timexType

  /** getter for timexType - gets 
   * @generated */
  public String getTimexType() {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_timexType == null)
      jcasType.jcas.throwFeatMissing("timexType", "pawel.types.pawel.Timex3");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_timexType);}
    
  /** setter for timexType - sets  
   * @generated */
  public void setTimexType(String v) {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_timexType == null)
      jcasType.jcas.throwFeatMissing("timexType", "pawel.types.pawel.Timex3");
    jcasType.ll_cas.ll_setStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_timexType, v);}    
   
    
  //*--------------*
  //* Feature: timexValue

  /** getter for timexValue - gets 
   * @generated */
  public String getTimexValue() {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_timexValue == null)
      jcasType.jcas.throwFeatMissing("timexValue", "pawel.types.pawel.Timex3");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_timexValue);}
    
  /** setter for timexValue - sets  
   * @generated */
  public void setTimexValue(String v) {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_timexValue == null)
      jcasType.jcas.throwFeatMissing("timexValue", "pawel.types.pawel.Timex3");
    jcasType.ll_cas.ll_setStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_timexValue, v);}    
   
    
  //*--------------*
  //* Feature: foundByRule

  /** getter for foundByRule - gets 
   * @generated */
  public String getFoundByRule() {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_foundByRule == null)
      jcasType.jcas.throwFeatMissing("foundByRule", "pawel.types.pawel.Timex3");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_foundByRule);}
    
  /** setter for foundByRule - sets  
   * @generated */
  public void setFoundByRule(String v) {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_foundByRule == null)
      jcasType.jcas.throwFeatMissing("foundByRule", "pawel.types.pawel.Timex3");
    jcasType.ll_cas.ll_setStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_foundByRule, v);}    
   
    
  //*--------------*
  //* Feature: timexQuant

  /** getter for timexQuant - gets 
   * @generated */
  public String getTimexQuant() {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_timexQuant == null)
      jcasType.jcas.throwFeatMissing("timexQuant", "pawel.types.pawel.Timex3");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_timexQuant);}
    
  /** setter for timexQuant - sets  
   * @generated */
  public void setTimexQuant(String v) {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_timexQuant == null)
      jcasType.jcas.throwFeatMissing("timexQuant", "pawel.types.pawel.Timex3");
    jcasType.ll_cas.ll_setStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_timexQuant, v);}    
   
    
  //*--------------*
  //* Feature: timexFreq

  /** getter for timexFreq - gets 
   * @generated */
  public String getTimexFreq() {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_timexFreq == null)
      jcasType.jcas.throwFeatMissing("timexFreq", "pawel.types.pawel.Timex3");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_timexFreq);}
    
  /** setter for timexFreq - sets  
   * @generated */
  public void setTimexFreq(String v) {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_timexFreq == null)
      jcasType.jcas.throwFeatMissing("timexFreq", "pawel.types.pawel.Timex3");
    jcasType.ll_cas.ll_setStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_timexFreq, v);}    
   
    
  //*--------------*
  //* Feature: timexMod

  /** getter for timexMod - gets 
   * @generated */
  public String getTimexMod() {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_timexMod == null)
      jcasType.jcas.throwFeatMissing("timexMod", "pawel.types.pawel.Timex3");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_timexMod);}
    
  /** setter for timexMod - sets  
   * @generated */
  public void setTimexMod(String v) {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_timexMod == null)
      jcasType.jcas.throwFeatMissing("timexMod", "pawel.types.pawel.Timex3");
    jcasType.ll_cas.ll_setStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_timexMod, v);}    
   
    
  //*--------------*
  //* Feature: Timex3

  /** getter for Timex3 - gets 
   * @generated */
  public String getTimex3() {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_Timex3 == null)
      jcasType.jcas.throwFeatMissing("Timex3", "pawel.types.pawel.Timex3");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_Timex3);}
    
  /** setter for Timex3 - sets  
   * @generated */
  public void setTimex3(String v) {
    if (Timex3_Type.featOkTst && ((Timex3_Type)jcasType).casFeat_Timex3 == null)
      jcasType.jcas.throwFeatMissing("Timex3", "pawel.types.pawel.Timex3");
    jcasType.ll_cas.ll_setStringValue(addr, ((Timex3_Type)jcasType).casFeatCode_Timex3, v);}    
  }

    