/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.screen;

import com.mojang.blaze3d.platform.InputConstants;
import fr.artus25200.automations.client.gui.widget.CButtonWidget;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

public class ConfirmationScreen extends ConfirmScreen {
	Screen parent;

	public ConfirmationScreen(Screen parent, BooleanConsumer callback, Component title, Component message, Component yesText, Component noText) {
		super(callback, title, message, yesText, noText);
		this.parent = parent;
	}

	@Override
	protected void addButtons(LinearLayout layout) {
		// the position of the buttons is now handled by the layout
		layout.addChild(new CButtonWidget(0xFF61e340, 0, 0, 150, 20, this.yesButtonComponent, (button) -> {
			this.callback.accept(true);
		}));
		layout.addChild(new CButtonWidget(0xFFe34040, 0, 0, 150, 20, this.noButtonComponent, (button) -> {
			this.callback.accept(false);
		}));
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
	@Override
	public boolean keyPressed(KeyEvent event) {
		if(event.key() == InputConstants.KEY_ESCAPE){
			assert this.minecraft != null;
			this.minecraft.gui.setScreen(this.parent);
		}
		return true;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
