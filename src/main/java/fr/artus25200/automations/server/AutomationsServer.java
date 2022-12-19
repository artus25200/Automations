/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.server;

import fr.artus25200.automations.client.gui.widget.NodeWidget;
import fr.artus25200.automations.common.Automations;
import fr.artus25200.automations.common.NodeWrapper;
import fr.artus25200.automations.common.networking.ModNetworking;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.Nodes;
import fr.artus25200.automations.common.node.events.BlockBreakEventNode;
import fr.artus25200.automations.common.node.events.EventNode;
import fr.artus25200.automations.common.node.events.ServerStartEventNode;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

// custom server mod initializer. It is not triggered from fabric
public class AutomationsServer {
	public static Path configDir;

	public static NodeWrapper nodeWrapper;

	public static void onInitializeServer() {
		ModNetworking.RegisterC2SPackets();

		configDir = FabricLoader.getInstance().getConfigDir().resolve(Automations.MOD_ID);

		readNodesFromFile();

		for (Node n : Nodes.NODE_REGISTERY) {
			if (n instanceof EventNode) {
				((EventNode) n).registerEvent();
			}
		}

		// Server Stopping
		ServerLifecycleEvents.SERVER_STOPPING.register((server)->{
			writeNodesToFile();
		});
	}

	public static void readNodesFromFile() {
		try {
			Path nodeFile = configDir.resolve("nodes");
			if(!Files.exists(nodeFile)){
				Files.createFile(nodeFile);
				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(nodeFile.toFile()));
				os.writeObject(new NodeWrapper());
				os.close();
			}
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(nodeFile.toFile()));
			nodeWrapper = (NodeWrapper) is.readObject();
			is.close();
		}
		catch(IOException | ClassNotFoundException e){
			throw new RuntimeException(e);
		}
	}

	public static void writeNodesToFile() {
		try {
			Path nodeFile = configDir.resolve("nodes");
			if(Files.exists(nodeFile)) Files.delete(nodeFile);
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(nodeFile.toFile()));
			os.writeObject(nodeWrapper);
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}
	}

	public static void test(){
		System.out.println("zzz");
	}
}
