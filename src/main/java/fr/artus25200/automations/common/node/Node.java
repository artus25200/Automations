package fr.artus25200.automations.common.node;

import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.common.node.data.DataNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Node implements Serializable {
    public List<Input> inputs = new ArrayList<>();
    public List<Output> outputs = new ArrayList<>();

    public Node() {
        this.setInputs();
        this.setOutputs();
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
        AutomationsClient.nodeList.nodes.remove(this);
    }

    public abstract void setInputs();

    public abstract void setOutputs();

    public abstract boolean onExecute();

    public abstract String getName();

    public abstract int getColor();

    public abstract Nodes.NodeCategory[] getCategories();
}

