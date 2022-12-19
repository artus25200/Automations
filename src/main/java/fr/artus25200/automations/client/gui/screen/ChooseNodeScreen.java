/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.screen;

import fr.artus25200.automations.client.gui.widget.AddNodeButtonWidget;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.Nodes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.*;

public class ChooseNodeScreen extends Screen {

	double mouseX, mouseY;

	Screen parent;

	protected ChooseNodeScreen(double mouseX, double mouseY, Screen parent) {
		super(Text.of("Choose Node"));
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.parent = parent;
	}

	public HashMap<CheckboxWidget, Nodes.NodeCategory> checkboxWidgets = new HashMap<>();

	public List<Nodes.NodeCategory> selectedCategories = new ArrayList<>();

	public HashMap<String, Node> nodesToDisplay = new HashMap<>();
	public List<ButtonWidget> nodesButtonsToDisplay = new ArrayList<>();

	public boolean checkCategories = true;
	public boolean checkNodes;

	@Override
	protected void init() {
		int width = 100;
		int height = 20;
		int spacing = 10;

		List<Nodes.NodeCategory> categories = new ArrayList<>();
		Collections.addAll(categories, Nodes.NodeCategory.values());

		for(int i = 0; i< categories.size(); i++){
			Nodes.NodeCategory category = categories.get(i);
			int y = (spacing+height) * i + spacing;
			CheckboxWidget checkboxWidget = new CheckboxWidget(spacing, y, width, height, Text.literal(category.name()), false);
			this.addDrawableChild(checkboxWidget);
			checkboxWidgets.put(checkboxWidget, category);
		}
	}

	@Override
	public void tick() {
		if(checkCategories) {
			List<Nodes.NodeCategory> selectedCategories = new ArrayList<>();
			for (Map.Entry<CheckboxWidget, Nodes.NodeCategory> e : checkboxWidgets.entrySet()) {
				if (e.getKey().isChecked()) selectedCategories.add(e.getValue());
			}
			this.selectedCategories = selectedCategories;

			HashMap<String, Node> n2 = new HashMap<>();

			for(Node n : Nodes.NODE_REGISTERY.stream().toList()){
				if(Arrays.asList(n.getCategories()).containsAll(selectedCategories)){
					n2.put(n.getName(), n);
				}
			}
			nodesToDisplay = n2;
			checkCategories = false;
			checkNodes = true;
		}
	}

	@Override
	public void renderBackground(MatrixStack matrices) {
		this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);

		if(checkNodes) {
			List<ButtonWidget> nodesButtonsToDisplay = new ArrayList<>();
			int width = 150;
			int height = 20;
			int spacing = 10;
			for (int i = 0; i < nodesToDisplay.size(); i++) {
				Map.Entry<String, Node> entry = nodesToDisplay.entrySet().stream().toList().get(i);

				int x = 120 + (width + spacing) * i;
				int y = spacing;
				while(x+width > MinecraftClient.getInstance().getWindow().getScaledWidth()){
					x -= (MinecraftClient.getInstance().getWindow().getScaledWidth() - 120);
					y += height + spacing;
				}
				ButtonWidget bw = new AddNodeButtonWidget(entry.getValue(),0xFF000000, x, y, width, height, Text.literal(entry.getKey()), this.mouseX, this.mouseY, this.parent);
				this.addDrawableChild(bw);
			}

			this.nodesButtonsToDisplay = nodesButtonsToDisplay;
			checkNodes = false;
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		checkCategories = true;
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
