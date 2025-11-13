package me.kall.nineone;

import com.google.common.base.Predicates;
import me.kall.nineone.ext.NineOneRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

@Mod.EventBusSubscriber(modid = NineOne.MOD_ID, value = Dist.CLIENT)
public class NineOneClient {
    private static long lastTriggerMoment = -1;

    public static int check(String s) {
        if (s == null || s.isEmpty()) return -1;
        for (int i = 0, len = s.length(); i < len; i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                return i;
            }
        }
        return -1;
    }

    public static String replace(@NotNull String text, NineOneRenderer nineOneRenderer) {
        String replacement = NineOneClient.Config.TEXT.get();
        int index = NineOneClient.check(text);
        if (index == -1) return text;

        if (nineOneRenderer.nineOne$getRemaining().contains(text)) return text.replaceAll("\\d+", replacement);

        if (ThreadLocalRandom.current().nextInt(NineOneClient.Config.CHANCE.get()) == 0) {
            nineOneRenderer.nineOne$getRemaining().add(text);
            return text.replaceAll("\\d+", replacement);
        }

        return text;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) {
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            if (font instanceof NineOneRenderer nineOneRenderer && !nineOneRenderer.nineOne$getRemaining().isEmpty()) {
                long currentMoment = System.currentTimeMillis();
                if (lastTriggerMoment == -1) {
                    lastTriggerMoment = currentMoment;
                } else {
                    if (currentMoment - lastTriggerMoment >= Config.DURATION.get()) {
                        nineOneRenderer.nineOne$getRemaining().clear();
                        lastTriggerMoment = -1;
                    }
                }
            }
        }
    }

    public static final class Config {
        public static final ForgeConfigSpec INSTANCE;
        public static final ForgeConfigSpec.IntValue CHANCE;
        public static final ForgeConfigSpec.LongValue DURATION;
        public static final ForgeConfigSpec.ConfigValue<? extends String> TEXT;

        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            builder.push("NineOne");
            CHANCE = builder.comment("The chance that a numeric string is replaced with \"91\". ", "The replacement occurs with probability 1 / 91TextRenderChance. ", "For example, a value of 10000 means a 1 in 10,000 chance per render tick.").defineInRange("91TextRenderChance", 10000, 1, Integer.MAX_VALUE);
            DURATION = builder.comment("How long will the 91 text remain on your screen.").defineInRange("91TextRemainingDuration(ms)", 10000, 0, Long.MAX_VALUE);
            TEXT = builder.define("TheRendered91Text", "91", Predicates.alwaysTrue());
            builder.pop();
            INSTANCE = builder.build();
        }
    }
}
