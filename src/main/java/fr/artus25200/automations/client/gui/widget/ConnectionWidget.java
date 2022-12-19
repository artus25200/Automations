/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.client.gui.screen.AutomationScreen;
import fr.artus25200.automations.common.Automations;
import fr.artus25200.automations.common.NodeWrapper;
import fr.artus25200.automations.common.node.Connection;
import fr.artus25200.automations.common.node.Node;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ConnectionWidget implements Drawable, Element, Selectable,Serializable, RightClickable {
	public Connection connection;
	public OutputWidget outputWidget;
	public InputWidget inputWidget;

	public ConnectionWidget(Connection connection){
		this.connection   = connection;
		this.outputWidget = AutomationsClient.nodeWrapper.outputs.get(connection.output);
		this.inputWidget  = AutomationsClient.nodeWrapper.inputs.get(connection.input);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		drawLine(3, connection.output.x + 4, connection.output.y + 4, connection.input.x + 4, connection.input.y + 4, outputWidget.output.color);
	}

	public static void drawLine(int width, int x1, int y1, int x2, int y2, int color){
		Tessellator tl = Tessellator.getInstance();
		BufferBuilder bb = tl.getBuffer();

		RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
		RenderSystem.enableDepthTest();
		RenderSystem.lineWidth(width);
		bb.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
		int xdiff = x2 - x1;
		int ydiff = y2 - y1;
		Vec2f v = new Vec2f(xdiff, ydiff).normalize();

		if(ydiff > 0){
			bb.vertex(x1, y1, 0).color(color).normal(v.x, v.y, 0).next();
			bb.vertex(x2, y2, 0).color(color).normal(v.x, v.y, 0).next();
		}else if( ydiff < 0){
			bb.vertex(x2, y2, 0).color(color).normal(v.x, v.y, 0).next();
			bb.vertex(x1, y1, 0).color(color).normal(v.x, v.y, 0).next();
		}
		else{
			bb.vertex(x1, y1, 0).color(color).normal(xdiff, ydiff, 0).next();
			bb.vertex(x2, y2, 0).color(color).normal(xdiff, ydiff, 0).next();
		}

		BufferRenderer.drawWithShader(bb.end());
		RenderSystem.enableTexture();
	}

	public void delete(){
		this.connection.delete();
		this.inputWidget.connectionWidget = null;
		this.outputWidget.connections.remove(this.connection);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		int var = Math.abs(this.inputWidget.y - this.outputWidget.y) / Math.abs(this.inputWidget.x - this.outputWidget.x);
		return Math.abs(mouseX * var - mouseY) <= 3;
	}

	@Override
	public SelectionType getType() {
		return null;
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {

	}

	@Override
	public ContextMenuWidget onRightCLick(double mouseX, double mouseY) {
		List<ContextMenuWidget.MenuEntry> entries = new ArrayList<>();
		entries.add(new ContextMenuWidget.MenuEntry("Delete", (connectionWidget) -> {
			((ConnectionWidget) connectionWidget).delete();
		}));
		return new ContextMenuWidget((int)mouseX, (int)mouseY, this, entries);
	}
}
