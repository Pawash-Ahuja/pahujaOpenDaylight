package org.nokia.pahuja.topologyManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class NeighborTopology implements NeighborList {

	// HashMap<Node1, Set of Nodes>
	HashMap<String, HashSet<String>> hm = new HashMap<String, HashSet<String>>();
	@Override
	public void addNeighbor(String nodeName,String neighborNodeName) {

		// check if entry for node exists in the Map
		if (hm.containsKey(nodeName)){
			// add node to the Set
			hm.get(nodeName).add(neighborNodeName);
		}
		else{
			//hm.put(nodeName, new HashSet().add(neighborNodeName));
			HashSet<String> h = new HashSet<String>();
			//new HashSet<String>().add(neighborNodeName);
			h.add(neighborNodeName);
			hm.put(nodeName, h);
		}
	}

	@Override
	public HashMap<String, HashSet<String>> getInstance() {

		return hm;
	}

	@Override
	public void removeNode(String nodeName) {

		/* Find neighbors of the node nodeName
		    	--> Remove nodeName from the neighbors list
		    	  	--> Remove nodeName entry
		eg. n1 - n2, n3 // find n2 and n3 list -- remove this entire entry
			n2 - n1, n4 // remove n1 from here
			n3 - n1, n4 // remove n1 from here
			n4 - n3
		*/

		if (hm.containsKey(nodeName)){

			// Find negihbors
			HashSet<String> neighbors = hm.get(nodeName);

			// Traverse neighbors
			for (String neighbor : neighbors){

				if (hm.containsKey(neighbor)){

					hm.get(neighbor).remove(nodeName);
				}
			}
			hm.remove(nodeName);
		}
		return;
	}
}
