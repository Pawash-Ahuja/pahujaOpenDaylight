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

public class NodeStats implements Nodes {

	static HashMap<String, HashSet<Integer>> nodeStat = new HashMap<>();


	@Override
	public HashSet<Integer> getInternalPorts(String nodeName) {

		if(nodeStat.containsKey(nodeName)){

			HashSet<Integer> externalPorts = new NetworkTopology().getExternalPorts(nodeName);
			HashSet<Integer> internalPorts = getAllPorts(nodeName);

			for (int p :externalPorts){
				internalPorts.remove(p);
			}

			return internalPorts;

		}
		return null;
	}

	@Override
	public HashSet<Integer> getExternalPorts(String nodeName) {

		return new NetworkTopology().getExternalPorts(nodeName);
	}

	@Override
	public HashSet<Integer> getAllPorts(String nodeName) {

		if (nodeStat.containsKey(nodeName)){

			return nodeStat.get(nodeName);
		}

		return null;
	}

	@Override
	public void addPort(String nodeName, int portNo) {

		if (nodeStat.containsKey(nodeName)){

			nodeStat.get(nodeName).add(portNo);
			printNodeStats();
		}
		return;

	}

	@Override
	public void removePort(String nodeName, int portNo) {

		if (nodeStat.containsKey(nodeName)){
			nodeStat.get(nodeName).remove(portNo);
			printNodeStats();
		}
		return;
	}

	@Override
	public void addNode(String nodeName) {

		if (nodeStat.containsKey(nodeName)){
			return;
		}

		nodeStat.put(nodeName, new HashSet<Integer>());
	}

	@Override
	public void removeNode(String nodeName) {

		if (nodeStat.containsKey(nodeName)){

			nodeStat.remove(nodeName);
		}
		return;

	}

	@Override
	public void printNodeStats() {

		for (int i=1 ; i <= 10; i++){
			System.out.println();
		}
		System.out.println("Node Stats");

		for(Map.Entry<String, HashSet<Integer>> entry : nodeStat.entrySet()){

			System.out.println();
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
			System.out.println();

		}
		System.out.println("/NodeStats");
	}
}
