/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import fr.artus25200.automations.common.node.Input;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;

import java.io.Serializable;

import static fr.artus25200.automations.client.AutomationsClient.darkenColor;
import static fr.artus25200.automations.client.AutomationsClient.getTr;



@Environment(EnvType.CLIENT)
public class InputWidget implements Renderable, NarratableEntry, GuiEventListener, Serializable, RightClickable {
	public Input input;
	public NodeWidget parent;
	public ConnectionWidget connectionWidget;

	public int x, y;

	public InputWidget(Input input, NodeWidget parent){
		this.input = input;

        this.parent = parent;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		int color = isMouseOver(mouseX, mouseY) ? darkenColor(this.input.color, 50) : this.input.color;

		graphics.fill(x, y, x+5, y+5, color);
		int textColor = this.input.isConnected() ? 0xFFFFFFFF : 0xFFFF0000;
		graphics.text(getTr(), input.name, x + 2 + 5, y, textColor, false);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseX >= this.x-1 && mouseX <= this.x+5+1 && mouseY >= this.y-1 && mouseY <= this.y + 5+1;
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		double mouseX = event.x();
		double mouseY = event.y();
		if(isMouseOver(mouseX, mouseY)){
			this.connectionWidget.delete();
			return true;
		}
		return false;
	}

	@Override
	public NarrationPriority narrationPriority() { return NarrationPriority.HOVERED; }

	@Override
	public void updateNarration(NarrationElementOutput builder) {}

	@Override
	public void setFocused(boolean focused) {}

	@Override
	public boolean isFocused() { return false; }

	@Override
	public ContextMenuWidget onRightCLick(double mouseX, double mouseY) {
		//TODO
		return null;
	}
}
