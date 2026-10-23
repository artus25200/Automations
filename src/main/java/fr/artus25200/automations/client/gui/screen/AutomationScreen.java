/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.screen;


import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.client.gui.widget.*;
import fr.artus25200.automations.common.Automations;
import fr.artus25200.automations.common.networking.NodeListC2SPacket;
import fr.artus25200.automations.common.node.Connection;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.Nodes;
import fr.artus25200.automations.common.node.RedirectNode;
import fr.artus25200.automations.common.node.action.Action;
import fr.artus25200.automations.common.node.action.ActionNode;
import fr.artus25200.automations.common.node.data.DataNode;
import fr.artus25200.automations.common.node.data.field.Editable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


@Environment(EnvType.CLIENT)
public class AutomationScreen extends Screen implements RightClickable{

    public static AutomationScreen instance;

    public AutomationScreen() {
        super(Component.literal("Automations Configuration Screen"));
    }

    public ContextMenuWidget contextMenuWidget;

    public List<RightClickable> rightClickables = new ArrayList<>();

    public boolean saved;

    private OutputWidget queuedRedirectOutputWidget;
    private boolean queuedRedirect;

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        // the background is extracted by Screen#extractRenderState (see extractBackground below)
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        if(contextMenuWidget!= null) contextMenuWidget.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    public void addNode(Node n){
        AutomationsClient.nodeList.savedNodes.add(n);
        this.refresh();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        graphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
    }

    public void refresh2(){
        this.clearWidgets();
        AutomationsClient.nodeList.nodes.forEach((node, nodeWidget) -> {
            this.addRenderableWidget(nodeWidget);
            rightClickables.add(nodeWidget);
            rightClickables.addAll(nodeWidget.inputs.values());
            for(OutputWidget o : nodeWidget.outputs.values()){
                rightClickables.add(o);
                rightClickables.addAll(o.connections.values());
            }
        });

        this.addRenderableWidget(new CButtonWidget(0xFF000000, 10, 10, 100, 20, Component.literal("Save and Reload"), (button) -> this.saveAndReload()));

        this.addRenderableWidget(new CButtonWidget(0xFF000000,120, 10, 100, 20, Component.literal("Add Node"), (button -> Minecraft.getInstance().gui.setScreen(new ChooseNodeScreen(100, 100, this)))));
    }

    public void refresh(){
        this.clearWidgets();
        this.rightClickables.clear();

        AutomationsClient.nodeList.savedNodes.forEach((node) -> {
                NodeWidget widget = new NodeWidget(node);
                if(node instanceof DataNode && node instanceof Editable){
                    widget = new EditableDataNodeWidget(node);
                } else if (node instanceof RedirectNode) {
                    widget = new RedirectNodeWidget(node);
                } else {
                    widget = new NodeWidget(node);
                }
                this.addRenderableWidget(widget);
                rightClickables.add(widget);
            });

        this.addRenderableWidget(new CButtonWidget(0xFF000000, 10, 10, 100, 20, Component.literal("Save and Reload"), (button) -> this.saveAndReload()));
        this.addRenderableWidget(new CButtonWidget(0xFF000000,120, 10, 100, 20, Component.literal("Add Node"), (button -> Minecraft.getInstance().gui.setScreen(new ChooseNodeScreen(100, 100, this)))));
    }

    @Override
    protected void init() {
        super.init();
        instance = this;
        this.refresh();

    }

