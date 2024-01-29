/*
 * Copyright (c) 2024. $name
 */

package fr.artus25200.automations.client.gui.widget;

import fr.artus25200.automations.common.node.Node;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class RedirectNodeWidget extends NodeWidget{
	public InputWidget inputWidget;
	public OutputWidget outputWidget;

	public RedirectNodeWidget(Node node, int x, int y) {
		super(node, x, y);
		this.inputWidget = this.inputs.values().stream().toList().get(0);
		this.outputWidget = this.outputs.values().stream().toList().get(0);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		calculatePos();
		int IOcolor = this.node.inputs.get(0).isConnected() ? this.node.inputs.get(0).connector.output.color : 0xFFFFFFFF;
		DrawableHelper.fill(matrices, x, y, x + 15, y + 5, 0xFF000000);
		inputWidget.input.color = IOcolor;
		outputWidget.output.color = IOcolor;
		if(inputWidget.input.isConnected()){
			inputWidget.input.type = inputWidget.input.connector.output.getType();
			outputWidget.output.type = inputWidget.input.type;
		}
		inputWidget.render(matrices, mouseX, mouseY, delta);
		outputWidget.render(matrices, mouseX, mouseY, delta);

	}

	@Override
	public void calculatePos() {
		this.height = 5;
		this.width = 15;
		this.inputWidget.x = this.x;
		this.inputWidget.y = this.y;
		this.outputWidget.x = this.x + 10;
		this.outputWidget.y = this.y;

		inputWidget.input.x = inputWidget.x;
		inputWidget.input.y = inputWidget.y;
		outputWidget.output.x = outputWidget.x;
		outputWidget.output.y = outputWidget.y;
	}


}
