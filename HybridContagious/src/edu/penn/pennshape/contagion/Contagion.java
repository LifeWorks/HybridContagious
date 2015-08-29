package edu.penn.pennshape.contagion;

import java.util.List;
import java.util.Random;

import edu.penn.pennshape.Graph;
import edu.penn.pennshape.Node;

public abstract class Contagion {
	Random random = null;
	double threshold = 0;

	public void start(List<Node> uncontigiousnodes,
			List<Node> unreceivedmessagenodes) {
		complexContagions(uncontigiousnodes);
		simpleContagions(unreceivedmessagenodes);

	}

	int countActiveNeighbors(Node node) {
		int count = 0;
		for (Node neighbor : node.getNeighbors()) {
			if (neighbor.getActived()) {
				count++;
			}
		}
		return count;
	}

	boolean checkmessagereceivedNeighbors(Node node) {
		for (Node neighbor : node.getNeighbors()) {
			if (neighbor.getMessagereceived()) {
				return true;
			}
		}
		return false;
	}

	protected void complexContagions(List<Node> uncontigiousnodes) {

		int uncontigiousnodeidx = random.nextInt(uncontigiousnodes.size());
		Node selectednode = uncontigiousnodes.get(uncontigiousnodeidx);

		int numofactivenodes = countActiveNeighbors(selectednode);

		if (numofactivenodes >= selectednode.getThreshold()) {
			selectednode.setActived(true);
			uncontigiousnodes.remove(selectednode);
		}
	}

	protected void simpleContagions(List<Node> unreceivedmessagenodes) {
		throw new UnsupportedOperationException();

	}

	public void setNodeThreshold(Graph graph) {
		List<Node> nodes = graph.getNodes();
		for (Node node : nodes) {
			node.setThreshold(threshold);
		}
	}
}
