package com.joplayx.mixin;

import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ItemEnchantments.class)
public class ItemEnchantmentsLevelCapMixin {

    // Change the max level constant from 255 to Integer.MAX_VALUE in constructor
    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 255))
    private int modifyMaxLevelConstructor(int original) {
        return Integer.MAX_VALUE;
    }

    // Change the max level constant in the static initializer (CODEC/STREAM_CODEC)
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 255))
    private static int modifyMaxLevelCodec(int original) {
        return Integer.MAX_VALUE;
    }
}
