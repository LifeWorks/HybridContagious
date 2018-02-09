package edu.penn.pennshape.simulation;

public class UniformContagionSim extends NetworkSim {

	public static void main(String[] args) {

		UniformContagionSim ns = new UniformContagionSim();

		int threshold = 15;
		int numofcommunityseeds = 1;
		int numofmessageseeds = 1;
		int offset = 1;
		for (int i = 1; i <= 20; i++) {
			ns.simUniformContigions(i, numofcommunityseeds, numofmessageseeds, offset);
		}
	}

}
