/*
 * Copyright (c) 2023. $name
 */

package fr.artus25200.automations.client.gui.screen;

import fr.artus25200.automations.common.node.data.DataNode;
import fr.artus25200.automations.common.node.data.field.BoolDataNode;
import fr.artus25200.automations.common.node.data.field.IntegerDataNode;
import fr.artus25200.automations.common.node.data.field.StringDataNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import static fr.artus25200.automations.client.AutomationsClient.getTr;

public class EditFieldScreen extends Screen {

	DataNode node;

	public static final int textBoxW = 220;
	public static final int textBoxH = 20;

	public boolean renderIntErrorText = false;
	public Component intErrorText = Component.literal("You must enter a valid integer.");

	Screen parent;

	public EditFieldScreen(DataNode node, Screen parent) {
		super(Component.literal("Edit String"));
		this.node = node;
		this.parent = parent;
	}

	@Override
	protected void init() {
		int wwidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int wheight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

		if(node instanceof StringDataNode stringDataNode) {
			EditBox textFieldWidget = new EditBox(getTr(), (wwidth - textBoxW) / 2, (wheight - textBoxH) / 2, textBoxW, textBoxH, Component.literal("String Edit"));
			textFieldWidget.setValue(stringDataNode.getData());
			textFieldWidget.setMaxLength(1000);
			this.addRenderableWidget(textFieldWidget);
		} else if(node instanceof IntegerDataNode integerDataNode){
			EditBox textFieldWidget = new EditBox(getTr(), (wwidth - textBoxW) / 2, (wheight - textBoxH) / 2, textBoxW, textBoxH, Component.literal("Integer Edit"));
			textFieldWidget.setValue(Integer.toString(integerDataNode.getData()));
			textFieldWidget.setMaxLength(20);
			this.addRenderableWidget(textFieldWidget);
		} else if (node instanceof BoolDataNode boolDataNode) {
			this.addRenderableWidget(Checkbox.builder(Component.literal("Bolean True/False"), getTr()).pos(wwidth/2 - 10, wheight/2 - 10).selected(boolDataNode.getData()).build());
		}

		this.addRenderableWidget(Button.builder(Component.literal("Save & Close"), (button)->{
			if(node instanceof StringDataNode stringDataNode) {
				stringDataNode.setData(((EditBox) this.children().get(0)).getValue());
			} else if (node instanceof IntegerDataNode integerDataNode){
				try {
					integerDataNode.setData(Integer.parseInt(((EditBox) this.children().get(0)).getValue()));
				} catch (NumberFormatException ignored){
					renderIntErrorText = true;
					return;
				}
			} else if (node instanceof BoolDataNode boolDataNode) {
				boolDataNode.setData(((Checkbox) this.children().get(0)).selected());
			}
			assert this.minecraft != null;
			this.minecraft.gui.setScreen(this.parent);
		}).bounds((wwidth)/2 - 100 - 10, (wheight-textBoxH)/2+30, 100, 20).build());
		this.addRenderableWidget(Button.builder(Component.literal("Cancel"), (button)->{
			assert this.minecraft != null;
			this.minecraft.gui.setScreen(this.parent);
		}).bounds((wwidth)/2 + 10, (wheight-textBoxH)/2+30, 100, 20).build());
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		super.extractRenderState(graphics, mouseX, mouseY, delta);
		if(renderIntErrorText){
			int wwidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
			int wheight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
			graphics.text(getTr(), intErrorText, (wwidth-getTr().width(intErrorText))/2, (wheight-getTr().lineHeight)/2, 0xFFfA6161, false);
		}
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		graphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		renderIntErrorText = false;
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
