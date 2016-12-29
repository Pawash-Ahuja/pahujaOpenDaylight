/*
 * Copyright © 2015 Nokia, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.nokia.pahuja.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.nokia.pahuja.InitialFlowWriter.InitialStaticFlowWriter;
import org.nokia.pahuja.macLearning.MacTable;
import org.nokia.pahuja.topologyManager.NetworkTopology;
import org.nokia.pahuja.topologyManager.NodeStats;
import org.nokia.pahuja.utils.InventoryUtils;
import org.opendaylight.controller.liblldp.Ethernet;
import org.opendaylight.controller.liblldp.LLDP;
import org.opendaylight.controller.liblldp.LLDPTLV;
import org.opendaylight.controller.liblldp.NetUtils;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataChangeListener;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.AsyncDataChangeEvent;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RpcRegistration;
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.opendaylight.controller.sal.binding.api.NotificationProviderService;
import org.opendaylight.controller.sal.binding.api.NotificationService;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yangtools.yang.binding.DataContainer;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev100924.MacAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnector;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.FlowCapableNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.FlowCapableNodeConnectorUpdated;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.FlowId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.TableKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.FlowAdded;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.FlowRemoved;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.FlowUpdated;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.Table;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.Table;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.TableKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.TableKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.Flow;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.Flow;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.FlowBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.FlowBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.FlowKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.FlowKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.NodeErrorNotification;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.NodeExperimenterErrorNotification;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.RemoveFlowInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.SalFlowListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.SalFlowService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.service.rev130819.SwitchFlowRemoved;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.topology.discovery.rev130819.FlowTopologyDiscoveryListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.topology.discovery.rev130819.LinkDiscovered;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.topology.discovery.rev130819.LinkOverutilized;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.topology.discovery.rev130819.LinkRemoved;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.topology.discovery.rev130819.LinkUtilizationNormal;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.port.rev130925.PortNumberUni;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.FlowCookie;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRemoved;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorUpdated;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRemoved;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeUpdated;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.OpendaylightInventoryListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnectorKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.ethernet.match.fields.EthernetDestination;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.ethernet.match.fields.EthernetSource;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketReceived;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.controller.config.rev130405.ServiceType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.controller.md.sal.binding.impl.rev131028.modules.module.configuration.binding.broker.impl.binding.broker.impl.NotificationPublishService;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.NotificationListener;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.CheckedFuture;

import org.opendaylight.yangtools.concepts.Registration;
public class PahujaProvider implements BindingAwareProvider, AutoCloseable, PacketProcessingListener, OpendaylightInventoryListener, SalFlowListener, FlowTopologyDiscoveryListener, DataChangeListener {

    private static final Logger LOG = LoggerFactory.getLogger(PahujaProvider.class);
    private ListenerRegistration<NotificationListener> listener;
    private List<Registration> registrations;
	private DataBroker dataBroker;
	private PacketProcessingService packetProcessingService;
	private SalFlowService salFlowService;

	public PahujaProvider(NotificationProviderService notificaitonService, RpcProviderRegistry rpcProviderRegistry, DataBroker dataBroker ) {
		// TODO Auto-generated constructor stub

		// Store DataBroker for reading / writing from inventory store
		this.dataBroker = dataBroker;

		// Get access to the packet processing service for making RPC calls
		this.packetProcessingService = rpcProviderRegistry.getRpcService(PacketProcessingService.class);

		//List of registrations to track notifications for both data changed and yang defined registrations
		this.registrations = Lists.newArrayList();

		// Register to receive notifications in case of events
		// first event - packet in
		registrations.add(notificaitonService.registerNotificationListener(this));

		 //Object used for flow programming through RPC calls
		this.salFlowService = rpcProviderRegistry.getRpcService(SalFlowService.class);
}
	@Override
    public void onSessionInitiated(ProviderContext session) {
        LOG.info("PahujaProvider Session Initiated");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("Nokia Controller started!!!");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

//      DataBroker db = session.getSALService(DataBroker.class);

    }

    @Override
    public void close() throws Exception {
        LOG.info("PahujaProvider Closed");
        System.out.println("Bye");
        this.listener.close();
        for (Registration registration : registrations) {
        	registration.close();
        }
        registrations.clear();

    }
    @Override
    public  void onPacketReceived(PacketReceived notification) {

       // System.out.println("packet received");
    	LOG.trace("Received packet notification {}", notification.getMatch());

    	//whenever there is a packet in event, learn MAC address
		// extract payload
		byte[] payload = notification.getPayload();

        //extract Src Mac Address and Dest MAC address
        byte[] srcMacRaw = Arrays.copyOfRange(payload, 6, 12);

        // MAC address --> byte to string
        StringBuilder srcMac = new StringBuilder();
        if (srcMacRaw != null && srcMacRaw.length == 6){
        	for (byte i : srcMacRaw){

        		srcMac.append(String.format(":%02X", i));
        	}
        }
        String srcMAC = srcMac.substring(1);

        // check for LLDP packets
        byte[] etherTypeRaw = Arrays.copyOfRange(payload, 12, 14);
        int etherType = (0x0000ffff & ByteBuffer.wrap(etherTypeRaw).getShort());

        if (etherType == 0x88cc){
        	// LLDP packet received

        	Ethernet ethPkt = new Ethernet();
        	    try {
        	        ethPkt.deserialize(payload, 0,payload.length * NetUtils.NumBitsInAByte);
        	       // System.out.println(ethPkt);
        	    } catch (Exception e) {
        	        LOG.warn("Failed to decode LLDP packet {}", e);
        	    }


        	 // Self Details:

        	 String selfNode =  notification.getIngress().getValue().firstKeyOf(NodeConnector.class).getId().getValue().toString();
        	 String selfNodeName = selfNode.substring(0,10);

        	 String selfNodePortValue = selfNode.substring(11,12);



        	 // Neighbor Details:
        	 LLDP lldpPakcet = (LLDP) ethPkt.getPayload();
             try {
				String neighborNodePortValue = new String(lldpPakcet.getPortId().getValue(), "UTF-8");

				String neighborNodeName = new String(lldpPakcet.getSystemNameId().getValue(), "UTF-8");


				new NetworkTopology().addNodeConnector(selfNodeName, Integer.parseInt(selfNodePortValue), neighborNodeName, Integer.parseInt(neighborNodePortValue.substring(1, 2)));

			  } catch (UnsupportedEncodingException e) {

				e.printStackTrace();
				LOG.warn("Failed to extract port Value", e);
				System.out.println("port conversion failed");
			  }
        	return;
        }

        //Src MAC address in macAdderss
        MacAddress srcMacAddress = new MacAddress(srcMAC);


        //get NodeConnectorRef from notification
        NodeConnectorRef nodeConnectorRef = notification.getIngress();

		// get NodeConnectorId (URI port no) from NodeConnectorReference
        NodeConnectorId nodeConnectorId =    InventoryUtils.getNodeConnectorId(nodeConnectorRef);

		int length = nodeConnectorId.toString().length();

		// port No from NodeConnectorId
		String portNo = nodeConnectorId.toString().substring(length-2,length-1);

    	// get NodeId from NodeConnectorRef
		NodeId nodeId = InventoryUtils.getNodeId(nodeConnectorId);

    	MacTable macTable = MacTable.getInstance();
    	macTable.macLearning( dataBroker, srcMAC, srcMacAddress, nodeConnectorId, portNo, nodeId);
    	macTable.forwardingTable( dataBroker, srcMAC, srcMacAddress, nodeConnectorId, portNo, nodeId);

        return;

    }
	@Override
	public void onNodeConnectorRemoved(NodeConnectorRemoved notification) {
		// TODO Auto-generated method stub
		//System.out.println("on node connector removed " + notification.toString());
	}
	@Override
	public void onNodeConnectorUpdated(NodeConnectorUpdated notification) {
		// TODO Auto-generated method stub

		String nodeName = notification.getId().getValue().toString().substring(0, 10);

		PortNumberUni portNoUni = notification.getAugmentation(FlowCapableNodeConnectorUpdated.class).
						getPortNumber();

		PortNumberUni obj = new PortNumberUni(portNoUni);
		int portNo = obj.getUint32().intValue();

		boolean isPortDown = notification.getAugmentation(FlowCapableNodeConnectorUpdated.class)
				.getConfiguration().isPORTDOWN();

		String portStatus = isPortDown == true ? "down" : "up";

		// remove the link from the Network Topology when the node connector disconnects
		if (portStatus == "down"){

			// remove port from Topology
			new NetworkTopology().removeNodeConnector(nodeName, portNo);

			// remove port from NodeStats

		}
		if (portStatus =="up"){

			new NodeStats().addPort(nodeName, portNo);
		}



	}

	@SuppressWarnings("deprecation")
	@Override
	public void onNodeRemoved(NodeRemoved notification) {

		// remove node from the Neighbor Topology


		System.out.println("Node Removed");
		String nodeName = notification.getNodeRef().getValue().toString().substring(308,318);
		System.out.println(nodeName);

		// remove node from Network Topology
		new NetworkTopology().removeNode(nodeName);

		// remove node from NodeStats
		new NodeStats().removeNode(nodeName);

		// remove all the flows from sal - config datastore
		removeFlowFromConfigDatastore(notification);


	}

	@Override
	public void onNodeUpdated(NodeUpdated notification) {


		System.out.println();
		System.out.println("Node UP");
		//System.out.println();


		NodeId nodeId = notification.getId();
		String nodeName = notification.getNodeRef().getValue().toString().substring(308,318);
		System.out.println(nodeName);

		//observed that this method was called twice for every node
		// hence we will maintain a list to counter any override.

		boolean result = new NetworkTopology().containsNode(nodeName);

		// Node already exists and flows already added
		if (result == true){
			return;
		}

		else{

			// add node To Network Topology
			new NetworkTopology().addNode(nodeName);

			// add node to NodeStats
			new NodeStats().addNode(nodeName);

			RemoveFlowInputBuilder flowBuilder = new RemoveFlowInputBuilder();
			flowBuilder.setBarrier(true);
			flowBuilder.setNode(notification.getNodeRef());

			salFlowService.removeFlow(flowBuilder.build());

			// add static flows
			new InitialStaticFlowWriter().addStaticFlows(nodeId , notification, dataBroker);


		}
	}

	private void removeFlowFromConfigDatastore(NodeRemoved notification){

		 Preconditions.checkNotNull(dataBroker);
		 NodeRef nodeRef = notification.getNodeRef();

		// System.out.println("node updated: node ID is " + nodeId);

		 // Node identification on the datstore tree
		 InstanceIdentifier<Node> node = notification.getNodeRef().getValue().firstIdentifierOf(Node.class);

		 // write on the configurational datastore to remove Flows

		 WriteTransaction removeFlows = dataBroker.newWriteOnlyTransaction();
		 removeFlows.delete(LogicalDatastoreType.CONFIGURATION, node);


		 CheckedFuture<Void, TransactionCommitFailedException> future = removeFlows.submit();

		 try{
			 future.checkedGet();
			// System.out.println("Conf Flows removed");
		 }

		 catch(TransactionCommitFailedException e){

			 System.out.println("Cannot remove flows from conf datastore");
		 }

		 return;

	}

	private void removeFlowFromConfigDatastore(SwitchFlowRemoved notification , String MAC) {
		// TODO Auto-generated method stub

		Preconditions.checkNotNull(dataBroker);

   	//flow.setCookie(new FlowCookie(106));
   	NodeRef nodeRef = notification.getNode();

   //	System.out.println("notifiation.getNode: " + notification.getNode());
   //	System.out.println("notifiation.geNode().getValue(): " + notification.getNode().getValue());
   //	System.out.println(" value path " +nodeRef.getValue().getPath());


   	NodeId nodeId = notification.getNode().getValue().firstKeyOf(Node.class).getId();


   	//get Node from nodeId
   	//Node node = new NodeBuilder().setId(nodeId).setKey(new NodeKey(nodeId)).build();

   	try{
   		// get Table key
   		TableKey tableKey = new TableKey(notification.getTableId());

   		// get Node instance from nodeId
   		InstanceIdentifier<Node> node = InstanceIdentifier.builder(Nodes.class)
   				.child(Node.class, new NodeKey(nodeId))
   				.build();

   		//get table Instance
   		InstanceIdentifier<Table> table = node.builder()
   				.augmentation(FlowCapableNode.class)
   				.child(Table.class, tableKey)
   				.build();

   		// get FlowID from match
       	//FlowId flowId = new FlowId(notification.getMatch().getEthernetMatch().getEthernetSource().toString());


       	//System.out.println();
       //	System.out.println(" in remove flow from config: MAC is " + MAC);

       	FlowId flowId = new FlowId(MAC);


       	//get flow instance

   		InstanceIdentifier<Flow> flow = table.child(Flow.class, new FlowKey(flowId));
/*
   	 InstanceIdentifier<Flow> flowIID = InstanceIdentifier.builder(Nodes.class)
	             .child(Node.class, notification.getNode().getValue().firstKeyOf(Node.class))
	             .augmentation(FlowCapableNode.class)
	             .child(Table.class, new TableKey(notification.getTableId()))
	             .child(Flow.class, new FlowKey(flowID))
	             .toInstance();
*/

   		WriteTransaction removeFlows = dataBroker.newWriteOnlyTransaction();
		removeFlows.delete(LogicalDatastoreType.CONFIGURATION, flow);


		CheckedFuture<Void, TransactionCommitFailedException> future = removeFlows.submit();

		try{
			future.checkedGet();
			//System.out.println("Timeout flows removed from conf datastore");
		}

		catch(TransactionCommitFailedException e){

			System.out.println("Cannot remove timeout flows from conf datastore");
		}
   	}
  	 catch (Exception e){

  		System.out.println("exception in writing flow " + e);
  	}

		 return;


	}
	@Override
	public void onFlowAdded(FlowAdded arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onFlowRemoved(FlowRemoved arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onFlowUpdated(FlowUpdated arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onNodeErrorNotification(NodeErrorNotification arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onNodeExperimenterErrorNotification(NodeExperimenterErrorNotification arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onSwitchFlowRemoved(SwitchFlowRemoved notification) {

		// Flow removed from switch-->
		// if the reason is timeout, delete it from config datastore
		//System.out.println(notification.getRemovedReason());
		//notification.getRemovedReason().isDELETE()

		//System.out.println("SwitchFlowRemoved");
		// On flow removal because of timeout, it will notify here
		// remove from config datastore checking the cookie if it is
		if (notification.getRemovedReason().isIDLETIMEOUT()){

			// this means the flow is removed because of idle timeout
			// now remove the corresponding flow config datastore.
    		String MAC = "";

    		if (notification.getCookie().equals(new FlowCookie(BigInteger.valueOf(200)))){
    			EthernetSource srcMac = notification.getMatch().getEthernetMatch().getEthernetSource();
            	// got source MAC address
    			// table 2
            	MAC = srcMac.getAddress().toString().substring(19,36);
    		}

    		if (notification.getCookie().equals(new FlowCookie(BigInteger.valueOf(400)))){
    			EthernetDestination destMac = notification.getMatch().getEthernetMatch().getEthernetDestination();
            	// got source MAC address
    			// table 1
            	MAC = destMac.getAddress().toString().substring(19,36);
    		}


			removeFlowFromConfigDatastore(notification , MAC);
		}
	}
	@Override
	public void onLinkDiscovered(LinkDiscovered arg0) {
		// TODO Auto-generated method stub
		//System.out.println("Link Discovered");
	}
	@Override
	public void onLinkOverutilized(LinkOverutilized arg0) {
		// TODO Auto-generated method stub
		System.out.println("over utilized");
	}
	@Override
	public void onLinkRemoved(LinkRemoved arg0) {
		// TODO Auto-generated method stub
		//System.out.println("on Link Removed");
	}
	@Override
	public void onLinkUtilizationNormal(LinkUtilizationNormal arg0) {
		// TODO Auto-generated method stub
		System.out.println("on Link Utilization normal");
	}
	@Override
	public void onDataChanged(AsyncDataChangeEvent<InstanceIdentifier<?>, DataObject> arg0) {
		// TODO Auto-generated method stub

		System.out.println("DataChanged " + arg0);

	}


}
