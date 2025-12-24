package com.joplayx.screen;

import com.joplayx.network.EnchantPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EnchantScreen extends Screen {
    private static final ResourceLocation BACKGROUND = ResourceLocation.withDefaultNamespace("textures/gui/demo_background.png");

    private EditBox enchantNameField;
    private EditBox levelField;

    public EnchantScreen() {
        super(Component.literal("Enchant Token"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.enchantNameField = new EditBox(
                this.font,
                centerX - 80,
                centerY - 30,
                160,
                20,
                Component.literal("Enchantment Name")
        );
        this.enchantNameField.setMaxLength(64);
        this.enchantNameField.setHint(Component.literal("sharpness, protection, etc."));
        this.addRenderableWidget(this.enchantNameField);

        this.levelField = new EditBox(
                this.font,
                centerX - 80,
                centerY,
                160,
                20,
                Component.literal("Level")
        );
        this.levelField.setMaxLength(20);
        this.levelField.setHint(Component.literal("Level (e.g. 1000)"));
        this.addRenderableWidget(this.levelField);

        Button enchantButton = Button.builder(Component.literal("Enchant"), button -> this.onEnchant())
                .bounds(centerX - 50, centerY + 30, 100, 20)
                .build();
        this.addRenderableWidget(enchantButton);

        this.setInitialFocus(this.enchantNameField);
    }

    private void onEnchant() {
        String enchantName = this.enchantNameField.getValue().trim();
        String levelStr = this.levelField.getValue().trim();

        if (enchantName.isEmpty()) {
            return;
        }

        // Remove commas, spaces, and other non-digit characters
        levelStr = levelStr.replaceAll("[^0-9]", "");

        int level;
        try {
            level = Integer.parseInt(levelStr);
        } catch (NumberFormatException e) {
            level = 1;
        }

        ClientPlayNetworking.send(new EnchantPayload(enchantName, level));
        this.onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int boxWidth = 200;
        int boxHeight = 120;
        int boxX = centerX - boxWidth / 2;
        int boxY = centerY - boxHeight / 2;

        // Draw background similar to vanilla
        graphics.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0xFFC6C6C6); // Light gray
        graphics.fill(boxX + 3, boxY + 3, boxX + boxWidth - 3, boxY + boxHeight - 3, 0xFF8B8B8B); // Darker inner
        graphics.fill(boxX + 4, boxY + 4, boxX + boxWidth - 4, boxY + boxHeight - 4, 0xFFC6C6C6); // Light again

        // Top border highlight
        graphics.fill(boxX, boxY, boxX + boxWidth, boxY + 3, 0xFFFFFFFF);
        graphics.fill(boxX, boxY, boxX + 3, boxY + boxHeight, 0xFFFFFFFF);

        // Bottom border shadow
        graphics.fill(boxX, boxY + boxHeight - 3, boxX + boxWidth, boxY + boxHeight, 0xFF555555);
        graphics.fill(boxX + boxWidth - 3, boxY, boxX + boxWidth, boxY + boxHeight, 0xFF555555);

        // Title
        graphics.drawCenteredString(this.font, this.title, centerX, boxY + 10, 0x404040);

        // Labels
        graphics.drawString(this.font, "Enchantment:", centerX - 80, centerY - 42, 0x404040);
        graphics.drawString(this.font, "Level:", centerX - 80, centerY - 12, 0x404040);

        super.render(graphics, mouseX, mouseY, delta);
    }
}
