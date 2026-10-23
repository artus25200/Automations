/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.artus25200.automations.common.networking.ModNetworking;
import fr.artus25200.automations.common.node.Nodes;
import fr.artus25200.automations.common.node.action.Action;
import fr.artus25200.automations.server.AutomationsServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class Automations implements ModInitializer {
	public static final String MOD_ID = "automations";
	public static final String MOD_NAME = "Automations";
	public static final String MOD_VERSION = "0.1.0 alpha";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing " + MOD_NAME + " " + MOD_VERSION);

		ModNetworking.RegisterPayloadTypes();

		// the registry must be created during mod initialization, it is frozen afterwards
		Nodes.registerNodes();

		ServerLifecycleEvents.SERVER_STARTED.register((server)-> {
			try {
				AutomationsServer.onInitializeServer();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		test();
	}

	public static void test(){

	}

	public static Object duplicateObject(Object obj){
		//Gson gson = new Gson();
		//return gson.fromJson(gson.toJson(obj, obj.getClass()), obj.getClass());
		/*try {
			
		}catch (IOException ignored){}*/
		return obj;
	}
    public static int getColor(Class<?> type){
		if(type == Action.class) return 0xFFfA6161;
		return 0xFFFFFFFF;
	}
}
