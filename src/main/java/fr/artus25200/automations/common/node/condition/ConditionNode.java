/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.node.condition;

import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.action.Action;

public abstract class ConditionNode extends Node {

	public ConditionNode(){
		this.inputs.add(new Input(this, "action", Action.class));
		this.inputs.add(new Input(this, "condition", Boolean.class));
	}

	@Override
	public boolean Execute() {
		return Condition();
	}

	public abstract boolean Condition();
}
