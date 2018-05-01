package edu.penn.pennshape.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

public class RandomContagionSim extends NetworkSim {
	public static void main(String[] args) {

		RandomContagionSim ns = new RandomContagionSim();

		int numofcommunityseeds = 1;
		int numofmessageseeds = 1;
		double threshold = 5;
		double mean = 5;
		double std = 0.0;
		
		List<ArrayList<Double>> combinations = new LinkedList< ArrayList<Double> >();
		for (int h=0;h<11;h++) {
			for (int i=2;i<9;i++) {
				for (int j=0;j<10;j++) {
					threshold = h;
					mean = i;
					std = 0.5 + j * 0.5;
					combinations.add(new ArrayList<Double>(Arrays.asList(threshold,mean,std)));
				}
			}
		}

		combinations.parallelStream().forEach(combo -> ns.simRandomContigions(combo.get(0).intValue(), numofcommunityseeds, numofmessageseeds, combo.get(1), combo.get(2))); 

	}
}
