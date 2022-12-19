/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.node;

import com.sun.jna.platform.win32.WinNT;
import fr.artus25200.automations.common.Automations;
import fr.artus25200.automations.common.node.action.KillEntityActionNode;
import fr.artus25200.automations.common.node.action.SendMessageToPlayersActionNode;
import fr.artus25200.automations.common.node.data.AllPlayersDataNode;
import fr.artus25200.automations.common.node.data.EntityDataNode;
import fr.artus25200.automations.common.node.events.BlockBreakEventNode;
import fr.artus25200.automations.common.node.events.ServerStartEventNode;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static fr.artus25200.automations.common.Automations.MOD_ID;

public class Nodes {

	public static final SimpleRegistry<Node> NODE_REGISTERY;

	public static final Node BLOCK_BREAK_EVENT_NODE;
	public static final Node KILL_ENTITY_ACTION_NODE;
	public static final Node ENTITY_DATA_NODE;
	public static final Node SEND_MESSAGE_TO_PLAYERS_ACTION_NODE;
	public static final Node ALL_PLAYERS_DATA_NODE;
	public static final Node SERVER_START_EVENT_NODE;

	public static void registerNodes() {
	}

	private static Node register(String id, Node node) {
		Registry.register(NODE_REGISTERY, new Identifier("automations", id), node);

		return node;
	}

	static {
		NODE_REGISTERY = FabricRegistryBuilder.createSimple(Node.class, new Identifier(MOD_ID)).attribute(RegistryAttribute.SYNCED).buildAndRegister();

		BLOCK_BREAK_EVENT_NODE = register("block_break_event_node", new BlockBreakEventNode());
		KILL_ENTITY_ACTION_NODE = register("kill_entity_action_node", new KillEntityActionNode());
		ENTITY_DATA_NODE = register("entity_data_node", new EntityDataNode());
		SEND_MESSAGE_TO_PLAYERS_ACTION_NODE = register("send_message_to_player_action_node", new SendMessageToPlayersActionNode());
		ALL_PLAYERS_DATA_NODE = register("all_players_data_node", new AllPlayersDataNode());
		SERVER_START_EVENT_NODE = register("server_start_event_node", new ServerStartEventNode());
	}

	public enum NodeCategory {
		EVENT,
		ACTION,
		DATA,
		CONDITION,
		PLAYER,
		ENTITY,
		BLOCK,
		ITEM,
		CHAT
	}
}
