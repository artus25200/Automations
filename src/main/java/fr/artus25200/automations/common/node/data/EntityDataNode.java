/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.node.data;

import fr.artus25200.automations.common.customTypes.EntityList;
import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Nodes;
import fr.artus25200.automations.common.node.Output;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityDataNode extends DataNode{

    public EntityDataNode(){
        super();
        this.categories.add(Nodes.NodeCategory.ENTITY);
    }

	@Override
	public void setInputs() {
		this.inputs.add(new Input(this, "Entity", EntityList.class));
	}

	@Override
	public void setOutputs() {
		this.outputs.add(new Output(this, "name", String.class));
		this.outputs.add(new Output(this, "position", Vec3.class));
		this.outputs.add(new Output(this, "world", Level.class));
		this.outputs.add(new Output(this, "server", MinecraftServer.class));
	}

	@Override
	public boolean onExecute() {
		Entity entity = ((EntityList)this.inputs.get(0).getValue()).entities[0];

		this.outputs.get(0).value = entity.getName().getString();
		this.outputs.get(1).value = entity.position();
		this.outputs.get(2).value = entity.level();
		this.outputs.get(3).value = entity.level().getServer();
		return true;
	}

	@Override
	public String getName() {
		return "Entity Data";
	}

}
