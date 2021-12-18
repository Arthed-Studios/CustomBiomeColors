package me.arthed.custombiomecolors.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.arthed.custombiomecolors.CustomBiomeColors;
import me.arthed.custombiomecolors.nms.NmsBiome;
import me.arthed.custombiomecolors.utils.objects.BiomeColors;
import me.arthed.custombiomecolors.utils.objects.BiomeKey;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private final CustomBiomeColors plugin = CustomBiomeColors.getInstance();

    private final Gson gson = new GsonBuilder().create();
    private Map<String, int[]> map = new HashMap<>();

    private final File file;

    public DataManager(String fileName) {
        this.file = new File(this.plugin.getDataFolder(), fileName);
        if (!this.file.exists()) {
            this.plugin.saveResource(fileName, false);
        }

        try {
            Type typeToken = new TypeToken<Map<String, int[]>>() {}.getType();
            this.map = gson.fromJson(new FileReader(this.file), typeToken);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save() throws IOException {
        final String json = gson.toJson(map);
        this.file.delete();
        Files.write(this.file.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    public void saveBiome(BiomeKey biomeKey, BiomeColors biomeColors) {
        this.map.put(biomeKey.key + ":" + biomeKey.value, new int[] {
                biomeColors.getGrassColor(),
                biomeColors.getFoliageColor(),
                biomeColors.getWaterColor(),
                biomeColors.getWaterFogColor(),
                biomeColors.getSkyColor(),
                biomeColors.getFogColor()
        });
    }

    @Nullable
    public NmsBiome getBiomeWithSpecificColors(BiomeColors biomeColors) {
        for(String biomeKeyString : this.map.keySet()) {
            int[] colors = map.get(biomeKeyString);
            if(colors[0] == biomeColors.getGrassColor() &&
                    colors[1] == biomeColors.getFoliageColor() &&
                    colors[2] == biomeColors.getWaterColor() &&
                    colors[3] == biomeColors.getWaterFogColor() &&
                    colors[4] == biomeColors.getSkyColor() &&
                    colors[5] == biomeColors.getFogColor()) {
                return plugin.getNmsServer().getBiomeFromBiomeKey(new BiomeKey(biomeKeyString));
            }
        }
        return null;
    }

    public void loadBiomes() {
        for (String biomeKeyString : this.map.keySet()) {
            int[] colors = map.get(biomeKeyString);
            plugin.getNmsServer().loadBiome(
                    new BiomeKey(biomeKeyString),
                    new BiomeColors()
                            .setGrassColor(colors[0])
                            .setFoliageColor(colors[1])
                            .setWaterColor(colors[2])
                            .setWaterFogColor(colors[3])
                            .setSkyColor(colors[4])
                            .setFogColor(colors[5]));
        }
    }

}
