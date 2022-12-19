/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.widget;

import net.minecraft.client.gui.Element;

public interface RightClickable extends Element {
	public ContextMenuWidget onRightCLick(double mouseX, double mouseY);
}
