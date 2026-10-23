/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client;

import com.mojang.blaze3d.platform.InputConstants;

import fr.artus25200.automations.client.gui.screen.AutomationScreen;
import fr.artus25200.automations.client.gui.widget.InputWidget;
import fr.artus25200.automations.client.gui.widget.NodeWidget;
import fr.artus25200.automations.client.gui.widget.OutputWidget;
import fr.artus25200.automations.common.Automations;
import fr.artus25200.automations.common.NodeList;
import fr.artus25200.automations.common.networking.AskForNodeListC2SPacket;
import fr.artus25200.automations.common.networking.ModNetworking;
import fr.artus25200.automations.common.networking.NodeListS2CPacket;
import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.Output;
import fr.artus25200.automations.common.node.action.Action;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class AutomationsClient implements ClientModInitializer {

	private static ToggleKeyMapping keyBinding;

	public static Font tr;
	public static boolean shouldOpenNodeScreen = false;


	@Override
	public void onInitializeClient() {

		ModNetworking.RegisterS2CPackets();
		test();
	}

	public static Font getTr(){
		tr = tr == null ? Minecraft.getInstance().font : tr;
		return tr;
	}

	private static void test(){
		keyBinding = (ToggleKeyMapping) KeyMappingHelper.registerKeyMapping(new ToggleKeyMapping(
				"node_editor",
				InputConstants.KEY_M,
				KeyMapping.Category.register(Identifier.fromNamespaceAndPath(Automations.MOD_ID, "automations")),
				() -> true,
				false
		));


		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if(keyBinding.consumeClick()){
				client.gui.setScreen(new AutomationScreen());
			}
		});
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
