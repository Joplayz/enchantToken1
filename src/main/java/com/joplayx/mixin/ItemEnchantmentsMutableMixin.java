package com.joplayx.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

@Mixin(ItemEnchantments.Mutable.class)
public class ItemEnchantmentsMutableMixin {

    @Shadow
    Object2IntOpenHashMap<Holder<Enchantment>> enchantments;

    @Inject(method = "set", at = @At("HEAD"), cancellable = true)
    private void bypassSetCap(Holder<Enchantment> enchantment, int level, CallbackInfo ci) {
        this.enchantments.put(enchantment, level);
        ci.cancel();
    }
}
