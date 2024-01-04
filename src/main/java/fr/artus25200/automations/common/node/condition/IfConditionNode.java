/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.node.condition;

import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.Nodes;
import fr.artus25200.automations.common.node.Output;
import fr.artus25200.automations.common.node.action.Action;

public class IfConditionNode extends Node {
	public Output actionOutputTrue;
	public Output actionOutputFalse;

	@Override
	public void setInputs() {
		this.inputs.add(new Input(this, "action", Action.class));
		this.inputs.add(new Input(this, "condition", Boolean.class));
	}

	@Override
	public void setOutputs() {
		actionOutputTrue = new Output(this, "True", Action.class);
		actionOutputFalse = new Output(this, "False", Action.class);
		this.outputs.add(actionOutputTrue);
		this.outputs.add(actionOutputFalse);
	}

	@Override
	public boolean onExecute() {
		return true;
	}

	@Override
	public String getName() {
		return "If condition";
	}

	@Override
	public int getColor() {
		return 0xFF34eb6b;
	}

	@Override
	public Nodes.NodeCategory[] getCategories() {
		return new Nodes.NodeCategory[]{Nodes.NodeCategory.CONDITION};
	}

	@Override
	public boolean Execute() {
		if(!super.Execute()) return false;
		if((Boolean) this.inputs.get(1).getValue()){
			if(actionOutputTrue.isConnected()) return actionOutputTrue.connections.get(0).input.parent.Execute();
		} else {
			if(actionOutputFalse.isConnected()) return actionOutputFalse.connections.get(0).input.parent.Execute();
		}
		return true;
	}
}
