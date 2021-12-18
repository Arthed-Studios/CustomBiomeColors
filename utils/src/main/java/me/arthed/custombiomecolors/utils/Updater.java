package me.arthed.custombiomecolors.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Updater implements Listener {

    public boolean update;
    private final String currentVersion;
    private final int pluginId;

    private final String message;



    public Updater(JavaPlugin plugin, int pluginId) {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        this.currentVersion = plugin.getDescription().getVersion();
        this.pluginId = pluginId;
        this.message = ChatColor.translateAlternateColorCodes('&', "&7[CustomBiomeColors] There is an update availabe! Download it from: https://www.spigotmc.org/resources/" + this.pluginId + "/");

        this.checkUpdates();
    }

    public void checkUpdates() {
        Thread thread = new Thread(() -> {
            URL url = null;
            try {
                url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.pluginId);
            } catch (MalformedURLException ignored) {}
            URLConnection conn = null;
            try {
                assert url != null;
                conn = url.openConnection();
            } catch (IOException ignored) {}
            try {
                assert conn != null;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                if (br.readLine().equals(currentVersion)) {
                    update = Boolean.FALSE;
                } else {
                    update = Boolean.TRUE;
                    Bukkit.getConsoleSender().sendMessage(this.message);
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        if(p.isOp()) {
                            p.sendMessage(this.message);
                        }
                    }
                }
            } catch (IOException ignored) {}
        });

        thread.start();

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(p.isOp() && update) {
            p.sendMessage(this.message);
        }
    }

}
