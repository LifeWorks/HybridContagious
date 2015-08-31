package edu.penn.pennshape.simulation;

public class NormalContagionSim extends NetworkSim {
	public static void main(String[] args) {

		NormalContagionSim ns = new NormalContagionSim();

		int threshold = 15;
		int numofcommunityseeds = 1;
		int numofmessageseeds = 1;
		int mean = 5;
		int std = 1;
		ns.simNormalContigions(threshold, numofcommunityseeds,
				numofmessageseeds, mean, std);

	}
}
