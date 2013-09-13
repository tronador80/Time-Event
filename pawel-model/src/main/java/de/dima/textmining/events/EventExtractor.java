package de.dima.textmining.events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.dima.textmining.conll.CoNLLNode;
import de.dima.textmining.conll.CoNLLNodeList;
import de.unihd.dbs.uima.types.heideltime.Timex3;
import de.unihd.dbs.uima.types.heideltime.Token;
import de.dima.textmining.types.Sentence;
import de.dima.textmining.types.Timespan;

/** class to generate events from parse trees and timex annotations */
public class EventExtractor {

	/** toggle saving of parse trees as .dot files and writing of debug output */
	private boolean debugMode = false;

	/** folder for saving parse trees as .dot files */
	private static final String DEFAULT_DOT_FOLDER = "/Users/Eumel/Documents/uni/tu_berlin/ws11/textmining/time_project/output/trees/dot_trees/";

	private final String dot_folder;
	// TODO exclude more relations
	/** deep parse relations for object generation */
	private static final Set<String> OBJ_RELS = new HashSet<String>(
			Arrays.asList("OBJ", "ADV", "EXT", "LGS", "PRD", "DIR"));

	// Arrays.asList("OBJ", "OPRD", "LOC", "ADV", "EXT", "MNR", "PRP",
	// "LGS", "PRD"));

	/**
	 * Constructor for Eventgeneration with parameter to toggle saving of parse
	 * output as dot graphs.
	 * 
	 * Saving in default folder.
	 * 
	 * @param saveDotTrees
	 *            toogle saving parse trees in dot format
	 */
	public EventExtractor(boolean saveDotTrees) {
		this(saveDotTrees, DEFAULT_DOT_FOLDER);
	}

	/**
	 * Constructor for Eventgeneration with parameter to toggle saving of parse
	 * output as dot graphs.
	 * 
	 * @param saveDotTrees
	 *            toogle saving parse trees in dot format
	 * @param dot_folder
	 *            folder to save dot files
	 */
	public EventExtractor(boolean saveDotTrees, String dot_folder) {
		this.debugMode = saveDotTrees;
		this.dot_folder = dot_folder;
	}

