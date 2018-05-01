package edu.penn.pennshape.simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

//import java.util.stream;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import edu.penn.pennshape.contagion.ComplexContagion;
import edu.penn.pennshape.contagion.NormalContagion;
import edu.penn.pennshape.contagion.RandomContagion;
import edu.penn.pennshape.contagion.UniformContagion;
import edu.penn.pennshape.graph.Lattice2D;

public class NetworkSim {

	public static int dimention = 100;
	public static int degree = 8;
	//public static double prob = 0.00001;
    public static double prob = 0.01;
	public static int max = 30000000;
	public static int rounds = 100;

	public static double step = 1.1;

	public void simComplexContigions(int threshold, int community, int message) {
		Map<Double, List<Integer>> hasht = new TreeMap<Double, List<Integer>>();
		List<Integer> valuelist = null;
		Lattice2D l1 = null; // complex =3
		double problocal = prob;
		for (int i = 0; i < rounds; i++) {
			problocal = prob;
			System.out.println("Rounds:" + (i + 1));
			int lastroundsteps = 0;
			while (problocal <= 1.01) {

				l1 = new Lattice2D(dimention, degree, problocal,
						new ComplexContagion(threshold));
				l1.init(community, message);

				if (!hasht.containsKey(problocal)) {
					valuelist = new ArrayList<Integer>();
				} else {
					valuelist = hasht.get(problocal);
				}
				int timesteps = l1.Contagions(max);
				outputDynamics(l1,"complex",i,problocal,threshold,0,0);
				System.out.print(problocal);
				System.out.println(" " + timesteps);
				valuelist.add(timesteps);
				hasht.put(problocal, valuelist);

				if (lastroundsteps == timesteps) {
					break;
				} else {
					lastroundsteps = timesteps;
				}

				if (problocal < 0.001) {
					problocal = problocal * step * 4;
				} else if ((problocal >= 0.92)) {
					problocal = problocal + 0.01;
				} else {
					problocal = problocal * step;
				}

			}
			
		}
		Map<String, Double> map = new HashMap<String, Double>();
		map.put("threshold", (double) threshold);
		map.put("community", (double) community);
		map.put("message", (double) message);

		StardardDeviationCalc(hasht, "complex", map);
	}

	public void simRandomContigions(int threshold, int community, int message,
			double mean, double variance) {
		Lattice2D l1 = null; // complex =3
		double problocal = prob;
		
		while (problocal <= 1.01) {
			for (int i = 0; i < rounds; i++) {

				l1 = new Lattice2D(dimention, degree, problocal,
						new RandomContagion(threshold, mean, variance));
				l1.init(community, message);

				l1.Contagions(max);
				outputDynamics(l1,"random",i,problocal,threshold,mean,variance);

			}
				
			if (problocal < 0.001) {
				problocal = problocal * step * 4;
			} else if ((problocal >= 0.92)) {
				problocal = problocal + 0.01;
			} else {
				problocal = problocal * step;
			}
		}
	}

	public void simUniformContigions(int threshold, int community, int message,
			int offset) {
		Map<Double, List<Integer>> hasht = new TreeMap<Double, List<Integer>>();
		List<Integer> valuelist = null;
		Lattice2D l1 = null; // complex =3
		double problocal = prob;
		for (int i = 0; i < rounds; i++) {
			problocal = prob;
			System.out.println("Rounds:" + (i + 1));
			int lastroundsteps = 0;

			while (problocal <= 1.01) {

				l1 = new Lattice2D(dimention, degree, problocal,
						new UniformContagion(threshold, offset));
	
				
				l1.init(community, message);

				if (!hasht.containsKey(problocal)) {
					valuelist = new ArrayList<Integer>();
				} else {
					valuelist = hasht.get(problocal);
				}
				int timesteps = l1.Contagions(max);
				outputDynamics(l1,"uniform",i,problocal,threshold,offset,0);
				System.out.print(problocal);
				System.out.println(" " + timesteps);
				valuelist.add(timesteps);
				hasht.put(problocal, valuelist);

				if (lastroundsteps == timesteps) {
					break;
				} else {
					lastroundsteps = timesteps;
				}
				
				if (problocal < 0.001) {
					problocal = problocal * step * 4;
				} else if ((problocal >= 0.92)) {
					problocal = problocal + 0.01;
				} else {
					problocal = problocal * step;
				}
				
			}
			
		}
		Map<String, Double> map = new HashMap<String, Double>();
		map.put("threshold", (double) threshold);
		map.put("community", (double) community);
		map.put("message", (double) message);
		map.put("offset", (double) offset);

		StardardDeviationCalc(hasht, "uniform", map);
	}

//	public void simNormalContigions(int threshold, int community, int message,
//			double mean, double variance) {
//		Map<Double, List<Integer>> hasht = new TreeMap<Double, List<Integer>>();
//		List<Integer> valuelist = null;
//		Lattice2D l1 = null; // complex =3
//		double problocal = prob;
//		for (int i = 0; i < rounds; i++) {
//			problocal = prob;
//			System.out.println("Rounds:" + (i + 1));
//			
//			int lastroundsteps = 0;
//			
//			while (problocal <= 1.01) {
//
//				l1 = new Lattice2D(dimention, degree, problocal,
//						new NormalContagion(threshold, mean, variance));
//				l1.init(community, message);
//				//System.out.print("Initialization Done!");
//
//				if (!hasht.containsKey(problocal)) {
//					valuelist = new ArrayList<Integer>();
//				} else {
//					valuelist = hasht.get(problocal);
//				}
//				int timesteps = l1.Contagions(max);
//				outputDynamics(l1,"normal",i,problocal,threshold,mean,variance);
//				System.out.print(problocal);
//				System.out.println(" " + timesteps);
//				valuelist.add(timesteps);
//				hasht.put(problocal, valuelist);
//
//				if (lastroundsteps == timesteps) {
//					break;
//				} else {
//					lastroundsteps = timesteps;
//				}
//				
//				if (problocal < 0.001) {
//					problocal = problocal * step * 4;
//				} else if ((problocal >= 0.92)) {
//					problocal = problocal + 0.01;
//				} else {
//					problocal = problocal * step;
//				}
//
//			}
//		}
//		Map<String, Double> map = new HashMap<String, Double>();
//		map.put("threshold", (double) threshold);
//		map.put("community", (double) community);
//		map.put("message", (double) message);
//		map.put("mean", (double) mean);
//		map.put("variance", (double) variance);
//		StardardDeviationCalc(hasht, "normal", map);
//	}

