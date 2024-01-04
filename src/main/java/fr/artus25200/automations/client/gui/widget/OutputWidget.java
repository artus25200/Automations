/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.client.gui.screen.AutomationScreen;
import fr.artus25200.automations.common.node.Connection;
import fr.artus25200.automations.common.node.Output;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;

import java.io.Serializable;
import java.util.HashMap;

import static fr.artus25200.automations.client.AutomationsClient.darkenColor;
import static fr.artus25200.automations.client.AutomationsClient.getTr;

@Environment(EnvType.CLIENT)
public class OutputWidget implements Drawable, Selectable, Element, Serializable, RightClickable {
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

		AutomationsClient.nodeList.connections.putAll(connections);
	}

	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		int color = isMouseOver(mouseX,mouseY) ? darkenColor(this.output.color, 50) : this.output.color;

		DrawableHelper.fill(matrices, this.x, this.y, this.x + widthANDheight, this.y + widthANDheight, color);
		if(!(parent instanceof EditableDataNodeWidget)){
			getTr().draw(matrices, output.name, this.x - getTr().getWidth(output.name) - 5, y, 0xFFFFFFFF);
		}

		for (ConnectionWidget cw : connections.values()){
			cw.render(matrices, mouseX, mouseY, delta);
		}

		if(this.dragging){
			ConnectionWidget.drawLine(3, this.x+4, this.y+4, mouseX, mouseY, this.output.color);
		}
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseX >= this.x && mouseX <= this.x+widthANDheight && mouseY >= this.y && mouseY <= this.y + widthANDheight;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(this.isMouseOver(mouseX, mouseY) && button == 0){
			dragging = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if(!this.dragging) return true;
		this.dragging = false;
		loop:
		for (NodeWidget nodeWidget : AutomationsClient.nodeList.nodes.values()) {
			for (InputWidget inputWidget : nodeWidget.inputs.values()) {
				if (this.output.getType() == null) continue;
				if (inputWidget.isMouseOver(mouseX, mouseY) && inputWidget.input.getType().isAssignableFrom(this.output.getType())) {
					AutomationScreen.createConnection(this, inputWidget);
					break loop;
				}
			}
		}
		return true;
	}



	@Override
	public SelectionType getType() {
		return SelectionType.HOVERED;
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {

	}

	@Override
	public ContextMenuWidget onRightCLick(double mouseX, double mouseY) {
		//TODO
		return null;
	}
}
