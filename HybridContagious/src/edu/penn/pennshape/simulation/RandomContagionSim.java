package edu.penn.pennshape.simulation;

public class RandomContagionSim extends NetworkSim {
	public static void main(String[] args) {

		RandomContagionSim ns = new RandomContagionSim();

		int threshold = 10  ;
		int numofcommunityseeds = 1;
		int numofmessageseeds = 1;
		double messageprod = 0.2;
		for (int i = 1; i <= 20; i++) {
		ns.simRandomContigions(i, numofcommunityseeds,
				numofmessageseeds, messageprod);
		}
	}
}
