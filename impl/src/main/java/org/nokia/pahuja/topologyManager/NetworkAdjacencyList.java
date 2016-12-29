package org.nokia.pahuja.topologyManager;

import java.util.HashSet;

public interface NetworkAdjacencyList {

	void addNode(String nodeName);
	void removeNode(String nodeName);

	void addNodeConnector(String nodeName, int portNo, String neighborNodeName, int neighborPortNo) ;
	void removeNodeConnector(String nodeName, int portNo);
	boolean containsNode(String nodeName);
	HashSet<Integer> getExternalPorts(String nodeName);

}
