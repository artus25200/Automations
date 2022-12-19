/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common;

import fr.artus25200.automations.client.gui.widget.ConnectionWidget;
import fr.artus25200.automations.client.gui.widget.InputWidget;
import fr.artus25200.automations.client.gui.widget.NodeWidget;
import fr.artus25200.automations.client.gui.widget.OutputWidget;
import fr.artus25200.automations.common.node.Connection;
import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.Output;
import fr.artus25200.automations.common.node.events.BlockBreakEventNode;
import fr.artus25200.automations.common.node.events.ServerStartEventNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NodeWrapper implements Serializable {
	public LinkedHashMap<Input, InputWidget>           inputs      = new LinkedHashMap<>();
	public LinkedHashMap<Output, OutputWidget>         outputs     = new LinkedHashMap<>();
	public LinkedHashMap<Node, NodeWidget>             nodes       = new LinkedHashMap<>();
	public LinkedHashMap<Connection, ConnectionWidget> connections = new LinkedHashMap<>();

	// events lists
	public List<BlockBreakEventNode> blockBreakEvents = new ArrayList<>();
	public List<ServerStartEventNode> serverStartedEvents = new ArrayList<>();

	public NodeWrapper(){}

	public Node getNode(NodeWidget nw){
		for(Map.Entry<Node, NodeWidget> e : nodes.entrySet()){
			if(e.getValue() == nw){
				return e.getKey();
			}
		}
		return null;
	}
}
