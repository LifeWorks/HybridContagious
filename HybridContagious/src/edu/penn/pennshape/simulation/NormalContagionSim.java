package edu.penn.pennshape.simulation;

public class NormalContagionSim extends NetworkSim {
	public static void main(String[] args) {

		NormalContagionSim ns = new NormalContagionSim();

		int threshold = 5;
		int numofcommunityseeds = 1;
		int numofmessageseeds = 1;
		double mean = 3;
		double std = 0.5;
		ns.simNormalContigions(threshold, numofcommunityseeds,
				numofmessageseeds, mean, std);

	}
}
