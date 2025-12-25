package com.joplayx.mixin;

import com.joplayx.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BedrockDropMixin {

    @Inject(method = "playerDestroy", at = @At("HEAD"))
    private void dropBedrockOnBreak(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
        // If player breaks bedrock with God Pickaxe, drop bedrock
        if (state.is(Blocks.BEDROCK) && tool.is(ModItems.GOD_PICKAXE)) {
            Block.popResource(world, pos, new ItemStack(Blocks.BEDROCK));
        }
    }
}
