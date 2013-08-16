
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
public class ShallowAnnotation_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (ShallowAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = ShallowAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new ShallowAnnotation(addr, ShallowAnnotation_Type.this);
  			   ShallowAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new ShallowAnnotation(addr, ShallowAnnotation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = ShallowAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.dima.textmining.types.ShallowAnnotation");
 
  /** @generated */
  final Feature casFeat_PosTag;
  /** @generated */
  final int     casFeatCode_PosTag;
  /** @generated */ 
  public String getPosTag(int addr) {
        if (featOkTst && casFeat_PosTag == null)
      jcas.throwFeatMissing("PosTag", "de.dima.textmining.types.ShallowAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_PosTag);
  }
  /** @generated */    
  public void setPosTag(int addr, String v) {
        if (featOkTst && casFeat_PosTag == null)
      jcas.throwFeatMissing("PosTag", "de.dima.textmining.types.ShallowAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_PosTag, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Lemma;
  /** @generated */
  final int     casFeatCode_Lemma;
  /** @generated */ 
  public String getLemma(int addr) {
        if (featOkTst && casFeat_Lemma == null)
      jcas.throwFeatMissing("Lemma", "de.dima.textmining.types.ShallowAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Lemma);
  }
  /** @generated */    
  public void setLemma(int addr, String v) {
        if (featOkTst && casFeat_Lemma == null)
      jcas.throwFeatMissing("Lemma", "de.dima.textmining.types.ShallowAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_Lemma, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public ShallowAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_PosTag = jcas.getRequiredFeatureDE(casType, "PosTag", "uima.cas.String", featOkTst);
    casFeatCode_PosTag  = (null == casFeat_PosTag) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_PosTag).getCode();

 
    casFeat_Lemma = jcas.getRequiredFeatureDE(casType, "Lemma", "uima.cas.String", featOkTst);
    casFeatCode_Lemma  = (null == casFeat_Lemma) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Lemma).getCode();

  }
}



    