INSTALLATION

#####################################################################
1. put the jars (sopremo-pawel.jar and pawel-model.jar and anna-3.3.jar 
	and index-import.jar) in the "lib" directory there where 
	the stratosphere is installed...
	also put the nephele-server-0.2.1.jar in "lib" (replace existing)

#####################################################################
2. add the both jars (sopremo-pawel.jar and pawel-model.jar and anna-3.3.jar 
		and index-import.jar) to classpath of stratosphere by editing following files:
			-> meteor-client.sh
			-> sopremo-server.sh
			-> nephele-jobmanager.sh
			-> meteor-webforntend.sh (optional)
			-> nephele-taskmanager.sh (optional)
			## add to this files following lines:
    elif [[ "$jarfile" =~ 'sopremo-pawel' ]]; then
        add=1
    elif [[ "$jarfile" =~ 'pawel-model' ]]; then
        add=1
    elif [[ "$jarfile" =~ 'anna-3.3' ]]; then
        add=1
    elif [[ "$jarfile" =~ 'index-import' ]]; then
        add=1

#####################################################################
3. in the same files that have been mentioned in [2.] modify the JVM arguments:
	-> increase the heap space from "-Xmx512m" to "-Xmx2560m"

=====================================================================	
NOTICE

** included nephele-server-0.2.1.jar has been build using stratosphere version 
	from DOPA project (0.2.1) from master-branch from 22.08.2013. If you
	newer version is required building the nephele-server.jar should be
	keeped in mind that the class "eu.stratosphere.nephele.rpc.ServerTypeUtils"
	should be modified - classes "ReutersNewsInputSplit" and "LuceneIndexInputSplit"
	should be added to the list that is created in class "ServerTyeUtils" within
	method "getRPCTypesToRegister()". (in order to build nephele-server required is
	then in nephele-server/pom.xml to add the pawel-model dependency).
	The  modified class "ServerTypeUtils" is presented at the bottom of this README.
	If using quite different stratosphere version must be always keeped in mind that
	the classes "ReutersNewsInputSplit" and "LuceneIndexInputSplit" must be registered
	in kryo:
	(To register this class use: kryo.register(pawel.model.sopremo.io.ReutersNewsInputSplit.class);)

/***********************************************************************************************************************
 *
 * Copyright (C) 2010-2013 by the Stratosphere project (http://stratosphere.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/

package eu.stratosphere.nephele.rpc;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;

import eu.stratosphere.nephele.deployment.ChannelDeploymentDescriptor;
import eu.stratosphere.nephele.deployment.GateDeploymentDescriptor;
import eu.stratosphere.nephele.deployment.TaskDeploymentDescriptor;
import eu.stratosphere.nephele.executiongraph.CheckpointState;
import eu.stratosphere.nephele.executiongraph.ExecutionVertexID;
import eu.stratosphere.nephele.fs.FileInputSplit;
import eu.stratosphere.nephele.instance.InstanceConnectionInfo;
import eu.stratosphere.nephele.instance.local.LocalInstance;
import eu.stratosphere.nephele.jobmanager.splitassigner.InputSplitWrapper;
import eu.stratosphere.nephele.taskmanager.AbstractTaskResult;
import eu.stratosphere.nephele.taskmanager.TaskCancelResult;
import eu.stratosphere.nephele.taskmanager.TaskCheckpointState;
import eu.stratosphere.nephele.taskmanager.TaskExecutionState;
import eu.stratosphere.nephele.taskmanager.TaskSubmissionResult;
import eu.stratosphere.nephele.taskmanager.routing.ConnectionInfoLookupResponse;
import eu.stratosphere.nephele.taskmanager.routing.RemoteReceiver;
import eu.stratosphere.nephele.template.GenericInputSplit;

import pawel.model.sopremo.io.ReutersNewsInputSplit;
import pawel.model.sopremo.io.LuceneIndexInputSplit;

/**
 * This utility class provides a list of types frequently used by the RPC protocols included in this package.
 * 
 * @author warneke
 */
public class ServerTypeUtils {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private ServerTypeUtils() {
	}

	/**
	 * Returns a list of types frequently used by the RPC protocols of this package and its parent packages.
	 * 
	 * @return a list of types frequently used by the RPC protocols of this package
	 */
	public static List<Class<?>> getRPCTypesToRegister() {

		final List<Class<?>> types = ManagementTypeUtils.getRPCTypesToRegister();

		types.add(AbstractTaskResult.ReturnCode.class);
		types.add(ChannelDeploymentDescriptor.class);
		types.add(CheckpointState.class);
		types.add(ConnectionInfoLookupResponse.class);
		types.add(ConnectionInfoLookupResponse.ReturnCode.class);
		types.add(ExecutionVertexID.class);
		types.add(FileInputSplit.class);
		types.add(GateDeploymentDescriptor.class);
		types.add(GenericInputSplit.class);
		types.add(HashSet.class);
		types.add(Inet4Address.class);
		types.add(InetSocketAddress.class);
		types.add(InputSplitWrapper.class);
		types.add(InstanceConnectionInfo.class);
		types.add(LocalInstance.class);
		types.add(RemoteReceiver.class);
		types.add(TaskCancelResult.class);
		types.add(TaskCheckpointState.class);
		types.add(TaskDeploymentDescriptor.class);
		types.add(TaskExecutionState.class);
		types.add(TaskSubmissionResult.class);
		
		
		/* TODO types required for HBase support -> creates runtime depedency to pact-hbase modulethis needs to be done in a better way*/
		try {
			types.add(Class.forName("eu.stratosphere.pact.common.io.TableInputSplit"));
			types.add(byte[].class);
			types.add(String[].class);

			types.add(ReutersNewsInputSplit.class);
			types.add(LuceneIndexInputSplit.class);

		} catch (ClassNotFoundException e) {
			System.out.println("Could not register class with Kryo: ");
			e.printStackTrace(System.out);
		}

		return types;
	}
}
