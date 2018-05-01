package edu.penn.pennshape.simulation;

public class NormalContagionSim extends NetworkSim {
	public static void main(String[] args) {

		NormalContagionSim ns = new NormalContagionSim();

		int threshold = 5;
		int numofcommunityseeds = 1;
		int numofmessageseeds = 1;
		double mean = 2;
		double std = 0.0;
		
		for (int i=0;i<7;i++) {
			for (int j=0;j<10;j++) {
				mean = 2 + i;
				std = 0.5 + j * 0.5;
				ns.simNormalContigions(threshold, numofcommunityseeds,
						numofmessageseeds, mean, std);
			}
		}
	}
}
