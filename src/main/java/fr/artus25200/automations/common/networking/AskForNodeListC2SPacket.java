/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.networking;

import fr.artus25200.automations.server.AutomationsServer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static fr.artus25200.automations.common.Automations.LOGGER;

public class AskForNodeListC2SPacket {

	// The packet has no data
	public record Payload() implements CustomPacketPayload {
		public static final Type<Payload> TYPE = new Type<>(ModNetworking.ASK_FOR_NODE_LIST_C2S_PACKET_ID);
		public static final StreamCodec<RegistryFriendlyByteBuf, Payload> CODEC = StreamCodec.unit(new Payload());

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	public static void receive(Payload payload, ServerPlayNetworking.Context context){
		ServerPlayer player = context.player();
		if(!player.permissions().hasPermission(Permissions.COMMANDS_OWNER)) {
			player.sendSystemMessage(Component.literal("You are not an operator on this server.").withStyle(ChatFormatting.RED, ChatFormatting.UNDERLINE),false);
			LOGGER.warn("Player {} tried to modify automations.", player.getName().getString());
			return;
		}
		try {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(byteArray);
			os.writeObject(AutomationsServer.nodes);
			os.close();
			ServerPlayNetworking.send(player, new NodeListS2CPacket.Payload(byteArray.toByteArray()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
