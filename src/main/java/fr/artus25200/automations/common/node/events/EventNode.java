package fr.artus25200.automations.common.node.events;

import fr.artus25200.automations.common.Automations;
import fr.artus25200.automations.common.node.Node;
import fr.artus25200.automations.common.node.Nodes;
import fr.artus25200.automations.common.node.Output;
import fr.artus25200.automations.common.node.action.Action;

import java.util.ArrayList;
import java.util.List;


public abstract class EventNode extends Node {
    public Output actionOutput;

    public EventNode(){
        this.actionOutput = new Output(this, "Action", Action.class);
        this.outputs.add(this.actionOutput);
    }

    @Override
    public int getColor() {
        return 0xFFfAEB61;
    }

    @Override
    public Nodes.NodeCategory[] getCategories() {
        return new Nodes.NodeCategory[]{Nodes.NodeCategory.EVENT};
    }

    @Override
    public boolean Execute() {
        this.onExecute();
        if(this.actionOutput.isConnected()){
            return this.actionOutput.connections.get(0).input.parent.Execute();
        }
        return false;
    }

    @Override
    public boolean onExecute() {
        Automations.LOGGER.info("Event triggered : " + this.getName() + ". Executing ActionNode : " + (this.actionOutput.isConnected() ? this.actionOutput.connections.get(0).input.parent.getName() : "None") + ".");
        return true;
    }

    public abstract void registerEvent();
}
