package org.nokia.pahuja.topologyManager;

import java.util.HashMap;
import java.util.HashSet;

public interface NeighborList {

	void addNeighbor(String nodeName, String neighborNodeName);
	void removeNode(String nodeName);
	HashMap<String, HashSet<String>> getInstance();
}
