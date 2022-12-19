/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.node;

import java.io.Serializable;

public class Connection implements Serializable {
	public Output output;
	public Input input;

	public Connection(Output o, Input i){
		this.output = o;
		this.input = i;
		o.connections.add(this);
		i.connector = this;
		//Automations.nodeWrapper.connections.add(this);
	}

	public static Connection createConnection(Output o, Input i){

		for (Connection c : o.connections){
			if(c.input == i) return null;
		}
		return new Connection(o, i);
	}

	public void delete(){
		this.input.connector = null;
		this.output.connections.remove(this);
		//Automations.nodeWrapper.connections.remove(this);
	}
}
