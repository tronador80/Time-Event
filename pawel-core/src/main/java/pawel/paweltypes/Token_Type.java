
/* First created by JCasGen Wed Sep 25 20:16:43 CEST 2013 */
package pawel.paweltypes;

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

/** 
 * Updated by JCasGen Wed Sep 25 20:16:43 CEST 2013
 * @generated */
public class Token_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Token_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Token_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Token(addr, Token_Type.this);
  			   Token_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Token(addr, Token_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Token.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("pawel.paweltypes.Token");
 
  /** @generated */
  final Feature casFeat_value;
  /** @generated */
  final int     casFeatCode_value;
  /** @generated */ 
  public String getValue(int addr) {
        if (featOkTst && casFeat_value == null)
      jcas.throwFeatMissing("value", "pawel.paweltypes.Token");
    return ll_cas.ll_getStringValue(addr, casFeatCode_value);
  }
  /** @generated */    
  public void setValue(int addr, String v) {
        if (featOkTst && casFeat_value == null)
      jcas.throwFeatMissing("value", "pawel.paweltypes.Token");
    ll_cas.ll_setStringValue(addr, casFeatCode_value, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Token;
  /** @generated */
  final int     casFeatCode_Token;
  /** @generated */ 
  public String getToken(int addr) {
        if (featOkTst && casFeat_Token == null)
      jcas.throwFeatMissing("Token", "pawel.paweltypes.Token");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Token);
  }
  /** @generated */    
  public void setToken(int addr, String v) {
        if (featOkTst && casFeat_Token == null)
      jcas.throwFeatMissing("Token", "pawel.paweltypes.Token");
    ll_cas.ll_setStringValue(addr, casFeatCode_Token, v);}
    
  
 
  /** @generated */
  final Feature casFeat_originalText;
  /** @generated */
  final int     casFeatCode_originalText;
  /** @generated */ 
  public String getOriginalText(int addr) {
        if (featOkTst && casFeat_originalText == null)
      jcas.throwFeatMissing("originalText", "pawel.paweltypes.Token");
    return ll_cas.ll_getStringValue(addr, casFeatCode_originalText);
  }
  /** @generated */    
  public void setOriginalText(int addr, String v) {
        if (featOkTst && casFeat_originalText == null)
      jcas.throwFeatMissing("originalText", "pawel.paweltypes.Token");
    ll_cas.ll_setStringValue(addr, casFeatCode_originalText, v);}
    
  
 
  /** @generated */
  final Feature casFeat_begin;
  /** @generated */
  final int     casFeatCode_begin;
  /** @generated */ 
  public int getBegin(int addr) {
        if (featOkTst && casFeat_begin == null)
      jcas.throwFeatMissing("begin", "pawel.paweltypes.Token");
    return ll_cas.ll_getIntValue(addr, casFeatCode_begin);
  }
  /** @generated */    
  public void setBegin(int addr, int v) {
        if (featOkTst && casFeat_begin == null)
      jcas.throwFeatMissing("begin", "pawel.paweltypes.Token");
    ll_cas.ll_setIntValue(addr, casFeatCode_begin, v);}
    
  
 
  /** @generated */
  final Feature casFeat_end;
  /** @generated */
  final int     casFeatCode_end;
  /** @generated */ 
  public int getEnd(int addr) {
        if (featOkTst && casFeat_end == null)
      jcas.throwFeatMissing("end", "pawel.paweltypes.Token");
    return ll_cas.ll_getIntValue(addr, casFeatCode_end);
  }
  /** @generated */    
  public void setEnd(int addr, int v) {
        if (featOkTst && casFeat_end == null)
      jcas.throwFeatMissing("end", "pawel.paweltypes.Token");
    ll_cas.ll_setIntValue(addr, casFeatCode_end, v);}
    
  
 
  /** @generated */
  final Feature casFeat_beforeToken;
  /** @generated */
  final int     casFeatCode_beforeToken;
  /** @generated */ 
  public String getBeforeToken(int addr) {
        if (featOkTst && casFeat_beforeToken == null)
      jcas.throwFeatMissing("beforeToken", "pawel.paweltypes.Token");
    return ll_cas.ll_getStringValue(addr, casFeatCode_beforeToken);
  }
  /** @generated */    
  public void setBeforeToken(int addr, String v) {
        if (featOkTst && casFeat_beforeToken == null)
      jcas.throwFeatMissing("beforeToken", "pawel.paweltypes.Token");
    ll_cas.ll_setStringValue(addr, casFeatCode_beforeToken, v);}
    
  
 
  /** @generated */
  final Feature casFeat_afterToken;
  /** @generated */
  final int     casFeatCode_afterToken;
  /** @generated */ 
  public String getAfterToken(int addr) {
        if (featOkTst && casFeat_afterToken == null)
      jcas.throwFeatMissing("afterToken", "pawel.paweltypes.Token");
    return ll_cas.ll_getStringValue(addr, casFeatCode_afterToken);
  }
  /** @generated */    
  public void setAfterToken(int addr, String v) {
        if (featOkTst && casFeat_afterToken == null)
      jcas.throwFeatMissing("afterToken", "pawel.paweltypes.Token");
    ll_cas.ll_setStringValue(addr, casFeatCode_afterToken, v);}
    
  
 
  /** @generated */
  final Feature casFeat_pos;
  /** @generated */
  final int     casFeatCode_pos;
  /** @generated */ 
  public String getPos(int addr) {
        if (featOkTst && casFeat_pos == null)
      jcas.throwFeatMissing("pos", "pawel.paweltypes.Token");
    return ll_cas.ll_getStringValue(addr, casFeatCode_pos);
  }
  /** @generated */    
  public void setPos(int addr, String v) {
        if (featOkTst && casFeat_pos == null)
      jcas.throwFeatMissing("pos", "pawel.paweltypes.Token");
    ll_cas.ll_setStringValue(addr, casFeatCode_pos, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Token_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_value = jcas.getRequiredFeatureDE(casType, "value", "uima.cas.String", featOkTst);
    casFeatCode_value  = (null == casFeat_value) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_value).getCode();

 
    casFeat_Token = jcas.getRequiredFeatureDE(casType, "Token", "uima.cas.String", featOkTst);
    casFeatCode_Token  = (null == casFeat_Token) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Token).getCode();

 
    casFeat_originalText = jcas.getRequiredFeatureDE(casType, "originalText", "uima.cas.String", featOkTst);
    casFeatCode_originalText  = (null == casFeat_originalText) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_originalText).getCode();

 
    casFeat_begin = jcas.getRequiredFeatureDE(casType, "begin", "uima.cas.Integer", featOkTst);
    casFeatCode_begin  = (null == casFeat_begin) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_begin).getCode();

 
    casFeat_end = jcas.getRequiredFeatureDE(casType, "end", "uima.cas.Integer", featOkTst);
    casFeatCode_end  = (null == casFeat_end) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_end).getCode();

 
    casFeat_beforeToken = jcas.getRequiredFeatureDE(casType, "beforeToken", "uima.cas.String", featOkTst);
    casFeatCode_beforeToken  = (null == casFeat_beforeToken) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_beforeToken).getCode();

 
    casFeat_afterToken = jcas.getRequiredFeatureDE(casType, "afterToken", "uima.cas.String", featOkTst);
    casFeatCode_afterToken  = (null == casFeat_afterToken) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_afterToken).getCode();

 
    casFeat_pos = jcas.getRequiredFeatureDE(casType, "pos", "uima.cas.String", featOkTst);
    casFeatCode_pos  = (null == casFeat_pos) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_pos).getCode();

  }
}



    