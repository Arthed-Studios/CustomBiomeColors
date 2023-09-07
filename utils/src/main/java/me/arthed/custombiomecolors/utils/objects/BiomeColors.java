package me.arthed.custombiomecolors.utils.objects;

public class BiomeColors {

    private int grassColor;
    private int foliageColor;
    private int waterColor;
    private int waterFogColor;
    private int skyColor;
    private int fogColor;

    public BiomeColors setGrassColor(int grassColor) {
        this.grassColor = grassColor;
        return this;
    }

    public BiomeColors setFoliageColor(int foliageColor) {
        this.foliageColor = foliageColor;
        return this;
    }

    public BiomeColors setWaterColor(int waterColor) {
        this.waterColor = waterColor;
        return this;
    }

    public BiomeColors setWaterFogColor(int waterFogColor) {
        this.waterFogColor = waterFogColor;
        return this;
    }

    public BiomeColors setSkyColor(int skyColor) {
        this.skyColor = skyColor;
        return this;
    }

    public BiomeColors setFogColor(int fogColor) {
        this.fogColor = fogColor;
        return this;
    }

    public void setColor(BiomeColorType colorType, int color) {
        if(colorType.equals(BiomeColorType.GRASS)) {
            this.setGrassColor(color);
        } else if(colorType.equals(BiomeColorType.FOLIAGE)) {
            this.setFoliageColor(color);
        } else if(colorType.equals(BiomeColorType.WATER)) {
            this.setWaterColor(color);
        } else if(colorType.equals(BiomeColorType.WATER_FOG)) {
            this.setWaterFogColor(color);
        } else if(colorType.equals(BiomeColorType.SKY)) {
            this.setSkyColor(color);
        } else if(colorType.equals(BiomeColorType.FOG)) {
            this.setFogColor(color);
        }
    }

    public int getGrassColor() {
        return grassColor;
    }

    public int getFoliageColor() {
        return foliageColor;
    }

    public int getWaterColor() {
        return waterColor;
    }

    public int getWaterFogColor() {
        return waterFogColor;
    }

    public int getSkyColor() {
        return skyColor;
    }

    public int getFogColor() {
        return fogColor;
    }

}
