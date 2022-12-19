/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.networking;

import fr.artus25200.automations.common.Automations;
import fr.artus25200.automations.common.NodeWrapper;
import fr.artus25200.automations.server.AutomationsServer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class NodeListC2SPacket {
	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
		if(!player.hasPermissionLevel(4)) return;
		try {
			byte[] array = buf.readByteArray();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(array);
			ObjectInputStream is = new ObjectInputStream(inputStream);
			AutomationsServer.nodeWrapper = (NodeWrapper) is.readObject();
			is.close();
		} catch (IOException | ClassNotFoundException e){
			throw new RuntimeException(e);
		}
	}
}
