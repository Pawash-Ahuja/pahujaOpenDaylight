package org.nokia.pahuja.topologyManager;

public interface NetworkAdjacencyList {

	void addNode(String nodeName);
	void removeNode(String nodeName);

	void addNodeConnector(String nodeName, int portNo);
	void removeNodeConnector(String nodeName, int portNo);

}
