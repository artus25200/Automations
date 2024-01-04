/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.node.action;

import fr.artus25200.automations.common.customTypes.EntityList;
import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Nodes;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class SendMessageToPlayersActionNode extends ActionNode{

	@Override
	public void setInputs() {
		super.setInputs();
		this.inputs.add(new Input(this, "Player(s)", EntityList.class));
		this.inputs.add(new Input(this, "String", String.class));
	}

	@Override
	public void setOutputs() {
		super.setOutputs();
	}

	@Override
	public boolean onExecute() {
		for(Entity player : ((EntityList)this.inputs.get(1).getValue()).entities){
			((ServerPlayerEntity)player).sendMessage(Text.literal((String) this.inputs.get(2).getValue()));
		}
		return true;
	}

	@Override
	public String getName() {
		return "Send Chat Message";
	}

	@Override
	public Nodes.NodeCategory[] getCategories() {
		return new Nodes.NodeCategory[]{Nodes.NodeCategory.ACTION, Nodes.NodeCategory.CHAT, Nodes.NodeCategory.PLAYER};
	}
}
