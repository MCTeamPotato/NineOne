package me.kall.nineone;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(NineOne.MOD_ID)
public final class NineOne {
    public static final String MOD_ID = "nineone";

    public NineOne(FMLJavaModLoadingContext context) {
        if (FMLLoader.getDist().isClient()) {
            context.registerConfig(ModConfig.Type.CLIENT, NineOneClient.Config.INSTANCE);
        }
    }
}
