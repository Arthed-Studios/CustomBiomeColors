package me.arthed.custombiomecolors.nms;

import me.arthed.custombiomecolors.utils.objects.BiomeColors;
import me.arthed.custombiomecolors.utils.objects.BiomeKey;

public interface NmsBiome {

    Object getBiomeBase();

    BiomeColors getBiomeColors();

    NmsBiome cloneWithDifferentColors(BiomeKey newBiomeKey, BiomeColors newColors);


}
