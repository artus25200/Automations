/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static fr.artus25200.automations.client.AutomationsClient.getTr;

public class ContextMenuWidget implements Renderable, NarratableEntry, GuiEventListener {
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
			int width = getTr().width(entry.text);
			maxWidth = Math.max(width, maxWidth);

			entry.parent = this;
		}
		maxWidth+= 2;
		width = maxWidth + 3 + 3;
		for (int i = 0; i < entries.size(); i++) {
			entries.get(i).x = x + 3;
			entries.get(i).y = this.y + i * (getTr().lineHeight + 3 + 3);
			entries.get(i).width = maxWidth;
			entries.get(i).height = getTr().lineHeight + 3 + 3;
		}

		height = entries.size() * (getTr().lineHeight + 3 + 3);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		graphics.fill(x, y-3, x + width, y + height + 3, 0xFF252525);
		for(MenuEntry entry : entries){
			entry.extractRenderState(graphics, mouseX, mouseY, delta);
		}
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if(this.isMouseOver(event.x(), event.y())){
			for(MenuEntry entry : entries){
				entry.mouseClicked(event, doubleClick);
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
	public NarrationPriority narrationPriority() {
		return NarrationPriority.NONE;
	}

	@Override
	public void updateNarration(NarrationElementOutput builder) {

	}

	@Override
	public void setFocused(boolean focused) {}

	@Override
	public boolean isFocused() { return false; }

	public static class MenuEntry implements Renderable, GuiEventListener, NarratableEntry {
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
		public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
			graphics.fill(x, y, x + width, y+ height, (isMouseOver(mouseX, mouseY) ? 0xFF626262 : 0xFF252525));
			graphics.centeredText(getTr(), text, x + width/2, y + height/4, 0XFFFFFFFF);
		}

		@Override
		public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
			if(this.isMouseOver(event.x(), event.y())){
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
		public NarrationPriority narrationPriority() {
			return NarrationPriority.NONE;
		}

		@Override
		public void updateNarration(NarrationElementOutput builder) {

		}

		@Override
		public void setFocused(boolean focused) {}

		@Override
		public boolean isFocused() { return false; }
	}

	public interface MenuAction{
		void Action(Object clicked) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
	}

}
