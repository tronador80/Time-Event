
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
public class Text_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Text_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Text_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Text(addr, Text_Type.this);
  			   Text_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Text(addr, Text_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Text.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("pawel.paweltypes.Text");
 
  /** @generated */
  final Feature casFeat_Text;
  /** @generated */
  final int     casFeatCode_Text;
  /** @generated */ 
  public String getText(int addr) {
        if (featOkTst && casFeat_Text == null)
      jcas.throwFeatMissing("Text", "pawel.paweltypes.Text");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Text);
  }
  /** @generated */    
  public void setText(int addr, String v) {
        if (featOkTst && casFeat_Text == null)
      jcas.throwFeatMissing("Text", "pawel.paweltypes.Text");
    ll_cas.ll_setStringValue(addr, casFeatCode_Text, v);}
    
  
 
  /** @generated */
  final Feature casFeat_date;
  /** @generated */
  final int     casFeatCode_date;
  /** @generated */ 
  public String getDate(int addr) {
        if (featOkTst && casFeat_date == null)
      jcas.throwFeatMissing("date", "pawel.paweltypes.Text");
    return ll_cas.ll_getStringValue(addr, casFeatCode_date);
  }
  /** @generated */    
  public void setDate(int addr, String v) {
        if (featOkTst && casFeat_date == null)
      jcas.throwFeatMissing("date", "pawel.paweltypes.Text");
    ll_cas.ll_setStringValue(addr, casFeatCode_date, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Text_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Text = jcas.getRequiredFeatureDE(casType, "Text", "uima.cas.String", featOkTst);
    casFeatCode_Text  = (null == casFeat_Text) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Text).getCode();

 
    casFeat_date = jcas.getRequiredFeatureDE(casType, "date", "uima.cas.String", featOkTst);
    casFeatCode_date  = (null == casFeat_date) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_date).getCode();

  }
}



    