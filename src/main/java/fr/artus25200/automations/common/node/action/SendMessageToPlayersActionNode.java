/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.node.action;

import fr.artus25200.automations.common.customTypes.EntityList;
import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Nodes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class SendMessageToPlayersActionNode extends ActionNode{

    public SendMessageToPlayersActionNode(){
        super();
        this.categories.add(Nodes.NodeCategory.CHAT);
        this.categories.add(Nodes.NodeCategory.PLAYER);
    }

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
			((ServerPlayer)player).sendSystemMessage(Component.literal((String) this.inputs.get(2).getValue()));
		}
		return true;
	}

	@Override
	public String getName() {
		return "Send Chat Message";
	}

}
