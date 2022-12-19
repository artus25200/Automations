/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.node.data;

import fr.artus25200.automations.common.customTypes.EntityList;
import fr.artus25200.automations.common.node.Nodes;
import fr.artus25200.automations.common.node.Output;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

import java.util.List;
import java.util.Objects;

public class AllPlayersDataNode extends DataNode{
	public AllPlayersDataNode(){
		this.outputs.add(new Output(this, "Players", EntityList.class));
	}

	@Override
	public boolean onExecute() {
		EntityList players = new EntityList();
		players.entities = Objects.requireNonNull(MinecraftClient.getInstance().getServer()).getPlayerManager().getPlayerList().toArray(new Entity[0]);
		this.outputs.get(0).value = players;
		return false;
	}

	@Override
	public String getName() {
		return "Get All Players";
	}

	@Override
	public Nodes.NodeCategory[] getCategories() {
		return new Nodes.NodeCategory[]{Nodes.NodeCategory.DATA, Nodes.NodeCategory.PLAYER};
	}
}
