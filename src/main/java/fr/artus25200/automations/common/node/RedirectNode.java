/*
 * Copyright (c) 2024. $name
 */

package fr.artus25200.automations.common.node;

import java.util.ArrayList;

import fr.artus25200.automations.common.node.action.Action;
import fr.artus25200.automations.common.node.events.EventNode;
import fr.artus25200.automations.common.node.events.HasParent;

public class RedirectNode extends Node implements HasParent{
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


    public ArrayList<Node> GetParents(){
        if(!this.inputs.get(0).isConnected()) return new ArrayList<>();
        return ((HasParent)this.inputs.get(0).connector.output.parent).GetParents();
        // Ignore redirect nodes
        // list.add(this);
        // return list;
    }

}
