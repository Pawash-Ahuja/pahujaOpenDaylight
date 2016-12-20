/*
 * Copyright © 2015 Nokia, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pahuja.impl.rev141210;



//import org.nokia.pahuja.impl.L2forwarding;
//import org.nokia.pahuja.impl.PahujaImpl;
import org.nokia.pahuja.impl.PahujaProvider;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.sal.binding.api.NotificationProviderService;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.controller.md.sal.core.general.entity.rev150820.GeneralEntityData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PahujaModule extends org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pahuja.impl.rev141210.AbstractPahujaModule {
	private static final Logger LOG = LoggerFactory.getLogger(PahujaModule.class);
    public PahujaModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public PahujaModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pahuja.impl.rev141210.PahujaModule oldModule, java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {

       	//DataBroker dataBroker = (DataBroker) getDataBrokerDependency();
    	RpcProviderRegistry rpcRegistry = getRpcRegistryDependency();
    	NotificationProviderService notificationService = getNotificationServiceDependency();
    	DataBroker dataBroker = getDataBrokerDependency();

        PahujaProvider provider = new PahujaProvider(notificationService, rpcRegistry,dataBroker);

        getBrokerDependency().registerProvider(provider);
        return provider;



    }

}
