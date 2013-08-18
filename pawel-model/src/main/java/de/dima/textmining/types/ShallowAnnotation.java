

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
public class ShallowAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(ShallowAnnotation.class);
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
  protected ShallowAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public ShallowAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public ShallowAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public ShallowAnnotation(JCas jcas, int begin, int end) {
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
  //* Feature: PosTag

  /** getter for PosTag - gets 
   * @generated */
  public String getPosTag() {
    if (ShallowAnnotation_Type.featOkTst && ((ShallowAnnotation_Type)jcasType).casFeat_PosTag == null)
      jcasType.jcas.throwFeatMissing("PosTag", "de.dima.textmining.types.ShallowAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ShallowAnnotation_Type)jcasType).casFeatCode_PosTag);}
    
  /** setter for PosTag - sets  
   * @generated */
  public void setPosTag(String v) {
    if (ShallowAnnotation_Type.featOkTst && ((ShallowAnnotation_Type)jcasType).casFeat_PosTag == null)
      jcasType.jcas.throwFeatMissing("PosTag", "de.dima.textmining.types.ShallowAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((ShallowAnnotation_Type)jcasType).casFeatCode_PosTag, v);}    
   
    
  //*--------------*
  //* Feature: Lemma

  /** getter for Lemma - gets 
   * @generated */
  public String getLemma() {
    if (ShallowAnnotation_Type.featOkTst && ((ShallowAnnotation_Type)jcasType).casFeat_Lemma == null)
      jcasType.jcas.throwFeatMissing("Lemma", "de.dima.textmining.types.ShallowAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ShallowAnnotation_Type)jcasType).casFeatCode_Lemma);}
    
  /** setter for Lemma - sets  
   * @generated */
  public void setLemma(String v) {
    if (ShallowAnnotation_Type.featOkTst && ((ShallowAnnotation_Type)jcasType).casFeat_Lemma == null)
      jcasType.jcas.throwFeatMissing("Lemma", "de.dima.textmining.types.ShallowAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((ShallowAnnotation_Type)jcasType).casFeatCode_Lemma, v);}    
  }

    