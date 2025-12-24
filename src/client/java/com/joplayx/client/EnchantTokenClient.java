package com.joplayx.client;

import com.joplayx.item.ModItems;
import com.joplayx.keybind.ModKeybinds;
import com.joplayx.screen.EnchantScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class EnchantTokenClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModKeybinds.initialize();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ModKeybinds.OPEN_ENCHANT_SCREEN.consumeClick()) {
                if (client.player != null && client.screen == null) {
                    ItemStack offhand = client.player.getOffhandItem();
                    ItemStack mainhand = client.player.getMainHandItem();

                    if (!offhand.is(ModItems.ENCHANT_TOKEN)) {
                        client.player.displayClientMessage(
                                Component.literal("§cYou need an Enchant Token in your offhand!"),
                                true
                        );
                        return;
                    }

                    if (mainhand.isEmpty()) {
                        client.player.displayClientMessage(
                                Component.literal("§cYou need an item in your main hand to enchant!"),
                                true
                        );
                        return;
                    }

                    Minecraft.getInstance().setScreen(new EnchantScreen());
                }
            }
        });
    }
}