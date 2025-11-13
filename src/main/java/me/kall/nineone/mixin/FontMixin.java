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
import java.util.concurrent.ThreadLocalRandom;

@Mixin(Font.class)
public class FontMixin implements NineOneRenderer {
    @Unique private final Set<String> nineOne$remaining = new ObjectOpenHashSet<>();

    @ModifyVariable(method = "drawInternal(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)I", at = @At("HEAD"), argsOnly = true)
    private String nineOneMemeInject(String text) {
        if (this.nineOne$remaining.contains(text)) return "91";

        if (ThreadLocalRandom.current().nextInt(NineOneClient.Config.CHANCE.get()) == 1 && NineOneClient.check(text)) {
            this.nineOne$remaining.add(text);
            return "91";
        }

        return text;
    }

    @Override
    public Set<String> nineOne$getRemaining() {
        return this.nineOne$remaining;
    }
}
