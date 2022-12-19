/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.common.Automations;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static fr.artus25200.automations.client.AutomationsClient.getTr;

public class CButtonWidget extends ButtonWidget {
	public int color;

	public CButtonWidget(int color, int x, int y, int width, int height, Text message, PressAction onPress) {
		super(x, y, width, height, message, onPress);
		this.color = color;
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		int color = isMouseOver(mouseX, mouseY) ? AutomationsClient.darkenColor(this.color, -50) : this.color;
		DrawableHelper.fill(matrices, x, y, x + width, y + height, color);
		DrawableHelper.drawCenteredText(matrices, getTr(), getMessage(), this.x + this.width/2, this.y+(this.height-getTr().fontHeight)/2, 0xFFFFFFFF);
	}
}
