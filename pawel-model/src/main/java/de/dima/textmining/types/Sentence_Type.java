
/* First created by JCasGen Mon Dec 10 10:45:35 CET 2012 */
package de.dima.textmining.types;

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
 * Updated by JCasGen Mon Dec 10 10:45:41 CET 2012
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
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.dima.textmining.types.Sentence");
 
  /** @generated */
  final Feature casFeat_CoNLLParse;
  /** @generated */
  final int     casFeatCode_CoNLLParse;
  /** @generated */ 
  public String getCoNLLParse(int addr) {
        if (featOkTst && casFeat_CoNLLParse == null)
      jcas.throwFeatMissing("CoNLLParse", "de.dima.textmining.types.Sentence");
    return ll_cas.ll_getStringValue(addr, casFeatCode_CoNLLParse);
  }
  /** @generated */    
  public void setCoNLLParse(int addr, String v) {
        if (featOkTst && casFeat_CoNLLParse == null)
      jcas.throwFeatMissing("CoNLLParse", "de.dima.textmining.types.Sentence");
    ll_cas.ll_setStringValue(addr, casFeatCode_CoNLLParse, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Sentence_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_CoNLLParse = jcas.getRequiredFeatureDE(casType, "CoNLLParse", "uima.cas.String", featOkTst);
    casFeatCode_CoNLLParse  = (null == casFeat_CoNLLParse) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_CoNLLParse).getCode();

  }
}



    