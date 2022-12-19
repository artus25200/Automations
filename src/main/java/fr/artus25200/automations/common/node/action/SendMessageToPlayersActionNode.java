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

	public SendMessageToPlayersActionNode(){
		this.inputs.add(new Input(this, "Player(s)", EntityList.class));
		this.inputs.add(new Input(this, "String", String.class));
	}

	@Override
	public boolean onExecute() {
		for(Entity player : ((EntityList)this.inputs.get(1).getValue()).entities){
			((ServerPlayerEntity)player).sendMessageToClient((Text)this.inputs.get(2).getValue(), false);
		}
		return true;
	}

	@Override
	public String getName() {
		return "Send Message to Player(s)";
	}

	@Override
	public Nodes.NodeCategory[] getCategories() {
		return new Nodes.NodeCategory[]{Nodes.NodeCategory.ACTION, Nodes.NodeCategory.CHAT, Nodes.NodeCategory.PLAYER};
	}
}
