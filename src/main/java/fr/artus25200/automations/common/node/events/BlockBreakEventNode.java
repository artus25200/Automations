package fr.artus25200.automations.common.node.events;

import fr.artus25200.automations.common.customTypes.EntityList;
import fr.artus25200.automations.common.node.Nodes;
import fr.artus25200.automations.common.node.Output;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockBreakEventNode extends EventNode{

    public BlockBreakEventNode() {
        super();
        this.categories.add(Nodes.NodeCategory.EVENT);
        this.categories.add(Nodes.NodeCategory.BLOCK);
        this.categories.add(Nodes.NodeCategory.PLAYER);
    }

    @Override
    public void setOutputs() {
        super.setOutputs();
        this.outputs.add(new Output(this, "World", Level.class));
        this.outputs.add(new Output(this, "BlockPos", BlockPos.class));
        this.outputs.add(new Output(this, "BlockState", BlockState.class));
        this.outputs.add(new Output(this, "Player", EntityList.class));
    }

    public void Execute(Level world, BlockPos pos, BlockState state, Player player) {
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

}
