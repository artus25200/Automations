package fr.artus25200.automations.common.node;

import fr.artus25200.automations.common.node.data.DataNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Node implements Serializable {
    public List<Nodes.NodeCategory> categories = new ArrayList<>();
    public List<Input> inputs = new ArrayList<>();
    public List<Output> outputs = new ArrayList<>();

    public int x,y;

    public Node() {
        this.setInputs();
        this.setOutputs();
    }

    public boolean Execute() {
        if(inputs.isEmpty()) return this.onExecute();
        for (Input input : inputs) {
            if(!input.isConnected()) return false;
            if(input.connector.output.parent instanceof DataNode || input.connector.output.parent instanceof RedirectNode) {
                if(!input.connector.output.parent.Execute()) return false;
            }
        }
        return this.onExecute();
    }


    public abstract void setInputs();

    public abstract void setOutputs();

    public abstract boolean onExecute();

    public abstract String getName();

    public abstract int getColor();

}

