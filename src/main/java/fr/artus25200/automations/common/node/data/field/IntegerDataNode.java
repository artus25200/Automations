/*
 * Copyright (c) 2024. $name
 */

package fr.artus25200.automations.common.node.data.field;

import fr.artus25200.automations.common.node.Output;
import fr.artus25200.automations.common.node.data.DataNode;

public class IntegerDataNode extends DataNode implements Editable{
	int data = 0;

	@Override
	public void setInputs() {

	}

	@Override
	public void setOutputs() {
		this.outputs.add(new Output(this, "Integer", Integer.class));
	}

	@Override
	public boolean onExecute() {
		this.outputs.get(0).value = data;
		return true;
	}

	@Override
	public String getName() {
		return "Integer";
	}

	@Override
	public Class<?> getType() {
		return Integer.class;
	}

	public int getData() {
		return data;
	}
	public void setData(int data) {
		this.data = data;
	}
}
