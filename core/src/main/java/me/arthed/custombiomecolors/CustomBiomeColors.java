package me.arthed.custombiomecolors;

import me.arthed.custombiomecolors.commands.GetBiomeColorsCommand;
import me.arthed.custombiomecolors.commands.SetBiomeColorCommand;
import me.arthed.custombiomecolors.data.DataManager;
import me.arthed.custombiomecolors.integration.WorldEditHandler;
import me.arthed.custombiomecolors.nms.NmsServer;
import me.arthed.custombiomecolors.nms.NmsServer_1_16;
import me.arthed.custombiomecolors.nms.NmsServer_1_17;
import me.arthed.custombiomecolors.nms.NmsServer_1_18;
import me.arthed.custombiomecolors.nms.NmsServer_1_19;
import me.arthed.custombiomecolors.utils.BStats;
import me.arthed.custombiomecolors.utils.Updater;
import me.arthed.custombiomecolors.utils.objects.BiomeColorType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public final class CustomBiomeColors extends JavaPlugin {

    private static CustomBiomeColors instance;
    public static CustomBiomeColors getInstance() {
        return instance;
    }

    private NmsServer nmsServer;
    @NotNull
    public NmsServer getNmsServer() {
        return this.nmsServer;
    }

    private BiomeManager biomeManager;
    @NotNull
    public BiomeManager getBiomeManager() {
        return this.biomeManager;
    }

    private WorldEditHandler worldEditHandler;
    public WorldEditHandler getWorldEditHandler() {
        return this.worldEditHandler;
    }

    private DataManager dataManager;
    public DataManager getDataManager() {
        return this.dataManager;
    }

    @Override
    public void onLoad() {
        instance = this;

        if(Bukkit.getVersion().contains("1.16")) {
            this.nmsServer = new NmsServer_1_16();
        } else if(Bukkit.getVersion().contains("1.17")) {
            this.nmsServer = new NmsServer_1_17();
        } else if(Bukkit.getVersion().contains("1.18")) {
            this.nmsServer = new NmsServer_1_18();
        } else if(Bukkit.getVersion().contains("1.19")) {
				    this.nmsServer = new NmsServer_1_19();
				} else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l[CustomBiomeColors] This plugin works only on 1.16 and higher versions."));
            Bukkit.getPluginManager().disablePlugin(this);
        }

        this.dataManager = new DataManager("data.json");
        this.dataManager.loadBiomes();

    }

    @Override
    public void onEnable() {
        new BStats(this, 12660);

        this.biomeManager = new BiomeManager();
        this.worldEditHandler = new WorldEditHandler();

        Objects.requireNonNull(this.getCommand("/setgrasscolor")).setExecutor(new SetBiomeColorCommand("/setgrasscolor", BiomeColorType.GRASS));
        Objects.requireNonNull(this.getCommand("/setfoliagecolor")).setExecutor(new SetBiomeColorCommand("/setfoliagecolor", BiomeColorType.FOLIAGE));
        Objects.requireNonNull(this.getCommand("/setwatercolor")).setExecutor(new SetBiomeColorCommand("/setwatercolor", BiomeColorType.WATER));
        Objects.requireNonNull(this.getCommand("/setwaterfogcolor")).setExecutor(new SetBiomeColorCommand("/setwaterfogcolor", BiomeColorType.WATER_FOG));
        Objects.requireNonNull(this.getCommand("/setskycolor")).setExecutor(new SetBiomeColorCommand("/setskycolor", BiomeColorType.SKY));
        Objects.requireNonNull(this.getCommand("/setfogcolor")).setExecutor(new SetBiomeColorCommand("/setfogcolor", BiomeColorType.FOG));
        Objects.requireNonNull(this.getCommand("/getbiomecolors")).setExecutor(new GetBiomeColorsCommand());

        new Updater(this, 95858);

    }

    @Override
    public void onDisable() {
        try {
            this.dataManager.save();
        } catch (IOException ignore) {}
    }

}
