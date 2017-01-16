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
import java.util.List;
import java.util.Map;

// stats of all Nodes, their ports (both internal and external, and their vlantags
public class NodeStats implements Nodes {

	static HashMap<String, HashMap<Integer, HashSet<Integer>>> nodeStats = new HashMap<>();
	static HashSet<String> allNodes = new HashSet<>();

	/* Key 					|  			Value
	 *

									Key   |  Value

		openflow:1					portNo, VlanIds (HashSet)
									portNo, VlanIds (HashSet)

		openflow:2 					portNo, vlanIds  (HashSet)

	*/


	@Override
	public HashMap<Integer,HashSet<Integer>> getInternalPorts(String nodeName) {

		if(nodeStats.containsKey(nodeName)){

			// External Ports contains just port Nos.
			HashSet<Integer> externalPorts = new NetworkTopology().getExternalPorts(nodeName);

			//All ports contains port Nos + VlanIds attached to them.
			HashMap<Integer,HashSet<Integer>> allPorts = getAllPorts(nodeName);

			for(int i : externalPorts){

				allPorts.remove(i);

			}

			// return allPorts - externalPorts
			return allPorts;

		}
		return null;
	}

	@Override
	public HashSet<Integer> getExternalPorts(String nodeName) {

		return new NetworkTopology().getExternalPorts(nodeName);
	}

	@Override
	public HashMap<Integer,HashSet<Integer>> getAllPorts(String nodeName) {

		if (nodeStats.containsKey(nodeName)){

			return nodeStats.get(nodeName);
		}

		return null;
	}

	@Override
	public void addPort(String nodeName, int portNo) {

		// Called only when new port is added


		/*  when a new port is added,
		 *  1) add the port to nodeName
		 *  2) tag the port as Vlan 0
		 *  which by default does nothing
		 */

		if (nodeStats.containsKey(nodeName)){

			HashSet<Integer> vlans = new HashSet<Integer>();
			vlans.add(0);
			nodeStats.get(nodeName).put(portNo,vlans);
			printNodeStats();
		}
		return;

	}

	@Override
	public void removePort(String nodeName, int portNo) {

		if (nodeStats.containsKey(nodeName)){
			nodeStats.get(nodeName).remove(portNo);
			printNodeStats();
		}
		return;
	}

	@Override
	public void addNode(String nodeName) {

		if (nodeStats.containsKey(nodeName)){
			return;
		}

		nodeStats.put(nodeName, new HashMap<Integer,HashSet<Integer>>());
		allNodes.add(nodeName);
	}

	@Override
	public void removeNode(String nodeName) {

		if (nodeStats.containsKey(nodeName)){

			nodeStats.remove(nodeName);
		}
		return;

	}

	@Override
	public void printNodeStats() {

		for (int i=1 ; i <= 10; i++){
			System.out.println();
		}

		System.out.println("Node Stats");

		for(Map.Entry<String, HashMap<Integer, HashSet<Integer>>> entry : nodeStats.entrySet()){

			System.out.println();
			System.out.println(entry.getKey()); // prints Node Name
			System.out.println(entry.getValue()); // prints Map(Key = portNo, Value = VlansIds)
			System.out.println();

		}
		System.out.println("/NodeStats");
	}

	@Override
	public boolean containsNode(String nodeName) {
		// TODO Auto-generated method stub

		if (nodeStats.containsKey(nodeName)){
			return true;
		}
		return false;
	}

	@Override
	public boolean containsPort(String nodeName, int portNo) {
		// TODO Auto-generated method stub

		if (nodeStats.get(nodeName).containsKey(portNo)){
			return true;
		}

		return false;
	}

	@Override
	public boolean validateVlan(List<Integer> vlanIds) {
		// TODO Auto-generated method stub



		for(int vlan: vlanIds){


			if (vlan > 4096 || vlan < 1){

				return false;
			}
		}
		return true;
	}

	@Override
	public void addVlanId(String nodeName, int portNo, List<Integer> vlanIds) {
		// TODO Auto-generated method stub

		// Traverse list and add to HashSet
		HashSet<Integer> vlans = new HashSet<Integer>();

		for(int vlan: vlanIds){
			vlans.add(vlan);
		}

		nodeStats.get(nodeName).put(portNo, vlans);
		printNodeStats();
	}

	public HashSet<Integer> getVlanIds(String nodeName, int portNo) {
		// TODO Auto-generated method stub

		return nodeStats.get(nodeName).get(portNo);
	}

	@Override
	public HashSet<String> getAllNodes() {
		// TODO Auto-generated method stub

		return allNodes;
	}

}
