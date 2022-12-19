/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.node.data;

import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.Nodes;

public abstract class DataNode extends Node {
	public DataNode(){
	}

	@Override
	public int getColor() {
		return 0xFF44adc9;
	}

	@Override
	public Nodes.NodeCategory[] getCategories() {
		return new Nodes.NodeCategory[]{Nodes.NodeCategory.DATA};
	}
}
