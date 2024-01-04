/*
 * Copyright (c) 2024. $name
 */

package fr.artus25200.automations.common.node.data.field;

import fr.artus25200.automations.common.node.Output;
import fr.artus25200.automations.common.node.data.DataNode;

public class StringDataNode extends DataNode implements Editable{

	String data = "";

	@Override
	public void setInputs() {

	}

	@Override
	public void setOutputs() {
		this.outputs.add(new Output(this, "string", String.class));
	}

	@Override
	public boolean onExecute() {
		this.outputs.get(0).value = data;
		return true;
	}

	@Override
	public String getName() {
		return "String";
	}

	@Override
	public Class<?> getType() {
		return String.class;
	}

	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
