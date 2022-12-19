package fr.artus25200.automations.common.node;

import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.common.node.data.DataNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Node implements Serializable, Cloneable {
    public List<Input> inputs = new ArrayList<>();
    public List<Output> outputs = new ArrayList<>();

    public Node() {
    }

    public boolean Execute() {
        if(inputs.isEmpty()) return this.onExecute();
        for (Input input : inputs) {
            if(!input.isConnected()) return false;
            if(input.connector.output.parent instanceof DataNode) {
                if(!input.connector.output.parent.Execute()) return false;
            }
        }
        return this.onExecute();
    }

    public void delete(){
        AutomationsClient.nodeWrapper.nodes.remove(this);
    }

    public abstract boolean onExecute();

    public abstract String getName();

    public abstract int getColor();

    public abstract Nodes.NodeCategory[] getCategories();

    @Override
    public Node clone() {
        try {
            return (Node) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