	public void simNormalContigions(int threshold, int community, int message,
			double mean, double variance) {
		Lattice2D l1 = null; // complex =3
		double problocal = prob;
		
		while (problocal <= 1.01) {
			for (int i = 0; i < rounds; i++) {

				l1 = new Lattice2D(dimention, degree, problocal,
						new NormalContagion(threshold, mean, variance));
				l1.init(community, message);

				l1.Contagions(max);
				outputDynamics(l1,"normal",i,problocal,threshold,mean,variance);
			
			}
			
			if (problocal < 0.001) {
				problocal = problocal * step * 4;
			} else if ((problocal >= 0.92)) {
				problocal = problocal + 0.01;
			} else {
				problocal = problocal * step;
			}
		}
	}
	
	public void StardardDeviationCalc(Map<Double, List<Integer>> hasht,
			String name, Map<String, Double> map) {

		String filename = name + "-" + dimention + "-" + degree;
		for (Entry<String, Double> e : map.entrySet()) {
			String key = e.getKey();
			String value = String.valueOf(e.getValue()).replaceAll("\\.", "_");
			filename = filename + "-" + key + "-" + value;
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();
		filename = filename + "_" + dateFormat.format(date);
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File("data/" + filename));
			
			for (Entry<Double, List<Integer>> entry : hasht.entrySet()) {
				double prob = entry.getKey();
				List<Integer> list = entry.getValue();
				double[] array = new double[list.size()];
				for (int i = 0; i < list.size(); i++) {
					array[i] = list.get(i);
				}
				// System.out.println("size:"+array.length);
				StandardDeviation sd = new StandardDeviation(true);
				Mean mean = new Mean();
				double average = mean.evaluate(array);
				double stardarddev = sd.evaluate(array);
				System.out.println(prob + " " + average + " " + stardarddev
						+ " " + (average - 1.96 * stardarddev) + " "
						+ (average + 1.96 * stardarddev));

				writer.write(prob + " " + average + " " + stardarddev + " "
						+ (average - 1.96 * stardarddev) + " "
						+ (average + 1.96 * stardarddev) + "\n");

			}

			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	public void outputDynamics(Lattice2D graph2d,
			String name, int round, double probability,double threshold,double mean,double std) {

		String dirName = "data/" + name + "-" + dimention + "-" + degree + "-" + threshold + "-" + mean + "-" + std + "/probability-" + probability;
		File directory = new File(dirName);
		if (! directory.exists()) {
			boolean success = directory.mkdirs();
			if(! success) {
				System.out.println(dirName + " is not created!\n");
				
			}
		}
		String fileName = "dynamics-" + round + ".txt";
		FileWriter writer = null;
		ArrayList<Double> dynamics = graph2d.getDynamics();
		int tf = /*(int)*/ dynamics.size() / 2;
		try {
			writer = new FileWriter(new File(dirName + "/" + fileName));
			
			for (int j = 0; j < tf; j++) {
				double timeStep = dynamics.get(2*j);
				double saturationLevel = dynamics.get(2*j+1);
				writer.write(timeStep + " " + saturationLevel + "\n");

			}
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}