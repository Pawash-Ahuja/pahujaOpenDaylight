/*
 * Copyright © 2015 Nokia, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.nokia.pahuja.vlanCheck;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Future;

import org.nokia.pahuja.topologyManager.NodeStats;
import org.opendaylight.yang.gen.v1.nokia.pahuja.setvlan.rev170105.SetVlanService;
import org.opendaylight.yang.gen.v1.nokia.pahuja.setvlan.rev170105.VlanConfigurationInput;
import org.opendaylight.yang.gen.v1.nokia.pahuja.setvlan.rev170105.VlanConfigurationOutput;
import org.opendaylight.yang.gen.v1.nokia.pahuja.setvlan.rev170105.VlanConfigurationOutputBuilder;
import org.opendaylight.yang.gen.v1.nokia.pahuja.setvlan.rev170105.vlanconfiguration.input.OpenflowNodes;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;

public class Vlans implements SetVlanService  {

	@Override
	public Future<RpcResult<VlanConfigurationOutput>> vlanConfiguration(VlanConfigurationInput input) {
		// TODO Auto-generated method stub

		String result = "";

		for (OpenflowNodes node: input.getOpenflowNodes()){

			String nodeName = node.getSwitchName();
			int portNo = node.getPortNo();

			// Replace the existing Vlans, by default every external port has no vlan tag
			// and every internal port has all the possible vlan tags (Trunk) 1 to 125


			List<Integer> vlanIds = node.getVlanId();
			NodeStats obj = new NodeStats();


			if (obj.containsNode(nodeName)){
				// use the above three to flood populate Node Stats

				if (obj.containsPort(nodeName, portNo)){


					if (obj.validateVlan(vlanIds)){

						// every field is correct
						// populate NodeStats
						obj.addVlanId(nodeName, portNo, vlanIds);


						/*rewrite the flood flows for the corresponding vlansIds
						 *
						 * Get all the ports corresponding to vlandIds and nodeName
						 * and rewrite the flows on these vlan tables
						 *
						 */

						// Traverse vlanids
						for (int vlan : vlanIds){

							// ports has both Internal and External ports of node nodename
							// and vlanId as vlan

							HashSet<Integer> ports = obj.getPortsHavingVlanId(nodeName, vlan);

							/* Now, we have all the ports tagged with vlanId as vlan
							 * and we can rewrite the flood flow on these specific vlans;
							 */

						}






						result = "success";

					}
					else{
						result = "Error: Vlans not in the range 1-4096";
					}
				}
				else{
					result = "Error: Port No does not exist";
				}
			}
			else{

				result = "Error: OpenFlow node does not exist";
			}

			System.out.println(node);


		}

		VlanConfigurationOutput output = new VlanConfigurationOutputBuilder()
				.setResult(result)
				.build();

		return RpcResultBuilder.success(output).buildFuture();
	}

}
