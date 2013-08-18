

/* First created by JCasGen Mon Dec 10 10:45:35 CET 2012 */
package de.dima.textmining.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Dec 10 10:45:41 CET 2012
 * XML source: /home/robert/Projekte/timeline/time/src/dima-uima/src/main/resources/desc/typesystem/compoundTypeSystem.xml
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
  //* Feature: CoNLLParse

  /** getter for CoNLLParse - gets This field may contain the parsed sentence in CoNLL format
   * @generated */
  public String getCoNLLParse() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_CoNLLParse == null)
      jcasType.jcas.throwFeatMissing("CoNLLParse", "de.dima.textmining.types.Sentence");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Sentence_Type)jcasType).casFeatCode_CoNLLParse);}
    
  /** setter for CoNLLParse - sets This field may contain the parsed sentence in CoNLL format 
   * @generated */
  public void setCoNLLParse(String v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_CoNLLParse == null)
      jcasType.jcas.throwFeatMissing("CoNLLParse", "de.dima.textmining.types.Sentence");
    jcasType.ll_cas.ll_setStringValue(addr, ((Sentence_Type)jcasType).casFeatCode_CoNLLParse, v);}    
  }

    