package fr.artus25200.automations.common.node.action;

import fr.artus25200.automations.common.customTypes.EntityList;
import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Nodes;
import net.minecraft.entity.Entity;

import java.util.List;

public class KillEntityActionNode extends ActionNode{

    public KillEntityActionNode() {
        this.inputs.add(new Input(this, "Entity(s)", EntityList.class));
    }

    @Override
    public boolean onExecute() {
        for(Entity e : ((EntityList) this.inputs.get(1).getValue()).entities){
            e.kill();
        }
        return true;
    }

    @Override
    public String getName() {
        return "Kill Entity";
    }

    @Override
    public Nodes.NodeCategory[] getCategories() {
        return new Nodes.NodeCategory[]{Nodes.NodeCategory.ACTION, Nodes.NodeCategory.ENTITY};
    }
}
