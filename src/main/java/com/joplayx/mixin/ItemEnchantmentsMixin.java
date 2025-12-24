package com.joplayx.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

@Mixin(ItemEnchantments.class)
public class ItemEnchantmentsMixin {

    @Shadow
    Object2IntOpenHashMap<Holder<Enchantment>> enchantments;

    @Inject(method = "getLevel", at = @At("HEAD"), cancellable = true)
    private void bypassLevelCap(Holder<Enchantment> enchantment, CallbackInfoReturnable<Integer> cir) {
        int level = this.enchantments.getInt(enchantment);
        cir.setReturnValue(level);
    }
}
