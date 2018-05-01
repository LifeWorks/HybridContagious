package edu.penn.pennshape.contagion;

import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;

import edu.penn.pennshape.Node;

public class RandomContagion extends Contagion {

	double mean = 0.0;
	double std =0.0;

	NormalDistribution normaldist = null;
	public RandomContagion(double threshold, double mean, double std) {
		random = new Random();
		normaldist = new NormalDistribution(mean, std);
		this.threshold = threshold;
		this.mean = mean;
		this.std=std;
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
				//System.out.println("Offset:"+offset);
				//System.out.println("SD:"+normaldist.getStandardDeviation()+" Mean:"+normaldist.getMean());
				if (offset >= selectednode.getThreshold()){
					selectednode.setActived(true);
				}
				
				unreceivedmessagenodes.remove(selectednode);

			}
		}
	}

}
