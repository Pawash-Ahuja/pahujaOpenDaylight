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
import java.util.Set;

import org.nokia.pahuja.utils.ConstantValues;

// maintains stats of all Nodes, their ports (both internal and external, and their vlantags

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
	public HashMap<Integer,HashSet<Integer>> getExternalPorts(String nodeName) {

		if(nodeStats.containsKey(nodeName)){

			// External Ports contains just port Nos.
			HashSet<Integer> externalPorts = new InternalNetworkTopology().getInternalPorts(nodeName);

			//All ports contains port Nos + VlanIds attached to them.
			HashMap<Integer,HashSet<Integer>> allPorts = getAllPorts(nodeName);

			// External ports = All ports - Internal Ports

			for(int i : externalPorts){

				allPorts.remove(i);

			}

			// return allPorts - externalPorts
			return allPorts;

		}
		return null;
	}

	@Override
	public HashSet<Integer> getInternalPorts(String nodeName) {

		return new InternalNetworkTopology().getInternalPorts(nodeName);
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
		 *  2) no vlan is attached
		 *  which by default does nothing
		 *
		 */

		if (nodeStats.containsKey(nodeName)){

			HashSet<Integer> vlans = new HashSet<Integer>();
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


			if (vlan > 125 || vlan < 1){

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

	@Override
	public HashSet<String> getAllNodes() {
		// TODO Auto-generated method stub

		return allNodes;
	}

	@Override
	public HashSet<Integer> getPortsHavingVlanId(String nodeName, int vlanId) {
		// TODO Auto-generated method stub

		if (nodeStats.containsKey(nodeName)){

			Set<Integer> allPorts = nodeStats.get(nodeName).keySet();
			HashSet<Integer> ports = new HashSet();

			/* 1) Store all the ports of nodename in Set
			 * 2) Traverse all the ports
			 * 3) Check if the port contains vlan id, if yes add to result
			 */
			for (int i : allPorts){

				HashSet<Integer> vlans = nodeStats.get(nodeName).get(i);
				// vlans contain all the vlans tagged to port
				if (vlans.contains(vlanId)){
					 // this port has vlanId, so add it to the list
					ports.add(i);
				}
			}

			// return all internal ports of nodeName with vlan id vlanId
			// this includes all the external ports of vlan Id

			return ports;
		}

		return null;
	}


}
