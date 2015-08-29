package edu.penn.pennshape.graph;

import java.util.ArrayList;
import java.util.Random;

import edu.penn.pennshape.Graph;
import edu.penn.pennshape.Node;
import edu.penn.pennshape.contagion.Contagion;


public class LatticeRing extends Graph {

	private Random random = null;

	public LatticeRing(int size, int degree, double prob, Contagion contagion) {
		this.size = size;
		this.degree = degree;
		this.prob = prob;
		this.nodes = new ArrayList<Node>();
		this.contagion=contagion;
		random = new Random();
	}

	public void generate() {

		for (int i = 0; i < size; i++) {
			nodes.add(new Node(i));
		}

		// init lattice ring

		int neighbor = degree / 2;

		for (int i = 0; i < size; i++) {
			for (int j = 1; j <= neighbor; j++) {
				int index = upIndex(i, j);
				connect(nodes.get(i), nodes.get(index));
			}
		}
	}

	public void rewired() {
		// rewire edges

		for (int i = 0; i < size; i++) {
			for (int step = 1; step <= degree; step++) {

				while (true) {

					double r = random.nextDouble();
					if (r < prob) {
						int randomnodeid = (int) random.nextInt(size);

						Node randomVertex = nodes.get(randomnodeid);
						Node ithVertex = nodes.get(i);
						Node neighborVertex = nodes.get(upIndex(i, step));

						if (!neighborVertex.isNeighborOf(randomVertex)
								&& neighborVertex != randomVertex) {
							disconnect(ithVertex,neighborVertex);
							connect(randomVertex,neighborVertex);
							break;
						}
					} else {
						break;
					}
				}
			}
		}

	}
	/*
	public int Contagions(int steps) {
		int activecount=0;
		for (Node node : graph) {
			if (node.getMessagereceived()) {
				for (Node neighbor : node.getNeighbors()) {
					if (!neighbor.getMessagereceived()) {
						neighbor.setMessagereceived(true);
						double r = random.nextDouble();
						if (r < messageprod) {
							neighbor.setActived(true);
						}
					}
				}
			}

			if (!node.getActived()) {
				int activenodes = countActiveNeighbors(node);
				if (activenodes >= threshold) {
					node.setActived(true);
					activecount++;
				}
			}else{
				activecount++;
			}
			

		}
		return activecount/size;
	}
	*/
	public int upIndex(int currentIndex, int numSteps)
	{		
		if (currentIndex + numSteps > size - 1)
		{
			return numSteps - (size - currentIndex);
		}
		return currentIndex + numSteps;
	}

}
