/*
 * Copyright (c) 2023. $name
 */

package fr.artus25200.automations.client.gui.widget;


import fr.artus25200.automations.client.gui.screen.AutomationScreen;
import fr.artus25200.automations.client.gui.screen.EditFieldScreen;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.data.DataNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import static fr.artus25200.automations.client.AutomationsClient.getTr;

import fr.artus25200.automations.client.AutomationsClient;

public class EditableDataNodeWidget extends NodeWidget {

	public transient CButtonWidget editButton;

	public EditableDataNodeWidget(Node node) {
		super(node);
		createEditButton();
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		calculatePos();

		int fontheight = getTr().lineHeight;
		graphics.fill(x, y, x + width, y + 5 + fontheight + 2, this.node.getColor());
        graphics.outline(x, y, width, 5 + fontheight + 2, isMouseOver(mouseX, mouseY) ? AutomationsClient.OUTLINE_COLOR_HOVER : AutomationsClient.OUTLINE_COLOR);

		OutputWidget outputWidget = outputs.values().stream().toList().get(0);
		outputWidget.output.x = outputWidget.x;
		outputWidget.output.y = outputWidget.y;
		outputWidget.extractRenderState(graphics, mouseX, mouseY, delta);

		editButton.extractRenderState(graphics, mouseX, mouseY, delta);


		graphics.text(getTr(), this.node.getName(), this.x + 5, this.y + 5, 0xFFFFFFFF, false);
	}

	@Override
	public void calculatePos(){
		//TODO: Clean up CButtonwidget == null
		if(this.editButton== null) createEditButton();

		int fontheight = getTr().lineHeight;
		int margin = 5;

		this.editButton.setX(this.x + margin + getTr().width(this.node.getName()) + margin);
		this.editButton.setY(this.y + 3);

		this.width = margin + getTr().width(this.node.getName()) + margin + 20 + margin + OutputWidget.widthANDheight;
		this.height = margin + fontheight + margin;

		OutputWidget o = outputs.values().stream().toList().get(0);
		o.x = this.x + this.width - OutputWidget.widthANDheight;
		o.y = this.y + margin;
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		double mouseX = event.x();
		double mouseY = event.y();
		if(editButton.mouseClicked(event, doubleClick)) return true;
		for(OutputWidget outputWidget : outputs.values()) {
			if (outputWidget.isMouseOver(mouseX, mouseY)) {
				if (outputWidget.mouseClicked(event, doubleClick)) return true;
			}
		}
		if(this.isMouseOver(mouseX, mouseY)){
			this.dragging = true;
			this.dx = (int) (mouseX - this.x);
			this.dy = (int) (mouseY - this.y);
			return true;
		}
		return false;
	}

	public void createEditButton(){
		this.editButton = new CButtonWidget(0xFF101010, x+ 5 + getTr().width(node.getName())+ 5, y + 5, 20, 10, Component.literal("edit"), (b)-> Minecraft.getInstance().gui.setScreen(new EditFieldScreen((DataNode) this.node, AutomationScreen.instance)));
	}
}
