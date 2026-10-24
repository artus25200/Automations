/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.server;

import fr.artus25200.automations.common.Automations;
import fr.artus25200.automations.common.NodeList;
import fr.artus25200.automations.common.networking.ModNetworking;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.Nodes;
import fr.artus25200.automations.common.node.events.BlockBreakEventNode;
import fr.artus25200.automations.common.node.events.EventNode;
import fr.artus25200.automations.common.node.events.ServerStartEventNode;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

// custom server mod initializer. It is not triggered from fabric
public class AutomationsServer {
    public static Path configDir;

    public static ArrayList<Node> nodes;
    public static MinecraftServer server;

    public static void onInitializeServer() throws IOException {
        ModNetworking.RegisterC2SPackets();

        // TODO: config/node file in worlds, not in config folder
        configDir = FabricLoader.getInstance().getConfigDir().resolve(Automations.MOD_ID);
        if (!Files.isDirectory(configDir))
            Files.createDirectory(configDir);
        readNodesFromFile();

        ServerLifecycleEvents.SERVER_STARTED.register((s) -> {
            server = s;
        });
        // Server Stopping
        ServerLifecycleEvents.SERVER_STOPPING.register((server) -> writeNodesToFile());

        registerEvents();
    }

    public static void readNodesFromFile() {
        Path nodeFile = configDir.resolve("nodes");
        try {
            if (!Files.exists(nodeFile)) {
                Files.createFile(nodeFile);
                ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(nodeFile.toFile()));
                os.writeObject(new ArrayList<Node>());
                os.close();
            }
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(nodeFile.toFile()));
            // nodeList = (NodeList) is.readObject();
            nodes = (ArrayList<Node>) is.readObject();
            is.close();
        } catch (IOException | ClassNotFoundException e) {
            String newName = "nodes_old_" + java.time.LocalDateTime.now();
            if (e.getClass() == InvalidClassException.class
                    && nodeFile.toFile().renameTo(configDir.resolve(newName).toFile())) {
                Automations.LOGGER.error(
                        "The Nodes save file version is incompatible with the current version of the mod. It has been renamed to "
                                + newName + " and a new nodes file has been created");
                readNodesFromFile();
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public static void registerEvents() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, ci) -> {
            for (Node node : nodes) {
                if (node instanceof BlockBreakEventNode bbeventnode) {
                    bbeventnode.Execute(world, pos, state, player);
                }
            }
        });
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            for (Node node : nodes) {
                if (node instanceof ServerStartEventNode sseventnode) {
                    sseventnode.Execute(server);
                }
            }
        });

    }

    public static void writeNodesToFile() {
        try {
            Path nodeFile = configDir.resolve("nodes");
            if (Files.exists(nodeFile))
                Files.delete(nodeFile);
            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(nodeFile.toFile()))) {
                os.writeObject(nodes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
