package edu.penn.pennshape.contagion;

import java.util.List;
import java.util.Random;

import edu.penn.pennshape.Node;

public class RandomContagion extends Contagion {

	double messageprob = 0.0;

	public RandomContagion(double threshold, double messageprob) {
		random = new Random();
		this.threshold = threshold;
		this.messageprob = messageprob;
	}


	protected void simpleContagions(List<Node> unreceivedmessagenodes) {
		int unreceivedmessagenodessize = unreceivedmessagenodes.size();
		if (unreceivedmessagenodessize != 0) {
			int unreceivedmessagenodesidx = random
					.nextInt(unreceivedmessagenodessize);

			Node selectednode = unreceivedmessagenodes
					.get(unreceivedmessagenodesidx);
			if (checkmessagereceivedNeighbors(selectednode)) {
				selectednode.setMessagereceived(true);
				double r = random.nextDouble();
				if (r < messageprob) {
					selectednode.setActived(true);
				}
				unreceivedmessagenodes.remove(selectednode);
			}
		}
	}

}
