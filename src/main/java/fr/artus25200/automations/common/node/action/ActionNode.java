package fr.artus25200.automations.common.node.action;

import fr.artus25200.automations.common.node.*;

import java.util.ArrayList;
import java.util.List;

public abstract class ActionNode extends Node {
    public Input actionInput;
    public Output actionOutput;

    public ActionNode(){
        actionInput = new Input(this, "Action", Action.class);
        actionOutput = new Output(this, "Action", Action.class);
        inputs.add(actionInput);
        outputs.add(actionOutput);
    }

    @Override
    public boolean Execute() {
        if(super.Execute()) {
            if(actionOutput.isConnected()) return actionOutput.connections.get(0).input.parent.Execute();
            return true;
        }
        return false;
    }

    @Override
    public int getColor() {
        return 0xFFfA6161;
    }

    @Override
    public Nodes.NodeCategory[] getCategories() {
        return new Nodes.NodeCategory[]{Nodes.NodeCategory.ACTION};
    }

    public abstract boolean onExecute();
}
