

/* First created by JCasGen Mon Dec 10 10:45:35 CET 2012 */
package de.dima.textmining.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** Chunk as annotated in PennTreebank.
 * Updated by JCasGen Mon Dec 10 10:45:41 CET 2012
 * XML source: /home/robert/Projekte/timeline/time/src/dima-uima/src/main/resources/desc/typesystem/compoundTypeSystem.xml
 * @generated */
public class ChunkPenn extends Chunk {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(ChunkPenn.class);
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
  protected ChunkPenn() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public ChunkPenn(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public ChunkPenn(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public ChunkPenn(JCas jcas, int begin, int end) {
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
     
}

    