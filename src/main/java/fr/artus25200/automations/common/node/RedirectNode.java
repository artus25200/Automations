/*
 * Copyright (c) 2024. $name
 */

package fr.artus25200.automations.common.node;

import fr.artus25200.automations.common.node.action.Action;

public class RedirectNode extends Node{
	@Override
	public void setInputs() {
		this.inputs.add(new Input(this, "", Object.class));
	}

	@Override
	public void setOutputs() {
		this.outputs.add(new Output(this, "", Object.class));
	}

	@Override
	public boolean onExecute() {
		this.outputs.get(0).value = this.inputs.get(0).getValue();
		if(this.outputs.get(0).value instanceof Action && this.outputs.get(0).isConnected()){
			return this.outputs.get(0).connections.get(0).input.parent.Execute();
		}
		return true;
	}

	@Override
	public String getName() {
		return "Redirect Node";
	}

	@Override
	public int getColor() {
		return 0xFFFFFFFF;
	}

	@Override
	public Nodes.NodeCategory[] getCategories() {
		return new Nodes.NodeCategory[0];
	}
}
