package org.nokia.pahuja.macLearning;

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
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.RemoveFlowInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.SalFlowService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.FlowCookie;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.FlowModFlags;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.flow.InstructionsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.flow.MatchBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.ApplyActionsCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.GoToTableCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.apply.actions._case.ApplyActionsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.go.to.table._case.GoToTableBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.Instruction;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.InstructionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.InstructionKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRemoved;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorUpdated;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRemoved;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeUpdated;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnector;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnectorKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.ethernet.match.fields.EthernetDestinationBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.ethernet.match.fields.EthernetSourceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.match.EthernetMatchBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketReceived;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

import com.google.common.base.Preconditions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.nokia.pahuja.utils.GenericTransactionUtils;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.Uri;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev100924.MacAddress;


//created a singleton Class MacTable
public class MacTable {

	private MacTable(){

	}

	//private static HashMap<NodeRef, HashMap<MacAddress, String> > listMacTable = new HashMap<NodeRef, HashMap<MacAddress, String> >();
	private static MacTable macTable = new MacTable();


	public static MacTable getInstance(){
		return macTable;
	}

	// add flow for src mAC - port matching
	//+
	// add flow for dest MAC - port mapping

	public void macLearning(DataBroker dataBroker, String srcMAC, MacAddress srcMacAddress, NodeConnectorId nodeConnectorId, String portNo, NodeId nodeId){

		// extract MAC address + Node Ref + Port No

		// Flow 1
		// Now, when we have Src MAC to port mapping, write this as a flow in
		// tabl1e 1 - if match go to table 2, else do nothing

		MatchBuilder matchBuilder = new MatchBuilder();

    	EthernetMatchBuilder ethernetMatchBuilder = new EthernetMatchBuilder();

    	EthernetSourceBuilder ethernetSourceBuilder = new EthernetSourceBuilder();
    	ethernetSourceBuilder.setAddress(srcMacAddress);
    	ethernetMatchBuilder.setEthernetSource(ethernetSourceBuilder.build());

    	matchBuilder.setInPort(nodeConnectorId);
    	matchBuilder.setEthernetMatch(ethernetMatchBuilder.build());

    	//add flow with priority 1000

    	InstructionsBuilder isb = new InstructionsBuilder();
    	InstructionBuilder ib = new InstructionBuilder();
    	List<Instruction> instructions = new ArrayList<Instruction>();


    	ib.setInstruction(new GoToTableCaseBuilder().setGoToTable(new GoToTableBuilder().setTableId(Short.valueOf("2")).build()).build())
    	.setKey(new InstructionKey(0)).build();

    	// now wrap it into instructions

    	instructions.add(ib.build());
    	isb.setInstruction(instructions);

    	//after wrapping -- now add the instructions to the flow


    	FlowBuilder flow = new FlowBuilder();
    	FlowId flowId = new FlowId(srcMAC);
    	//flow.setCookie(new FlowCookie(200));
    	FlowModFlags flowRemovedNotification = new FlowModFlags(true, false, false, false, true);


    	flow.setFlowName("MacLearning");
    	flow.setIdleTimeout(60); //
    	flow.setHardTimeout(0); //
    	flow.setStrict(false);
    	flow.setTableId((short)1);
    	//flow.setFlags(FlowModFlags.getDefaultInstance("sENDFLOWREM"));
       	flow.setCookie(new FlowCookie(BigInteger.valueOf(200)));
    	flow.setPriority(1000);
    	flow.setInstructions(isb.build());
    	flow.setKey(new FlowKey(flowId));
    	flow.setMatch(matchBuilder.build());
    	flow.setFlags(flowRemovedNotification);

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
           	System.out.println("error in writing Mac flow" + e);
           }
           //System.out.println("MAC flow written: "+srcMAC);
           return;


	}



   public void forwardingTable(DataBroker dataBroker, String srcMAC, MacAddress srcMacAddress, NodeConnectorId nodeConnectorId, String portNo, NodeId nodeId){

        //Flow 2 - For Forwarding Table --> MAC address to port mapping


        MatchBuilder matchBuilder = new MatchBuilder();

       	EthernetMatchBuilder ethernetmatchBuilder = new EthernetMatchBuilder();

       	EthernetDestinationBuilder ethernetDestinationBuilder = new EthernetDestinationBuilder();

       	ethernetDestinationBuilder.setAddress(srcMacAddress);

       	ethernetmatchBuilder.setEthernetDestination(ethernetDestinationBuilder.build());

       	matchBuilder.setEthernetMatch(ethernetmatchBuilder.build());



       	//add flow with priority 1000

       	InstructionsBuilder isb = new InstructionsBuilder();
       	InstructionBuilder ib = new InstructionBuilder();
       	List<Instruction> instructions = new ArrayList<Instruction>();



       	ApplyActionsBuilder applyActionsBuilder= new ApplyActionsBuilder();
       	List<Action> actionList = new ArrayList<Action>();


       	// action to Flood the Frame
       	ActionBuilder ab= new ActionBuilder();
       	OutputActionBuilder outputActionBuilder= new OutputActionBuilder();
       	Uri outputPort = new Uri(portNo);
       	outputActionBuilder.setOutputNodeConnector(outputPort);
       	ab.setAction(new OutputActionCaseBuilder().setOutputAction(outputActionBuilder.build()).build());
       	ab.setOrder(0);
       	ab.setKey(new ActionKey(0));



       	// now action list is ready
       	actionList.add(ab.build());

       	//apply the actions
       	applyActionsBuilder.setAction(actionList);

       	// now wrap it into instructions
       	ib.setInstruction(new ApplyActionsCaseBuilder().setApplyActions(applyActionsBuilder.build()).build());
       	ib.setOrder(0);
       	instructions.add(ib.build());
       	isb.setInstruction(instructions);

       	//after wrapping now add the instructions to the flow

       	// Convert MAC address to String


       	FlowBuilder flow = new FlowBuilder();
       	FlowId flowId = new FlowId(srcMAC);
       	flow.setCookie(new FlowCookie(BigInteger.valueOf(400)));
    	FlowModFlags flowRemovedNotification = new FlowModFlags(true, false, false, false, true);

       	flow.setFlowName("FT");
       	flow.setIdleTimeout(60);
       	flow.setHardTimeout(0);
       	flow.setStrict(false);
       	flow.setTableId((short)2);
       	flow.setPriority(1000);
       	// for sending back notification;
        //flow.setFlags(FlowModFlags.getDefaultInstance("sENDFLOWREM"));
       	flow.setInstructions(isb.build());
       	flow.setKey(new FlowKey(flowId));
       	flow.setMatch(matchBuilder.build());
       	flow.setFlags(flowRemovedNotification);


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
        	System.out.println("error in writing FT flow" + e);
        }
              //System.out.println("FT flow written: "+srcMAC);
        return;

	}
}
