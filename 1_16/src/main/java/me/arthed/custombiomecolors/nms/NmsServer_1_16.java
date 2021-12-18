package me.arthed.custombiomecolors.nms;

import com.mojang.serialization.Lifecycle;
import me.arthed.custombiomecolors.utils.objects.BiomeColors;
import me.arthed.custombiomecolors.utils.objects.BiomeKey;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import java.lang.reflect.Field;

public class NmsServer_1_16 implements NmsServer {

    private final IRegistryWritable<BiomeBase> biomeRegistry = ((CraftServer) Bukkit.getServer()).getServer().getCustomRegistry().b(IRegistry.ay);

    @Override
    public NmsBiome getBiomeFromBiomeKey(BiomeKey biomeKey) {
        return new NmsBiome_1_16(this.biomeRegistry.a(ResourceKey.a(
                IRegistry.ay,
                new MinecraftKey(biomeKey.key, biomeKey.value)
        )));
    }

    @Override
    public NmsBiome getBiomeFromBiomeBase(Object biomeBase) {
        return new NmsBiome_1_16((BiomeBase) biomeBase);
    }

    @Override
    public boolean doesBiomeExist(BiomeKey biomeKey) {
        return this.biomeRegistry.a(ResourceKey.a(
                IRegistry.ay,
                new MinecraftKey(biomeKey.key, biomeKey.value)
        )) == null;
    }

    @Override
    public void loadBiome(BiomeKey biomeKey, BiomeColors biomeColors) {
        BiomeBase biomeBase = this.biomeRegistry.a(ResourceKey.a(
                IRegistry.ay,
                new MinecraftKey("minecraft", "plains")
        ));
        ResourceKey<BiomeBase> customBiomeKey = ResourceKey.a(IRegistry.ay, new MinecraftKey(biomeKey.key, biomeKey.value));
        BiomeBase.a customBiomeBuilder = new BiomeBase.a();

        customBiomeBuilder.a(biomeBase.t());
        customBiomeBuilder.a(biomeBase.c());
        try {
            Field biomeSettingMobsField = BiomeBase.class.getDeclaredField("l");
            biomeSettingMobsField.setAccessible(true);
            BiomeSettingsMobs biomeSettingMobs = (BiomeSettingsMobs) biomeSettingMobsField.get(biomeBase);
            customBiomeBuilder.a(biomeSettingMobs);

            Field biomeSettingGenField = BiomeBase.class.getDeclaredField("k");
            biomeSettingGenField.setAccessible(true);
            BiomeSettingsGeneration biomeSettingGen = (BiomeSettingsGeneration) biomeSettingGenField.get(biomeBase);
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

        this.registerBiome(customBiome, customBiomeKey);
    }

    @Override
    public void setBlocksBiome(Block block, NmsBiome nmsBiome) {
        BlockPosition blockPosition = new BlockPosition(block.getX(), block.getY(), block.getZ());
        World nmsWorld = ((CraftWorld) block.getWorld()).getHandle();

        net.minecraft.server.v1_16_R3.Chunk chunk = nmsWorld.getChunkAtWorldCoords(blockPosition);
        if (chunk != null && chunk.getBiomeIndex() != null) {
            chunk.getBiomeIndex().setBiome(
                    blockPosition.getX() >> 2,
                    blockPosition.getY() >> 2,
                    blockPosition.getZ() >> 2,
                    (BiomeBase) nmsBiome.getBiomeBase());
            chunk.markDirty();
        }
    }

    @Override
    public Object getBlocksBiomeBase(Block block) {
        BlockPosition blockPosition = new BlockPosition(block.getX(), block.getY(), block.getZ());
        World nmsWorld = ((CraftWorld) block.getWorld()).getHandle();

        net.minecraft.server.v1_16_R3.Chunk chunk = nmsWorld.getChunkAtWorldCoords(blockPosition);
        if (chunk != null && chunk.getBiomeIndex() != null) {
            return chunk.getBiomeIndex().getBiome(
                    blockPosition.getX() >> 2,
                    block.getY() >> 2,
                    blockPosition.getZ() >> 2);
        }
        return null;
    }

    @Override
    public void registerBiome(Object biomeBase, Object biomeMinecraftKey) {
        this.biomeRegistry.a((ResourceKey<BiomeBase>) biomeMinecraftKey, (BiomeBase) biomeBase, Lifecycle.stable());
    }

}