    public static void createConnection(OutputWidget outputWidget, InputWidget inputWidget){
        if(inputWidget.connectionWidget != null) inputWidget.connectionWidget.delete();
        // Verify that the connection is allowed.
        NodeWidget one = outputWidget.parent;
        NodeWidget two = inputWidget.parent;

        if((one.node.categories.contains(Nodes.NodeCategory.EVENT) || one.node.categories.contains(Nodes.NodeCategory.ACTION)) && two.node.categories.contains(Nodes.NodeCategory.ACTION) && outputWidget.output.getType() != Action.class){
            if(((ActionNode)two.node).actionInput.isConnected()){
                if(((ActionNode)two.node).actionInput.connector.output.parent != one.node) return;
            }
            else {
                return;
            }
        }

        // end of verification


        Connection connection = Connection.createConnection(outputWidget.output, inputWidget.input);
        if(connection == null) return;
        ConnectionWidget connectionWidget = new ConnectionWidget(connection);
        outputWidget.connections.put(connection, connectionWidget);
        inputWidget.connectionWidget = connectionWidget;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();
        if(event.button() == InputConstants.MOUSE_BUTTON_RIGHT){
            // context Menu
            for(RightClickable rightClickable: this.rightClickables){
                if(rightClickable.isMouseOver(mouseX, mouseY)){
                    contextMenuWidget = rightClickable.onRightCLick(mouseX, mouseY);
                    return true;
                }
            }
            contextMenuWidget = this.onRightCLick(mouseX, mouseY);
        } else {
            if(contextMenuWidget != null){
                contextMenuWidget.mouseClicked(event, doubleClick);
                contextMenuWidget = null;
            }
            for (GuiEventListener e : this.children()) {
                if(e.mouseClicked(event, doubleClick)) return true;
            }
        }
        return true;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        double mouseX = event.x();
        double mouseY = event.y();
        for (GuiEventListener e : this.children()) {
            e.mouseReleased(event);
        }
        if(queuedRedirect){
            RedirectNode redirectNode =  new RedirectNode();
            redirectNode.x = (int) mouseX;
            redirectNode.y = (int) mouseY;
            this.addNode(redirectNode);
            AutomationScreen.createConnection(queuedRedirectOutputWidget, ((RedirectNodeWidget)AutomationsClient.nodeList.nodes.get(redirectNode)).inputWidget);
            queuedRedirectOutputWidget = null;
            queuedRedirect = false;
        }
        return true;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        for (GuiEventListener e : this.children()) {
            e.mouseMoved(mouseX, mouseY);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void saveAndReload(){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(byteArrayOutputStream);
            os.writeObject(AutomationsClient.nodeList);
            os.close();
            ClientPlayNetworking.send(new NodeListC2SPacket.Payload(byteArrayOutputStream.toByteArray()));
            this.saved = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ContextMenuWidget onRightCLick(double mouseX, double mouseY) {
        List<ContextMenuWidget.MenuEntry> entries = new ArrayList<>();
        entries.add(new ContextMenuWidget.MenuEntry("Add Node", (nodeWidget) -> {
            assert this.minecraft != null;
            this.minecraft.gui.setScreen(new ChooseNodeScreen(mouseX, mouseY, this));
        }));
        entries.add(new ContextMenuWidget.MenuEntry("Save and Quit", (nodeWidget) -> {
            this.saveAndReload();
            assert this.minecraft != null;
            this.minecraft.gui.setScreen(null);
        }));
        entries.add(new ContextMenuWidget.MenuEntry("Quit without Saving", (nodeWidget) -> {
            assert this.minecraft != null;
            this.minecraft.gui.setScreen(null);
        }));
        return new ContextMenuWidget((int)mouseX, (int)mouseY, this, entries);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if(event.key() == InputConstants.KEY_ESCAPE){
            if(saved) {
                this.onClose();
                return true;
            }
            else{
                assert this.minecraft != null;
                this.minecraft.gui.setScreen(new ConfirmationScreen(this, (save) -> {
                    if(save) {
                        saveAndReload();
                        this.onClose();
                    }
                    else{
                        this.onClose();
                    }
                }, Component.literal("Not Saved"), Component.literal("Do you want to quit without saving ?"), Component.literal("Save and quit"), Component.literal("Quit without saving")));
            }
        }
        return super.keyPressed(event);
    }

    public void queueAddRedirectNode(OutputWidget outputWidget){
        this.queuedRedirectOutputWidget = outputWidget;
        this.queuedRedirect = true;
    }

    public static Vec2 bezier(double t, Vec2 start, Vec2 end, Vec2 p1, Vec2 p2){
        return start.scale((float) Math.pow(1-t, 3)).add(p1.scale((float) (t*3* Math.pow(1-t, 2)))).add(p2.scale((float) (3*(1-t)*t*t))).add(end.scale((float) Math.pow(t,3)));
    }

    public static Vec2 bezierNodeConnection(double t, Vec2 start, Vec2 end){
        Vec2 p1 = new Vec2((float) (start.x + 0.75 * (end.x - start.x)), start.y);
        Vec2 p2 = new Vec2((float) (end.x - 0.75 * (end.x - start.x)), end.y);
        return bezier(t, start, end, p1, p2);
    }
}
