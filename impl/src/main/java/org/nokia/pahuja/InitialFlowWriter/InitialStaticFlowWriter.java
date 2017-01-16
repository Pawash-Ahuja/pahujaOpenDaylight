/*
 * Copyright © 2015 Nokia, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.nokia.pahuja.InitialFlowWriter;

import org.nokia.pahuja.InitialFlowWriter.broadcast.Flood;
import org.nokia.pahuja.InitialFlowWriter.broadcast.FloodGroup;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeUpdated;

public class InitialStaticFlowWriter{



	/* There are 3 tables
	 * Table 0,  Table 1 and Table 2
	 * Table 0 will have all the Firewall rules from priority 1000 to 2000 (loaded dynamically)
	 *
	 * We will have 3 static entries:-
	 * (i)Table 0 will have a static entry for ARP which will say all the arp packets
	 * have to be sent to the Controller (priority 100)
	 * (ii)Table 0 will have another static entry for ARP which will say in case of
	 * no match go to Table 1 (priority 0)
	 * (iii)Table 1 will have a static entry which will say if the destination address is
	 * ff:ff:ff:ff:ff:ff, flood the packet (priority 0)
	 *
	 *  Rest all entries will be dynamic.
	 */


	public void addStaticFlows(NodeId nodeId , NodeUpdated notification, DataBroker dataBroker) {
		// TODO Auto-generated method stub


		// go to controller in case of no match - Table 1
		new GoToController().addFlow(nodeId, notification, dataBroker);



		// punt LLDP packets to Controller
		new LlldpFlow().addFlow(nodeId, notification, dataBroker);

		//Go to table 1 from 0 in case of no match
		new NextTable().addFlow(nodeId, notification, dataBroker);

		// check
		new FloodGroup().addGroup(nodeId, notification, dataBroker);

		// flood in case of no match
		new Flood().addFlow(nodeId , notification, dataBroker);




	}

}
