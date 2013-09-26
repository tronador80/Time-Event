

/* First created by JCasGen Wed Sep 25 20:16:43 CEST 2013 */
package pawel.paweltypes;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Sep 25 20:16:43 CEST 2013
 * XML source: /home/ptondryk/git/sopremo-pawel/pawel-core/src/main/resources/desc/pawel/Pawel_TypeSystem.xml
 * @generated */
public class Sentence extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Sentence.class);
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
  protected Sentence() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Sentence(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Sentence(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Sentence(JCas jcas, int begin, int end) {
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
  //* Feature: Sentence

  /** getter for Sentence - gets 
   * @generated */
  public String getSentence() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_Sentence == null)
      jcasType.jcas.throwFeatMissing("Sentence", "pawel.paweltypes.Sentence");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Sentence_Type)jcasType).casFeatCode_Sentence);}
    
  /** setter for Sentence - sets  
   * @generated */
  public void setSentence(String v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_Sentence == null)
      jcasType.jcas.throwFeatMissing("Sentence", "pawel.paweltypes.Sentence");
    jcasType.ll_cas.ll_setStringValue(addr, ((Sentence_Type)jcasType).casFeatCode_Sentence, v);}    
   
    
  //*--------------*
  //* Feature: begin

  /** getter for begin - gets 
   * @generated */
  public int getBegin() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_begin == null)
      jcasType.jcas.throwFeatMissing("begin", "pawel.paweltypes.Sentence");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Sentence_Type)jcasType).casFeatCode_begin);}
    
  /** setter for begin - sets  
   * @generated */
  public void setBegin(int v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_begin == null)
      jcasType.jcas.throwFeatMissing("begin", "pawel.paweltypes.Sentence");
    jcasType.ll_cas.ll_setIntValue(addr, ((Sentence_Type)jcasType).casFeatCode_begin, v);}    
   
    
  //*--------------*
  //* Feature: end

  /** getter for end - gets 
   * @generated */
  public int getEnd() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_end == null)
      jcasType.jcas.throwFeatMissing("end", "pawel.paweltypes.Sentence");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Sentence_Type)jcasType).casFeatCode_end);}
    
  /** setter for end - sets  
   * @generated */
  public void setEnd(int v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_end == null)
      jcasType.jcas.throwFeatMissing("end", "pawel.paweltypes.Sentence");
    jcasType.ll_cas.ll_setIntValue(addr, ((Sentence_Type)jcasType).casFeatCode_end, v);}    
   
    
  //*--------------*
  //* Feature: tokenBegin

  /** getter for tokenBegin - gets 
   * @generated */
  public int getTokenBegin() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_tokenBegin == null)
      jcasType.jcas.throwFeatMissing("tokenBegin", "pawel.paweltypes.Sentence");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Sentence_Type)jcasType).casFeatCode_tokenBegin);}
    
  /** setter for tokenBegin - sets  
   * @generated */
  public void setTokenBegin(int v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_tokenBegin == null)
      jcasType.jcas.throwFeatMissing("tokenBegin", "pawel.paweltypes.Sentence");
    jcasType.ll_cas.ll_setIntValue(addr, ((Sentence_Type)jcasType).casFeatCode_tokenBegin, v);}    
   
    
  //*--------------*
  //* Feature: tokenEnd

  /** getter for tokenEnd - gets 
   * @generated */
  public int getTokenEnd() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_tokenEnd == null)
      jcasType.jcas.throwFeatMissing("tokenEnd", "pawel.paweltypes.Sentence");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Sentence_Type)jcasType).casFeatCode_tokenEnd);}
    
  /** setter for tokenEnd - sets  
   * @generated */
  public void setTokenEnd(int v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_tokenEnd == null)
      jcasType.jcas.throwFeatMissing("tokenEnd", "pawel.paweltypes.Sentence");
    jcasType.ll_cas.ll_setIntValue(addr, ((Sentence_Type)jcasType).casFeatCode_tokenEnd, v);}    
   
    
  //*--------------*
  //* Feature: sentenceIndex

  /** getter for sentenceIndex - gets 
   * @generated */
  public int getSentenceIndex() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_sentenceIndex == null)
      jcasType.jcas.throwFeatMissing("sentenceIndex", "pawel.paweltypes.Sentence");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Sentence_Type)jcasType).casFeatCode_sentenceIndex);}
    
  /** setter for sentenceIndex - sets  
   * @generated */
  public void setSentenceIndex(int v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_sentenceIndex == null)
      jcasType.jcas.throwFeatMissing("sentenceIndex", "pawel.paweltypes.Sentence");
    jcasType.ll_cas.ll_setIntValue(addr, ((Sentence_Type)jcasType).casFeatCode_sentenceIndex, v);}    
  }

    