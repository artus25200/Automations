/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.networking;

import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.common.NodeWrapper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class NodeListS2CPacket {
	public static boolean received;

	public static void receive(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf buf, PacketSender packetSender) {
		try {
			byte[] array = buf.readByteArray();
			ByteArrayInputStream array2 = new ByteArrayInputStream(array);
			ObjectInputStream is = new ObjectInputStream(array2);
			AutomationsClient.nodeWrapper = (NodeWrapper) is.readObject();
			is.close();
			received = true;
		}
		catch (IOException | ClassNotFoundException e){
			throw new RuntimeException(e.getMessage());
		}

	}


}
