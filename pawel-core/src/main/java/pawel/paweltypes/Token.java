

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
public class Token extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Token.class);
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
  protected Token() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Token(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Token(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Token(JCas jcas, int begin, int end) {
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
  //* Feature: value

  /** getter for value - gets 
   * @generated */
  public String getValue() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_value == null)
      jcasType.jcas.throwFeatMissing("value", "pawel.paweltypes.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_value);}
    
  /** setter for value - sets  
   * @generated */
  public void setValue(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_value == null)
      jcasType.jcas.throwFeatMissing("value", "pawel.paweltypes.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_value, v);}    
   
    
  //*--------------*
  //* Feature: Token

  /** getter for Token - gets 
   * @generated */
  public String getToken() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_Token == null)
      jcasType.jcas.throwFeatMissing("Token", "pawel.paweltypes.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_Token);}
    
  /** setter for Token - sets  
   * @generated */
  public void setToken(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_Token == null)
      jcasType.jcas.throwFeatMissing("Token", "pawel.paweltypes.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_Token, v);}    
   
    
  //*--------------*
  //* Feature: originalText

  /** getter for originalText - gets 
   * @generated */
  public String getOriginalText() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_originalText == null)
      jcasType.jcas.throwFeatMissing("originalText", "pawel.paweltypes.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_originalText);}
    
  /** setter for originalText - sets  
   * @generated */
  public void setOriginalText(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_originalText == null)
      jcasType.jcas.throwFeatMissing("originalText", "pawel.paweltypes.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_originalText, v);}    
   
    
  //*--------------*
  //* Feature: begin

  /** getter for begin - gets 
   * @generated */
  public int getBegin() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_begin == null)
      jcasType.jcas.throwFeatMissing("begin", "pawel.paweltypes.Token");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Token_Type)jcasType).casFeatCode_begin);}
    
  /** setter for begin - sets  
   * @generated */
  public void setBegin(int v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_begin == null)
      jcasType.jcas.throwFeatMissing("begin", "pawel.paweltypes.Token");
    jcasType.ll_cas.ll_setIntValue(addr, ((Token_Type)jcasType).casFeatCode_begin, v);}    
   
    
  //*--------------*
  //* Feature: end

  /** getter for end - gets 
   * @generated */
  public int getEnd() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_end == null)
      jcasType.jcas.throwFeatMissing("end", "pawel.paweltypes.Token");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Token_Type)jcasType).casFeatCode_end);}
    
  /** setter for end - sets  
   * @generated */
  public void setEnd(int v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_end == null)
      jcasType.jcas.throwFeatMissing("end", "pawel.paweltypes.Token");
    jcasType.ll_cas.ll_setIntValue(addr, ((Token_Type)jcasType).casFeatCode_end, v);}    
   
    
  //*--------------*
  //* Feature: beforeToken

  /** getter for beforeToken - gets 
   * @generated */
  public String getBeforeToken() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_beforeToken == null)
      jcasType.jcas.throwFeatMissing("beforeToken", "pawel.paweltypes.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_beforeToken);}
    
  /** setter for beforeToken - sets  
   * @generated */
  public void setBeforeToken(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_beforeToken == null)
      jcasType.jcas.throwFeatMissing("beforeToken", "pawel.paweltypes.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_beforeToken, v);}    
   
    
  //*--------------*
  //* Feature: afterToken

  /** getter for afterToken - gets 
   * @generated */
  public String getAfterToken() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_afterToken == null)
      jcasType.jcas.throwFeatMissing("afterToken", "pawel.paweltypes.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_afterToken);}
    
  /** setter for afterToken - sets  
   * @generated */
  public void setAfterToken(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_afterToken == null)
      jcasType.jcas.throwFeatMissing("afterToken", "pawel.paweltypes.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_afterToken, v);}    
   
    
  //*--------------*
  //* Feature: pos

  /** getter for pos - gets 
   * @generated */
  public String getPos() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_pos == null)
      jcasType.jcas.throwFeatMissing("pos", "pawel.paweltypes.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_pos);}
    
  /** setter for pos - sets  
   * @generated */
  public void setPos(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_pos == null)
      jcasType.jcas.throwFeatMissing("pos", "pawel.paweltypes.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_pos, v);}    
  }

    