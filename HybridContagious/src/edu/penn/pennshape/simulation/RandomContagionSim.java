package edu.penn.pennshape.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;
//import java.lang.Math;

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
			for (int i=1;i<10;i++) {
				for (int j=1;j<8;j++) {
					threshold = h;
					mean = i * 0.1;
					std = 0.1 * j;
					combinations.add(new ArrayList<Double>(Arrays.asList(threshold,mean,std)));
				}
			}
		}

		combinations.parallelStream().forEach(combo -> ns.simRandomContigions(combo.get(0).intValue(), numofcommunityseeds, numofmessageseeds, combo.get(1), combo.get(2))); 

	}
}
