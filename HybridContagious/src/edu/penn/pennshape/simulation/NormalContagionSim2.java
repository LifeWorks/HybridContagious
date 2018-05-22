package edu.penn.pennshape.simulation;

//import java.util.ArrayList;

public class NormalContagionSim2 extends NetworkSim {
	public static void main(String[] args) {

		NormalContagionSim ns = new NormalContagionSim();

		int threshold = 5;
		int numofcommunityseeds = 1;
		int numofmessageseeds = 1;
		double mean = 5;
		double std = 0.0;
		
		for (int i=9;i<11;i++) {
			for (int j=0;j<10;j++) {
				if (i != 9 || j > 2) {
					threshold = i;
					std = 0.5 + j * 0.5;
					ns.simNormalContigions(threshold, numofcommunityseeds,
							numofmessageseeds, mean, std);
				}
			}
		}
	}
}