/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client;

import fr.artus25200.automations.client.gui.screen.AutomationScreen;
import fr.artus25200.automations.common.Automations;
import fr.artus25200.automations.common.NodeWrapper;
import fr.artus25200.automations.common.networking.ModNetworking;
import fr.artus25200.automations.common.networking.NodeListS2CPacket;
import fr.artus25200.automations.common.node.Connection;
import fr.artus25200.automations.common.node.action.Action;
import fr.artus25200.automations.common.node.action.KillEntityActionNode;
import fr.artus25200.automations.common.node.events.BlockBreakEventNode;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.StickyKeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class AutomationsClient implements ClientModInitializer {

	private static StickyKeyBinding keyBinding;

	public static TextRenderer tr;
	public static boolean shouldOpenNodeScreen = false;

	public static NodeWrapper nodeWrapper;

	@Override
	public void onInitializeClient() {
		nodeWrapper = new NodeWrapper();

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
		int r = Math.max(Math.min(((color >> 16) & 0x00FF) - amount, 0xFF), 0);
		int g = Math.max(Math.min(((color >> 8) & 0x0000FF) - amount, 0xFF), 0);
		int b = Math.max(Math.min((((color)& 0x000000FF) - amount), 0xFF), 0);
		return g | (b << 8) | (r << 16) | (0xFF << 24);
	}
}
