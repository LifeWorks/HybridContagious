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

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import edu.penn.pennshape.contagion.ComplexContagion;
import edu.penn.pennshape.contagion.NormalContagion;
import edu.penn.pennshape.contagion.RandomContagion;
import edu.penn.pennshape.contagion.UniformContagion;
import edu.penn.pennshape.graph.Lattice2D;

public class NetworkSim {

	public static int dimention = 400;
	public static int degree = 48;
	//public static double prob = 0.00001;
    public static double prob = 0.001;
	public static int max = 30000000;
	public static int rounds = 1;

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
			while (problocal <= 1.0) {

				l1 = new Lattice2D(dimention, degree, problocal,
						new ComplexContagion(threshold));
				l1.init(community, message);

				if (!hasht.containsKey(problocal)) {
					valuelist = new ArrayList<Integer>();
				} else {
					valuelist = hasht.get(problocal);
				}
				int timesteps = l1.Contagions(max);
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
			double messageprod) {
		Map<Double, List<Integer>> hasht = new TreeMap<Double, List<Integer>>();
		List<Integer> valuelist = null;
		Lattice2D l1 = null; // complex =3
		double problocal = prob;
		for (int i = 0; i < rounds; i++) {
			problocal = prob;
			System.out.println("Rounds:" + (i + 1));
			int lastroundsteps = 0;
			while (problocal <= 1.0) {

				l1 = new Lattice2D(dimention, degree, problocal,
						new RandomContagion(threshold, messageprod));
				l1.init(community, message);

				if (!hasht.containsKey(problocal)) {
					valuelist = new ArrayList<Integer>();
				} else {
					valuelist = hasht.get(problocal);
				}
				int timesteps = l1.Contagions(max);
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
		map.put("messageprod", (double) messageprod);
		StardardDeviationCalc(hasht, "random", map);
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

			while (problocal <= 1.0) {

				l1 = new Lattice2D(dimention, degree, problocal,
						new UniformContagion(threshold, offset));
	
				
				l1.init(community, message);

				if (!hasht.containsKey(problocal)) {
					valuelist = new ArrayList<Integer>();
				} else {
					valuelist = hasht.get(problocal);
				}
				int timesteps = l1.Contagions(max);
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

	public void simNormalContigions(int threshold, int community, int message,
			int mean, int variance) {
		Map<Double, List<Integer>> hasht = new TreeMap<Double, List<Integer>>();
		List<Integer> valuelist = null;
		Lattice2D l1 = null; // complex =3
		double problocal = prob;
		for (int i = 0; i < rounds; i++) {
			problocal = prob;
			System.out.println("Rounds:" + (i + 1));
			
			int lastroundsteps = 0;
			
			while (problocal <= 1.0) {

				l1 = new Lattice2D(dimention, degree, problocal,
						new NormalContagion(threshold, mean, variance));
				l1.init(community, message);
				System.out.print("Initilization Done!");

				if (!hasht.containsKey(problocal)) {
					valuelist = new ArrayList<Integer>();
				} else {
					valuelist = hasht.get(problocal);
				}
				int timesteps = l1.Contagions(max);
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
		map.put("mean", (double) mean);
		map.put("variance", (double) variance);
		StardardDeviationCalc(hasht, "normal", map);
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

}