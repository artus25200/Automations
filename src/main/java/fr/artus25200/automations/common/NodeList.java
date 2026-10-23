/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.artus25200.automations.client.gui.widget.ConnectionWidget;
import fr.artus25200.automations.client.gui.widget.InputWidget;
import fr.artus25200.automations.client.gui.widget.NodeWidget;
import fr.artus25200.automations.client.gui.widget.OutputWidget;
import fr.artus25200.automations.common.node.Connection;
import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.Output;

public class NodeList implements Serializable {
	public transient LinkedHashMap<Input, InputWidget> inputs = new LinkedHashMap<>();
	public transient LinkedHashMap<Output, OutputWidget> outputs = new LinkedHashMap<>();
	public transient LinkedHashMap<Node, NodeWidget> nodes = new LinkedHashMap<>();
    public transient LinkedHashMap<Connection, ConnectionWidget> connections = new LinkedHashMap<>();

    public ArrayList<Node> savedNodes = new ArrayList<>();

	public NodeList(){
    }

    public void initialize(){
        inputs = new LinkedHashMap<>();
        outputs = new LinkedHashMap<>();
        nodes = new LinkedHashMap<>();
        connections = new LinkedHashMap<>();
    }

	public Node getNode(NodeWidget nodeWidget){
		for(Map.Entry<Node, NodeWidget> entry : nodes.entrySet()){
			if(entry.getValue() == nodeWidget){
				return entry.getKey();
			}
		}
		return null;
	}
}
