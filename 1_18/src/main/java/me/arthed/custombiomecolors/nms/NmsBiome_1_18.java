package me.arthed.custombiomecolors.nms;

import me.arthed.custombiomecolors.utils.ReflectionUtils;
import me.arthed.custombiomecolors.utils.objects.BiomeColors;
import me.arthed.custombiomecolors.utils.objects.BiomeKey;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeFog;

import java.lang.reflect.Field;

public class NmsBiome_1_18 implements NmsBiome {

    private final BiomeBase biomeBase;

    public NmsBiome_1_18(BiomeBase biomeBase) {
        this.biomeBase = biomeBase;
    }

    public BiomeBase getBiomeBase() {
        return this.biomeBase;
    }

    @Override
    public BiomeColors getBiomeColors() {
        try {
            BiomeFog biomeFog = (BiomeFog) ReflectionUtils.getPrivateObject(this.biomeBase, "n");
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
    public NmsBiome cloneWithDifferentColors(NmsServer nmsServer, BiomeKey biomeKey, BiomeColors biomeColors) {
        ResourceKey<BiomeBase> customBiomeKey = ResourceKey.a(IRegistry.aP, new MinecraftKey(biomeKey.key, biomeKey.value));
        BiomeBase.a customBiomeBuilder = new BiomeBase.a();

        customBiomeBuilder.a(biomeBase.e());
        customBiomeBuilder.a(biomeBase.b());
        customBiomeBuilder.a(biomeBase.c());
        try {
            Field geographyField = BiomeBase.class.getDeclaredField("t");
            geographyField.setAccessible(true);
            BiomeBase.Geography geography = (BiomeBase.Geography) geographyField.get(biomeBase);
            customBiomeBuilder.a(geography);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
        customBiomeBuilder.a(0.7F);
        customBiomeBuilder.b(0.8F);
        customBiomeBuilder.a(BiomeBase.TemperatureModifier.a);

        BiomeFog.a customBiomeColors = new BiomeFog.a();
        customBiomeColors.a(BiomeFog.GrassColor.a);

        if(biomeColors.getGrassColor() != 0) {
            customBiomeColors.f(biomeColors.getGrassColor());
        }
        if(biomeColors.getFoliageColor() != 0) {
            customBiomeColors.e(biomeColors.getFoliageColor());
        }
        customBiomeColors.b(biomeColors.getWaterColor());
        customBiomeColors.c(biomeColors.getWaterFogColor());
        customBiomeColors.d(biomeColors.getSkyColor());
        customBiomeColors.a(biomeColors.getFogColor());

        customBiomeBuilder.a(customBiomeColors.a());
        BiomeBase customBiome = customBiomeBuilder.a();

        nmsServer.registerBiome(customBiome, customBiomeKey);

        return new NmsBiome_1_18(customBiome);
    }

    public boolean equals(Object object) {
        return object instanceof NmsBiome && ((NmsBiome)object).getBiomeBase().equals(this.biomeBase);
    }

}
