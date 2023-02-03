package me.arthed.custombiomecolors.nms;

import me.arthed.custombiomecolors.utils.ReflectionUtils;
import me.arthed.custombiomecolors.utils.objects.BiomeColors;
import me.arthed.custombiomecolors.utils.objects.BiomeKey;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeFog;
import net.minecraft.world.level.biome.BiomeSettingsGeneration;
import net.minecraft.world.level.biome.BiomeSettingsMobs;

import java.lang.reflect.Field;

public class NmsBiome_1_19 implements NmsBiome {

    private final BiomeBase biomeBase;

    public NmsBiome_1_19(BiomeBase biomeBase) {
        this.biomeBase = biomeBase;
    }

    public BiomeBase getBiomeBase() {
        return this.biomeBase;
    }

    @Override
    public BiomeColors getBiomeColors() {
        try {
            BiomeFog biomeFog = (BiomeFog) ReflectionUtils.getPrivateObject(this.biomeBase, "l");
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
    public NmsBiome cloneWithDifferentColors(NmsServer nmsServer, BiomeKey newBiomeKey, BiomeColors newColors) {
        ResourceKey<BiomeBase> customBiomeKey = ResourceKey.a(IRegistry.aR, new MinecraftKey(newBiomeKey.key, newBiomeKey.value));
        BiomeBase.a customBiomeBuilder = new BiomeBase.a();

        customBiomeBuilder.a(this.biomeBase.c());
        try {
            Field biomeSettingMobsField = BiomeBase.class.getDeclaredField("k");
            biomeSettingMobsField.setAccessible(true);
            BiomeSettingsMobs biomeSettingMobs = (BiomeSettingsMobs) biomeSettingMobsField.get(this.biomeBase);
            customBiomeBuilder.a(biomeSettingMobs);

            Field biomeSettingGenField = BiomeBase.class.getDeclaredField("j");
            biomeSettingGenField.setAccessible(true);
            BiomeSettingsGeneration biomeSettingGen = (BiomeSettingsGeneration) biomeSettingGenField.get(this.biomeBase);
            customBiomeBuilder.a(biomeSettingGen);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
        customBiomeBuilder.a(0.2F);
        customBiomeBuilder.b(0.05F);

        customBiomeBuilder.a(BiomeBase.TemperatureModifier.a);

        BiomeFog.a customBiomeColors = new BiomeFog.a();
        customBiomeColors.a(BiomeFog.GrassColor.a);

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

        nmsServer.registerBiome(customBiome, customBiomeKey);

        return new NmsBiome_1_19(customBiome);
    }

    public boolean equals(Object object) {
        return object instanceof NmsBiome && ((NmsBiome)object).getBiomeBase().equals(this.biomeBase);
    }

}
