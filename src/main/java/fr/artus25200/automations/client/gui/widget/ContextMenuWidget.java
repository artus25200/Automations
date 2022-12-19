/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static fr.artus25200.automations.client.AutomationsClient.getTr;

public class ContextMenuWidget implements Drawable, Selectable, Element {
	public Object clicked;
	public Class<?> clickedType;
	public List<MenuEntry> entries;

	public int width, height, x, y;


	public ContextMenuWidget(int x, int y, Object clicked, List<MenuEntry> entries){
		this.x = x;
		this.y = y;
		this.clicked = clicked;
		this.entries = entries;

		int maxWidth = 0;
		for (MenuEntry entry : entries) {
			int width = getTr().getWidth(entry.text);
			maxWidth = Math.max(width, maxWidth);

			entry.parent = this;
		}
		maxWidth+= 2;
		width = maxWidth + 3 + 3;
		for (int i = 0; i < entries.size(); i++) {
			entries.get(i).x = x + 3;
			entries.get(i).y = this.y + i * (getTr().fontHeight + 3 + 3);
			entries.get(i).width = maxWidth;
			entries.get(i).height = getTr().fontHeight + 3 + 3;
		}

		height = entries.size() * (getTr().fontHeight + 3 + 3);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		DrawableHelper.fill(matrices, x, y-3, x + width, y + height + 3, 0xFF252525);
		for(MenuEntry entry : entries){
			entry.render(matrices, mouseX, mouseY, delta);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(this.isMouseOver(mouseX, mouseY)){
			for(MenuEntry entry : entries){
				entry.mouseClicked(mouseX, mouseY, button);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return (mouseX >= this.x ) && (mouseX <= this.x + this.width) && (mouseY >= this.y) && (mouseY <= this.y + this.height);
	}

	@Override
	public SelectionType getType() {
		return null;
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {

	}

	public static class MenuEntry implements Drawable, Element, Selectable {
		public String text;
		public MenuAction action;
		public ContextMenuWidget parent;

		public int x, y, width, height;

		public MenuEntry(String text, MenuAction action){
			this.text = text;
			this.action = action;
		}

		@Override
		public boolean isMouseOver(double mouseX, double mouseY) {
			return (mouseX >= this.x ) && (mouseX <= this.x + this.width) && (mouseY >= this.y) && (mouseY <= this.y + this.height);
		}

		@Override
		public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			DrawableHelper.fill(matrices, x, y, x + width, y+ height, (isMouseOver(mouseX, mouseY) ? 0xFF626262 : 0xFF252525));
			DrawableHelper.drawCenteredText(matrices, getTr(), text, x + width/2, y + height/4, 0XFFFFFFFF);
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			if(this.isMouseOver(mouseX, mouseY)){
				try {

					action.Action(parent.clicked);
				} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				return true;
			}
			return false;
		}

		@Override
		public SelectionType getType() {
			return null;
		}

		@Override
		public void appendNarrations(NarrationMessageBuilder builder) {

		}
	}

	public interface MenuAction{
		void Action(Object clicked) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
	}

}
