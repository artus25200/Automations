package fr.artus25200.automations.mixin;

import fr.artus25200.automations.common.node.events.BlockBreakEventNode;
import fr.artus25200.automations.server.AutomationsServer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "onBreak", at = @At("TAIL"))
    private void onBlockBreak(@NotNull World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci)
    {
        if(world.isClient) return;
        for(BlockBreakEventNode event : AutomationsServer.nodeWrapper.blockBreakEvents){
            event.Execute(world, pos, state, player);
        }
    }
}
