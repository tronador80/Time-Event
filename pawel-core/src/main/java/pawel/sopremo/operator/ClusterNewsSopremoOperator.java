/**
 * 
 */
package pawel.sopremo.operator;

import pawel.algorithms.CosineSimilarity;
import eu.stratosphere.pact.common.contract.CrossContract;
import eu.stratosphere.pact.common.contract.MapContract;
import eu.stratosphere.pact.common.contract.ReduceContract;
import eu.stratosphere.pact.common.plan.PactModule;
import eu.stratosphere.sopremo.EvaluationContext;
import eu.stratosphere.sopremo.expressions.ObjectAccess;
import eu.stratosphere.sopremo.operator.ElementaryOperator;
import eu.stratosphere.sopremo.operator.InputCardinality;
import eu.stratosphere.sopremo.operator.Name;
import eu.stratosphere.sopremo.operator.OutputCardinality;
import eu.stratosphere.sopremo.pact.JsonCollector;
import eu.stratosphere.sopremo.pact.SopremoCross;
import eu.stratosphere.sopremo.pact.SopremoMap;
import eu.stratosphere.sopremo.pact.SopremoReduce;
import eu.stratosphere.sopremo.pact.SopremoUtil;
import eu.stratosphere.sopremo.type.ArrayNode;
import eu.stratosphere.sopremo.type.IJsonNode;
import eu.stratosphere.sopremo.type.IStreamNode;
import eu.stratosphere.sopremo.type.MissingNode;
import eu.stratosphere.sopremo.type.ObjectNode;
import eu.stratosphere.sopremo.type.TextNode;

/**
 * This class implements hierarchical agglomerative clustering algorithm. As
 * similarity measure cosine similarity is used. Algorithm uses the shortest
 * linkage variant.
 * 
 * @author pawel
 * 
 */
