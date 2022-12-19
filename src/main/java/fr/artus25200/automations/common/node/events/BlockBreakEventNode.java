package fr.artus25200.automations.common.node.events;

import fr.artus25200.automations.client.AutomationsClient;
import fr.artus25200.automations.common.Automations;
import fr.artus25200.automations.common.customTypes.EntityList;
import fr.artus25200.automations.common.node.Nodes;
import fr.artus25200.automations.common.node.Output;
import fr.artus25200.automations.server.AutomationsServer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockBreakEventNode extends EventNode{

    public BlockBreakEventNode() {
        super();
        this.outputs.add(new Output(this, "World", World.class));
        this.outputs.add(new Output(this, "BlockPos", BlockPos.class));
        this.outputs.add(new Output(this, "BlockState", BlockState.class));
        this.outputs.add(new Output(this, "Player", EntityList.class));
        AutomationsClient.nodeWrapper.blockBreakEvents.add(this);
    }

    public void Execute(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        this.outputs.get(1).value = world;
        this.outputs.get(2).value = pos;
        this.outputs.get(3).value = state;
        this.outputs.get(4).value = new EntityList(player);
        super.Execute();
    }

    @Override
    public String getName() {
        return "On Block Break";
    }

    @Override
    public Nodes.NodeCategory[] getCategories() {
        return new Nodes.NodeCategory[]{Nodes.NodeCategory.EVENT, Nodes.NodeCategory.BLOCK};
    }

    @Override
    public void registerEvent() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, ci) -> {
            Automations.LOGGER.info("Block broken : " + state.getBlock().getName().toString());
            for(BlockBreakEventNode e : AutomationsServer.nodeWrapper.blockBreakEvents){
                e.Execute(world, pos, state, player);
            }
        });
    }
}
