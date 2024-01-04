/*
 * Copyright (c) 2023. $name
 */

package fr.artus25200.automations.common.node.data;

import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Output;

public class StringConcatenateDataNode extends DataNode {

	@Override
	public void setInputs() {
		this.inputs.add(new Input(this, "String", String.class));
		this.inputs.add(new Input(this, "String", String.class));
	}

	@Override
	public void setOutputs() {
		this.outputs.add(new Output(this, "String", String.class));
	}

	@Override
	public boolean onExecute() {
		this.outputs.get(0).value = this.inputs.get(0).getValue() +((String) this.inputs.get(1).getValue());
		return true;
	}

	@Override
	public String getName() {
		return "String Concatenate";
	}
}
