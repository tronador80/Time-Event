package de.dima.textmining.conll;

import java.util.Comparator;
import java.util.List;

/**
 * The CoNLLNodeListComparator is used to compare lists of CoNLLNodes.
 * 
 * Critical for the comparision of CoNLLNodeLists are the first node elements in
 * each list. If one of the passed lists is empty, the comparator will return 0.
 * This comparator is especially useful when it's necessary to sort a
 * List<List<CoNLLNode>> using Lists.sort().
 * 
 * @author Stefan Schramm
 * 
 */
public class CoNLLNodeListComparator implements Comparator<List<CoNLLNode>> {

	public int compare(List<CoNLLNode> o1, List<CoNLLNode> o2) {
		if (o1 == null || o2 == null || o1.size() == 0 || o2.size() == 0) {
			return 0;
		}
		// compare first node
		return o1.get(0).compareTo(o2.get(0));
	}

}
