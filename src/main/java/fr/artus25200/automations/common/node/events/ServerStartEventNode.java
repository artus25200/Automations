/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.node.events;

import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.common.node.Output;
import fr.artus25200.automations.common.node.action.Action;
import fr.artus25200.automations.server.AutomationsServer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerStartEventNode extends EventNode{

	public ServerStartEventNode() {
		super();
		AutomationsClient.nodeList.serverStartedEvents.add(this);
	}

	@Override
	public void setOutputs() {
		super.setOutputs();
		this.outputs.add(new Output(this, "Action", Action.class));
		this.outputs.add(new Output(this, "server", MinecraftServer.class));
	}

	public void Execute(MinecraftServer server){
		this.outputs.get(1).value = server;
	}

	@Override
	public String getName() {
		return "On Server Start";
	}

	@Override
	public void registerEvent() {
		ServerLifecycleEvents.SERVER_STARTED.register((server)->{
			for(ServerStartEventNode n : AutomationsServer.nodeList.serverStartedEvents){
				n.Execute(server);
			}
		});
	}
}
