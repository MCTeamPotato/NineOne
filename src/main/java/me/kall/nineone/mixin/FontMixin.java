package me.kall.nineone.mixin;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.kall.nineone.NineOneClient;
import me.kall.nineone.ext.NineOneRenderer;
import net.minecraft.client.gui.Font;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;

@Mixin(value = Font.class, priority = 91)
public class FontMixin implements NineOneRenderer {
    @Unique private final Set<String> nineOne$remaining = new ObjectOpenHashSet<>();

    @ModifyVariable(method = {"drawInternal(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)I", "drawInBatch(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I", "drawInBatch(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)I"}, at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String nineOneMemeInject(String text) {
        return NineOneClient.replace(text, this);
    }

    @Override
    public Set<String> nineOne$getRemaining() {
        return this.nineOne$remaining;
    }
}