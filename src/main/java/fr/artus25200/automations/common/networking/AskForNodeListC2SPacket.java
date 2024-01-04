/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.networking;

import fr.artus25200.automations.server.AutomationsServer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static fr.artus25200.automations.common.Automations.LOGGER;

public class AskForNodeListC2SPacket {
	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
		if(!player.hasPermissionLevel(4)) {
			player.sendMessageToClient(Text.literal("You are not an operator on this server.").formatted(Formatting.RED, Formatting.UNDERLINE),false);
			LOGGER.warn("Player {} tried to modify automations.", player.getName().getString());
			return;
		}
		try {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(byteArray);
			os.writeObject(AutomationsServer.nodeList);
			os.close();
			PacketByteBuf buffer = PacketByteBufs.create();
			buffer.writeByteArray(byteArray.toByteArray());
			ServerPlayNetworking.send(player, ModNetworking.NODE_LIST_S2C_PACKET_ID, buffer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
