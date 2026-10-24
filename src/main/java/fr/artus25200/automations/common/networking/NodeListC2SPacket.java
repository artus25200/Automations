/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.networking;

import fr.artus25200.automations.common.NodeList;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.server.AutomationsServer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.permissions.Permissions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class NodeListC2SPacket {

	public record Payload(byte[] data) implements CustomPacketPayload {
		public static final Type<Payload> TYPE = new Type<>(ModNetworking.NODE_LIST_C2S_PACKET_ID);
		public static final StreamCodec<RegistryFriendlyByteBuf, Payload> CODEC = StreamCodec.composite(ByteBufCodecs.BYTE_ARRAY, Payload::data, Payload::new);

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	public static void receive(Payload payload, ServerPlayNetworking.Context context){
		if(!context.player().permissions().hasPermission(Permissions.COMMANDS_OWNER)) return;
		try {
			byte[] array = payload.data();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(array);
			ObjectInputStream is = new ObjectInputStream(inputStream);
			AutomationsServer.nodes = (ArrayList<Node>) is.readObject();
			is.close();
		} catch (IOException | ClassNotFoundException e){
			throw new RuntimeException(e);
		}
	}
}
