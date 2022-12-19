/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

import static fr.artus25200.automations.common.Automations.MOD_ID;

public class ModNetworking {
	public static final Identifier NODE_LIST_S2C_PACKET_ID = new Identifier(MOD_ID, "node_list_s2c_packet");
	public static final Identifier NODE_LIST_C2S_PACKET_ID = new Identifier(MOD_ID, "node_list_c2s_packet");
	public static final Identifier ASK_FOR_NODE_LIST_C2S_PACKET_ID = new Identifier(MOD_ID, "ask_for_node_list_c2s_packet");

	public static void RegisterC2SPackets() {
		ServerPlayNetworking.registerGlobalReceiver(NODE_LIST_C2S_PACKET_ID, NodeListC2SPacket::receive);
		ServerPlayNetworking.registerGlobalReceiver(ASK_FOR_NODE_LIST_C2S_PACKET_ID, AskForNodeListC2SPacket::receive);
	}

	public static void RegisterS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(NODE_LIST_S2C_PACKET_ID, NodeListS2CPacket::receive);
	}
}
