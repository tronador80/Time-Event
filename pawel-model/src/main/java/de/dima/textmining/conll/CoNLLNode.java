package de.dima.textmining.conll;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import de.dima.textmining.shallow.ShallowToken;

/**
 * Tree-like representation of CoNLL data as described in
 * http://nextens.uvt.nl/depparse-wiki/DataFormat
 * 
 * CoNLLNodes are compared using their id (== teir position in the sentence).
 * 
 * @author Stefan Schramm
 */
public class CoNLLNode implements Comparable<CoNLLNode>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String form;
	private String lemma;
	private String cpostag;
	private String postag;
	private String feats;
	private Integer head;
	private String deprel;
	private Integer phead;
	private String pdeprel;

	private SortedMap<Integer, CoNLLNode> completeSentence = new TreeMap<Integer, CoNLLNode>();
	private CoNLLNode parent = null;
	private List<CoNLLNode> children = new ArrayList<CoNLLNode>();

	public CoNLLNode(String[] fields) throws IOException {

		if (fields.length == 10) {
			setMembers(Integer.parseInt(fields[0]), fields[1], fields[2],
					fields[3], fields[4], fields[5],
					Integer.parseInt(fields[6]), fields[7],
					Integer.parseInt(fields[8]), fields[9]);
		} else if (fields.length == 8) {
			// last 2 fields missing (as in MaltParser output)
			setMembers(Integer.parseInt(fields[0]), fields[1], fields[2],
					fields[3], fields[4], fields[5],
					Integer.parseInt(fields[6]), fields[7], null, null);
		} else {
			throw new IOException("CoNLLNode: Wrong number of fields.");
		}
	}

	public CoNLLNode(Integer id, String form, String lemma, String cpostag,
			String postag, String feats, Integer head, String deprel,
			Integer phead, String pdeprel) {
		setMembers(id, form, lemma, cpostag, postag, feats, head, deprel,
				phead, pdeprel);
	}

	/**
	 * Copy constructor. Copies everything but parents, children and complete
	 * sentence.
	 * 
	 * @param aConllNode
	 *            the conll node to copy the values from
	 */
	public CoNLLNode(CoNLLNode aConllNode) {
		this(aConllNode.getId(), aConllNode.getForm(), aConllNode.getLemma(),
				aConllNode.getCpostag(), aConllNode.getPostag(), aConllNode
						.getFeats(), aConllNode.getHead(), aConllNode
						.getDeprel(), aConllNode.getPhead(), aConllNode
						.getPdeprel());
	}

	private void setMembers(Integer id, String form, String lemma,
			String cpostag, String postag, String feats, Integer head,
			String deprel, Integer phead, String pdeprel) {
		setId(id);
		setForm(form);
		setLemma(lemma);
		setCpostag(cpostag);
		setPostag(postag);
		setFeats(feats);
		setHead(head);
		setDeprel(deprel);
		setPhead(phead);
		setPdeprel(pdeprel);
	}

	public void addChild(CoNLLNode child) {
		children.add(child);
	}

	/**
	 * Reads tab-seperated CoNLL data from specified BufferedReader and returns
	 * CoNLLNode of root node (usually main verb) of the first read sentence.
	 * Empty lines or wrongly formatted lines are treated as delimiters between
	 * sentences. On wrong input (eg. a node pointing to a parent id that
	 * doesn't exist) null pointer exceptions will occur!
	 * 
	 * @param br
	 * @return root node of tree
	 * @throws IOException
	 */
	public static CoNLLNode parseCoNLL(BufferedReader br) throws IOException {

		List<String> conllLines = new ArrayList<String>();
		String line = null;
		// collect lines from reader
		while ((line = br.readLine()) != null) {
			if (line.split("\t").length != 10) {
				// stop if wrong format (empty line, ...)
				break;
			}
			conllLines.add(line);
		}
		return parseCoNLL(conllLines);
	}

	public static CoNLLNode parseCoNLL(String conll) throws IOException {

		List<String> lines = Arrays.asList(conll.split("\n"));

		return parseCoNLL(lines);

	}

	/**
	 * Reads tab-seperated CoNLL data from passed lines and returns CoNLLNode of
	 * root node (usually main verb). On wrong input (eg. a node pointing to a
	 * parent id that doesn't exist) null pointer exceptions will occur!
	 * 
	 * @param conllLines
	 * @return root node of tree
	 * @throws IOException
	 */
	public static CoNLLNode parseCoNLL(List<String> conllLines)
			throws IOException {

		SortedMap<Integer, CoNLLNode> nodes = new TreeMap<Integer, CoNLLNode>();

		// first loop: create node objects without references
		for (String line : conllLines) {
			CoNLLNode node = new CoNLLNode(line.split("\t"));
			nodes.put(node.getId(), node);
		}

		// second loop: set references (parent, children, ...)
		CoNLLNode rootNode = null;
		for (CoNLLNode node : nodes.values()) {
			node.setCompleteSentence(nodes);
			if (node.getIdParent() == 0) {
				// root node has no parent - remember and skip it
				rootNode = node;
				continue;
			}
			// non-root nodes:
			node.setParent(nodes.get(node.getIdParent()));
			nodes.get(node.getIdParent()).addChild(node);
		}

		return rootNode;
	}

	/**
	 * Get CoNLL-line of current node.
	 * 
	 * @return CoNLL-line of current node
	 */
	public String getAsCoNLL() {
		return getId() + "\t" + getForm() + "\t" + getLemma() + "\t"
				+ getCpostag() + "\t" + getPostag() + "\t"
				+ (getFeats() == null ? "_" : getFeats()) + "\t" + getHead()
				+ "\t" + getDeprel() + "\t"
				+ (getPhead() == null ? "_" : getPhead()) + "\t"
				+ (getPdeprel() == null ? "_" : getPdeprel()) + "\n";
	}

	/**
	 * Get CoNLL-line of current node.
	 * 
	 * @return CoNLL-line of current node
	 * @throws Exception
	 */
	public String getAsCoNLL(List<Integer> idMap) throws Exception {
		String form = getForm();
		Integer newId = idMap.indexOf(getId()) + 1;
		if (newId == 0) {
			// no match in mapping for own id
			throw new Exception(
					"getAsCoNLL: idMap is missing some mapping for the current node or its heads.");
		}
		Integer newHead = idMap.indexOf(getHead()) + 1;
		Integer newPhead = idMap.indexOf(getPhead()) + 1;
		if ((newHead == 0 || newPhead == 0) && !getDeprel().equals("ROOT")) {
			// no root element, but root not found in mapping
			throw new Exception(
					"getAsCoNLL: idMap is missing the mapping for the head of the current node.");
		}

		if (getId() == 1 && newId != 1) {
			// token is moved away from the start
			if (!getPostag().equals("NN") && !getPostag().equals("NE")) {
				form = form.toLowerCase();
			}
		} else if (getId() != 1 && newId == 1) {
			// token is moved to the start
			form = form.substring(0, 1).toUpperCase() + form.substring(1);
		}
		return newId + "\t" + form + "\t" + getLemma() + "\t" + getCpostag()
				+ "\t" + getPostag() + "\t" + getFeats() + "\t" + newHead
				+ "\t" + getDeprel() + "\t" + newPhead + "\t" + getPdeprel()
				+ "\n";
	}

	public List<ShallowToken> getCompleteSentenceAsListOfShallowToken() {
		List<ShallowToken> tokens = new Vector<ShallowToken>();
		for (int i = 1; i < completeSentence.size(); i++) {
			CoNLLNode token = completeSentence.get(i);
			ShallowToken st = new ShallowToken(token.getForm(),
					token.getPostag(), token.getLemma());
			tokens.add(st);
		}
		return tokens;
	}

	/**
	 * Get CoNLL-string of the complete sentence this node belongs to.
	 * 
	 * @return CoNLL-string of the complete sentence this node belongs to.
	 */
	public String getCompleteSentenceAsCoNLL() {
		String conll = "";
		Integer i = 1;
		while (completeSentence.containsKey(i)) {
			conll += completeSentence.get(i).getAsCoNLL();
			i++;
		}
		return conll;
	}

	public String getCompleteSentenceAsCoNLL(List<Integer> idMap)
			throws Exception {
		if (!idMapCorrect(idMap)) {
			throw new Exception(
					"getCompleteSentenceAsCoNLL: idMap is incorrect: " + idMap
							+ " sentence: "
							+ this.getCompleteSentenceAsString());
		}
		String conll = "";
		Integer i = 1;
		for (Integer id : idMap) {
			String line = "";
			if (id == 999) {
				// add comma
				// TODO:
				// Problem: Es ist nicht trivial, was bei Kommas Head werden
				// muss. Hier wird erstmal nur getId() (die Wurzel des Satzes,
				// wenn die Methode dafür aufgerufen wurde) eingetragen, was
				// aber in den meisten Faellen falsch sein duerfte!
				//
				// Manchmal ist der Head eines Kommas genau das Token vor oder
				// hinter ihm.
				// line = i + "\t" + "," + "\t" + "_" + "\t" + "$," + "\t" +
				// "$,"
				// + "\t" + "_" + "\t" + getId() + "\t" + "PUNC" + "\t"
				// + getId() + "\t" + "PUNC" + "\n";

				// Head fuer Komma zunächst auf ROOT-Node setzen.
				Integer rootId = getRoot().getId();
				Integer newRootId = idMap.indexOf(rootId) + 1;
				if (newRootId == 0) {
					// no match in mapping
					throw new Exception(
							"getAsCoNLL: idMap is missing a mapping for the root node (maybe it has been removed?).");
				}
				line = i + "\t" + "," + "\t" + "_" + "\t" + "$," + "\t" + "$,"
						+ "\t" + "_" + "\t" + newRootId + "\t" + "PUNC" + "\t"
						+ newRootId + "\t" + "PUNC" + "\n";
				if (i == newRootId) {
					System.out.println("ERROR: id == head");
					System.exit(1);
				}
			} else {
				line = completeSentence.get(id).getAsCoNLL(idMap);
			}
			conll += line;
			i++;
		}
		return conll;
	}

	private boolean idMapCorrect(List<Integer> idMap) {
		List<Integer> sortedList = new ArrayList<Integer>(idMap);
		Collections.sort(sortedList);
		for (int i = 1; i < sortedList.size(); i++) {

			if (sortedList.get(i).equals(sortedList.get(i - 1))) {

				if (!sortedList.get(i).equals(999))
					// if a number occurs multiple times in the list
					return false;
			}
		}

		return true;
	}

	public CoNLLNode getPreviousInSentence() {
		if (completeSentence.containsKey(getId() - 1)) {
			return completeSentence.get(getId() - 1);
		}
		return null;
	}

	public CoNLLNode getNextInSentence() {
		if (completeSentence.containsKey(getId() + 1)) {
			return completeSentence.get(getId() + 1);
		}
		return null;
	}

	public CoNLLNode getRoot() {
		if (getHead() == 0) {
			// myself is the root
			return this;
		}
		// climb up further
		return completeSentence.get(getHead()).getRoot();
	}

	public String toString() {
		return toString(0);
	}

	public String getCompleteSentenceAsString() {
		String s = "";
		for (CoNLLNode node : completeSentence.values()) {
			s += node.getForm() + " ";
		}
		return s.trim();
	}

	/**
	 * Store tree structure of sentence in DOT-format.
	 * 
	 * The resulting file can be converted using graphviz's dot command: dot
	 * /tmp/test.dot -Tpng > /tmp/test.png
	 * 
	 * @param filename
	 *            Filename where to store dot-file.
	 */
	public void storeCompleteSentenceAsDot(String filename) {
		String dot = "digraph sentence {\n";
		for (int i = 1; i <= completeSentence.size(); i++) {
			CoNLLNode token = completeSentence.get(i);
			dot += "\t" + token.getId() + " [label=\""
					+ token.getForm().replace("\"", "\\\"") + "/"
					+ token.getPostag().replace("\"", "\\\"") + "\"];\n";
		}
		dot += getSubtreeAsDot();
		dot += "}\n";
		try {
			FileWriter fw = new FileWriter(filename);
			fw.write(dot);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getSubtreeAsDot() {
		String dot = "";
		for (CoNLLNode child : getChildren()) {
			dot += "\t" + getId() + " -> " + child.getId() + ";\n";
			dot += child.getSubtreeAsDot();
		}
		return dot;
	}

	private String toString(int level) {
		String s = "";
		// indentation
		for (int i = 0; i < level; i++) {
			s += "\t";
		}
		// node itself
		s += getForm() + " (" + getPostag() + "," + getDeprel() + ")\n";
		// children
		for (CoNLLNode node : children) {
			s += node.toString(level + 1);
		}
		return s;
	}

	// getter and setter

	public void setForm(String form) {
		this.form = form;
	}

	public String getForm() {
		return form;
	}

	public void setPostag(String postag) {
		this.postag = postag;
	}

	public String getPostag() {
		return postag;
	}

	public void setDeprel(String deprel) {
		this.deprel = deprel;
	}

	public String getDeprel() {
		return deprel;
	}

	public void setParent(CoNLLNode parent) {
		this.parent = parent;
	}

	public CoNLLNode getParent() {
		return parent;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setHead(Integer head) {
		this.head = head;
	}

	public Integer getHead() {
		return head;
	}

	public Integer getIdParent() {
		return head;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getLemma() {
		return lemma;
	}

	public void setCpostag(String cpostag) {
		this.cpostag = cpostag;
	}

	public String getCpostag() {
		return cpostag;
	}

	public void setFeats(String feats) {
		this.feats = feats;
	}

	public String getFeats() {
		return feats;
	}

	public void setPhead(Integer phead) {
		this.phead = phead;
	}

	public Integer getPhead() {
		return phead;
	}

	public void setPdeprel(String pdeprel) {
		this.pdeprel = pdeprel;
	}

	public String getPdeprel() {
		return pdeprel;
	}

	public void setCompleteSentence(SortedMap<Integer, CoNLLNode> nodes) {
		this.completeSentence = nodes;
	}

	public Map<Integer, CoNLLNode> getCompleteSentence() {
		return completeSentence;
	}

	public List<CoNLLNode> getChildren() {
		return children;
	}

	public void setChildren(List<CoNLLNode> children) {
		this.children = children;
	}

	public int compareTo(CoNLLNode other) {
		return getId().compareTo(other.getId());
	}

	/**
	 * Generates a copy of the given CoNLL Node and all child CoNLL Nodes. Can
	 * NOT copy field completeSentence because of the references in the map.
	 * 
	 * @param currentNode
	 *            the node to copy
	 * @param nodeCopy
	 *            the copy of the node without children and parent
	 * @return the copy with children and parent also copied
	 */
	public static CoNLLNode generateCopyWithDependencies(CoNLLNode currentNode,
			CoNLLNode nodeCopy) {
		// Go through all children and add a copy of them
		for (CoNLLNode childNode : currentNode.getChildren()) {
			CoNLLNode newChild = new CoNLLNode(childNode);
			newChild.setParent(nodeCopy);
			nodeCopy.addChild(newChild);

			// Also copy all children of the child
			generateCopyWithDependencies(childNode, newChild);
		}

		return nodeCopy;
	}

	public CoNLLNodeList getDescendants() {
		CoNLLNodeList descendants = getDescendants(this);
		Collections.sort(descendants);
		return descendants;
	}

	/**
	 * Gets the descendants.
	 * 
	 * @param subtree
	 *            the subtree
	 * @return the descendants
	 */
	private CoNLLNodeList getDescendants(CoNLLNode subtree) {
		CoNLLNodeList descendants = new CoNLLNodeList();
		descendants.add(subtree);
		for (CoNLLNode child : subtree.getChildren()) {
			descendants.addAll(getDescendants(child));
		}
		return descendants;
	}
}
