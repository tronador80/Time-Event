

/* First created by JCasGen Mon Dec 10 10:45:18 CET 2012 */
package de.dima.textmining.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import de.unihd.dbs.uima.types.heideltime.Timex3;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Dec 10 10:45:41 CET 2012
 * XML source: /home/robert/Projekte/timeline/time/src/dima-uima/src/main/resources/desc/typesystem/compoundTypeSystem.xml
 * @generated */
public class Timespan extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Timespan.class);
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
  protected Timespan() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Timespan(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Timespan(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Timespan(JCas jcas, int begin, int end) {
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
  //* Feature: startTime

  /** getter for startTime - gets 
   * @generated */
  public Timex3 getStartTime() {
    if (Timespan_Type.featOkTst && ((Timespan_Type)jcasType).casFeat_startTime == null)
      jcasType.jcas.throwFeatMissing("startTime", "de.dima.textmining.types.Timespan");
    return (Timex3)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Timespan_Type)jcasType).casFeatCode_startTime)));}
    
  /** setter for startTime - sets  
   * @generated */
  public void setStartTime(Timex3 v) {
    if (Timespan_Type.featOkTst && ((Timespan_Type)jcasType).casFeat_startTime == null)
      jcasType.jcas.throwFeatMissing("startTime", "de.dima.textmining.types.Timespan");
    jcasType.ll_cas.ll_setRefValue(addr, ((Timespan_Type)jcasType).casFeatCode_startTime, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: EndTime

  /** getter for EndTime - gets 
   * @generated */
  public Timex3 getEndTime() {
    if (Timespan_Type.featOkTst && ((Timespan_Type)jcasType).casFeat_EndTime == null)
      jcasType.jcas.throwFeatMissing("EndTime", "de.dima.textmining.types.Timespan");
    return (Timex3)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Timespan_Type)jcasType).casFeatCode_EndTime)));}
    
  /** setter for EndTime - sets  
   * @generated */
  public void setEndTime(Timex3 v) {
    if (Timespan_Type.featOkTst && ((Timespan_Type)jcasType).casFeat_EndTime == null)
      jcasType.jcas.throwFeatMissing("EndTime", "de.dima.textmining.types.Timespan");
    jcasType.ll_cas.ll_setRefValue(addr, ((Timespan_Type)jcasType).casFeatCode_EndTime, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    