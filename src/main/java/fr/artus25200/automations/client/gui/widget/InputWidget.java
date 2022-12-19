/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import fr.artus25200.automations.common.node.Input;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;

import java.io.Serializable;

import static fr.artus25200.automations.client.AutomationsClient.darkenColor;
import static fr.artus25200.automations.client.AutomationsClient.getTr;

@Environment(EnvType.CLIENT)
public class InputWidget implements Drawable, Selectable, Element, Serializable, RightClickable {
	public Input input;
	public NodeWidget parent;
	public ConnectionWidget connectionWidget;

	public int x, y;

	public InputWidget(Input input, NodeWidget parent){
		this.input = input;
		this.parent = parent;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		int color = isMouseOver(mouseX, mouseY) ? darkenColor(this.input.color, 50) : this.input.color;

		DrawableHelper.fill(matrices, x, y, x+5, y+5, color);
		getTr().draw(matrices, input.name, x + 2 + 5, y, 0xFFFFFFFF);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseX >= this.x && mouseX <= this.x+5 && mouseY >= this.y && mouseY <= this.y + 5;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(isMouseOver(mouseX, mouseY) && button == 1){
			this.connectionWidget.delete();
			return true;
		}
		return false;
	}

	@Override
	public SelectionType getType() { return SelectionType.HOVERED; }

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {}

	@Override
	public ContextMenuWidget onRightCLick(double mouseX, double mouseY) {
		//TODO
		return null;
	}
}
