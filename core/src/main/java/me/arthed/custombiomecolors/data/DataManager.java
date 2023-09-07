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
    private Map<String, String[]> map = new HashMap<>();

    private final File file;

    public DataManager(String fileName) {
        this.file = new File(this.plugin.getDataFolder(), fileName);
        if (!this.file.exists()) {
            this.plugin.saveResource(fileName, false);
        }

        try {
            Type typeToken = new TypeToken<Map<String, String[]>>() {}.getType();
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
        String biomeKeyString = biomeKey.key + ":" + biomeKey.value;
        BiomeColors.customBiomeColors.put(biomeKeyString, biomeColors);
        this.map.put(biomeKeyString, new String[] {
                String.valueOf(biomeColors.getGrassColor()),
                String.valueOf(biomeColors.getFoliageColor()),
                String.valueOf(biomeColors.getWaterColor()),
                String.valueOf(biomeColors.getWaterFogColor()),
                String.valueOf(biomeColors.getSkyColor()),
                String.valueOf(biomeColors.getFogColor()),
                biomeColors.getBaseBiomeKey()
        });
    }

    @Nullable
    public NmsBiome getBiomeWithSpecificColors(BiomeColors biomeColors) {
        for(String biomeKeyString : this.map.keySet()) {
            String[] data = map.get(biomeKeyString);

            int[] colors = new int[] {
                    Integer.parseInt(data[0]),
                    Integer.parseInt(data[1]),
                    Integer.parseInt(data[2]),
                    Integer.parseInt(data[3]),
                    Integer.parseInt(data[4]),
                    Integer.parseInt(data[5])
            };
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
            String[] data = map.get(biomeKeyString);

            int[] colors = new int[] {
                    Integer.parseInt(data[0]),
                    Integer.parseInt(data[1]),
                    Integer.parseInt(data[2]),
                    Integer.parseInt(data[3]),
                    Integer.parseInt(data[4]),
                    Integer.parseInt(data[5])
            };
            BiomeColors biomeColors = null;
            if(data.length == 6) {
                biomeColors = new BiomeColors()
                        .setGrassColor(colors[0])
                        .setFoliageColor(colors[1])
                        .setWaterColor(colors[2])
                        .setWaterFogColor(colors[3])
                        .setSkyColor(colors[4])
                        .setFogColor(colors[5]);
            }
            else {
                biomeColors = new BiomeColors()
                        .setGrassColor(colors[0])
                        .setFoliageColor(colors[1])
                        .setWaterColor(colors[2])
                        .setWaterFogColor(colors[3])
                        .setSkyColor(colors[4])
                        .setFogColor(colors[5])
                        .setBaseBiomeKey(data[6]);
            }
            plugin.getNmsServer().loadBiome(new BiomeKey(biomeKeyString), biomeColors);
            BiomeColors.customBiomeColors.put(biomeKeyString, biomeColors);

        }
    }

}
