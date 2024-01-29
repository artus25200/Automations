/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.node;

import fr.artus25200.automations.common.node.action.KillEntityActionNode;
import fr.artus25200.automations.common.node.action.SendMessageToPlayersActionNode;
import fr.artus25200.automations.common.node.condition.IfConditionNode;
import fr.artus25200.automations.common.node.data.AllPlayersDataNode;
import fr.artus25200.automations.common.node.data.EntityDataNode;
import fr.artus25200.automations.common.node.data.StringConcatenateDataNode;
import fr.artus25200.automations.common.node.data.field.BoolDataNode;
import fr.artus25200.automations.common.node.data.field.IntegerDataNode;
import fr.artus25200.automations.common.node.data.field.StringDataNode;
import fr.artus25200.automations.common.node.events.BlockBreakEventNode;
import fr.artus25200.automations.common.node.events.ServerStartEventNode;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

import static fr.artus25200.automations.common.Automations.MOD_ID;

public class Nodes {

	public static final SimpleRegistry<Node> NODE_REGISTERY;

	//EVENTS
	public static final Node BLOCK_BREAK_EVENT_NODE;
	public static final Node SERVER_START_EVENT_NODE;

	//ACTION
	public static final Node KILL_ENTITY_ACTION_NODE;
	public static final Node SEND_MESSAGE_TO_PLAYERS_ACTION_NODE;

	//DATA
	public static final Node ALL_PLAYERS_DATA_NODE;
	public static final Node ENTITY_DATA_NODE;
	public static final Node STRING_CONCATENATE_DATA_NODE;

	//FIELDS
	public static final Node STRING_FIELD_DATA_NODE;
	public static final Node BOOL_FIELD_DATA_NODE;
	public static final Node INT_FIELD_DATA_NODE;

	//CONDITION
	public static final Node IF_CONDITION_NODE;


	//OTHERS
	public static final Node REDIRECT_NODE;

	public static void registerNodes() {}

	private static Node register(String id, Node node) {
		Registry.register(NODE_REGISTERY, new Identifier("automations", id), node);

		return node;
	}

	static {
		NODE_REGISTERY = FabricRegistryBuilder.createSimple(Node.class, new Identifier(MOD_ID)).attribute(RegistryAttribute.SYNCED).buildAndRegister();

		//EVENTS
		BLOCK_BREAK_EVENT_NODE = register("block_break_event_node", new BlockBreakEventNode());
		SERVER_START_EVENT_NODE = register("server_start_event_node", new ServerStartEventNode());

		//ACTION
		KILL_ENTITY_ACTION_NODE = register("kill_entity_action_node", new KillEntityActionNode());
		SEND_MESSAGE_TO_PLAYERS_ACTION_NODE = register("send_message_to_player_action_node", new SendMessageToPlayersActionNode());

		//DATA
		ALL_PLAYERS_DATA_NODE = register("all_players_data_node", new AllPlayersDataNode());
		ENTITY_DATA_NODE = register("entity_data_node", new EntityDataNode());
		STRING_CONCATENATE_DATA_NODE = register("string_concatenate_data_node", new StringConcatenateDataNode());

		//FIELDS
		STRING_FIELD_DATA_NODE = register("string_field_data_node", new StringDataNode());
		BOOL_FIELD_DATA_NODE = register("bool_field_data_node", new BoolDataNode());
		INT_FIELD_DATA_NODE = register("int_field_data_node", new IntegerDataNode());

		//CONDITION
		IF_CONDITION_NODE = register("if_condition_node", new IfConditionNode());

		//OTHERS
		REDIRECT_NODE = register("redirect_node", new RedirectNode());
	}

	public enum NodeCategory {
		EVENT,
		ACTION,
		DATA,
		FIELD,
		CONDITION,
		PLAYER,
		ENTITY,
		BLOCK,
		ITEM,
		CHAT
	}
}
