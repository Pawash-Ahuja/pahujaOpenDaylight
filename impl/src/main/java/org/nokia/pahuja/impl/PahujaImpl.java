/*
* Copyright © 2015 Nokia, Inc. and others. All rights reserved.
*
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*/

/*package org.nokia.pahuja.impl;

import java.util.concurrent.Future;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketReceived;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pahujahellorpc.rev150105.HelloWorldInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pahujahellorpc.rev150105.HelloWorldOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pahujahellorpc.rev150105.HelloWorldOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.pahujahellorpc.rev150105.PahujaHelloRpcService;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yangtools.yang.binding.DataContainer;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PahujaImpl implements PahujaHelloRpcService  {

	private DataBroker db;
	private static final Logger LOG = LoggerFactory.getLogger(PahujaImpl.class);


	public PahujaImpl() {
		// TODO Auto-generated constructor stub
		//this.db= db;
	}
	@Override
	public Future<RpcResult<HelloWorldOutput>> helloWorld(HelloWorldInput input) {
		// TODO Auto-generated method stub

		 String name = input.getName();
		 HelloWorldOutput output = new HelloWorldOutputBuilder()
		            .setGreating("Hello " + name)
		            .build();
		 return RpcResultBuilder.success(output).buildFuture();

	}



}
*/