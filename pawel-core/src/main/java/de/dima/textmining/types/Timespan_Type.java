
/* First created by JCasGen Mon Dec 10 10:45:18 CET 2012 */
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
public class Timespan_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Timespan_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Timespan_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Timespan(addr, Timespan_Type.this);
  			   Timespan_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Timespan(addr, Timespan_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Timespan.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.dima.textmining.types.Timespan");
 
  /** @generated */
  final Feature casFeat_startTime;
  /** @generated */
  final int     casFeatCode_startTime;
  /** @generated */ 
  public int getStartTime(int addr) {
        if (featOkTst && casFeat_startTime == null)
      jcas.throwFeatMissing("startTime", "de.dima.textmining.types.Timespan");
    return ll_cas.ll_getRefValue(addr, casFeatCode_startTime);
  }
  /** @generated */    
  public void setStartTime(int addr, int v) {
        if (featOkTst && casFeat_startTime == null)
      jcas.throwFeatMissing("startTime", "de.dima.textmining.types.Timespan");
    ll_cas.ll_setRefValue(addr, casFeatCode_startTime, v);}
    
  
 
  /** @generated */
  final Feature casFeat_EndTime;
  /** @generated */
  final int     casFeatCode_EndTime;
  /** @generated */ 
  public int getEndTime(int addr) {
        if (featOkTst && casFeat_EndTime == null)
      jcas.throwFeatMissing("EndTime", "de.dima.textmining.types.Timespan");
    return ll_cas.ll_getRefValue(addr, casFeatCode_EndTime);
  }
  /** @generated */    
  public void setEndTime(int addr, int v) {
        if (featOkTst && casFeat_EndTime == null)
      jcas.throwFeatMissing("EndTime", "de.dima.textmining.types.Timespan");
    ll_cas.ll_setRefValue(addr, casFeatCode_EndTime, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Timespan_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_startTime = jcas.getRequiredFeatureDE(casType, "startTime", "de.unihd.dbs.uima.types.heideltime.Timex3", featOkTst);
    casFeatCode_startTime  = (null == casFeat_startTime) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_startTime).getCode();

 
    casFeat_EndTime = jcas.getRequiredFeatureDE(casType, "EndTime", "de.unihd.dbs.uima.types.heideltime.Timex3", featOkTst);
    casFeatCode_EndTime  = (null == casFeat_EndTime) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_EndTime).getCode();

  }
}



    