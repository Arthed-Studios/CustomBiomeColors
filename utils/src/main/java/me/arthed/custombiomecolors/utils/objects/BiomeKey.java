package me.arthed.custombiomecolors.utils.objects;

public class BiomeKey {

    public final String key;
    public final String value;

    public BiomeKey(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public BiomeKey(String biomeKeyString) {
        String[] split = biomeKeyString.split(":", 2);
        this.key = split[0];
        this.value = split[1];
    }

    @Override
    public String toString() {
        return key + ":" + value;
    }

}
