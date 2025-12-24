package com.joplayx;

import com.joplayx.item.ModItems;
import com.joplayx.network.EnchantPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class EnchantToken implements ModInitializer {
    public static final String MOD_ID = "enchanttoken";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Enchant Token!");

        ModItems.initialize();

        PayloadTypeRegistry.playC2S().register(EnchantPayload.ID, EnchantPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(EnchantPayload.ID, (payload, context) -> {
            ServerPlayer player = context.player();
            String enchantName = payload.enchantName();
            int level = payload.level();

            context.server().execute(() -> {
                handleEnchantRequest(player, enchantName, level);
            });
        });
    }

    private void handleEnchantRequest(ServerPlayer player, String enchantName, int level) {
        ItemStack offhand = player.getOffhandItem();
        if (!offhand.is(ModItems.ENCHANT_TOKEN)) {
            player.sendSystemMessage(Component.literal("§cYou need an Enchant Token in your offhand!"));
            return;
        }

        ItemStack mainhand = player.getMainHandItem();
        if (mainhand.isEmpty()) {
            player.sendSystemMessage(Component.literal("§cYou need an item in your main hand to enchant!"));
            return;
        }

        Optional<Holder.Reference<Enchantment>> enchantmentHolder = findEnchantment(player, enchantName);
        if (enchantmentHolder.isEmpty()) {
            player.sendSystemMessage(Component.literal("§cUnknown enchantment: " + enchantName));
            return;
        }

        if (level < 1) level = 1;

        Holder<Enchantment> enchant = enchantmentHolder.get();

        ItemEnchantments currentEnchantments = mainhand.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments.Mutable mutableEnchantments = new ItemEnchantments.Mutable(currentEnchantments);

        mutableEnchantments.set(enchant, level);

        mainhand.set(DataComponents.ENCHANTMENTS, mutableEnchantments.toImmutable());

        offhand.shrink(1);

        String enchantDisplayName = enchantName.substring(0, 1).toUpperCase() + enchantName.substring(1).replace("_", " ");
        player.sendSystemMessage(Component.literal("§aEnchanted with " + enchantDisplayName + " " + level + "!"));
    }

    private Optional<Holder.Reference<Enchantment>> findEnchantment(ServerPlayer player, String shortName) {
        String normalized = shortName.toLowerCase().replace(" ", "_");

        ResourceLocation mcLocation = ResourceLocation.withDefaultNamespace(normalized);
        ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, mcLocation);

        var registry = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Optional<Holder.Reference<Enchantment>> result = registry.get(key);

        if (result.isPresent()) {
            return result;
        }

        for (Holder.Reference<Enchantment> holder : registry.listElements().toList()) {
            if (holder.key().location().getPath().equals(normalized)) {
                return Optional.of(holder);
            }
        }

        return Optional.empty();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
