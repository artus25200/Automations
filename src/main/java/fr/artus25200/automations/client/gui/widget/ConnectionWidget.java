/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.common.node.Connection;
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
		this.outputWidget = AutomationsClient.nodeList.outputs.get(connection.output);
		this.inputWidget  = AutomationsClient.nodeList.inputs.get(connection.input);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		//int color = isMouseOver(mouseX, mouseY) ? AutomationsClient.darkenColor(outputWidget.output.color, 50) : outputWidget.output.color;
		int color = outputWidget.output.color;
		drawLine(3, connection.output.x + 4, connection.output.y + 4, connection.input.x + 4, connection.input.y + 4, color);
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
		// x3 : mouseX
		// y3 : mouseY
		// x1 : this.outputWidget.x
		// y1 : this.outputWidget.y
		// x2 : this.inputWidget.x
		// y2 : this.inputWidget.y
		int inputWidgetX = this.inputWidget.x + 4;
		int inputWidgetY = this.inputWidget.y + 4;
		int outputWidgetX = this.outputWidget.x + 4;
		int outputWidgetY = this.outputWidget.y + 4;


		// source : https://stackoverflow.com/a/2233538
		float px=inputWidgetX-outputWidgetX;
		float py=inputWidgetY-outputWidgetY;
		float temp=(px*px)+(py*py);
		float u = (float) (((mouseX - outputWidgetX) * px + (mouseY - outputWidgetY) * py) / (temp));
		if(u>1){
			u=1;
		}
		else if(u<0){
			u=0;
		}
		float x = outputWidgetX + u * px;
		float y = outputWidgetY + u * py;

		float dx = (float) (x - mouseX);
		float dy = (float) (y - mouseY);
		double dist = Math.sqrt(dx*dx + dy*dy);
		return dist <= 4;
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
		entries.add(new ContextMenuWidget.MenuEntry("Delete", (connectionWidget) -> ((ConnectionWidget) connectionWidget).delete()));
		return new ContextMenuWidget((int)mouseX, (int)mouseY, this, entries);
	}
}