@InputCardinality(1)
@OutputCardinality(1)
@Name(verb = "build_clusters")
public class ClusterNewsSopremoOperator extends
		ElementaryOperator<ClusterNewsSopremoOperator> {

	/**
	 * Parameter that indicates what is the minimal similarity that must occur
	 * between two clusters if they should be merged. If there are now two
	 * clusters thats similarity is above this parameter then clustering is
	 * done.
	 */
	private static final double MINIMAL_SIMILARITY_TO_BUILD_CLUSTER = 0.1d;

	/**
	 * constructor
	 */
	public ClusterNewsSopremoOperator() {
		this.setKeyExpressions(0, new ObjectAccess("doc")); // define key object
	}

	/**
	 * Calculate similarity between every cluster.
	 * 
	 * @author pawel
	 * 
	 */
	public static class FirstStepCross extends SopremoCross {

		@Override
		protected void cross(IJsonNode in1, IJsonNode in2, JsonCollector out) {
			ObjectNode res = new ObjectNode();

			res.put("doc1", in1);
			res.put("doc2", in2);
			res.put("similarity", ClusterNewsSopremoOperator
					.calculateSimilarityOfClusters(in1, in2));

			out.collect(res);

		}
	}

	/**
	 * 
	 * @author pawel
	 * 
	 */
	public static class SecondStepMapper extends SopremoMap {

		@Override
		protected void map(IJsonNode value, JsonCollector out) {
			if (value instanceof ObjectNode) {
				ObjectNode similarityPair = (ObjectNode) value;

				ObjectNode res1 = new ObjectNode();
				ObjectNode res2 = new ObjectNode();

				res1.put("doc", similarityPair.get("doc1"));
				res1.put("other_doc", similarityPair.get("doc2"));
				res1.put("similarity", similarityPair.get("similarity"));

				res2.put("doc", similarityPair.get("doc2"));
				res1.put("other_doc", similarityPair.get("doc1"));
				res1.put("similarity", similarityPair.get("similarity"));

				out.collect(res1);
				out.collect(res2);
			}
		}

	}

	/**
	 * 
	 * @author pawel
	 * 
	 */
	public static class ThirdStepReducer extends SopremoReduce {

		@Override
		protected void reduce(IStreamNode<IJsonNode> values, JsonCollector out) {
			Double highestSimilarity = 0.0;
			ObjectNode nodeWithHighestSimilarity = new ObjectNode();
			for (IJsonNode node : values) {
				if (node instanceof ObjectNode) {
					ObjectNode similarityNode = (ObjectNode) node;
					Double similarity = Double
							.parseDouble(((TextNode) similarityNode
									.get("similarity")).getTextValue()
									.toString());
					if (similarity > highestSimilarity
							|| nodeWithHighestSimilarity == null) {
						nodeWithHighestSimilarity.put("doc1",
								similarityNode.get("doc"));
						nodeWithHighestSimilarity.put("doc2",
								similarityNode.get("other_doc"));
						nodeWithHighestSimilarity.put("similarity",
								similarityNode.get("similarity"));
						highestSimilarity = similarity;
					}
				}
			}

			nodeWithHighestSimilarity.put("doc", new TextNode("1"));
			out.collect(nodeWithHighestSimilarity);

		}
	}

	/**
	 * 
	 * @author pawel
	 * 
	 */
	public static class FourthStepReducer extends SopremoReduce {

		@Override
		protected void reduce(IStreamNode<IJsonNode> values, JsonCollector out) {
			Double highestSimilarity = 0.0;
			ObjectNode nodeWithHighestSimilarity = null;
			ObjectNode tmpNode = null;
			for (IJsonNode node : values) {
				if (node instanceof ObjectNode) {
					ObjectNode similarityNode = (ObjectNode) node;
					Double similarity = Double
							.parseDouble(((TextNode) similarityNode
									.get("similarity")).getTextValue()
									.toString());
					if ((similarity > highestSimilarity || nodeWithHighestSimilarity == null)
							&& similarity > ClusterNewsSopremoOperator.MINIMAL_SIMILARITY_TO_BUILD_CLUSTER) {
						nodeWithHighestSimilarity = similarityNode;
						highestSimilarity = similarity;
						if (tmpNode != null
								&& !nodeWithHighestSimilarity.get("doc2")
										.equals(tmpNode.get("doc1"))) {
							out.collect(tmpNode.get("doc1"));
							tmpNode = null;
						}
					} /*
					 * TODO verify this condition, it should check whether the
					 * JSON structure is equal
					 */
					else if (nodeWithHighestSimilarity.get("doc2").equals(
							similarityNode.get("doc1"))) {
						tmpNode = similarityNode;
					} else {
						out.collect(similarityNode.get("doc1"));
					}
				}
			}

			if (nodeWithHighestSimilarity != null) {
				ObjectNode res = new ObjectNode();
				ArrayNode<IJsonNode> clusteredTexts = new ArrayNode<IJsonNode>();

				ObjectNode firstCluster = (ObjectNode) nodeWithHighestSimilarity
						.get("doc1");
				ObjectNode secondCluster = (ObjectNode) nodeWithHighestSimilarity
						.get("doc2");

				if (firstCluster.get("texts") instanceof MissingNode) {
					clusteredTexts.add((TextNode) firstCluster.get("text"));
				} else {
					if (firstCluster.get("texts") instanceof ArrayNode<?>)
						clusteredTexts.addAll((ArrayNode<?>) firstCluster
								.get("texts"));
				}

				if (secondCluster.get("texts") instanceof MissingNode) {
					clusteredTexts.add((TextNode) secondCluster.get("text"));
				} else {
					if (secondCluster.get("texts") instanceof ArrayNode<?>)
						clusteredTexts.addAll((ArrayNode<?>) secondCluster
								.get("texts"));
				}

				res.put("texts", clusteredTexts);
				out.collect(res);
			}

		}
	}

	/**
	 * 
	 * @author pawel
	 * 
	 */
	public static class BreakConditionMapper extends SopremoMap {

		@Override
		protected void map(IJsonNode value, JsonCollector out) {
			ObjectNode res = new ObjectNode();
			res.put("doc", new TextNode("1"));
			out.collect(res);
		}
	}

	/**
	 * 
	 * @author pawel
	 * 
	 */
	public static class BreakConditionReducer extends SopremoReduce {

		@Override
		protected void reduce(IStreamNode<IJsonNode> values, JsonCollector out) {
			ObjectNode res = new ObjectNode();
			Integer docsCounter = 0;
			for (IJsonNode doc : values) {
				if (doc instanceof ObjectNode
						&& !(((ObjectNode) doc).get("doc") instanceof MissingNode)) {
					docsCounter++;
				}
			}
			res.put("count", new TextNode(docsCounter.toString()));
			out.collect(res);
		}
	}

	@Override
	public PactModule asPactModule(EvaluationContext context) {
		context.setInputsAndOutputs(this.getNumInputs(), this.getNumOutputs());
		PactModule module = new PactModule(1, 1);

		// 1st step: cross
		CrossContract.Builder crossBuilder = CrossContract
				.builder(FirstStepCross.class);
		crossBuilder.name("Cross, similarity calculation");
		crossBuilder.input1(module.getInput(0));
		crossBuilder.input2(module.getInput(0));
		CrossContract crossContract = crossBuilder.build();

		// 2nd step: map
		MapContract.Builder mapBuilder = MapContract
				.builder(SecondStepMapper.class);
		mapBuilder.name("Mapper");
		mapBuilder.input(crossContract);
		MapContract mapContract = mapBuilder.build();

		// 3rd step: reduce
		ReduceContract.Builder reduceBuilder1 = ReduceContract
				.builder(ThirdStepReducer.class);
		reduceBuilder1.name("1st Reducer");
		reduceBuilder1.input(mapContract);
		ReduceContract reduceContract1 = reduceBuilder1.build();

		// 4th step: reduce
		ReduceContract.Builder reduceBuilder2 = ReduceContract
				.builder(ThirdStepReducer.class);
		reduceBuilder1.name("2nd Reducer");
		reduceBuilder1.input(reduceContract1);
		ReduceContract reduceContract2 = reduceBuilder2.build();

		// 5nd step: map (break condition 1st step)
		MapContract.Builder mapBuilderBreakCondition = MapContract
				.builder(BreakConditionMapper.class);
		mapBuilderBreakCondition.name("Break Condition Mapper");
		mapBuilderBreakCondition.input(reduceContract2);
		MapContract mapContractBreakCondition = mapBuilderBreakCondition
				.build();

		// 6th step: reduce (break condition 2nd step)
		ReduceContract.Builder reduceBuilderBreakCondition = ReduceContract
				.builder(ThirdStepReducer.class);
		reduceBuilderBreakCondition.name("Break Condition Reducer");
		reduceBuilderBreakCondition.input(mapContractBreakCondition);
		ReduceContract reduceContractBreakCondition = reduceBuilderBreakCondition
				.build();

		SopremoUtil.setObject(reduceContractBreakCondition.getParameters(),
				SopremoUtil.CONTEXT, context);

		module.getOutput(0).setInput(reduceContractBreakCondition);
		return module;
	}

	/**
	 * Method calculates similarity of two clusters.
	 * 
	 * @param cluster1
	 *            1st cluster to calculate similarity (should be
	 *            {@link ObjectNode} containing {@link ArrayNode} under name
	 *            'texts' with clustered texts or {@link TextNode} under name
	 *            'text' with single text if not cluster but single document)
	 * @param cluster2
	 *            2nd cluster to calculate similarity (analog to 1st cluster)
	 * @return similarity of cluster1 and cluster2 (using shortest linkage =
	 *         similarity of clusters is equal to similarity of two most similar
	 *         documents from cluster one and two)
	 */
	public static TextNode calculateSimilarityOfClusters(IJsonNode cluster1,
			IJsonNode cluster2) {

		TextNode res = new TextNode();
		Double shortestLinkage = 0.0; // 0.0 not similar, 1.0 identical

		ArrayNode<?> textsFromFirstCluster = null;
		TextNode text1 = null;
		if (cluster1 instanceof ObjectNode) {
			ObjectNode clusterNode = (ObjectNode) cluster1;
			if (!(clusterNode.get("texts") instanceof MissingNode)
					&& clusterNode.get("texts") instanceof ArrayNode<?>) {
				textsFromFirstCluster = (ArrayNode<?>) clusterNode.get("texts");
			} else if (!(clusterNode.get("text") instanceof MissingNode)
					&& clusterNode.get("text") instanceof TextNode) {
				text1 = (TextNode) clusterNode.get("text");
			} else {
				res.append("0.0");
				return res;
			}
		}

		ArrayNode<?> textsFromSecondCluster = null;
		TextNode text2 = null;
		if (cluster2 instanceof ObjectNode) {
			ObjectNode clusterNode = (ObjectNode) cluster2;
			if (!(clusterNode.get("texts") instanceof MissingNode)
					&& clusterNode.get("texts") instanceof ArrayNode<?>) {
				textsFromSecondCluster = (ArrayNode<?>) clusterNode
						.get("texts");
			} else if (!(clusterNode.get("text") instanceof MissingNode)
					&& clusterNode.get("text") instanceof TextNode) {
				text2 = (TextNode) clusterNode.get("text");
			} else {
				res.append("0.0");
				return res;
			}
		}

		if (textsFromFirstCluster != null) {
			String textFromSecondCluster = null;
			boolean secondClusterContainsMoreThenOneText = true;
			if (textsFromSecondCluster == null) {
				textFromSecondCluster = text2.getTextValue().toString();
				secondClusterContainsMoreThenOneText = false;
			}
			for (int k = 0; k < textsFromFirstCluster.size(); k++) {
				IJsonNode elemFirstCluster = textsFromFirstCluster.get(k);
				if (elemFirstCluster instanceof TextNode) {
					String textFromFirstCluster = ((TextNode) elemFirstCluster)
							.getTextValue().toString();

					if (secondClusterContainsMoreThenOneText) {
						for (int i = 0; i < textsFromSecondCluster.size(); i++) {
							IJsonNode elemSecondCluster = textsFromSecondCluster
									.get(i);
							if (elemSecondCluster instanceof TextNode) {
								textFromSecondCluster = ((TextNode) elemSecondCluster)
										.getTextValue().toString();
								double similarity = CosineSimilarity
										.calculateCosineSimilarity(
												textFromFirstCluster,
												textFromSecondCluster);
								if (similarity > shortestLinkage) {
									shortestLinkage = similarity;
								}
							}
						}
					} else {
						double similarity = CosineSimilarity
								.calculateCosineSimilarity(
										textFromFirstCluster,
										textFromSecondCluster);
						if (similarity > shortestLinkage) {
							shortestLinkage = similarity;
						}
					}
				}
			}
		} else {
			String textFromFirstCluster = text1.getTextValue().toString();
			if (textsFromSecondCluster != null) {
				for (int i = 0; i < textsFromSecondCluster.size(); i++) {
					IJsonNode elem = textsFromSecondCluster.get(i);
					if (elem instanceof TextNode) {
						String textFromSecondCluster = ((TextNode) elem)
								.getTextValue().toString();
						double similarity = CosineSimilarity
								.calculateCosineSimilarity(
										textFromFirstCluster,
										textFromSecondCluster);
						if (similarity > shortestLinkage) {
							shortestLinkage = similarity;
						}
					}
				}
			} else {
				String textFromSecondCluster = text2.getTextValue().toString();
				res.append((new Double(CosineSimilarity
						.calculateCosineSimilarity(textFromFirstCluster,
								textFromSecondCluster))).toString());
			}
		}

		res.append(shortestLinkage.toString());
		return res;
	}
}
