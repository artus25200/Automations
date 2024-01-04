/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import com.google.gson.Gson;
import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.client.gui.screen.AutomationScreen;
import fr.artus25200.automations.common.Automations;
import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Node;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static fr.artus25200.automations.client.AutomationsClient.getTr;

@Environment(EnvType.CLIENT)
public class NodeWidget implements Drawable, Element, Selectable, Serializable, RightClickable {

	public Node node;

	public LinkedHashMap<Input, InputWidget> inputs = new LinkedHashMap<>();
	public LinkedHashMap<Output, OutputWidget> outputs = new LinkedHashMap<>();

	public int x, y;
	public int width, height;

	public boolean dragging;
	public int dx, dy;

	public NodeWidget(Node node, int x, int y) {
		this.node = node;
		this.x = x;
		this.y = y;

		for (Input i : node.inputs) {
			inputs.put(i, new InputWidget(i, this));
		}
		for (Output o : node.outputs) {
			outputs.put(o, new OutputWidget(o, this));
		}

		AutomationsClient.nodeList.nodes.put(node, this);
		AutomationsClient.nodeList.outputs.putAll(outputs);
		AutomationsClient.nodeList.inputs.putAll(inputs);
	}

	public void delete(){
		for(OutputWidget ow : outputs.values()){
			for (ConnectionWidget cw : ow.connections.values()){
				cw.delete();
			}
		}
		for(InputWidget iw : inputs.values()){
			if(iw.connectionWidget != null) iw.connectionWidget.delete();
		}
		this.node.delete();

	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		calculatePos();
		int fontheight = getTr().fontHeight;

		DrawableHelper.fill(matrices, x, y, x + width, y + height, 0xFF000000);
		DrawableHelper.fill(matrices, x, y, x + width, y + 5 + fontheight + 2, this.node.getColor());

		for(InputWidget inputWidget : inputs.values()) {
			inputWidget.input.x = inputWidget.x;
			inputWidget.input.y = inputWidget.y;
			inputWidget.render(matrices, mouseX, mouseY, delta);
		}
		for(OutputWidget outputWidget : outputs.values()) {
			outputWidget.output.x = outputWidget.x;
			outputWidget.output.y = outputWidget.y;
			outputWidget.render(matrices, mouseX, mouseY, delta);
		}

		DrawableHelper.drawCenteredText(matrices, getTr(), this.node.getName(), this.x + this.width / 2, this.y + 5, 0xFFFFFFFF);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		for(InputWidget inputWidget : inputs.values()) {
			if(inputWidget.isMouseOver(mouseX, mouseY)) return false;
		}
		for(OutputWidget outputWidget : outputs.values()) {
			if(outputWidget.isMouseOver(mouseX, mouseY)) return false;
		}
		return (mouseX >= this.x ) && (mouseX <= this.x + this.width) && (mouseY >= this.y) && (mouseY <= this.y + this.height);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for(InputWidget inputWidget : inputs.values()){
			if(inputWidget.isMouseOver(mouseX, mouseY)) {
				if(inputWidget.mouseClicked(mouseX, mouseY, button)) return true;
			}
		}
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

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		dragging = false;
		boolean released = false;
		for (InputWidget inputWidget : inputs.values()){
			if(inputWidget.mouseReleased(mouseX, mouseY, button)) released = true;
		}
		for (OutputWidget outputWidget : outputs.values()){
			if(outputWidget.mouseReleased(mouseX, mouseY, button)) released = true;
		}
		return released;
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		for (InputWidget inputWidget : inputs.values()){
			inputWidget.mouseMoved(mouseX, mouseY);
		}
		for (OutputWidget outputWidget : outputs.values()){
			outputWidget.mouseMoved(mouseX, mouseY);
		}
		if(dragging){
			x = (int) (mouseX - dx);
			y = (int) (mouseY - dy);
		}
	}

	@Override
	public SelectionType getType() {
		return SelectionType.HOVERED;
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {

	}

	public void calculatePos(){
		int fontheight = getTr().fontHeight;
		int margin = 5;
		int maxInputWidth = 0;
		int maxOutputWidth = 0;

		for (int i = 0; i < this.inputs.size(); i++) {
			this.inputs.values().stream().toList().get(i).x = this.x;
			this.inputs.values().stream().toList().get(i).y = this.y + (i + 1) * (margin + fontheight) + margin;

			int w = getTr().getWidth(this.inputs.values().stream().toList().get(i).input.name);
			if(w > maxInputWidth){
				maxInputWidth = w;
			}
		}
		for(OutputWidget outputWidget : this.outputs.values()){
			int w = getTr().getWidth(outputWidget.output.name);
			if(w > maxOutputWidth){
				maxOutputWidth = w;
			}
		}
		this.width = 5 + margin + maxInputWidth + margin + maxOutputWidth + margin + 5;
		if(getTr().getWidth(this.node.getName()) + margin*2 > this.width){
			this.width = getTr().getWidth(this.node.getName()) + margin*2;
		}

		for (int i = 0; i < this.outputs.size(); i++) {
			OutputWidget o = this.outputs.values().stream().toList().get(i);
			o.x = this.x + this.width - 5;
			o.y = this.y + (i + 1) * (margin + fontheight) + margin;
		}

		int maxIOSize = Math.max(this.inputs.size(), this.outputs.size());
		this.height = (maxIOSize + 1)*(margin + fontheight) + margin;
	}

	@Override
	public ContextMenuWidget onRightCLick(double mouseX, double mouseY) {
		List<ContextMenuWidget.MenuEntry> entries = new ArrayList<>();
		entries.add(new ContextMenuWidget.MenuEntry("Delete", (nodeWidget) -> {
			((NodeWidget) nodeWidget).delete();
			AutomationScreen.instance.reload();
		}));
		entries.add(new ContextMenuWidget.MenuEntry("Duplicate", (nodeWidget) -> {
			Node original = ((NodeWidget)nodeWidget).node;
			Node duplicatedNode = (Node) Automations.duplicateObject(original);
			AutomationScreen.instance.addNode(duplicatedNode);
		}));
		return new ContextMenuWidget((int) mouseX, (int) mouseY, this, entries);
	}
}
