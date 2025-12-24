package com.joplayx.network;

import com.joplayx.EnchantToken;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record EnchantPayload(String enchantName, int level) implements CustomPacketPayload {
    public static final Type<EnchantPayload> ID = new Type<>(EnchantToken.id("enchant"));

    public static final StreamCodec<FriendlyByteBuf, EnchantPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, EnchantPayload::enchantName,
            ByteBufCodecs.VAR_INT, EnchantPayload::level,
            EnchantPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}