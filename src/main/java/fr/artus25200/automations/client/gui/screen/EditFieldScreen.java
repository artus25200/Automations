/*
 * Copyright (c) 2023. $name
 */

package fr.artus25200.automations.client.gui.screen;

import fr.artus25200.automations.common.node.data.DataNode;
import fr.artus25200.automations.common.node.data.field.BoolDataNode;
import fr.artus25200.automations.common.node.data.field.IntegerDataNode;
import fr.artus25200.automations.common.node.data.field.StringDataNode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static fr.artus25200.automations.client.AutomationsClient.getTr;

public class EditFieldScreen extends Screen {

	DataNode node;

	public static final int textBoxW = 220;
	public static final int textBoxH = 20;

	public boolean renderIntErrorText = false;
	public Text intErrorText = Text.literal("You must enter a valid integer.");

	Screen parent;

	public EditFieldScreen(DataNode node, Screen parent) {
		super(Text.literal("Edit String"));
		this.node = node;
		this.parent = parent;
	}

	@Override
	protected void init() {
		int wwidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
		int wheight = MinecraftClient.getInstance().getWindow().getScaledHeight();

		if(node instanceof StringDataNode stringDataNode) {
			TextFieldWidget textFieldWidget = new TextFieldWidget(getTr(), (wwidth - textBoxW) / 2, (wheight - textBoxH) / 2, textBoxW, textBoxH, Text.literal("String Edit"));
			textFieldWidget.setText(stringDataNode.getData());
			textFieldWidget.setMaxLength(1000);
			this.addDrawableChild(textFieldWidget);
		} else if(node instanceof IntegerDataNode integerDataNode){
			TextFieldWidget textFieldWidget = new TextFieldWidget(getTr(), (wwidth - textBoxW) / 2, (wheight - textBoxH) / 2, textBoxW, textBoxH, Text.literal("Integer Edit"));
			textFieldWidget.setText(Integer.toString(integerDataNode.getData()));
			textFieldWidget.setMaxLength(20);
			this.addDrawableChild(textFieldWidget);
		} else if (node instanceof BoolDataNode boolDataNode) {
			this.addDrawableChild(new CheckboxWidget(wwidth/2 - 10, wheight/2 - 10, 20, 20, Text.literal("Bolean True/False"), boolDataNode.getData()));
		}

		this.addDrawableChild(new ButtonWidget((wwidth)/2 - 100 - 10, (wheight-textBoxH)/2+30, 100, 20, Text.literal("Save & Close"), (button)->{
			if(node instanceof StringDataNode stringDataNode) {
				stringDataNode.setData(((TextFieldWidget) this.children().get(0)).getText());
			} else if (node instanceof IntegerDataNode integerDataNode){
				try {
					integerDataNode.setData(Integer.parseInt(((TextFieldWidget) this.children().get(0)).getText()));
				} catch (NumberFormatException ignored){
					renderIntErrorText = true;
					return;
				}
			} else if (node instanceof BoolDataNode boolDataNode) {
				boolDataNode.setData(((CheckboxWidget) this.children().get(0)).isChecked());
			}
			assert this.client != null;
			this.client.setScreen(this.parent);
		}));
		this.addDrawableChild(new ButtonWidget((wwidth)/2 + 10, (wheight-textBoxH)/2+30, 100, 20, Text.literal("Cancel"), (button)->{
			assert this.client != null;
			this.client.setScreen(this.parent);
		}));
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		if(renderIntErrorText){
			int wwidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
			int wheight = MinecraftClient.getInstance().getWindow().getScaledHeight();
			getTr().draw(matrices, intErrorText, ((float)(wwidth-getTr().getWidth(intErrorText)))/2, ((float)(wheight-getTr().fontHeight))/2, 0xFFfA6161);
		}
	}

	@Override
	public void renderBackground(MatrixStack matrices) {
		this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		renderIntErrorText = false;
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
