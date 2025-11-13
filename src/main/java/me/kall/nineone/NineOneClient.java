package me.kall.nineone;

import me.kall.nineone.ext.NineOneRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NineOne.MOD_ID, value = Dist.CLIENT)
public class NineOneClient {
    private static long lastTriggerMoment = -1;

    public static boolean check(String s) {
        if (s == null || s.isEmpty()) return false;
        for (int i = 0, len = s.length(); i < len; i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
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

        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            builder.push("NineOne");
            CHANCE = builder.comment("The chance that a numeric string is replaced with \"91\". "
                    + "The replacement occurs with probability 1 / 91TextRenderChance. "
                    + "For example, a value of 10000 means a 1 in 10,000 chance per render.")
                    .defineInRange("91TextRenderChance", 10000, 1, Integer.MAX_VALUE);
            DURATION = builder.comment("How long will the 91 text remain on your screen.").defineInRange("91TextRemainingDuration(ms)", 10000, 0, Long.MAX_VALUE);
            builder.pop();
            INSTANCE = builder.build();
        }
    }
}
