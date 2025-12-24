package com.joplayx.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentNameMixin {

    private static final String[] ROMAN_NUMERALS = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    @Inject(method = "getFullname", at = @At("HEAD"), cancellable = true)
    private static void customEnchantmentName(Holder<Enchantment> enchantment, int level, CallbackInfoReturnable<Component> cir) {
        MutableComponent name = enchantment.value().description().copy();
        
        if (level != 1 || enchantment.value().getMaxLevel() != 1) {
            if (level >= 1 && level <= 10) {
                // Roman numerals for levels 1-10
                name.append(" ").append(Component.literal(ROMAN_NUMERALS[level]));
            } else {
                // Level 11+: GOLD, ALL CAPS, ITALIC, numeric
                String enchantName = name.getString().toUpperCase();
                MutableComponent styledName = Component.literal(enchantName)
                        .withStyle(ChatFormatting.GOLD)
                        .withStyle(ChatFormatting.ITALIC);
                styledName.append(" ").append(Component.literal(String.valueOf(level)));
                cir.setReturnValue(styledName);
                return;
            }
        }
        
        cir.setReturnValue(name);
    }
}
