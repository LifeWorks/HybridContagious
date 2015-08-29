package edu.penn.pennshape;

import java.util.ArrayList;
import java.util.List;

public class Node {

	int id;
	List<Node> neighbors;
	boolean actived = false;
	boolean messagereceived = false;
	double threshold;

	public Node(int id) {
		neighbors = new ArrayList<Node>();
		this.id = id;

	}

	public boolean getMessagereceived() {
		return messagereceived;
	}

	public void setMessagereceived(boolean messagereceived) {
		this.messagereceived = messagereceived;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Node> getNeighbors() {
		return neighbors;
	}

	public void setNeighrbors(Node node) {
		this.neighbors.add(node);
	}

	public boolean getActived() {

		return actived;
	}

	public void setActived(boolean actived) {
		this.actived = actived;
	}

	public double getThreshold() {

		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public void sendMessage(double prod) {
		for (Node node : neighbors) {
			if (!node.getMessagereceived()) {

			}
		}

	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Node))
			return false;

		Node other = (Node) o;
		if (this.id != other.id)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return (int) id;
	}

	public boolean isNeighborOf(Node node) {

		if (this.neighbors.contains(node)) {
			return true;
		} else {
			return false;
		}

	}

	public static void main(String[] args) {

		List<Node> list = new ArrayList<Node>();
		Node node1 = new Node(1);
		Node node2 = new Node(2);
		Node node3 = new Node(3);
		list.add(node1);
		node1.neighbors.add(node2);
		node1.neighbors.add(node2);
		System.out.println(node1.neighbors.size());

		System.out.println(node1.isNeighborOf(node2));
		System.out.println(node1.isNeighborOf(node3));

	}

}
