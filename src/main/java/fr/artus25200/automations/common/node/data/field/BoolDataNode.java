/*
 * Copyright (c) 2024. $name
 */

package fr.artus25200.automations.common.node.data.field;

import fr.artus25200.automations.common.node.Output;
import fr.artus25200.automations.common.node.data.DataNode;

public class BoolDataNode extends DataNode implements Editable{

	Boolean data = false;

	@Override
	public void setInputs() {

	}

	@Override
	public void setOutputs() {
		this.outputs.add(new Output(this, "boolean", Boolean.class));
	}

	@Override
	public boolean onExecute() {
		this.outputs.get(0).value = data;
		return true;
	}

	@Override
	public String getName() {
		return "Boolean";
	}

	@Override
	public Class<?> getType() {
		return Boolean.class;
	}

	public Boolean getData(){
		return data;
	}
	public void setData(Boolean data) {
		this.data = data;
	}
}
