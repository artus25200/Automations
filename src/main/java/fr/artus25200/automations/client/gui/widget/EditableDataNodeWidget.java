/*
 * Copyright (c) 2023. $name
 */

package fr.artus25200.automations.client.gui.widget;


import fr.artus25200.automations.client.gui.screen.AutomationScreen;
import fr.artus25200.automations.client.gui.screen.EditFieldScreen;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.data.DataNode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static fr.artus25200.automations.client.AutomationsClient.getTr;

public class EditableDataNodeWidget extends NodeWidget {

	public transient CButtonWidget editButton;

	public EditableDataNodeWidget(Node node, int x, int y) {
		super(node, x, y);
		createEditButton();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		calculatePos();

		int fontheight = getTr().fontHeight;
		DrawableHelper.fill(matrices, x, y, x + width, y + 5 + fontheight + 2, this.node.getColor());

		OutputWidget outputWidget = outputs.values().stream().toList().get(0);
		outputWidget.output.x = outputWidget.x;
		outputWidget.output.y = outputWidget.y;
		outputWidget.render(matrices, mouseX, mouseY, delta);

		editButton.render(matrices, mouseX, mouseY, delta);


		getTr().draw(matrices, this.node.getName(), this.x + 5, this.y + 5, 0xFFFFFFFF);
	}

	@Override
	public void calculatePos(){
		//TODO: Clean up CButtonwidget == null
		if(this.editButton== null) createEditButton();

		int fontheight = getTr().fontHeight;
		int margin = 5;

		this.editButton.x = this.x + margin + getTr().getWidth(this.node.getName()) + margin;
		this.editButton.y = this.y + 3;

		this.width = margin + getTr().getWidth(this.node.getName()) + margin + 20 + margin + OutputWidget.widthANDheight;
		this.height = margin + fontheight + margin;

		OutputWidget o = outputs.values().stream().toList().get(0);
		o.x = this.x + this.width - OutputWidget.widthANDheight;
		o.y = this.y + margin;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(editButton.mouseClicked(mouseX, mouseY, button)) return true;
		for(OutputWidget outputWidget : outputs.values()) {
			if (outputWidget.isMouseOver(mouseX, mouseY)) {
				if (outputWidget.mouseClicked(mouseX, mouseY, button)) return true;
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
		this.editButton = new CButtonWidget(0xFF101010, x+ 5 + getTr().getWidth(node.getName())+ 5, y + 5, 20, 10, Text.literal("edit"), (b)-> MinecraftClient.getInstance().setScreen(new EditFieldScreen((DataNode) this.node, AutomationScreen.instance)));
	}
}
