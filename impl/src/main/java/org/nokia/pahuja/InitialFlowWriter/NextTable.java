package org.nokia.pahuja.InitialFlowWriter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.nokia.pahuja.utils.GenericTransactionUtils;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.FlowCapableNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.FlowId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.Table;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.TableKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.Flow;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.FlowBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.FlowKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.FlowCookie;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.FlowModFlags;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.flow.InstructionsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.GoToTableCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.go.to.table._case.GoToTableBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.Instruction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.InstructionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.InstructionKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeUpdated;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

import com.google.common.base.Preconditions;


public class NextTable {

	public void addFlow(NodeId nodeId, NodeUpdated notification, DataBroker dataBroker) {
		// TODO Auto-generated method stub




		InstructionsBuilder isb = new InstructionsBuilder();
    	InstructionBuilder ib = new InstructionBuilder();
    	List<Instruction> instructions = new ArrayList<Instruction>();

    	ib.setInstruction(new GoToTableCaseBuilder().setGoToTable(new GoToTableBuilder().setTableId(Short.valueOf("1")).build()).build())
    	.setKey(new InstructionKey(0)).build();


    	instructions.add(ib.build());
    	isb.setInstruction(instructions);

    	//after wrapping now add the instructions to the flow

    	FlowBuilder flow = new FlowBuilder();
    	FlowId flowId = new FlowId("NextTable");
      	flow.setCookie(new FlowCookie(BigInteger.valueOf(300)));
    	flow.setFlowName("NextTable");
    	flow.setBarrier(true);
    	flow.setIdleTimeout(0);
    	flow.setHardTimeout(0);
    	flow.setStrict(false);
    	flow.setTableId((short)0);
    	flow.setPriority(105);
    	flow.setInstructions(isb.build());
    	flow.setKey(new FlowKey(flowId));
    	flow.setFlags(new FlowModFlags(true, false, false, false, true));


    	// now add to the datastore

    	// for this the first step shall be identifying the node of the data tree where the flow has to be added
    	// i.e. instance identifier

    	   @SuppressWarnings("deprecation")
    	   InstanceIdentifier<Flow> flowIID = InstanceIdentifier.builder(Nodes.class)
                .child(Node.class, new NodeKey(nodeId))
                .augmentation(FlowCapableNode.class)
                .child(Table.class, new TableKey(flow.getTableId()))
                .child(Flow.class, flow.getKey())
                .build();


           //now the actual writing
           try{

        	 /*  Preconditions.checkNotNull(dataBroker);
        	   WriteTransaction addFlows = dataBroker.newWriteOnlyTransaction();
        	   */


           GenericTransactionUtils.writeData(dataBroker, LogicalDatastoreType.CONFIGURATION, flowIID, flow.build(), true);
           }
           catch (Exception e){
        	   System.out.println("error in writing flow" + e);
           }
          System.out.println("Next Table written");
           return;

	}


}
