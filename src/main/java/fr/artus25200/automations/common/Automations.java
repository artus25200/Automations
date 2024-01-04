/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common;

import com.google.gson.Gson;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.server.AutomationsServer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.registry.SimpleRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Automations implements ModInitializer {
	public static final String MOD_ID = "automations";
	public static final String MOD_NAME = "Automations";
	public static final String MOD_VERSION = "0.1.0 alpha";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing " + MOD_NAME + " " + MOD_VERSION);

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
}
