package org.nokia.pahuja.impl;


import java.util.List;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataChangeListener;
import org.opendaylight.controller.md.sal.common.api.data.AsyncDataChangeEvent;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketReceived;
import org.opendaylight.yangtools.concepts.Registration;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// nothing done here.. want to make this main class
public class Controller implements DataChangeListener, AutoCloseable, PacketProcessingListener{


	private List<Registration> registrations;
	private DataBroker databroker;
	private PacketProcessingService packetProcessingService;

	public Controller() {
		// TODO Auto-generated constructor stub
		System.out.println("inside L2 forwarding");
		//this.packetProcessingService = rpcProviderRegistry.getRpcService(PacketProcessingService.class);
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}
	@Override
	public void onDataChanged(AsyncDataChangeEvent<InstanceIdentifier<?>, DataObject> arg0) {
		// TODO Auto-generated method stub
		System.out.println("data changed");

	}

	@Override
	public void onPacketReceived(PacketReceived notification) {
		// TODO Auto-generated method stub

	}
}
