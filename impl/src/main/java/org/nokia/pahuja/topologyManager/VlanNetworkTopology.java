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

/* maintains Network Topology for every instance of Vlan
 * (includes both external & internal ports)
 */
class Value1{

	int portNo;
	String neighborNode;


	Value1 (int portNo, String neighborNode){
		this.portNo = portNo;
		this.neighborNode = neighborNode;

	}
}
public class VlanNetworkTopology {


	/*key = NodeName , Value = portNo , neighborNode

	Key       |            Value

	N1				Key      |           Value

					port 1         port 2 , Node 2
					port 2         port 1 , Node 3
					port 3		   null (because it is an external port)

	N2				port 2         port 1 , Node 1
					port 1	       null

	N3 				port 1		   port 2 , Node 1
					port 2		   null

	This will be for every vlan Id saved in HashMap of vlanid , its Object

	*/

	static HashMap<Integer, VlanNetworkTopology> obj = new HashMap<>();
	HashMap<String, Value1> topology = null;

	static VlanNetworkTopology getInstance(int vlanId){

		if (obj.containsKey(vlanId)){
			return obj.get(vlanId);
		}
		else{
			VlanNetworkTopology vlanObj = new VlanNetworkTopology();
			vlanObj.createTopology(vlanObj, vlanId);
			return vlanObj;
		}
	}

	private void createTopology(VlanNetworkTopology vlanObj, int vlanId) {
		// TODO Auto-generated method stub


		/* extra data from Node Stats and make Topology like:
		 *
		 *

	/*key = NodeName , Value = portNo , neighborNode

	Key       |            Value

	N1				Key      |           Value

					port 1         null
					port 2         null
					port 3		   null

	N2				port 2
					port 1	       null

	N3 				port 1		   null
					port 2		   null
		 */

		HashSet<String> allNodes = new NodeStats().getAllNodes();

		for (String node : allNodes){


		}





	}

}
