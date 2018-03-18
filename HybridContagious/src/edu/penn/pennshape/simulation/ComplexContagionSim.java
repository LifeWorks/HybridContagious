package edu.penn.pennshape.simulation;

public class ComplexContagionSim extends NetworkSim {
	public static void main(String[] args) {

		ComplexContagionSim ns = new ComplexContagionSim();

		int threshold = 3;
		int numofcommunityseeds = 1;
		int numofmessageseeds = 1;
		ns.simComplexContigions(threshold, numofcommunityseeds,
				numofmessageseeds);

	}
}
