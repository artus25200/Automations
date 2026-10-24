/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import static fr.artus25200.automations.client.AutomationsClient.OUTLINE_COLOR;
import static fr.artus25200.automations.client.AutomationsClient.getTr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.client.gui.screen.AutomationScreen;
import fr.artus25200.automations.common.Automations;
import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.Output;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;

@Environment(EnvType.CLIENT)
public class NodeWidget implements Renderable, NarratableEntry, Serializable, RightClickable {

	public Node node;

	public LinkedHashMap<Input, InputWidget> inputs = new LinkedHashMap<>();
	public LinkedHashMap<Output, OutputWidget> outputs = new LinkedHashMap<>();

	public int x, y;
	public int width, height;

	public boolean dragging;
	public int dx, dy;

	public NodeWidget(Node node) {
		this.node = node;
		this.x = node.x;
		this.y = node.y;

		for (Input i : node.inputs) {
            InputWidget inputWidget = new InputWidget(i, this);
            AutomationScreen.instance.nodeList.inputs.put(i, inputWidget);
            this.inputs.put(i, inputWidget);
		}
		for (Output o : node.outputs) {
            OutputWidget outputWidget = new OutputWidget(o, this);
            AutomationScreen.instance.nodeList.outputs.put(o, outputWidget);
            this.outputs.put(o, outputWidget);
		}
	}

	public void delete(){
		AutomationScreen screen = AutomationScreen.instance;

		// Connections going out of this node
		for(OutputWidget ow : outputs.values()){
			for (ConnectionWidget cw : new ArrayList<>(ow.connections.values())){
				cw.delete();
			}
		}

		// Connections coming into this node
		for (InputWidget iw : inputs.values()) {
			if (iw.connectionWidget != null) iw.connectionWidget.delete();
		}

		screen.deleteNodeWidget(this);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		calculatePos();
		int fontheight = getTr().lineHeight;

		graphics.fill(x, y, x + width, y + height, 0xFF000000);
		graphics.fill(x, y, x + width, y + 5 + fontheight + 2, this.node.getColor());
        graphics.outline(x, y, width, height, isMouseOver(mouseX, mouseY) ? AutomationsClient.OUTLINE_COLOR_HOVER : AutomationsClient.OUTLINE_COLOR);

		for(InputWidget inputWidget : inputs.values()) {
			inputWidget.input.x = inputWidget.x;
			inputWidget.input.y = inputWidget.y;
			inputWidget.extractRenderState(graphics, mouseX, mouseY, delta);
		}
		for(OutputWidget outputWidget : outputs.values()) {
			outputWidget.output.x = outputWidget.x;
			outputWidget.output.y = outputWidget.y;
			outputWidget.extractRenderState(graphics, mouseX, mouseY, delta);
		}

		graphics.centeredText(getTr(), this.node.getName(), this.x + this.width / 2, this.y + 5, 0xFFFFFFFF);
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
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		double mouseX = event.x();
		double mouseY = event.y();
		for(InputWidget inputWidget : inputs.values()){
			if(inputWidget.isMouseOver(mouseX, mouseY)) {
				if(inputWidget.mouseClicked(event, doubleClick)) return true;
			}
		}
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

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		dragging = false;
		boolean released = false;
		for (InputWidget inputWidget : inputs.values()){
			if(inputWidget.mouseReleased(event)) released = true;
		}
		for (OutputWidget outputWidget : outputs.values()){
			if(outputWidget.mouseReleased(event)) released = true;
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

	public void calculatePos(){
		int fontheight = getTr().lineHeight;
		int margin = 5;
		int maxInputWidth = 0;
		int maxOutputWidth = 0;

		for (int i = 0; i < this.inputs.size(); i++) {
			this.inputs.values().stream().toList().get(i).x = this.x;
			this.inputs.values().stream().toList().get(i).y = this.y + (i + 1) * (margin + fontheight) + margin;

			int w = getTr().width(this.inputs.values().stream().toList().get(i).input.name);
			if(w > maxInputWidth){
				maxInputWidth = w;
			}
		}
		for(OutputWidget outputWidget : this.outputs.values()){
			int w = getTr().width(outputWidget.output.name);
			if(w > maxOutputWidth){
				maxOutputWidth = w;
			}
		}
		this.width = 5 + margin + maxInputWidth + margin + maxOutputWidth + margin + 5;
		if(getTr().width(this.node.getName()) + margin*2 > this.width){
			this.width = getTr().width(this.node.getName()) + margin*2;
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
		}));
		entries.add(new ContextMenuWidget.MenuEntry("Duplicate", (nodeWidget) -> {
			Node original = ((NodeWidget)nodeWidget).node;
            Node duplicatedNode = original.getClass().getDeclaredConstructor().newInstance();
            duplicatedNode.x = ((NodeWidget)nodeWidget).x + 20;
            duplicatedNode.y = ((NodeWidget)nodeWidget).y + 20;
			AutomationScreen.instance.addNode(duplicatedNode);
		}));
		return new ContextMenuWidget((int) mouseX, (int) mouseY, this, entries);
	}

}
