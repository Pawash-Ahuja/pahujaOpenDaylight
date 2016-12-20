package org.nokia.pahuja.InitialFlowWriter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.nokia.pahuja.utils.GenericTransactionUtils;
import org.opendaylight.controller.liblldp.LLDP;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.Uri;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev100924.MacAddress;
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
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.FlowCookie;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.OutputPortValues;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.flow.InstructionsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.flow.MatchBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.ApplyActionsCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.apply.actions._case.ApplyActionsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.Instruction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.InstructionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeUpdated;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.l2.types.rev130827.EtherType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.ethernet.match.fields.EthernetDestinationBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.ethernet.match.fields.EthernetType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.ethernet.match.fields.EthernetTypeBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.match.EthernetMatchBuilder;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yangtools.yang.binding.DataContainer;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;



public class LlldpFlow {

	public void addFlow(NodeId nodeId , NodeUpdated notification, DataBroker dataBroker){



		MatchBuilder matchBuilder = new MatchBuilder();
    	EthernetMatchBuilder ethernetMatchBuilder = new EthernetMatchBuilder();
    	EthernetTypeBuilder ethernetTypeBuilder = new EthernetTypeBuilder();
     	ethernetTypeBuilder.setType(new EtherType(Long.valueOf(35020)));
     	ethernetMatchBuilder.setEthernetType(ethernetTypeBuilder.build());
     	matchBuilder.setEthernetMatch(ethernetMatchBuilder.build());

    	InstructionsBuilder isb = new InstructionsBuilder();
    	InstructionBuilder ib = new InstructionBuilder();
    	List<Instruction> instructions = new ArrayList<Instruction>();


    	ApplyActionsBuilder applyActionsBuilder= new ApplyActionsBuilder();
    	List<Action> actionList = new ArrayList<Action>();

    	// Action 1 -> send to Controller
    	ActionBuilder ab= new ActionBuilder();
    	OutputActionBuilder outputActionBuilderContoller = new OutputActionBuilder();
    	Uri controller = new Uri(OutputPortValues.CONTROLLER.toString());
    	outputActionBuilderContoller.setOutputNodeConnector(controller);
    	ab.setAction(new OutputActionCaseBuilder().setOutputAction(outputActionBuilderContoller.build()).build());
    	ab.setOrder(0);
    	ab.setKey(new ActionKey(0));

    	actionList.add(ab.build());

    	//apply the actions
    	applyActionsBuilder.setAction(actionList);

    	// now wrap it into instructions


    	ib.setInstruction(new ApplyActionsCaseBuilder().setApplyActions(applyActionsBuilder.build()).build());
    	ib.setOrder(0);
    	instructions.add(ib.build());
    	instructions.add(ib.build());
    	isb.setInstruction(instructions);

    	//after wrapping now add the instructions to the flow



    	FlowBuilder flow = new FlowBuilder();
    	FlowId flowId = new FlowId("900");
    	//flow.setCookie(new FlowCookie(106));

    	flow.setFlowName("LLDP");
    	flow.setIdleTimeout(0);
    	flow.setHardTimeout(0);
    	flow.setStrict(false);
    	flow.setTableId((short)1);
    	flow.setCookie(new FlowCookie(BigInteger.valueOf(900)));
    	flow.setPriority(10000);
    	flow.setInstructions(isb.build());
    	flow.setKey(new FlowKey(flowId));
    	flow.setMatch(matchBuilder.build());

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
           GenericTransactionUtils.writeData(dataBroker, LogicalDatastoreType.CONFIGURATION, flowIID, flow.build(), true);
           }
           catch (Exception e){
           	System.out.println("error in writing LLDP flow" + e);
           }
           System.out.println("LLDP Flow written");
           return;
	}

}
