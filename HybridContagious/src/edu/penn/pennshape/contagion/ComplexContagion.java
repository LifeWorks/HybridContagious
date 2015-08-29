package edu.penn.pennshape.contagion;

import java.util.List;
import java.util.Random;

import edu.penn.pennshape.Node;

public class ComplexContagion extends Contagion {

	public ComplexContagion(double threshold) {
		random = new Random();
		this.threshold = threshold;
	}


	protected void simpleContagions(List<Node> unreceivedmessagenodes) {
	
	}


}
