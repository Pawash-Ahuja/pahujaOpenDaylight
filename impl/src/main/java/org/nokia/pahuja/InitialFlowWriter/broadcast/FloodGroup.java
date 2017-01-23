/*
 * Copyright © 2015 Nokia, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.nokia.pahuja.InitialFlowWriter.broadcast;


import java.util.ArrayList;
import java.util.List;

import org.nokia.pahuja.utils.GenericTransactionUtils;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.Uri;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.OutputActionCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.action.output.action._case.OutputActionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.list.Action;
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.list.ActionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.FlowCapableNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.OutputPortValues;
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.types.rev131018.BucketId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.types.rev131018.GroupId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.types.rev131018.GroupTypes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.types.rev131018.group.BucketsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.types.rev131018.group.buckets.Bucket;
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.types.rev131018.group.buckets.BucketBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.types.rev131018.group.buckets.BucketKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.types.rev131018.groups.Group;
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.types.rev131018.groups.GroupBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.types.rev131018.groups.GroupKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeUpdated;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;


public class FloodGroup {

	public void addGroup(NodeId nodeId , NodeUpdated notification, DataBroker dataBroker){

		/* Group has 3 things (apart from counters):
		 * 	1) Group ID
		 *  2) State
		 *  3) Action Bucket
		 *  Construct all 3
		 */


    	// Create Buckets :
		BucketsBuilder bucketsBuilder = new BucketsBuilder();

		// create List of Bucket
		BucketBuilder bucketBuilder = new BucketBuilder();
		BucketId bucketId = new BucketId((long)1);
		List<Bucket> bucketList = new ArrayList();


		bucketBuilder.setBucketId(bucketId);
		bucketBuilder.setKey(new BucketKey(bucketId));

		// put actions to the bucket :

		// List of Actions
		List<Action> actionList = new ArrayList<Action>();
		ActionBuilder abFlood= new ActionBuilder();
    	OutputActionBuilder outputActionBuilderFlood = new OutputActionBuilder();
    	Uri flood = new Uri(OutputPortValues.FLOOD.toString());
    	outputActionBuilderFlood.setOutputNodeConnector(flood);
    	abFlood.setAction(new OutputActionCaseBuilder().setOutputAction(outputActionBuilderFlood.build()).build());
    	abFlood.setOrder(0);

    	actionList.add(abFlood.build());

    	bucketBuilder.setAction(actionList);
    	bucketList.add(bucketBuilder.build());
    	bucketsBuilder.setBucket(bucketList);

    	// Create Group:

		GroupBuilder group = new GroupBuilder();

		GroupId grpId= new GroupId((long)4);

		group.setGroupId(grpId);
		group.setGroupName("Check");
		group.setBarrier(false);
		group.setKey(new GroupKey(grpId));
		group.setGroupType(GroupTypes.GroupAll);
		group.setBuckets(bucketsBuilder.build());

	   	// now add to the datastore


		// for this the first step shall be identifying the node of the data tree where the flow has to be added
    	// i.e. instance identifier

		GroupKey groupKey = new GroupKey(grpId);

		InstanceIdentifier<Group> groupIID = InstanceIdentifier.create(Nodes.class)
                .child(Node.class, new NodeKey(nodeId))
                .augmentation(FlowCapableNode.class)
                .child(Group.class,  group.getKey());

           //now the actual writing
         try{
        	 GenericTransactionUtils.writeData(dataBroker, LogicalDatastoreType.CONFIGURATION, groupIID, group.build(), true);
        	 System.out.println("Group Table written");
         }
         catch (Exception e){
           	 System.out.println("error in writing group table" + e);
         }
           return;
	}

}
