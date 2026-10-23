/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.client.gui.screen.AutomationScreen;
import fr.artus25200.automations.common.node.Connection;
import fr.artus25200.automations.common.node.Output;
import fr.artus25200.automations.common.node.RedirectNode;
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
import java.util.HashMap;

import static fr.artus25200.automations.client.AutomationsClient.darkenColor;
import static fr.artus25200.automations.client.AutomationsClient.getTr;

@Environment(EnvType.CLIENT)
public class OutputWidget implements Renderable, NarratableEntry, GuiEventListener, Serializable, RightClickable {
	public static final int widthANDheight = 5;

	public Output output;
	public NodeWidget parent;
	public HashMap<Connection, ConnectionWidget> connections = new HashMap<>();

	public boolean dragging;

	public int x, y;

	public OutputWidget(Output output, NodeWidget parent){
		this.output = output;
		this.parent = parent;
		for (Connection c : output.connections){
			this.connections.put(c, new ConnectionWidget(c));
		}

	}

	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		int color = isMouseOver(mouseX,mouseY) ? darkenColor(this.output.color, 50) : this.output.color;

		graphics.fill(this.x, this.y, this.x + widthANDheight, this.y + widthANDheight, color);
		if(!(parent instanceof EditableDataNodeWidget)){
			graphics.text(getTr(), output.name, this.x - getTr().width(output.name) - 5, y, 0xFFFFFFFF, false);
		}

		for (ConnectionWidget cw : connections.values()){
			cw.extractRenderState(graphics, mouseX, mouseY, delta);
		}

		if(this.dragging){
			ConnectionWidget.drawLine(graphics, 3, this.x+4, this.y+4, mouseX, mouseY, this.output.color);
		}
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseX >= this.x && mouseX <= this.x+widthANDheight && mouseY >= this.y && mouseY <= this.y + widthANDheight;
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		double mouseX = event.x();
		double mouseY = event.y();
		int button = event.button();
		if(this.isMouseOver(mouseX, mouseY) && button == InputConstants.MOUSE_BUTTON_LEFT){
			dragging = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		double mouseX = event.x();
		double mouseY = event.y();
		if(!this.dragging) return true;
		this.dragging = false;
		for (NodeWidget nodeWidget : AutomationsClient.nodeList.nodes.values()) {
			for (InputWidget inputWidget : nodeWidget.inputs.values()) {
				if (this.output.getType() == null) continue;
				if (inputWidget.isMouseOver(mouseX, mouseY) && inputWidget.input.getType().isAssignableFrom(this.output.getType())) {
					AutomationScreen.createConnection(this, inputWidget);
					return true;
				}
			}
		}
		AutomationScreen.instance.queueAddRedirectNode(this);
		return true;
	}



	@Override
	public NarrationPriority narrationPriority() {
		return NarrationPriority.HOVERED;
	}

	@Override
	public void updateNarration(NarrationElementOutput builder) {

	}

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
