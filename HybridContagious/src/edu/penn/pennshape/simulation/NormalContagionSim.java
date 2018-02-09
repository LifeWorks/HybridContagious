package edu.penn.pennshape.simulation;




//
public class NormalContagionSim extends NetworkSim {
	public static void main(String[] args) {

		NormalContagionSim ns = new NormalContagionSim();

		int threshold = 4;
		int numofcommunityseeds = 1;
		int numofmessageseeds = 1;
		int mean = 2;
		int std = 1;
		for (int i = 1; i <= 20; i++) {
		ns.simNormalContigions(i, numofcommunityseeds,
				numofmessageseeds, mean, std);
		}
	}
}
