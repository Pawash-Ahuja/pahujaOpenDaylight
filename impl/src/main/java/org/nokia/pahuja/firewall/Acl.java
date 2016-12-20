package org.nokia.pahuja.firewall;

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
import org.opendaylight.yang.gen.v1.urn.opendaylight.action.types.rev131112.action.list.ActionKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.FlowCapableNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.FlowId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.Table;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.TableKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.Flow;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.FlowBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.FlowKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.OutputPortValues;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.flow.InstructionsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.ApplyActionsCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.apply.actions._case.ApplyActionsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.Instruction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.InstructionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.TransmitPacketInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.TransmitPacketInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.packet.received.MatchBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

import com.google.common.base.Preconditions;

public class Acl {


    public void addFlow(NodeId nodeId, NodeConnectorId ingressNodeConnectorId, DataBroker dataBroker) {

    	/* Programming a flow involves:
    	 * 1. Creating a Flow object that has a match and a list of instructions,
    	 * 2. Adding Flow object as an augmentation to the Node object in the inventory.
    	 * 3. FlowProgrammer module of OpenFlowPlugin will pick up this data change and eventually program the switch.
    	 */

    	MatchBuilder matchBuilder = new MatchBuilder();


    	//check for arp: if arp call arp class


    	//add flow to flood the packet with priority 100 in case of arp
    	InstructionBuilder ib = new InstructionBuilder();
    	List<Instruction> instructions = new ArrayList<Instruction>();
    	InstructionsBuilder isb = new InstructionsBuilder();


    	ApplyActionsBuilder applyActionsBuilder= new ApplyActionsBuilder();
    	List<Action> actionList = new ArrayList<Action>();
    	ActionBuilder ab= new ActionBuilder();
    	OutputActionBuilder outputActionBuilder = new OutputActionBuilder();
    	Uri flood = new Uri(OutputPortValues.FLOOD.toString());
    	outputActionBuilder.setOutputNodeConnector(flood);

    	/*FloodActionBuilder floodActionBuilder = new FloodActionBuilder();
    	FloodActionCaseBuilder floodActionCaseBuilder = new FloodActionCaseBuilder();*/
    	//floodActionCaseBuilder.setFloodAction(floodActionBuilder.build());

    	ab.setAction(new OutputActionCaseBuilder().setOutputAction(outputActionBuilder.build()).build());
    	ab.setOrder(0);
    	ab.setKey(new ActionKey(0));
    	actionList.add(ab.build()); // now action list is ready

    	//apply the actions
    	applyActionsBuilder.setAction(actionList);

    	// now wrap it into instructions
    	ib.setInstruction(new ApplyActionsCaseBuilder().setApplyActions(applyActionsBuilder.build()).build());
    	ib.setOrder(0);
    	instructions.add(ib.build());
    	isb.setInstruction(instructions);

    	//after wrapping now add the instructions to the flow



    	FlowBuilder flow = new FlowBuilder();
    	FlowId flowId = new FlowId("3");
    	//flow.setCookie(new FlowCookie(106));
    	flow.setFlowName("flood");
    	flow.setIdleTimeout(0);
    	flow.setHardTimeout(0);
    	flow.setStrict(false);
    	flow.setTableId((short) 0);
    	flow.setPriority(100);
    	flow.setInstructions(isb.build());
    	flow.setKey(new FlowKey(flowId));



    	// now add to the datastore
    	System.out.println(flow.toString());
    	// for this the first step shall be identifying the node of the data tree where the flow has to be added
    	// i.e. instance identifier

    	   @SuppressWarnings("deprecation")
    	   InstanceIdentifier<Flow> flowIID = InstanceIdentifier.builder(Nodes.class)
                .child(Node.class, new NodeKey(nodeId))
                .augmentation(FlowCapableNode.class)
                .child(Table.class, new TableKey(flow.getTableId()))
                .child(Flow.class, flow.getKey())
                .build();

    	   System.out.println("Flow ready");
           //now the actual writing
           try{
           GenericTransactionUtils.writeData(dataBroker, LogicalDatastoreType.CONFIGURATION, flowIID, flow.build(), true);
           }
           catch (Exception e){
           	System.out.println("error in read write" + e);
           }
           System.out.println("Flow written");
           return;

    }
}
