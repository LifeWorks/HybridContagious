package edu.penn.pennshape.simulation;

public class RandomContagionSim extends NetworkSim {
	public static void main(String[] args) {

		RandomContagionSim ns = new RandomContagionSim();

		int threshold = 5;
		int numofcommunityseeds = 0;
		int numofmessageseeds = 1;
		double mean = 5;
		double std = 0.0;
		
		ns.simRandomContigions(threshold, numofcommunityseeds,
				numofmessageseeds, mean, std);

	}
}
