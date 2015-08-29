package edu.penn.pennshape.contagion;

import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;

import edu.penn.pennshape.Node;

public class NormalContagion extends Contagion {
	double mean = 0.0;
	double variance =0.0;
	NormalDistribution normaldist = null;
	public NormalContagion(double threshold, double mean, double variance) {
		random = new Random();
		normaldist = new NormalDistribution();
		this.threshold = threshold;
		this.mean = mean;
		this.variance=variance;
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
				double offset = normaldist.sample();
				if (offset<0){
					offset=0;
				}
				selectednode.setThreshold(selectednode.getThreshold() - offset);
				unreceivedmessagenodes.remove(selectednode);
			}
		}
	}
}
