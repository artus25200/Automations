/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.node.data;

import fr.artus25200.automations.common.customTypes.EntityList;
import fr.artus25200.automations.common.node.Nodes;
import fr.artus25200.automations.common.node.Output;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

import java.util.Objects;

public class AllPlayersDataNode extends DataNode{

    public AllPlayersDataNode(){
        super();
        this.categories.add(Nodes.NodeCategory.PLAYER);
    }

	@Override
	public void setInputs() {
	}

	@Override
	public void setOutputs() {
		this.outputs.add(new Output(this, "Players", EntityList.class));
	}

	@Override
	public boolean onExecute() {
		EntityList players = new EntityList();
		players.entities = Objects.requireNonNull(Minecraft.getInstance().getSingleplayerServer()).getPlayerList().getPlayers().toArray(new Entity[0]);
		this.outputs.get(0).value = players;
		return true;
	}

	@Override
	public String getName() {
		return "Get All Players";
	}

}
