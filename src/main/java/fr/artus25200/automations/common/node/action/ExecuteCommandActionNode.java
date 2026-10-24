/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.node.action;


import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import fr.artus25200.automations.common.node.Input;
import fr.artus25200.automations.common.node.Nodes;
import fr.artus25200.automations.server.AutomationsServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;

public class ExecuteCommandActionNode extends ActionNode{

    public ExecuteCommandActionNode(){
        super();
        this.categories.add(Nodes.NodeCategory.CHAT);
        this.categories.add(Nodes.NodeCategory.SERVER);
    }

	@Override
	public String getName() {
		return "Execute Command";
	}

	@Override
	public boolean onExecute() {
        MinecraftServer server = AutomationsServer.server;
        String command = (String) this.inputs.get(1).getValue();
        CommandSourceStack sourceStack = server.createCommandSourceStack();
        try {
            server.createCommandSourceStack();
            StringReader reader = new StringReader(command);
            ParseResults<CommandSourceStack> parseResults = server.getCommands().getDispatcher().parse(reader, sourceStack);
            server.getCommands().getDispatcher().execute(parseResults);
        }
        catch (CommandSyntaxException e){
            e.printStackTrace();
        }
		return true;
	}

	@Override
	public void setInputs() {
		super.setInputs();
		this.inputs.add(new Input(this, "Command (String)", String.class));
	}

	@Override
	public void setOutputs() {
		super.setOutputs();
	}

}
