package org.nokia.pahuja.topologyManager;

import java.util.HashMap;
import java.util.HashSet;

//keeps a list of all the Nodes and their ports for topology

public class NetworkTopology implements NetworkAdjacencyList {

	// Key= Node, value = set of ports it has
	HashMap<String, HashSet<Integer>> hm = new HashMap<>();

	@Override
	public void addNode(String nodeName) {
		// TODO Auto-generated method stub
		if (!hm.containsKey(nodeName)){
			// Node does not exist - add Node
			hm.put(nodeName, null);
		}
	}

	@Override
	public void removeNode(String nodeName) {
		// TODO Auto-generated method stub
		if (hm.containsKey(nodeName)){
			// Node does not exist - add Node
			hm.remove(nodeName);
		}
	}

	@Override
	public void addNodeConnector(String nodeName, int portNo) {
		// TODO Auto-generated method stub

		if (hm.containsKey(nodeName)){
			// Node does not exist - add Node
			hm.get(nodeName).add(portNo);
		}
	}

	@Override
	public void removeNodeConnector(String nodeName, int portNo) {
		// TODO Auto-generated method stub

		if (hm.containsKey(nodeName)){
			// Node does not exist - add Node
			HashSet<Integer> portList = hm.get(nodeName); // set of ports
			if (portList.contains(portNo)){

				hm.get(nodeName).remove(portNo);
			}
		}
	}

}
