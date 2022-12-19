package fr.artus25200.automations.common.node;

import fr.artus25200.automations.client.AutomationsClient;

import java.io.Serializable;
import java.util.ArrayList;

public class Output implements Serializable {

    public final ArrayList<Connection> connections = new ArrayList<>();

    public String name;
    public Node parent;
    public Class<?> type;
    public transient Object value;

    public int x,y;

    public int color = 0xFFFFFFFF;

    public Output(Node parent, String name, Class<?> type){
        this.parent = parent;
        this.name = name;
        this.type = type;
        this.color = AutomationsClient.getColor(type);
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isConnected(){
        return !this.connections.isEmpty();
    }
}
