/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;

import static fr.artus25200.automations.common.Automations.MOD_ID;

public class ModNetworking {
	public static final Identifier NODE_LIST_S2C_PACKET_ID = Identifier.fromNamespaceAndPath(MOD_ID, "node_list_s2c_packet");
	public static final Identifier NODE_LIST_C2S_PACKET_ID = Identifier.fromNamespaceAndPath(MOD_ID, "node_list_c2s_packet");
	public static final Identifier ASK_FOR_NODE_LIST_C2S_PACKET_ID = Identifier.fromNamespaceAndPath(MOD_ID, "ask_for_node_list_c2s_packet");

	// Since 1.20.5 the payload types must be registered (on both sides) before they can be sent or received
	public static void RegisterPayloadTypes() {
		PayloadTypeRegistry.serverboundPlay().register(NodeListC2SPacket.Payload.TYPE, NodeListC2SPacket.Payload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(AskForNodeListC2SPacket.Payload.TYPE, AskForNodeListC2SPacket.Payload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(NodeListS2CPacket.Payload.TYPE, NodeListS2CPacket.Payload.CODEC);
	}

	public static void RegisterC2SPackets() {
		ServerPlayNetworking.registerGlobalReceiver(NodeListC2SPacket.Payload.TYPE, NodeListC2SPacket::receive);
		ServerPlayNetworking.registerGlobalReceiver(AskForNodeListC2SPacket.Payload.TYPE, AskForNodeListC2SPacket::receive);
	}

	public static void RegisterS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(NodeListS2CPacket.Payload.TYPE, NodeListS2CPacket::receive);
	}
}
