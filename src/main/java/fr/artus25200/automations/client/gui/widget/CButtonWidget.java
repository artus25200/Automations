/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.common.Automations;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import static fr.artus25200.automations.client.AutomationsClient.getTr;

public class CButtonWidget extends Button {
	public int color;

	public CButtonWidget(int color, int x, int y, int width, int height, Component message, OnPress onPress) {
		super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
		this.color = color;
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		int color_ = isMouseOver(mouseX, mouseY) ? AutomationsClient.darkenColor(this.color, 20) : this.color;
		graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), color_);
		graphics.centeredText(getTr(), getMessage(), this.getX() + this.getWidth()/2, this.getY()+(this.getHeight()-getTr().lineHeight)/2 +1, 0xFFFFFFFF);
        graphics.outline(getX(), getY(), getWidth(), getHeight(), isMouseOver(mouseX, mouseY) ? AutomationsClient.OUTLINE_COLOR_HOVER : AutomationsClient.OUTLINE_COLOR);
	}
}
