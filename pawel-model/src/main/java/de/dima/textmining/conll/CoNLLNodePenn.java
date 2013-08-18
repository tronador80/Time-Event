package de.dima.textmining.conll;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class CoNLLNodePenn {

	public static CoNLLNode parsePennCoNLL(BufferedReader br)
			throws IOException {

		List<String> conllLines = new ArrayList<String>();
		String line = null;
		// collect lines from reader
		while ((line = br.readLine()) != null) {
			if (line.split("\t").length != 3) {
				// stop if wrong format (empty line, ...)
				break;
			}
			conllLines.add(line);
		}
		return parsePennCoNLL(conllLines);
	}

	public static CoNLLNode parsePennCoNLL(List<String> conllLines)
			throws IOException {

		SortedMap<Integer, CoNLLNode> nodes = new TreeMap<Integer, CoNLLNode>();

		// first loop: create node objects without references
		int id = 1;
		for (String line : conllLines) {
			String[] fields = line.split("\t");

			CoNLLNode node = new CoNLLNode(id, fields[0], "-", "-", fields[1],
					"-", Integer.parseInt(fields[2]), "", null, null);
			nodes.put(node.getId(), node);
			id++;
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

}
