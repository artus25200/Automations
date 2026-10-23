/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.networking;

import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.common.NodeList;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class NodeListS2CPacket {
	public static boolean received;

	public record Payload(byte[] data) implements CustomPacketPayload {
		public static final Type<Payload> TYPE = new Type<>(ModNetworking.NODE_LIST_S2C_PACKET_ID);
		public static final StreamCodec<RegistryFriendlyByteBuf, Payload> CODEC = StreamCodec.composite(ByteBufCodecs.BYTE_ARRAY, Payload::data, Payload::new);

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	public static void receive(Payload payload, ClientPlayNetworking.Context context) {
		try {
			byte[] array = payload.data();
			ByteArrayInputStream array2 = new ByteArrayInputStream(array);
			ObjectInputStream is = new ObjectInputStream(array2);
			AutomationsClient.nodeList = (NodeList) is.readObject();
            AutomationsClient.nodeList.initialize();

			is.close();
			received = true;
		}
		catch (IOException | ClassNotFoundException e){
			throw new RuntimeException(e.getMessage());
		}

	}


}
