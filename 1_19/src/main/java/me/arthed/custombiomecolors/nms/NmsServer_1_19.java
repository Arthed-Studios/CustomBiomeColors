package me.arthed.custombiomecolors.nms;

import com.mojang.serialization.Lifecycle;
import me.arthed.custombiomecolors.utils.objects.BiomeColors;
import me.arthed.custombiomecolors.utils.objects.BiomeKey;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.World;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeFog;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public class NmsServer_1_19 implements NmsServer {

	private final RegistryMaterials<BiomeBase> biomeRegistry = (RegistryMaterials<BiomeBase>) ((CraftServer) Bukkit.getServer()).getServer().aX().d(Registries.an);

	@Override
	public NmsBiome getBiomeFromBiomeKey(BiomeKey biomeKey) {
		return new NmsBiome_1_19(this.biomeRegistry.a(ResourceKey.a(Registries.an, new MinecraftKey(biomeKey.key, biomeKey.value))));
	}

	@Override
	public NmsBiome getBiomeFromBiomeBase(Object biomeBase) {
		return new NmsBiome_1_19(((Holder<BiomeBase>) biomeBase).a());
	}

	@Override
	public boolean doesBiomeExist(BiomeKey biomeKey) {
		return this.biomeRegistry.a(ResourceKey.a(Registries.an, new MinecraftKey(biomeKey.key, biomeKey.value))) == null;
	}

	@Override
	public void loadBiome(BiomeKey biomeKey, BiomeColors biomeColors) {
		BiomeBase biomeBase = this.biomeRegistry.a(ResourceKey.a(
				Registries.an,
				new MinecraftKey("minecraft", "plains")
		));

		ResourceKey<BiomeBase> customBiomeKey = ResourceKey.a(Registries.an, new MinecraftKey(biomeKey.key, biomeKey.value));
		BiomeBase.a customBiomeBuilder = new BiomeBase.a();

		customBiomeBuilder.a(biomeBase.d());
		customBiomeBuilder.a(biomeBase.b());
		customBiomeBuilder.a(0.7F);
		customBiomeBuilder.b(0.8F);
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

		try {
			Field frozen = RegistryMaterials.class.getDeclaredField("l");
			frozen.setAccessible(true);
			frozen.set(this.biomeRegistry, false);

			Field unregisteredIntrusiveHolders = RegistryMaterials.class.getDeclaredField("m");
			unregisteredIntrusiveHolders.setAccessible(true);
			unregisteredIntrusiveHolders.set(this.biomeRegistry, new IdentityHashMap<>());

			//biome is the BiomeBase that you're registering
			//f is createIntrusiveHolder
			this.biomeRegistry.f((BiomeBase) biomeBase);
			//a is RegistryMaterials.register
			this.biomeRegistry.a((ResourceKey<BiomeBase>) biomeMinecraftKey, (BiomeBase) biomeBase, Lifecycle.stable());

			//Make unregisteredIntrusiveHolders null again to remove potential for undefined behaviour
			unregisteredIntrusiveHolders.set(this.biomeRegistry, null);

			frozen.setAccessible(true);
			frozen.set(this.biomeRegistry, true);
		} catch(Exception error) {
			error.printStackTrace();
		}
	}
}
