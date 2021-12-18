package me.arthed.custombiomecolors;

import me.arthed.custombiomecolors.data.DataManager;
import me.arthed.custombiomecolors.nms.NmsBiome;
import me.arthed.custombiomecolors.nms.NmsServer;
import me.arthed.custombiomecolors.utils.StringUtils;
import me.arthed.custombiomecolors.utils.objects.BiomeColorType;
import me.arthed.custombiomecolors.utils.objects.BiomeColors;
import me.arthed.custombiomecolors.utils.objects.BiomeKey;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import java.util.*;

public class BiomeManager {

    private final NmsServer nmsServer = CustomBiomeColors.getInstance().getNmsServer();
    private final DataManager dataManager = CustomBiomeColors.getInstance().getDataManager();

    public void changeBiomeColor(Block[] blocks, BiomeColorType colorType, int color) {
        this.changeBiomeColor(blocks, colorType, color, new BiomeKey("cbc", StringUtils.randomString(8)));
    }

    public void changeBiomeColor(Block[] blocks, BiomeColorType colorType, int color, BiomeKey biomeKey) {
                // Separate blocks by their biome

                // key - BiomeBase of the biome
                // value - list of blocks in that biome
                Map<Object, List<Block>> blocksInEachBiome = new HashMap<>();

                for (Block block : blocks) {
                    boolean added = false;
                    Object blocksBiomeBase = nmsServer.getBlocksBiomeBase(block);
                    for (Object biomeBase : blocksInEachBiome.keySet()) {
                        if (blocksBiomeBase.equals(biomeBase)) {
                            blocksInEachBiome.get(biomeBase).add(block);
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        List<Block> blocksInBiome = new ArrayList<>();
                        blocksInBiome.add(block);
                        blocksInEachBiome.put(blocksBiomeBase, blocksInBiome);
                    }
                }

                // Add the new color change to every block from each biome type

                BiomeKey individualBiomeKey = biomeKey;
                int i = 0;
                for (Object biomeBase : blocksInEachBiome.keySet()) {
                    NmsBiome biome = nmsServer.getBiomeFromBiomeBase(biomeBase);
                    BiomeColors biomeColors = biome.getBiomeColors();
                    biomeColors.setColor(colorType, color);

                    NmsBiome newBiome = this.dataManager.getBiomeWithSpecificColors(biomeColors);

                    if (newBiome == null) {
                        newBiome = biome.cloneWithDifferentColors(this.nmsServer, individualBiomeKey, biomeColors);
                        this.dataManager.saveBiome(individualBiomeKey, biomeColors);
                        individualBiomeKey = new BiomeKey(biomeKey.key, biomeKey.value + "." + i);
                        i++;
                    }

                    for (Block block : blocksInEachBiome.get(biomeBase)) {
                        nmsServer.setBlocksBiome(block, newBiome);
                    }
                }
    }

}
