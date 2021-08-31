package me.arthed.custombiomecolors.nms;

import me.arthed.custombiomecolors.CustomBiomeColors;
import me.arthed.custombiomecolors.utils.ReflectionUtils;
import me.arthed.custombiomecolors.utils.objects.BiomeColors;
import me.arthed.custombiomecolors.utils.objects.BiomeKey;
import net.minecraft.server.v1_16_R3.*;

import java.lang.reflect.Field;

public class NmsBiome_1_16 implements NmsBiome {

    private final BiomeBase biomeBase;

    public NmsBiome_1_16(BiomeBase biomeBase) {
        this.biomeBase = biomeBase;
    }

    public BiomeBase getBiomeBase() {
        return this.biomeBase;
    }

    @Override
    public BiomeColors getBiomeColors() {
        try {
            BiomeFog biomeFog = (BiomeFog) ReflectionUtils.getPrivateObject(this.biomeBase, "p");
            assert biomeFog != null;
            return new BiomeColors()
                    .setGrassColor(ReflectionUtils.getPrivateOptionalInteger(biomeFog, "g"))
                    .setFoliageColor(ReflectionUtils.getPrivateOptionalInteger(biomeFog, "f"))
                    .setWaterColor(ReflectionUtils.getPrivateInteger(biomeFog, "c"))
                    .setWaterFogColor(ReflectionUtils.getPrivateInteger(biomeFog, "d"))
                    .setSkyColor(ReflectionUtils.getPrivateInteger(biomeFog, "e"))
                    .setFogColor(ReflectionUtils.getPrivateInteger(biomeFog, "b"));
        } catch(NoSuchFieldException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public NmsBiome cloneWithDifferentColors(BiomeKey newBiomeKey, BiomeColors newColors) {
        ResourceKey<BiomeBase> customBiomeKey = ResourceKey.a(IRegistry.ay, new MinecraftKey(newBiomeKey.key, newBiomeKey.value));
        BiomeBase.a customBiomeBuilder = new BiomeBase.a();

        customBiomeBuilder.a(this.biomeBase.t());
        customBiomeBuilder.a(this.biomeBase.c());
        try {
            Field biomeSettingMobsField = BiomeBase.class.getDeclaredField("l");
            biomeSettingMobsField.setAccessible(true);
            BiomeSettingsMobs biomeSettingMobs = (BiomeSettingsMobs) biomeSettingMobsField.get(this.biomeBase);
            customBiomeBuilder.a(biomeSettingMobs);

            Field biomeSettingGenField = BiomeBase.class.getDeclaredField("k");
            biomeSettingGenField.setAccessible(true);
            BiomeSettingsGeneration biomeSettingGen = (BiomeSettingsGeneration) biomeSettingGenField.get(this.biomeBase);
            customBiomeBuilder.a(biomeSettingGen);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
        customBiomeBuilder.a(0.2F);
        customBiomeBuilder.b(0.05F);
        customBiomeBuilder.c(0.7F);
        customBiomeBuilder.d(0.8F);
        customBiomeBuilder.a(BiomeBase.TemperatureModifier.NONE);

        BiomeFog.a customBiomeColors = new BiomeFog.a();
        customBiomeColors.a(BiomeFog.GrassColor.NONE);

        if(newColors.getGrassColor() != 0) {
            customBiomeColors.f(newColors.getGrassColor());
        }
        if(newColors.getFoliageColor() != 0) {
            customBiomeColors.e(newColors.getFoliageColor());
        }
        customBiomeColors.b(newColors.getWaterColor());
        customBiomeColors.c(newColors.getWaterFogColor());
        customBiomeColors.d(newColors.getSkyColor());
        customBiomeColors.a(newColors.getFogColor());

        customBiomeBuilder.a(customBiomeColors.a());
        BiomeBase customBiome = customBiomeBuilder.a();

        CustomBiomeColors.getInstance().getNmsServer().registerBiome(customBiome, customBiomeKey);

        return new NmsBiome_1_16(customBiome);
    }

    public boolean equals(Object object) {
        return object instanceof NmsBiome && ((NmsBiome)object).getBiomeBase().equals(this.biomeBase);
    }

}
