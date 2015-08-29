import java.util.ArrayList;
import java.util.List;

import edu.penn.pennshape.Node;


public class NodeTest {

	public static void main(String [] args){
		
		Node node1 = new Node(1);
		node1.setThreshold(10);
		List<Node> list1 = new ArrayList<Node>();
		List<Node> list2 = new ArrayList<Node>();
		list1.add(node1);
		list2.add(node1);
		Node nodetemp = list1.get(0);
		nodetemp.setThreshold(8);
		Node nodetemp1 = list2.get(0);
		System.out.println(nodetemp1.getThreshold());
		
	}
	
	
}
