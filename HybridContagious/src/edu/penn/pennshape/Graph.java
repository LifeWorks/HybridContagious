package edu.penn.pennshape;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import edu.penn.pennshape.contagion.Contagion;

public abstract class Graph {

	protected List<Node> nodes = null;
	protected int dimention = 0;
	protected int size = 0;
	protected int degree = 0;
	protected double prob = 0.0;
	protected List<Node> unreceivedmessagenodes = null;
	protected List<Node> uncontigiousnodes = null;
	protected Contagion contagion = null;
	Random random = null;

	BufferedWriter bw = null;
	FileWriter fw = null;

	public abstract void generate();

	public abstract void rewired();

	public void init(int community, int message) {
		generate();
		rewired();
		setCommunitySeed(community);
		setMessageSeed(message);
	}

	protected void connect(Node a, Node b) {
		a.neighbors.add(b);
		b.neighbors.add(a);
	}

	protected void disconnect(Node a, Node b) {
		a.neighbors.remove(b);
		b.neighbors.remove(a);
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public abstract int upIndex(int currentIndex, int numSteps);

	public void printNeighbors() {

		for (Node node : nodes) {
			System.out.print(node.getNeighbors().size() + ": ");
			for (Node neighbor : node.getNeighbors()) {
				System.out.print(neighbor.getId() + " ");
			}
			System.out.println();

		}

	}

	public void printNeighborsToFile(int counter) {

		try {

			String FILENAME = "./pic/graph" + counter + ".txt";

			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			for (Node node : nodes) {
				bw.append(node.getId() + "==" + node.getActived() + "\n");
			}

			for (Node node : nodes) {
				for (Node neighbor : node.getNeighbors()) {
					if (neighbor.getId() > node.getId()) {
						bw.append(node.getId() + ":" + neighbor.getId() + "\n");
					}
				}
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public int Contagions(int maxrounds) {
		random = new Random();
		int count = 0;
		contagion.setNodeThreshold(this);
		int uncontigiousnodesize = uncontigiousnodes.size();

		while (uncontigiousnodesize * 1.0 / size > 0.001 && count != maxrounds) {

			contagion.start(uncontigiousnodes, unreceivedmessagenodes);
			uncontigiousnodesize = uncontigiousnodes.size();
			if (count % 200 == 0) {
				System.out.println(count + ":" + (1 - uncontigiousnodesize * 1.0 / size));
			}
			if (count % 200 == 0) {
				printNeighborsToFile(count);
			}

			count++;
		}
		printNeighborsToFile(count);
		return count;
	}
	
	
	public double ContagionsAndGetActiveNodes(int maxrounds) {
		random = new Random();
		int count = 0;
		contagion.setNodeThreshold(this);
		int uncontigiousnodesize = uncontigiousnodes.size();

		while (uncontigiousnodesize * 1.0 / size > 0.001 && count != maxrounds) {

			contagion.start(uncontigiousnodes, unreceivedmessagenodes);
			uncontigiousnodesize = uncontigiousnodes.size();
			count++;
		}
		return (1 - uncontigiousnodesize * 1.0 / size);
	}

	

	public void setCommunitySeed(int community) {
		for (int i = 0; i < community; i++) {
			Node node = nodes.get(i);
			node.setActived(true);
			uncontigiousnodes.remove(node);
			for (Node neighbor : node.getNeighbors()) {
				neighbor.setActived(true);
				uncontigiousnodes.remove(neighbor);
			}
		}
	}

	public void setMessageSeed(int message) {
		for (int i = 0; i < message; i++) {
			Node node = nodes.get(i);
			node.setMessagereceived(true);
			unreceivedmessagenodes.remove(node);
		}

	}

}
