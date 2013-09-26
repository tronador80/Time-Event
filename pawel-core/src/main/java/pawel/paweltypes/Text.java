

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
public class Text extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Text.class);
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
  protected Text() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Text(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Text(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Text(JCas jcas, int begin, int end) {
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
  //* Feature: Text

  /** getter for Text - gets 
   * @generated */
  public String getText() {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_Text == null)
      jcasType.jcas.throwFeatMissing("Text", "pawel.paweltypes.Text");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Text_Type)jcasType).casFeatCode_Text);}
    
  /** setter for Text - sets  
   * @generated */
  public void setText(String v) {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_Text == null)
      jcasType.jcas.throwFeatMissing("Text", "pawel.paweltypes.Text");
    jcasType.ll_cas.ll_setStringValue(addr, ((Text_Type)jcasType).casFeatCode_Text, v);}    
   
    
  //*--------------*
  //* Feature: date

  /** getter for date - gets text's timestamp in form: yyyyMMddHHmmss
   * @generated */
  public String getDate() {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "pawel.paweltypes.Text");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Text_Type)jcasType).casFeatCode_date);}
    
  /** setter for date - sets text's timestamp in form: yyyyMMddHHmmss 
   * @generated */
  public void setDate(String v) {
    if (Text_Type.featOkTst && ((Text_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "pawel.paweltypes.Text");
    jcasType.ll_cas.ll_setStringValue(addr, ((Text_Type)jcasType).casFeatCode_date, v);}    
  }

    