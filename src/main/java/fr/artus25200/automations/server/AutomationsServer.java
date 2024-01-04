/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.server;

import fr.artus25200.automations.common.Automations;
import fr.artus25200.automations.common.NodeList;
import fr.artus25200.automations.common.networking.ModNetworking;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.Nodes;
import fr.artus25200.automations.common.node.events.EventNode;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

// custom server mod initializer. It is not triggered from fabric
public class AutomationsServer {
	public static Path configDir;

	public static NodeList nodeList;

	public static void onInitializeServer() throws IOException {
		ModNetworking.RegisterC2SPackets();

		// TODO: config/node file in worlds, not in config folder
		configDir = FabricLoader.getInstance().getConfigDir().resolve(Automations.MOD_ID);
		if(!Files.isDirectory(configDir)) Files.createDirectory(configDir);
		readNodesFromFile();

		for (Node n : Nodes.NODE_REGISTERY) {
			if (n instanceof EventNode) {
				((EventNode) n).registerEvent();
			}
		}

		// Server Stopping
		ServerLifecycleEvents.SERVER_STOPPING.register((server)-> writeNodesToFile());
	}

	public static void readNodesFromFile() {
		Path nodeFile = configDir.resolve("nodes");
		try {
			if(!Files.exists(nodeFile)){
				Files.createFile(nodeFile);
				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(nodeFile.toFile()));
				os.writeObject(new NodeList());
				os.close();
			}
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(nodeFile.toFile()));
			nodeList = (NodeList) is.readObject();
			is.close();
		}
		catch(IOException | ClassNotFoundException e){
			String newName = "nodes_old_"+java.time.LocalDateTime.now();
			if (e.getClass() == InvalidClassException.class && nodeFile.toFile().renameTo(configDir.resolve(newName).toFile())){
				Automations.LOGGER.error("The Nodes save file version is incompatible with the current version of the mod. It has been renamed to " + newName + " And a new nodes file has been created");
				readNodesFromFile();
			}
			else {
				throw new RuntimeException(e);
			}
		}
	}

	public static void writeNodesToFile() {
		try {
			Path nodeFile = configDir.resolve("nodes");
			if(Files.exists(nodeFile)) Files.delete(nodeFile);
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(nodeFile.toFile()));
			os.writeObject(nodeList);
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}
	}
}
