/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.client.gui.screen.AutomationScreen;
import fr.artus25200.automations.common.node.Connection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.world.phys.Vec2;

@Environment(EnvType.CLIENT)
public class ConnectionWidget implements Renderable, GuiEventListener, NarratableEntry, Serializable, RightClickable {
    public Connection connection;
    public OutputWidget outputWidget;
    public InputWidget inputWidget;

    private transient int prevX1;
    private transient int prevY1;

    private transient int prevX2;
    private transient int prevY2;

    public static final double BEZIER_PRECISION = 30.0;
    // Points to render lines in between
    public transient ArrayList<Vec2> curvePoints = new ArrayList<>();

    public ConnectionWidget(Connection connection, OutputWidget outputWidget, InputWidget inputWidget) {
        this.connection = connection;
        this.outputWidget = outputWidget;
        this.inputWidget = inputWidget;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        if (prevX1 != outputWidget.x || prevY1 != outputWidget.y || prevX2 != inputWidget.x || prevY2 != inputWidget.y)
            curvePoints = calculateBezier(outputWidget.x, inputWidget.x, outputWidget.y, inputWidget.y);
        prevX1 = outputWidget.x;
        prevY1 = outputWidget.y;
        prevX2 = inputWidget.x;
        prevY2 = inputWidget.y;
        // int color = isMouseOver(mouseX, mouseY) ?
        // AutomationsClient.darkenColor(outputWidget.output.color, 50) :
        // outputWidget.output.color;
        int color = outputWidget.output.color;
        renderBezier(graphics, color);
        // drawLine(3, connection.output.x + 4, connection.output.y + 4,
        // connection.input.x + 4, connection.input.y + 4, color);
    }

    private void renderBezier(GuiGraphicsExtractor graphics, int color) {
        for (int i = 0; i < curvePoints.size() - 1; i++) {
            drawLine(graphics, 3f, curvePoints.get(i).x + 4, curvePoints.get(i).y + 3,
                     curvePoints.get(i + 1).x + 4, curvePoints.get(i + 1).y + 3, color - 0x40000000);
            drawLine(graphics, 2, curvePoints.get(i).x + 4, curvePoints.get(i).y + 3,
                     curvePoints.get(i + 1).x + 4, curvePoints.get(i + 1).y + 3, color);
        }
    }

    public static void drawLine2(GuiGraphicsExtractor graphics, int width, int x1, int y1, int x2, int y2, int color) {
        // Tessellator/RenderSystem immediate mode drawing was removed from the game, so
        // the line is now
        // drawn as a rotated filled rectangle going from (x1, y1) to (x2, y2).
        float xdiff = x2 - x1;
        float ydiff = y2 - y1;
        float length = (float) Math.sqrt(xdiff * xdiff + ydiff * ydiff) + 1;
        if (length == 0)
            return;

        graphics.pose().pushMatrix();
        graphics.pose().translate(x1, y1);
        graphics.pose().rotate((float) Math.atan2(ydiff, xdiff));
        graphics.fill(0, -(width / 2), Math.round(length), width - (width / 2), color);
        graphics.pose().popMatrix();
    }

    public static void drawLine(GuiGraphicsExtractor g, float width, float x1, float y1, float x2, float y2,
            int color) {
        float dx = x2 - x1, dy = y2 - y1;
        float length = (float) Math.sqrt(dx * dx + dy * dy) + 0.5f;
        if (length == 0)
            return;

        g.pose().pushMatrix();
        g.pose().translate(x1, y1);
        g.pose().rotate((float) Math.atan2(dy, dx));
        g.pose().translate(0, -width / 2);
        g.pose().scale(length, width);
        g.fill(0, 0, 1, 1, color);
        g.pose().popMatrix();
    }

    public void delete() {
        if (this.inputWidget != null && this.inputWidget.connectionWidget == this)
            this.inputWidget.connectionWidget = null;
        this.outputWidget.connections.remove(this.connection);
        AutomationScreen.instance.nodeList.connections.remove(this.connection);
        this.connection.delete();
    }

    public static ArrayList<Vec2> calculateBezier(int x1, int x2, int y1, int y2) {
        // double bezierPrecision = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)) / 5.0;
        double bezierPrecision = BEZIER_PRECISION;
        if (Math.abs(y2 - y1) <= 2)
            bezierPrecision = 1;
        ArrayList<Vec2> curvePoints = new ArrayList<>();
        curvePoints.removeAll(curvePoints.stream().toList());
        double t = 0;
        Vec2 start = new Vec2(x1, y1);
        Vec2 end = new Vec2(x2, y2);
        while (t <= 1) {
            curvePoints.add(AutomationScreen.bezierNodeConnection(t, start, end));
            t = t + (1 / bezierPrecision);
        }

        curvePoints.add(AutomationScreen.bezierNodeConnection(1, start, end));
        return curvePoints;
    }

    public static void drawBezier(GuiGraphicsExtractor graphics, int color, int x1, int x2, int y1, int y2) {
        ArrayList<Vec2> curvePoints = calculateBezier(x1, x2, y1, y2);
        for (int i = 0; i < curvePoints.size() - 1; i++) {
            drawLine(graphics, 3, curvePoints.get(i).x + 4, curvePoints.get(i).y + 3,
                     curvePoints.get(i + 1).x + 4, curvePoints.get(i + 1).y + 3, color - 0x40000000);
            drawLine(graphics, 2, curvePoints.get(i).x + 4, curvePoints.get(i).y + 3,
                     curvePoints.get(i + 1).x + 4, curvePoints.get(i + 1).y + 3, color);
        }
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
        float px = inputWidgetX - outputWidgetX;
        float py = inputWidgetY - outputWidgetY;
        float temp = (px * px) + (py * py);
        float u = (float) (((mouseX - outputWidgetX) * px + (mouseY - outputWidgetY) * py) / (temp));
        if (u > 1) {
            u = 1;
        } else if (u < 0) {
            u = 0;
        }
        float x = outputWidgetX + u * px;
        float y = outputWidgetY + u * py;

        float dx = (float) (x - mouseX);
        float dy = (float) (y - mouseY);
        double dist = Math.sqrt(dx * dx + dy * dy);
        return false;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput builder) {

    }

    @Override
    public void setFocused(boolean focused) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public ContextMenuWidget onRightCLick(double mouseX, double mouseY) {
        List<ContextMenuWidget.MenuEntry> entries = new ArrayList<>();
        entries.add(new ContextMenuWidget.MenuEntry("Delete",
                (connectionWidget) -> ((ConnectionWidget) connectionWidget).delete()));
        return new ContextMenuWidget((int) mouseX, (int) mouseY, this, entries);
    }
}
