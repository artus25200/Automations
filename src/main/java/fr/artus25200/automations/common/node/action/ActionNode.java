package fr.artus25200.automations.common.node.action;

import fr.artus25200.automations.common.node.*;
import fr.artus25200.automations.common.node.events.EventNode;
import fr.artus25200.automations.common.node.events.HasParent;

import java.util.ArrayList;
import java.util.List;

public abstract class ActionNode extends Node implements HasParent{
    public Input actionInput;
    public Output actionOutput;

    public ActionNode(){
        this.categories.add(Nodes.NodeCategory.ACTION);
    }

    @Override
    public void setInputs() {
        actionInput = new Input(this, "Action", Action.class);
        inputs.add(actionInput);
    }

    @Override
    public void setOutputs() {
        actionOutput = new Output(this, "Action", Action.class);
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

    public ArrayList<Node> GetParents(){
        if(!actionInput.isConnected()) return new ArrayList<>();
        ArrayList<Node> list = ((HasParent)actionInput.connector.output.parent).GetParents();
        list.add(this);
        return list;
    }

    public abstract boolean onExecute();
}
