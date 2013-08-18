package de.dima.textmining.conll;

import java.util.ArrayList;
import java.util.List;

import de.dima.textmining.shallow.ShallowToken;

public class CoNLLNodeList extends ArrayList<CoNLLNode> {

	/**
	 * ID
	 */
	private static final long serialVersionUID = 1L;

	public List<ShallowToken> getAsShallowTokens() {

		List<ShallowToken> tokens = new ArrayList<ShallowToken>();

		for (int i = 0; i < this.size(); i++) {

			CoNLLNode node = this.get(i);

			tokens.add(new ShallowToken(node.getForm(), node.getPostag(), node
					.getLemma()));
		}

		return tokens;

	}

	public List<Integer> nodeListAsIdList() {
		List<Integer> ids = new ArrayList<Integer>();
		for (CoNLLNode node : this) {
			ids.add(node.getId());
		}
		return ids;
	}

	public String getCanonicalPosString() {

		String s = "";

		for (int i = 0; i < this.size(); i++) {

			CoNLLNode node = this.get(i);

			/*
			 * uncapitalize non nouns
			 */
			s += node.getPostag() + " ";

		}

		return s.trim();
	}

	public List<Integer> getIdList() {

		List<Integer> idList = new ArrayList<Integer>();

		for (CoNLLNode node : this) {

			idList.add(node.getId());

		}

		return idList;

	}

	public String getInitialPreposition() {

		if ((this.get(0).getPostag().startsWith("I") || this.get(0).getPostag()
				.startsWith("T"))) {

			return this.get(0).getForm();

		} else
			return "";
	}

	public String getInitialDeterminer() {

		if (this.get(0).getPostag().startsWith("D")) {

			return this.get(0).getForm();

		} else if (this.size() > 1 && this.get(1).getPostag().startsWith("D")) {

			return this.get(1).getForm();
		} else
			return "";
	}

	public String getCanonicalString(boolean filterDeterminers) {
		String s = "";

		for (int i = 0; i < this.size(); i++) {

			CoNLLNode node = this.get(i);

			/*
			 * skip determiner if at first position
			 */
			if (filterDeterminers && i == 0 && node.getPostag().startsWith("D"))
				continue;

			/*
			 * skip determiner if at second position
			 */
			if (filterDeterminers && i == 1 && node.getPostag().startsWith("D"))
				continue;

			/*
			 * skip preposition if at first position
			 */
			if (filterDeterminers
					&& i == 0
					&& (node.getPostag().startsWith("I") || node.getPostag()
							.startsWith("T")))
				continue;

			/*
			 * uncapitalize non nouns
			 */
			if (i == 0 && !node.getPostag().startsWith("N")) {

				s += node.getForm().substring(0, 1).toLowerCase()
						+ node.getForm().substring(1) + " ";

			} else {
				s += node.getForm() + " ";
			}

		}

		return s.trim();
	}

	/**
	 * Gets the head of argument.
	 * 
	 * @param argument
	 *            the argument
	 * @return the head of argument
	 */
	public CoNLLNode getHeadOfArgument() {
		if (this == null || this.size() == 0) {
			return null;
		}
		return getHeadOfArgument(this, this.get(0));
	}

	/**
	 * Gets the head of argument.
	 * 
	 * @param argument
	 *            the argument
	 * @param node
	 *            the node
	 * @return the head of argument
	 */
	private CoNLLNode getHeadOfArgument(List<CoNLLNode> argument, CoNLLNode node) {
		if (node == null
				|| node.getParent() == null
				|| node.getParent().getId() < argument.get(0).getId()
				|| node.getParent().getId() > argument.get(argument.size() - 1)
						.getId()) {
			// stop recursion
			return node;
		}
		return getHeadOfArgument(argument, node.getParent());
	}

}
