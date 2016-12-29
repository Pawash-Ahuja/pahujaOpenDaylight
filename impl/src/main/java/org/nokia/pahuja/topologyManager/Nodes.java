/*
 * Copyright © 2015 Nokia, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.nokia.pahuja.topologyManager;

import java.util.HashSet;

public interface Nodes {

	public HashSet<Integer> getInternalPorts(String nodeName);
	public HashSet<Integer> getExternalPorts(String nodeName);
	public HashSet<Integer> getAllPorts(String nodeName);
	public void addPort(String nodeName, int portNo);
	public void removePort(String nodeName, int portNo);
	public void addNode(String nodeName);
	public void removeNode(String nodeName);
	public void printNodeStats();

}
