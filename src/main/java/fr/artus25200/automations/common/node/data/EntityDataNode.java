/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.node.data;

import fr.artus25200.automations.common.customTypes.EntityList;
import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Nodes;
import fr.artus25200.automations.common.node.Output;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityDataNode extends DataNode{

	public EntityDataNode(){
		super();
		this.inputs.add(new Input(this, "Entity", EntityList.class));

		this.outputs.add(new Output(this, "name", String.class));
		this.outputs.add(new Output(this, "position", Vec3d.class));
		this.outputs.add(new Output(this, "world", World.class));
		this.outputs.add(new Output(this, "server", MinecraftServer.class));
	}

	@Override
	public boolean onExecute() {
		System.out.println("called");
		Entity entity = ((EntityList)this.inputs.get(0).getValue()).entities[0];

		this.outputs.get(0).value = entity.getName();
		this.outputs.get(1).value = entity.getPos();
		this.outputs.get(2).value = entity.getWorld();
		this.outputs.get(3).value = entity.getServer();
		return true;
	}

	@Override
	public String getName() {
		return "Entity Data Extractor";
	}

	@Override
	public Nodes.NodeCategory[] getCategories() {
		return new Nodes.NodeCategory[]{Nodes.NodeCategory.DATA, Nodes.NodeCategory.ENTITY};
	}
}