	private Map<String, List<Integer>> mapAnnotTypeToTokens(int typeInt,
			JCas jcas, Sentence sent) {
		// map for timex and
		Map<String, List<Integer>> tokenMap = new HashMap<String, List<Integer>>();

		AnnotationIndex<Annotation> timeIndex = jcas
				.getAnnotationIndex(typeInt);
		AnnotationIndex<Annotation> tokenIndex = jcas
				.getAnnotationIndex(Token.type);
		FSIterator timeIter = timeIndex.subiterator(sent);

		FSIterator tokenIter = tokenIndex.subiterator(sent);
		CoNLLNode root = null;
		String conll = sent.getCoNLLParse();
		if (conll != null) {
			try {
				root = CoNLLNode.parseCoNLL(conll);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int tokenNr = 0;
		// iterate over all timex annotation
		while (timeIter.hasNext()) {
			// list of positions of the tokens of this timex
			List<Integer> posList = new ArrayList<Integer>();

			Annotation time = (Annotation) timeIter.next();
			int timeBegin = time.getBegin();
			int timeEnd = time.getEnd();

			// iterate over all tokens of a timex annotation
			while (tokenIter.hasNext()) {
				tokenNr++;
				Token token = (Token) tokenIter.next();
				// check if timex has ended
				if (token.getBegin() >= timeEnd) {
					break;
				}
				// check for overlap of current token with timex
				if (token.getBegin() >= timeBegin) {
					if (root != null) {
						// add position entry
						posList.add(tokenNr);
					} else {
					}
					// root.getCompleteSentence().get(tokenNr).getForm();
				}

			}

			// add positions for this timex if they were found,
			// this is not the case if multiple timex are in one token (e.g.
			// 1926-27)
			// then only for the first timex a token pos is added
			if (posList.size() != 0) {
				tokenMap.put(time.getCoveredText(), posList);

			}
		}
		return tokenMap;
	}

	/**
	 * Maps found timex annotations to token ids
	 * 
	 * @param jcas
	 * @param sent
	 * @return Map of timex text and corresponding token ids
	 */
	public Map<String, List<Integer>> mapTimexToTokens(JCas jcas, Sentence sent) {

		return mapAnnotTypeToTokens(Timex3.type, jcas, sent);
	}

	/**
	 * Maps found timespan annotations to token ids
	 * 
	 * @param jcas
	 * @param sent
	 * @return Map of timex text and corresponding token ids
	 */
	public Map<String, List<Integer>> mapTimespanToTokens(JCas jcas,
			Sentence sent) {

		return mapAnnotTypeToTokens(Timespan.type, jcas, sent);
	}

	/**
	 * Find Events based on dependency parse and positions of found timex
	 * expression.
	 * 
	 * Uses only first token position of timex annotations.
	 * 
	 * @param root
	 *            dependency parse tree of the sentence
	 * @param timeTokenPostions
	 *            positions found time tokens (Timex and Timespan) in the tree
	 * @return the event as a string made of subject verb and object
	 */
	public String makeEvent(CoNLLNode root, List<Integer> timeTokenPostions) {

		String verb = "";
		String subj = "";
		String obj = "";

		Map<Integer, CoNLLNode> nodes = root.getCompleteSentence();
		// get node of first timex token by index
		CoNLLNode timeNode = nodes.get(timeTokenPostions.get(0));
		// search for parent node thats a verb, look if it has a TMP relation
		CoNLLNode currentNode = timeNode;

		// is parent of timeNode is available start event generation
		if (currentNode != null) {
			boolean tmpRelFound = false;
			// go up until a TMP relation was found or the root is reached
			while (!(tmpRelFound || currentNode.getDeprel().equals("ROOT"))) {
				if (currentNode.getDeprel().equals("TMP")) {
					tmpRelFound = true;
				}

				// go up one step
				currentNode = currentNode.getParent();
				if (currentNode == null) {
					System.out.println("Parser Error no ROOT node");
					return null;
				}
			}
			boolean objWasOnlyRelClause = false;

			/** check if verb (or modal) was found if continue to build event */
			if (tmpRelFound
					&& (currentNode.getPostag().startsWith("VB") || currentNode
							.getPostag().equals("MD"))) {

				/** go up the verb chain befor start building sub and obj */
				while (currentNode.getDeprel().equals("VC")) {
					currentNode = currentNode.getParent();
				}

				/**
				 * build verb string from top verb node by looking for VC
				 * children and make list of children for later subj and obj
				 * construction
				 */
				CoNLLNode verbNode = currentNode;

				List<CoNLLNode> verbChilds = new ArrayList<CoNLLNode>();
				boolean verbFound = true;
				while (verbFound) {
					// add part of the verb
					verb = verb + " " + verbNode.getForm();
					verbFound = false;
					for (CoNLLNode verbChild : verbNode.getChildren()) {
						String childDeprel = verbChild.getDeprel();
						// go down one step in verb chain
						if (childDeprel.equals("VC")) {
							verbFound = true;
							// set new verb node thats further down in verb
							// chain
							verbNode = verbChild;
						} else {
							// add non verbs
							verbChilds.add(verbChild);
						}
					}
				}

				// to save root nodes of obj elements
				CoNLLNodeList objNodes = new CoNLLNodeList();

				// TODO remove
				boolean printEvent = false;

				for (CoNLLNode node : verbChilds) {
					if (node.getDeprel().equals("SBJ")) {
						// subj = node.getDescendants().getCanonicalString(
						// false);

						// get subj string with out relative clauses
						subj = getSimpleString(node);

						// check if relation is in allowed set of object
						// relations
					} else if (this.OBJ_RELS.contains(node.getDeprel())) {
						// if (node.getDeprel().equals("DIR")){
						// printEvent = true;
						// }
						objNodes.add(node);

						// add particle to verb
					} else if (node.getDeprel().equals("PRT")) {
						verb += " " + node.getForm();

					}
				}

				// construct obj string
				boolean nonAdvObjNodeFound = false;
				for (CoNLLNode node : objNodes) {
					if (!node.getDeprel().equals("ADV")) {
						nonAdvObjNodeFound = true;
					}
				}

				/**
				 * Construct obj String out of object Nodes
				 */
				boolean useNode = false;
				for (CoNLLNode node : objNodes) {
					useNode = false;
					// ignore ADV if OBJ found
					if (node.getDeprel().equals("ADV")) {

						// use ADV nodes only if no other Obj node found
						if (!(nonAdvObjNodeFound)) {
							useNode = true;
						} else {
							// or if ADV is small and obj is not to big
							if (obj.length() < 35
									&& node.getDescendants().size() < 6) {
								useNode = true;
								// printEvent = true;
							}
						}
					} else if (node.getDeprel().equals("LOC")) {
						// use LOC nodes if obj is short and loc has max 5 nodes
						if (obj.length() < 10
								&& node.getDescendants().size() < 6) {
							useNode = true;
						}
					} else {

						// use all non ADV non LOC nodes
						useNode = true;
					}
					// add string part of good nodes
					if (useNode) {
						// add to obj string without relative clauses
						// node.getDescendants().getCanonicalString(false));
						String objPart = getSimpleString(node);
						if (objPart.length() != 0) {
							obj += " " + objPart;

						}
					}
				}

				// check if Obj nodes where found but their complete content is
				// ignored because it is only a relative clause
				if (objNodes.size() != 0 && obj.trim().length() == 0) {
					objWasOnlyRelClause = true;
				}
			}

			subj = subj.replaceAll(", ,", "").trim();
			verb = verb.trim();
			obj = obj.replaceAll(", ,", "").trim();

			// // save parse trees as dot for debugging
			// if (saveDotTrees) {
			// root.storeCompleteSentenceAsDot(DEFAULT_DOT_FOLDER
			// + root.getDescendants().getCanonicalString(false)
			// .substring(0, 10) + "_" + timeTokenPostions.get(0)
			// + "_" + timeNode.getForm() + "_S;" + subj + "_V;" + verb
			// + "_O;" + obj + ".dot");
			// }

			boolean validEvent = true;

			if (objWasOnlyRelClause) {
				validEvent = false;
			}

			// check subj and verb are not empty and are not single non
			// alphabetic
			// chars, produced by parser errors
			if (subj.length() == 0 || verb.length() == 0) {
				validEvent = false;
			}
			if (subj.length() == 1 && !Character.isLetter(subj.charAt(0))) {
				validEvent = false;
			}
			if (verb.length() == 1 && !Character.isLetter(verb.charAt(0))) {
				validEvent = false;
			}

			// TODO check if this is always good
			// don't use if verb is said
			// if (verb.equals("said")) {
			// validEvent = false;
			// }

			if (validEvent) {
				return (subj + " " + verb + " " + obj).trim();

			} else {
				return null;
			}
		} else {
			System.err.println("NODE NOT AVAILABLE");
		}
		return null;

	}

	/**
	 * Recursively look for a node the represents a relative clause by having a
	 * subject.
	 * 
	 * @param tree
	 * @return node with a subject that is top node of the relative clause
	 */
	// private CoNLLNode getBadNode(CoNLLNode tree) {
	protected CoNLLNode getRelClauseTopNode(CoNLLNode tree) {
		CoNLLNode result = null;
		if (!(tree.getChildren().size() == 0)) {

			// for all child get their childs and check dep rel to child of tree
			for (CoNLLNode child : tree.getChildren()) {
				// find nodes that have sbj as children
				if (child.getDeprel().equals("SBJ")) {
					return tree;
				} else {
					result = getRelClauseTopNode(child);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Recursively look for a node are top nodes of unwanted subtrees e.g. APPO
	 * or TMP inside of OBJs or ADVs
	 * 
	 * @param tree
	 * @return node with a subject that is top node of the relative clause
	 */
	// private CoNLLNode getBadNode(CoNLLNode tree) {
	protected CoNLLNode getUnwantedNode(CoNLLNode tree) {
		CoNLLNode result = null;
		if (!(tree.getChildren().size() == 0)) {

			// for all child get their childs and check dep rel to child of tree
			for (CoNLLNode child : tree.getChildren()) {

				String form = child.getForm();
				// find nodes that have sbj as children
				if (child.getDeprel().equals("APPO")
						|| child.getDeprel().equals("TMP")) {
					return child;
				} else {
					result = getUnwantedNode(child);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get String for a parse tree without relative clauses
	 * 
	 * @param tree
	 * @return string representation without relative clauses
	 */
	protected String getSimpleString(CoNLLNode tree) {
		// get all descending nodes
		CoNLLNodeList goodNodes = tree.getDescendants();
		CoNLLNodeList badNodes = null;
		// find rel clause top nodes
		CoNLLNode relClauseTop = getRelClauseTopNode(tree);
		if (relClauseTop != null) {
			// check if parent of relClauseTop is that
			if (relClauseTop.getParent().getForm().equals("that")) {
				// go one step up to remove also the 'that' node
				relClauseTop = relClauseTop.getParent();
			}

			// make set of unwanted nodes = top node + all descending nodes
			badNodes = relClauseTop.getDescendants();
			goodNodes.removeAll(badNodes);
		}

		// TODO if more than one than one node?s
		// remove nodes with unwanted relations from sub tree
		CoNLLNode badNode = getUnwantedNode(tree);

		if (badNode != null) {
			badNodes = badNode.getDescendants();
			goodNodes.removeAll(badNodes);
		}

		return goodNodes.getCanonicalString(false);
	}

}
