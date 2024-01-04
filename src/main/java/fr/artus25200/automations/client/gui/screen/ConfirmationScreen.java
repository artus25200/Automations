/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.screen;

import fr.artus25200.automations.client.gui.widget.CButtonWidget;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfirmationScreen extends ConfirmScreen {
	Screen parent;

	public ConfirmationScreen(Screen parent, BooleanConsumer callback, Text title, Text message, Text yesText, Text noText) {
		super(callback, title, message, yesText, noText);
		this.parent = parent;
	}

	@Override
	protected void addButtons(int y) {
		this.addButton(new CButtonWidget(0xFF61e340,this.width / 2 - 155, y, 150, 20, this.yesText, (button) -> {
			this.callback.accept(true);
		}));
		this.addButton(new CButtonWidget(0xFFe34040,this.width / 2 - 155 + 160, y, 150, 20, this.noText, (button) -> {
			this.callback.accept(false);
		}));
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if(keyCode == 256){
			assert this.client != null;
			this.client.setScreen(this.parent);
		}
		return true;
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
