/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import fr.artus25200.automations.client.gui.screen.AutomationScreen;
import fr.artus25200.automations.common.node.Node;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;

import static fr.artus25200.automations.client.AutomationsClient.openAutomationScreen;

public class AddNodeButtonWidget extends CButtonWidget{
	Node node;
	double mouseX, mouseY;

	Screen parent;

	public AddNodeButtonWidget(Node node, int color, int x, int y, int width, int height, Text message, double mouseX, double mouseY, Screen parent) {
		super(color, x, y, width, height, message, (button)->{

		});
		this.node = node;
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.parent = parent;
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		Node duplicatedNode;
		try {
			duplicatedNode = this.node.getClass().getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		((AutomationScreen)parent).addNode(duplicatedNode, (int) this.mouseX, (int) this.mouseY);
		MinecraftClient.getInstance().setScreen(parent);
	}
}
