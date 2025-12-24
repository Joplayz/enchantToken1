package com.joplayx.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {
    public static KeyMapping OPEN_ENCHANT_SCREEN;

    public static void initialize() {
        OPEN_ENCHANT_SCREEN = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.enchanttoken.open_enchant",
                GLFW.GLFW_KEY_H,
                KeyMapping.Category.MISC
        ));
    }
}