package edu.penn.pennshape.graph;

import java.util.ArrayList;
import java.util.Random;

import edu.penn.pennshape.Graph;
import edu.penn.pennshape.Node;
import edu.penn.pennshape.contagion.Contagion;

public class Lattice2D extends Graph {

	private Random random = null;

	public Lattice2D(int dimention, int degree, double prob, Contagion contagion) {
		this.size = dimention * dimention;
		this.dimention = dimention;
		this.degree = degree;
		this.prob = prob;
		this.nodes = new ArrayList<Node>();
		this.uncontigiousnodes = new ArrayList<Node>();
		this.unreceivedmessagenodes = new ArrayList<Node>();
		this.contagion = contagion;
		random = new Random();
		this.contagionLevels = new ArrayList<Double>();
	}

	public void generate() {

		for (int i = 0; i < size; i++) {
			nodes.add(new Node(i));
		}

		for (int i = 0; i < size; i++) {
			uncontigiousnodes.add(nodes.get(i));
		}

		for (int i = 0; i < size; i++) {
			unreceivedmessagenodes.add(nodes.get(i));
		}

		// init lattice ring
		int neighbor = degree / 2;
		for (int i = 0; i < size; i++) {
			for (int j = 1; j <= neighbor; j++) {
				int index = upIndex(i, j);
				// System.out.println(i + ":" + j + ":" + index);
				connect(nodes.get(i), nodes.get(index));
			}
		}
		// System.out.println("Done!");
	}

	public void rewired() {
		// rewire edges
		int neighbor = degree / 2;

		for (int i = 0; i < size; i++) {
			for (int step = 1; step <= neighbor; step++) {

				while (true) {

					double r = random.nextDouble();
					if (r < prob) {
						int randomnodeid = (int) random.nextInt(size);

						Node randomVertex = nodes.get(randomnodeid);
						Node ithVertex = nodes.get(i);
						Node neighborVertex = ithVertex.getNeighbors().get(
								step - 1);

						if (!neighborVertex.isNeighborOf(randomVertex)
								&& neighborVertex != randomVertex) {
							disconnect(ithVertex, neighborVertex);
							connect(randomVertex, neighborVertex);
							break;
						}
					} else {
						break;
					}
				}
			}
		}
		// System.out.println("Done!");

	}

	public int getRadix(int numSteps) {

		return (int) ((Math.sqrt(numSteps + 1) - 1) / 2);

	}
	
	public ArrayList<Double> getDynamics() {
		return(contagionLevels);
	}

	public int upIndex(int currentIndex, int numSteps) {

		int rowidx = currentIndex / dimention;
		int colidx = currentIndex % dimention;

		int r = getRadix(degree);

		int rowidxtop = rowidx - r;
		int rowidxbottom = rowidx + r;

		int colidxleft = colidx - r;
		int colidxright = colidx + r;

		int position = 1;
		int index = 0;
		for (int row = rowidxtop; row <= rowidxbottom; row++) {
			for (int col = colidxleft; col <= colidxright; col++) {
				if ((row != rowidx || col != colidx) && position == numSteps) {
					int temprow = row;
					int tempcol = col;
					if (temprow < 0) {
						temprow = temprow + dimention;
					} else if (temprow >= dimention) {
						temprow = temprow - dimention;
					}

					if (tempcol < 0) {
						tempcol = tempcol + dimention;
					} else if (tempcol >= dimention) {
						tempcol = tempcol - dimention;
					}

					index = temprow * dimention + tempcol;
					return index;
					// if (graph.get(i).getNeighbors().size() <= degree / 2) {
					// graph.get(i).getNeighbors().add(graph.get(index));
					// connect(graph.get(i), graph.get(index));
					// }else{
					// break;
					// }

				} else if ((row != rowidx || col != colidx)
						&& position != numSteps) {
					position++;
				} else if (position > numSteps) {
					break;
				}
			}
		}
		return index;
	}

}
