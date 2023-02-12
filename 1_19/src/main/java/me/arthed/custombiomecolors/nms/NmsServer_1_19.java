package me.arthed.custombiomecolors.nms;

import com.mojang.serialization.Lifecycle;
import me.arthed.custombiomecolors.utils.objects.BiomeColors;
import me.arthed.custombiomecolors.utils.objects.BiomeKey;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistryWritable;
import net.minecraft.core.RegistryMaterials;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.World;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeFog;
import net.minecraft.world.level.biome.BiomeSettingsGeneration;
import net.minecraft.world.level.biome.BiomeSettingsMobs;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R2.CraftServer;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;

import java.lang.reflect.Field;

public class NmsServer_1_19 implements NmsServer {

	private final IRegistryWritable<BiomeBase> biomeRegistry = (IRegistryWritable<BiomeBase>) ((CraftServer) Bukkit.getServer()).getServer().aW().b(Registries.al);

	@Override
	public NmsBiome getBiomeFromBiomeKey(BiomeKey biomeKey) {
		return new NmsBiome_1_19(this.biomeRegistry.a(ResourceKey.a(Registries.al, new MinecraftKey(biomeKey.key, biomeKey.value))));
	}

	@Override
	public NmsBiome getBiomeFromBiomeBase(Object biomeBase) {
		return new NmsBiome_1_19(((Holder<BiomeBase>) biomeBase).a());
	}

	@Override
	public boolean doesBiomeExist(BiomeKey biomeKey) {
		return this.biomeRegistry.a(ResourceKey.a(Registries.al, new MinecraftKey(biomeKey.key, biomeKey.value))) == null;
	}

	@Override
	public void loadBiome(BiomeKey biomeKey, BiomeColors biomeColors) {
		BiomeBase biomeBase = this.biomeRegistry.a(ResourceKey.a(Registries.al, new MinecraftKey("minecraft", "plains")));
		ResourceKey<BiomeBase> customBiomeKey = ResourceKey.a(Registries.al, new MinecraftKey(biomeKey.key, biomeKey.value));
		BiomeBase.a customBiomeBuilder = new BiomeBase.a();

		customBiomeBuilder.a(biomeBase.a());
		customBiomeBuilder.a(biomeBase.c());
		try {
			Field biomeSettingMobsField = BiomeBase.class.getDeclaredField("k");
			biomeSettingMobsField.setAccessible(true);
			BiomeSettingsMobs biomeSettingMobs = (BiomeSettingsMobs) biomeSettingMobsField.get(biomeBase);
			customBiomeBuilder.a(biomeSettingMobs);

			Field biomeSettingGenField = BiomeBase.class.getDeclaredField("j");
			biomeSettingGenField.setAccessible(true);
			BiomeSettingsGeneration biomeSettingGen = (BiomeSettingsGeneration) biomeSettingGenField.get(biomeBase);
			customBiomeBuilder.a(biomeSettingGen);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		customBiomeBuilder.a(0.2F);
		customBiomeBuilder.b(0.05F);
		customBiomeBuilder.a(BiomeBase.TemperatureModifier.a);

		BiomeFog.a customBiomeColors = new BiomeFog.a();
		customBiomeColors.a(BiomeFog.GrassColor.a);

		if (biomeColors.getGrassColor() != 0) {
			customBiomeColors.f(biomeColors.getGrassColor());
		}
		if (biomeColors.getFoliageColor() != 0) {
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
			return chunk.getNoiseBiome(block.getX() >> 2, block.getY() >> 2, block.getZ() >> 2);
		}
		return null;
	}

	@Override
	public void registerBiome(Object biomeBase, Object biomeMinecraftKey) {
		this.biomeRegistry.a((ResourceKey<BiomeBase>) biomeMinecraftKey, (BiomeBase) biomeBase, Lifecycle.stable());
	}
}
