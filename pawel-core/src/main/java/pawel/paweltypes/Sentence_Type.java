
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
public class Sentence_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Sentence_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Sentence_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Sentence(addr, Sentence_Type.this);
  			   Sentence_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Sentence(addr, Sentence_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Sentence.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("pawel.paweltypes.Sentence");
 
  /** @generated */
  final Feature casFeat_Sentence;
  /** @generated */
  final int     casFeatCode_Sentence;
  /** @generated */ 
  public String getSentence(int addr) {
        if (featOkTst && casFeat_Sentence == null)
      jcas.throwFeatMissing("Sentence", "pawel.paweltypes.Sentence");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Sentence);
  }
  /** @generated */    
  public void setSentence(int addr, String v) {
        if (featOkTst && casFeat_Sentence == null)
      jcas.throwFeatMissing("Sentence", "pawel.paweltypes.Sentence");
    ll_cas.ll_setStringValue(addr, casFeatCode_Sentence, v);}
    
  
 
  /** @generated */
  final Feature casFeat_begin;
  /** @generated */
  final int     casFeatCode_begin;
  /** @generated */ 
  public int getBegin(int addr) {
        if (featOkTst && casFeat_begin == null)
      jcas.throwFeatMissing("begin", "pawel.paweltypes.Sentence");
    return ll_cas.ll_getIntValue(addr, casFeatCode_begin);
  }
  /** @generated */    
  public void setBegin(int addr, int v) {
        if (featOkTst && casFeat_begin == null)
      jcas.throwFeatMissing("begin", "pawel.paweltypes.Sentence");
    ll_cas.ll_setIntValue(addr, casFeatCode_begin, v);}
    
  
 
  /** @generated */
  final Feature casFeat_end;
  /** @generated */
  final int     casFeatCode_end;
  /** @generated */ 
  public int getEnd(int addr) {
        if (featOkTst && casFeat_end == null)
      jcas.throwFeatMissing("end", "pawel.paweltypes.Sentence");
    return ll_cas.ll_getIntValue(addr, casFeatCode_end);
  }
  /** @generated */    
  public void setEnd(int addr, int v) {
        if (featOkTst && casFeat_end == null)
      jcas.throwFeatMissing("end", "pawel.paweltypes.Sentence");
    ll_cas.ll_setIntValue(addr, casFeatCode_end, v);}
    
  
 
  /** @generated */
  final Feature casFeat_tokenBegin;
  /** @generated */
  final int     casFeatCode_tokenBegin;
  /** @generated */ 
  public int getTokenBegin(int addr) {
        if (featOkTst && casFeat_tokenBegin == null)
      jcas.throwFeatMissing("tokenBegin", "pawel.paweltypes.Sentence");
    return ll_cas.ll_getIntValue(addr, casFeatCode_tokenBegin);
  }
  /** @generated */    
  public void setTokenBegin(int addr, int v) {
        if (featOkTst && casFeat_tokenBegin == null)
      jcas.throwFeatMissing("tokenBegin", "pawel.paweltypes.Sentence");
    ll_cas.ll_setIntValue(addr, casFeatCode_tokenBegin, v);}
    
  
 
  /** @generated */
  final Feature casFeat_tokenEnd;
  /** @generated */
  final int     casFeatCode_tokenEnd;
  /** @generated */ 
  public int getTokenEnd(int addr) {
        if (featOkTst && casFeat_tokenEnd == null)
      jcas.throwFeatMissing("tokenEnd", "pawel.paweltypes.Sentence");
    return ll_cas.ll_getIntValue(addr, casFeatCode_tokenEnd);
  }
  /** @generated */    
  public void setTokenEnd(int addr, int v) {
        if (featOkTst && casFeat_tokenEnd == null)
      jcas.throwFeatMissing("tokenEnd", "pawel.paweltypes.Sentence");
    ll_cas.ll_setIntValue(addr, casFeatCode_tokenEnd, v);}
    
  
 
  /** @generated */
  final Feature casFeat_sentenceIndex;
  /** @generated */
  final int     casFeatCode_sentenceIndex;
  /** @generated */ 
  public int getSentenceIndex(int addr) {
        if (featOkTst && casFeat_sentenceIndex == null)
      jcas.throwFeatMissing("sentenceIndex", "pawel.paweltypes.Sentence");
    return ll_cas.ll_getIntValue(addr, casFeatCode_sentenceIndex);
  }
  /** @generated */    
  public void setSentenceIndex(int addr, int v) {
        if (featOkTst && casFeat_sentenceIndex == null)
      jcas.throwFeatMissing("sentenceIndex", "pawel.paweltypes.Sentence");
    ll_cas.ll_setIntValue(addr, casFeatCode_sentenceIndex, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Sentence_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Sentence = jcas.getRequiredFeatureDE(casType, "Sentence", "uima.cas.String", featOkTst);
    casFeatCode_Sentence  = (null == casFeat_Sentence) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Sentence).getCode();

 
    casFeat_begin = jcas.getRequiredFeatureDE(casType, "begin", "uima.cas.Integer", featOkTst);
    casFeatCode_begin  = (null == casFeat_begin) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_begin).getCode();

 
    casFeat_end = jcas.getRequiredFeatureDE(casType, "end", "uima.cas.Integer", featOkTst);
    casFeatCode_end  = (null == casFeat_end) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_end).getCode();

 
    casFeat_tokenBegin = jcas.getRequiredFeatureDE(casType, "tokenBegin", "uima.cas.Integer", featOkTst);
    casFeatCode_tokenBegin  = (null == casFeat_tokenBegin) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_tokenBegin).getCode();

 
    casFeat_tokenEnd = jcas.getRequiredFeatureDE(casType, "tokenEnd", "uima.cas.Integer", featOkTst);
    casFeatCode_tokenEnd  = (null == casFeat_tokenEnd) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_tokenEnd).getCode();

 
    casFeat_sentenceIndex = jcas.getRequiredFeatureDE(casType, "sentenceIndex", "uima.cas.Integer", featOkTst);
    casFeatCode_sentenceIndex  = (null == casFeat_sentenceIndex) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_sentenceIndex).getCode();

  }
}



    