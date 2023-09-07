package me.arthed.custombiomecolors.nms;

import com.mojang.serialization.Lifecycle;
import me.arthed.custombiomecolors.utils.objects.BiomeColors;
import me.arthed.custombiomecolors.utils.objects.BiomeKey;
import net.minecraft.core.*;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.World;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeFog;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;

import java.lang.reflect.Field;

public class NmsServer_1_18 implements NmsServer {

    private final RegistryMaterials<BiomeBase> biomeRegistry = ((RegistryMaterials<BiomeBase>) ((CraftServer) Bukkit.getServer()).getServer().aU().b(IRegistry.aP));

    @Override
    public NmsBiome getBiomeFromBiomeKey(BiomeKey biomeKey) {
        return new NmsBiome_1_18(this.biomeRegistry.a(ResourceKey.a(
                IRegistry.aP,
                new MinecraftKey(biomeKey.key, biomeKey.value)
        )));
    }

    @Override
    public NmsBiome getBiomeFromBiomeBase(Object biomeBase) {
        return new NmsBiome_1_18((BiomeBase) biomeBase);
    }

    @Override
    public boolean doesBiomeExist(BiomeKey biomeKey) {
        return this.biomeRegistry.a(ResourceKey.a(
                IRegistry.aP,
                new MinecraftKey(biomeKey.key, biomeKey.value)
        )) != null;
    }

    @Override
    public void loadBiome(BiomeKey biomeKey, BiomeColors biomeColors) {
        BiomeBase biomeBase = this.biomeRegistry.a(ResourceKey.a(
                IRegistry.aP,
                new MinecraftKey("minecraft", "plains")
        ));
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

        this.registerBiome(customBiome, customBiomeKey);
    }

    @Override
    public void setBlocksBiome(Block block, NmsBiome nmsBiome) {
        BlockPosition blockPosition = new BlockPosition(block.getX(), block.getY(), block.getZ());
        World nmsWorld = ((CraftWorld) block.getWorld()).getHandle();

        net.minecraft.world.level.chunk.Chunk chunk = nmsWorld.l(blockPosition);
        if (chunk != null) {
            chunk.setBiome(block.getX() >> 2, block.getY() >> 2, block.getZ() >> 2, Holder.a((BiomeBase) nmsBiome.getBiomeBase()));
        }
    }

    @Override
    public Object getBlocksBiomeBase(Block block) {
        BlockPosition blockPosition = new BlockPosition(block.getX(), block.getY(), block.getZ());
        World nmsWorld = ((CraftWorld) block.getWorld()).getHandle();

        net.minecraft.world.level.chunk.Chunk chunk = nmsWorld.l(blockPosition);
        if (chunk != null) {
            return chunk.getNoiseBiome(
                    block.getX() >> 2,
                    block.getY() >> 2,
                    block.getZ() >> 2);
        }
        return null;
    }

    @Override
    public void registerBiome(Object biomeBase, Object biomeMinecraftKey) {
        try {
            Field isFrozen = this.biomeRegistry.getClass().getDeclaredField("bL");
            isFrozen.setAccessible(true);
            isFrozen.set(this.biomeRegistry, false);
            this.biomeRegistry.a((ResourceKey<BiomeBase>) biomeMinecraftKey, (BiomeBase) biomeBase, Lifecycle.stable());
            isFrozen.set(this.biomeRegistry, true);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String getBiomeString(NmsBiome nmsBiome) {
        MinecraftKey minecraftKey = this.biomeRegistry.b((BiomeBase) nmsBiome.getBiomeBase());
        if(minecraftKey != null)
            return minecraftKey.toString();
        return "minecraft:forest";
    }

}
