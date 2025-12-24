package com.joplayx.item;

import com.joplayx.EnchantToken;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.function.Function;

public class ModItems {

    // Base UUIDs that Minecraft uses - these show as green base values, not blue modifiers
    private static final ResourceLocation BASE_ATTACK_DAMAGE_ID = ResourceLocation.withDefaultNamespace("base_attack_damage");
    private static final ResourceLocation BASE_ATTACK_SPEED_ID = ResourceLocation.withDefaultNamespace("base_attack_speed");

    public static final Item ENCHANT_TOKEN = register(
            "enchant_token",
            Item::new,
            new Item.Properties().stacksTo(64)
    );

    public static final Item GOD_GEM = register(
            "god_gem",
            Item::new,
            new Item.Properties().stacksTo(64)
    );

    public static final Item GOD_SWORD = register(
            "god_sword",
            GodSwordItem::new,
            new Item.Properties()
                    .stacksTo(1)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE,
                                    new AttributeModifier(
                                            BASE_ATTACK_DAMAGE_ID,
                                            999,
                                            AttributeModifier.Operation.ADD_VALUE
                                    ),
                                    EquipmentSlotGroup.MAINHAND)
                            .add(Attributes.ATTACK_SPEED,
                                    new AttributeModifier(
                                            BASE_ATTACK_SPEED_ID,
                                            -2.4,
                                            AttributeModifier.Operation.ADD_VALUE
                                    ),
                                    EquipmentSlotGroup.MAINHAND)
                            .build())
    );

    public static final CreativeModeTab MOD_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(EnchantToken.MOD_ID, "mod_tab"),
            FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup.enchanttoken.mod_tab"))
                    .icon(() -> new ItemStack(ENCHANT_TOKEN))
                    .displayItems((context, entries) -> {
                        entries.accept(ENCHANT_TOKEN);
                        entries.accept(GOD_GEM);
                        entries.accept(GOD_SWORD);
                    })
                    .build()
    );

    public static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(EnchantToken.MOD_ID, name));
        Item item = factory.apply(properties.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void initialize() {
        EnchantToken.LOGGER.info("Registering items for " + EnchantToken.MOD_ID);
    }
}
