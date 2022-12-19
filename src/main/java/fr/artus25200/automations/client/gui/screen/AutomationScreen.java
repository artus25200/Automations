/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.screen;


import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.client.gui.widget.*;
import fr.artus25200.automations.common.networking.ModNetworking;
import fr.artus25200.automations.common.node.Connection;
import fr.artus25200.automations.common.node.Node;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class AutomationScreen extends Screen implements RightClickable{

    public static AutomationScreen instance;

    public AutomationScreen() {
        super(Text.literal("Automations Configuration Screen"));
    }

    public ContextMenuWidget contextMenuWidget;

    public List<RightClickable> rightClickables = new ArrayList<>();

    public boolean saved;

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        if(contextMenuWidget!= null) contextMenuWidget.render(matrices, mouseX, mouseY, delta);
    }

    public void addNode(Node n, int x, int y){
        NodeWidget nodeWidget = new NodeWidget(n, x, y);
        AutomationsClient.nodeWrapper.nodes.put(n, nodeWidget);
        this.reload();
    }

    public void addNode(Node n){
        this.addNode(n, 100, 100);
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);
    }

    public void reload(){
        if(!this.children().isEmpty()) {
            Iterator<? extends Element> itr = this.children().iterator();
            while (itr.hasNext()) {
                Element e = itr.next();
                itr.remove();
            }
        }
        AutomationsClient.nodeWrapper.nodes.forEach((node, nodeWidget) -> {
            this.addDrawableChild(nodeWidget);
            rightClickables.add(nodeWidget);
            rightClickables.addAll(nodeWidget.inputs.values());
            for(OutputWidget o : nodeWidget.outputs.values()){
                rightClickables.add(o);
                rightClickables.addAll(o.connections.values());
            }
        });
    }

    @Override
    protected void init() {
        instance = this;
        this.reload();

        super.init();

        this.addDrawableChild(new CButtonWidget(0xFF000000, 10, 10, 100, 20, Text.literal("Save and Reload"), (button) -> this.saveAndReload()));

        this.addDrawableChild(new CButtonWidget(0xFF000000,120, 10, 100, 20, Text.literal("Add Node"), (button -> MinecraftClient.getInstance().setScreen(new ChooseNodeScreen(100, 100, this)))));
    }

    public static void createConnection(OutputWidget outputWidget, InputWidget inputWidget){
        if(inputWidget.connectionWidget != null) inputWidget.connectionWidget.delete();
        Connection connection = Connection.createConnection(outputWidget.output, inputWidget.input);
        if(connection == null) return;
        ConnectionWidget connectionWidget = new ConnectionWidget(connection);
        outputWidget.connections.put(connection, connectionWidget);
        inputWidget.connectionWidget = connectionWidget;
        AutomationsClient.nodeWrapper.connections.put(connection, connectionWidget);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 1){
            // context Menu
            for(RightClickable rightClickable: this.rightClickables){
                if(rightClickable.isMouseOver(mouseX, mouseY)){
                    contextMenuWidget = rightClickable.onRightCLick(mouseX, mouseY);
                    return true;
                }
            }
            contextMenuWidget = this.onRightCLick(mouseX, mouseY);
        } else {
            if(contextMenuWidget != null) if(!contextMenuWidget.mouseClicked(mouseX, mouseY, button)) contextMenuWidget = null;
            for (Element e : this.children()) {
                if(e.mouseClicked(mouseX, mouseY, button)) return true;
            }
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (Element e : this.children()) {
            e.mouseReleased(mouseX, mouseY, button);
        }
        return true;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        for (Element e : this.children()) {
            e.mouseMoved(mouseX, mouseY);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public void saveAndReload(){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(byteArrayOutputStream);
            os.writeObject(AutomationsClient.nodeWrapper);
            os.close();
            PacketByteBuf p = PacketByteBufs.create();
            p.writeByteArray(byteArrayOutputStream.toByteArray());
            ClientPlayNetworking.send(ModNetworking.NODE_LIST_C2S_PACKET_ID, p);
            this.saved = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ContextMenuWidget onRightCLick(double mouseX, double mouseY) {
        List<ContextMenuWidget.MenuEntry> entries = new ArrayList<>();
        entries.add(new ContextMenuWidget.MenuEntry("Add Node", (nodeWidget) -> {
            assert this.client != null;
            this.client.setScreen(new ChooseNodeScreen(mouseX, mouseY, this));
        }));
        entries.add(new ContextMenuWidget.MenuEntry("Save and Quit", (nodeWidget) -> {
            this.saveAndReload();
            assert this.client != null;
            this.client.setScreen(null);
        }));
        entries.add(new ContextMenuWidget.MenuEntry("Quit without Saving", (nodeWidget) -> {
            assert this.client != null;
            this.client.setScreen(null);
        }));
        return new ContextMenuWidget((int)mouseX, (int)mouseY, this, entries);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == 256){
            if(saved) {
                this.close();
                return true;
            }
            else {

            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
