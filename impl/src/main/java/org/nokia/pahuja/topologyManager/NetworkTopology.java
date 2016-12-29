/*
 * Copyright © 2015 Nokia, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.nokia.pahuja.topologyManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.common.collect.HashMultimap;

//keeps a list of all the Nodes and their ports for topology
class Value{

	int portNo;
	String neighborNode;

	Value (int portNo, String neighborNode){
		this.portNo = portNo;
		this.neighborNode = neighborNode;
	}

}

// Toppolgy maintains all the internal ports' connection
public class NetworkTopology implements NetworkAdjacencyList {

	/*key = NodeName , Value = HashMap (key = portNo , value = neighborNode

	Key       |            Value

	N1				Key      |     Value

					port 1        port 2 , Node 2
					port 2        port 1 , Node 3

	N2				port 2        port 1 , Node 1

	N3 				port 1		  port 2 , Node 1

	*/
	static HashMap<String, HashMap<Integer, Value>> hm = new HashMap<String, HashMap<Integer, Value>>();


	@Override
	public void addNode(String nodeName) {

		if (!hm.containsKey(nodeName)){
			// Node does not exist - add Node
			hm.put(nodeName, new HashMap<Integer, Value>());
			System.out.println("Node added");

			printTopology();
		}
	}

	@Override
	public void removeNode(String nodeName) {

		// Before removing the nodeName, remove the ports of all those nodes whose neighbor is nodeName

		try{

		if (hm.containsKey(nodeName)){

			// Find nodes having nodeName as neighbor


			if (hm.get(nodeName) != (null)){

				// Traverse neighbors

					HashMap<Integer, Value> neighbors = hm.get(nodeName);

					for(Map.Entry<Integer, Value> entry: neighbors.entrySet()){

						Value neighbor = entry.getValue();
						int portNo = neighbor.portNo;
						String neighborNode = neighbor.neighborNode;

						// remove all the ports containing nodeName as Neighbor
						removeNodeConnector(neighborNode, portNo);
					}
				}

				hm.remove(nodeName);

				printTopology();

			}

		}catch (Exception e){

			System.out.println("inside catch removeNode");
			System.out.println(hm);

		}

		return;
	}

	@Override
	public void addNodeConnector(String nodeName, int portNo, String neighborNodeName, int neighborPortNo) {
		// Because first Node will come up, when Node Connector is up,
		// both the nodes should already be in the topology
		System.out.println(" Node Connector added");
		try{
			if (hm.containsKey(nodeName)){

				hm.get(nodeName).put(portNo, new Value(neighborPortNo, neighborNodeName));
				System.out.println("Node connector added");
				printTopology();

			}

			else{
				return;
			}

		}catch (Exception e){
			System.out.println(" In catch of Node connector added ");

		}
	}

	@Override
	public void removeNodeConnector(String nodeName, int portNo) {
		// TODO Auto-generated method stub
		System.out.println("Node connector removed");
		try{
			if (hm.containsKey(nodeName)){
				// Node exists -- remove the port from the list

				if (hm.get(nodeName).containsKey(portNo)){

					hm.get(nodeName).remove(portNo);
				}

			}
			printTopology();

		}catch (Exception e){

			System.out.println("In catch of remove Node Connector");

		}
	}

	@Override
	public boolean containsNode(String nodeName) {
		// TODO Auto-generated method stub

		if (hm.containsKey(nodeName)){
			System.out.println(" Contains Node");
			return true;
		}
		System.out.println(" Doesn Not contain Node ");
		return false;
	}

	public void printTopology(){

		if (hm != null){

			for(Map.Entry<String, HashMap<Integer, Value>>  entry : hm.entrySet()){

				System.out.println();
				System.out.println();

				System.out.println(entry.getKey() + "  : ");

				if (hm.get(entry.getKey()) != null){

					HashMap<Integer, Value> h = entry.getValue();
					for(Map.Entry<Integer, Value>  map : h.entrySet()){

						System.out.print(map.getKey() + " --> ");
						System.out.println(map.getValue().portNo + " , " + map.getValue().neighborNode);
					}
				}

			}
		}
		System.out.println();
	}

	@Override
	public HashSet<Integer> getExternalPorts(String nodeName) {

		HashSet<Integer> ports = new HashSet<>();

		if (hm.containsKey(nodeName)){

			HashMap<Integer, Value> nodeNamePorts = hm.get(nodeName);

			for(Map.Entry<Integer, Value>  entry : nodeNamePorts.entrySet()){

				ports.add(entry.getKey());
			}
		}
		return ports;
	}






}
