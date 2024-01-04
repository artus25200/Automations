/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client;

import fr.artus25200.automations.client.gui.screen.AutomationScreen;
import fr.artus25200.automations.common.NodeList;
import fr.artus25200.automations.common.networking.ModNetworking;
import fr.artus25200.automations.common.networking.NodeListS2CPacket;
import fr.artus25200.automations.common.node.action.Action;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.StickyKeyBinding;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class AutomationsClient implements ClientModInitializer {

	private static StickyKeyBinding keyBinding;

	public static TextRenderer tr;
	public static boolean shouldOpenNodeScreen = false;

	public static NodeList nodeList;

	@Override
	public void onInitializeClient() {
		nodeList = new NodeList();

		ModNetworking.RegisterS2CPackets();
		test();
	}

	public static TextRenderer getTr(){
		tr = tr == null ? MinecraftClient.getInstance().textRenderer : tr;
		return tr;
	}

	private static void test(){
		keyBinding = (StickyKeyBinding) KeyBindingHelper.registerKeyBinding(new StickyKeyBinding(
				"node_editor",
				GLFW.GLFW_KEY_M,
				"Automations",
				() -> true
		));


		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if(keyBinding.wasPressed()){
				openAutomationScreen();
			}
			if(shouldOpenNodeScreen){
				shouldOpenNodeScreen = false;
				client.setScreen(new AutomationScreen());
			}
		});
	}

	public static void openAutomationScreen(){
		ClientPlayNetworking.send(ModNetworking.ASK_FOR_NODE_LIST_C2S_PACKET_ID, PacketByteBufs.create());
		new Thread(()->{
			while(!NodeListS2CPacket.received){
				try {
					Thread.sleep(100L);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			NodeListS2CPacket.received = false;
			shouldOpenNodeScreen = true;
		}).start();
	}

	public static int getColor(Class<?> type){
		if(type == Action.class) return 0xFFfA6161;
		return 0xFFFFFFFF;
	}

	public static int darkenColor(int color, int amount){
		int r = mathClamp(((color >> 16) & 0x00FF) - amount, 0, 0xFF);
		int g = mathClamp(((color >> 8) & 0x0000FF) - amount, 0, 0xFF);
		int b = mathClamp(((color)& 0x000000FF) - amount, 0, 0xFF);
		return b | (g << 8) | (r << 16) | (0xFF << 24);
	}

	public static int mathClamp(int a, int low, int high){
		if (a < low ) a = low;
		if (a > high) a = high;
		return a;
	}
}
