package fr.artus25200.automations.common.node;

import fr.artus25200.automations.client.AutomationsClient;

import java.io.Serializable;

public class Input implements Serializable {
    public String name;
    public Node parent;
    public Class<?> type;
    public String ClassTypeJson;

    public int color = 0xFFFFFFFF;

    public int x,y;

    public Connection connector;

    public Input(Node parent, String name, Class<?> type) {
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.color = AutomationsClient.getColor(type);
        ClassTypeJson = type.getName();
    }

    public boolean isConnected(){
        return this.connector != null;
    }

    public boolean hasValue(){
        if(!this.isConnected()) return false;
        return this.connector.output.value != null;
    }

    public Object getValue(){
        return hasValue() ? this.connector.output.value : null;
    }

    public Class<?> getType(){
        return type;
    }
}
