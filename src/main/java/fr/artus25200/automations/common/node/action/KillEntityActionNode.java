package fr.artus25200.automations.common.node.action;

import fr.artus25200.automations.common.customTypes.EntityList;
import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Nodes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class KillEntityActionNode extends ActionNode{

    public KillEntityActionNode(){
        super();
        this.categories.add(Nodes.NodeCategory.ENTITY);
        this.categories.add(Nodes.NodeCategory.PLAYER);
    }

    @Override
    public void setInputs() {
        super.setInputs();
        this.inputs.add(new Input(this, "Entity(s)", EntityList.class));
    }

    @Override
    public void setOutputs() {
        super.setOutputs();
    }

    @Override
    public boolean onExecute() {
        for(Entity e : ((EntityList) this.inputs.get(1).getValue()).entities){
            e.kill((ServerLevel) e.level());
        }
        return true;
    }

    @Override
    public String getName() {
        return "Kill Entity";
    }

}
