/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.client.gui.screen;

import fr.artus25200.automations.client.gui.widget.AddNodeButtonWidget;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.Nodes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.*;

//TODO: Tabs or search
public class ChooseNodeScreen extends Screen {

	double mouseX, mouseY;

	Screen parent;

	protected ChooseNodeScreen(double mouseX, double mouseY, Screen parent) {
		super(Component.nullToEmpty("Choose Node"));
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.parent = parent;
	}

	public HashMap<Checkbox, Nodes.NodeCategory> checkboxWidgets = new HashMap<>();

	public List<Nodes.NodeCategory> selectedCategories = new ArrayList<>();

	public HashMap<String, Node> nodesToDisplay = new HashMap<>();
    public List<AddNodeButtonWidget> buttonChildren = new ArrayList<>();


	public boolean checkCategories = true;
	public boolean checkNodes;

	@Override
	protected void init() {
        this.children().clear();
		int width = 100;
		int height = 20;
		int spacing = 10;

		List<Nodes.NodeCategory> categories = new ArrayList<>();
		Collections.addAll(categories, Nodes.NodeCategory.values());

		for(int i = 0; i< categories.size(); i++){
			Nodes.NodeCategory category = categories.get(i);
			int y = (spacing+height) * i + spacing;
			Checkbox checkboxWidget = Checkbox.builder(Component.literal(category.name()), this.font).pos(spacing, y).selected(selectedCategories.contains(category)).build();
			this.addRenderableWidget(checkboxWidget);
			checkboxWidgets.put(checkboxWidget, category);
		}
	}

	@Override
	public void tick() {
		if(checkCategories) {
			List<Nodes.NodeCategory> selectedCategories = new ArrayList<>();
			for (Map.Entry<Checkbox, Nodes.NodeCategory> e : checkboxWidgets.entrySet()) {
				if (e.getKey().selected()) selectedCategories.add(e.getValue());
			}
			this.selectedCategories = selectedCategories;

			HashMap<String, Node> n2 = new HashMap<>();

			for(Node n : Nodes.NODE_REGISTERY.stream().toList()){
				if(new HashSet<>(n.categories).containsAll(selectedCategories)){
					n2.put(n.getName(), n);
				}
			}
			nodesToDisplay = n2;
			checkCategories = false;
			checkNodes = true;
		}
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		graphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {

		if(checkNodes) {
            for(AddNodeButtonWidget bw : this.buttonChildren) this.removeWidget(bw);
            this.buttonChildren.clear();
			int width = 150;
			int height = 20;
			int spacing = 10;
            int j = 0;
            int y = spacing;
			for (int i = 0; i < nodesToDisplay.size(); i++) {
				Map.Entry<String, Node> entry = nodesToDisplay.entrySet().stream().toList().get(i);

				int x = 120 + (width + spacing) * j;
                if(x + width > Minecraft.getInstance().getWindow().getGuiScaledWidth()){
                    y += height + spacing;
                    j = 0;
                    x = 120;
                }
				AddNodeButtonWidget bw = new AddNodeButtonWidget(entry.getValue(),entry.getValue().getColor(),x, y, width, height, Component.literal(entry.getKey()), this.mouseX, this.mouseY, this.parent);
				this.addRenderableWidget(bw);
                this.buttonChildren.add(bw);
                j++;
			}


			checkNodes = false;
		}

		super.extractRenderState(graphics, mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		checkCategories = true;
		return super.mouseClicked(event, doubleClick);
	}


}
