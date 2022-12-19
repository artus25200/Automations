/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.text.Text;

public class ConfirmationScreen extends ConfirmScreen {
	public ConfirmationScreen(BooleanConsumer callback, Text title, Text message, Text yesText, Text noText) {
		super(callback, title, message, yesText, noText);
	}
}
