/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import net.minecraft.client.gui.components.events.GuiEventListener;

public interface RightClickable extends GuiEventListener {
	public ContextMenuWidget onRightCLick(double mouseX, double mouseY);
}
