/*
 * Copyright © 2015 Nokia, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.nokia.pahuja.impl;

import java.util.List;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataChangeListener;

import org.opendaylight.controller.md.sal.common.api.data.AsyncDataChangeEvent;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RpcRegistration;
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.opendaylight.controller.sal.binding.api.NotificationProviderService;
import org.opendaylight.controller.sal.binding.api.NotificationService;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketReceived;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.NotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import org.opendaylight.yangtools.concepts.Registration;
public class PahujaProvider implements BindingAwareProvider, AutoCloseable, PacketProcessingListener {

    private static final Logger LOG = LoggerFactory.getLogger(PahujaProvider.class);
    private ListenerRegistration<NotificationListener> listener;
    private List<Registration> registrations;
	private DataBroker dataBroker;
	private PacketProcessingService packetProcessingService;

   public PahujaProvider(DataBroker dataBroker, NotificationProviderService notificaitonService, RpcProviderRegistry rpcProviderRegistry) {
	// TODO Auto-generated constructor stub

	// Store DataBroker for reading/ writing from inventory store
	this.dataBroker = dataBroker;

	// Get access to the packet processing service for making RPC calls
	this.packetProcessingService = rpcProviderRegistry.getRpcService(PacketProcessingService.class);

	//List of registrations to track notifications for both data changed and yand defined registrations
	this.registrations = Lists.newArrayList();

	// Register to recieve notificaitons in case of events
	// first event - packet in
	registrations.add(notificaitonService.registerNotificationListener(this));


}
	@Override
    public void onSessionInitiated(ProviderContext session) {
        LOG.info("PahujaProvider Session Initiated");
        System.out.println("Hiiiiiiii!");
//      DataBroker db = session.getSALService(DataBroker.class);



    }

    @Override
    public void close() throws Exception {
        LOG.info("PahujaProvider Closed");
        System.out.println("Bye");
        this.listener.close();

    }
    @Override
    public  void onPacketReceived(PacketReceived packet) {
	LOG.debug("reveived the packet :" + packet.toString());
        System.out.println("packet received");


	}
}
