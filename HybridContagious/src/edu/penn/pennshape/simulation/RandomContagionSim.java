package edu.penn.pennshape.simulation;

public class RandomContagionSim extends NetworkSim {
	public static void main(String[] args) {

		RandomContagionSim ns = new RandomContagionSim();

		int threshold = 10;
		int numofcommunityseeds = 1;
		int numofmessageseeds = 1;
		double messageprod = 1.2;
		ns.simRandomContigions(threshold, numofcommunityseeds,
				numofmessageseeds, messageprod);

	}
}
