package edu.penn.pennshape.contagion;

import java.util.List;
import java.util.Random;

import edu.penn.pennshape.Node;

public class UniformContagion extends Contagion {

	int offset = 0;

	public UniformContagion(double threshold, int offset) {
		random = new Random();
		this.threshold = threshold;
		this.offset = offset;
	}

	protected void simpleContagions(List<Node> unreceivedmessagenodes) {
		/*
		 * int unreceivedmessagenodessize = unreceivedmessagenodes.size(); if
		 * (unreceivedmessagenodessize != 0) { int unreceivedmessagenodesidx =
		 * random .nextInt(unreceivedmessagenodessize);
		 * 
		 * Node selectednode = unreceivedmessagenodes
		 * .get(unreceivedmessagenodesidx); if
		 * (checkmessagereceivedNeighbors(selectednode)) {
		 * selectednode.setMessagereceived(true);
		 * selectednode.setThreshold(selectednode.getThreshold() - offset);
		 * unreceivedmessagenodes.remove(selectednode); } }
		 */
	}

}
