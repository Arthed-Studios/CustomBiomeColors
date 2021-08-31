package me.arthed.custombiomecolors.commands;

import me.arthed.custombiomecolors.CustomBiomeColors;
import me.arthed.custombiomecolors.nms.NmsBiome;
import me.arthed.custombiomecolors.nms.NmsServer;
import me.arthed.custombiomecolors.utils.ColorUtils;
import me.arthed.custombiomecolors.utils.objects.BiomeColors;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetBiomeColorsCommand implements CommandExecutor {

    private static final NmsServer nmsServer = CustomBiomeColors.getInstance().getNmsServer();

    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("/getbiomecolors")) {
           if(sender instanceof Player) {
               Player player = (Player) sender;
               NmsBiome biome = nmsServer.getBiomeFromBiomeBase(nmsServer.getBlocksBiomeBase(player.getLocation().getBlock()));
               BiomeColors biomeColors = biome.getBiomeColors();
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aColors of the biome you're in:"));
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7 - Grass: &7&l" + ColorUtils.intToHex(biomeColors.getGrassColor())));
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7 - Foliage: &7&l" + ColorUtils.intToHex(biomeColors.getFoliageColor())));
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7 - Water: &7&l" + ColorUtils.intToHex(biomeColors.getWaterColor())));
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7 - Water Fog: &7&l" + ColorUtils.intToHex(biomeColors.getWaterFogColor())));
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7 - Sky: &7&l" + ColorUtils.intToHex(biomeColors.getSkyColor())));
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7 - Fog: &7&l" + ColorUtils.intToHex(biomeColors.getFogColor())));

               return true;
           }
           sender.sendMessage(ChatColor.RED + "Only players can use this command.");
        }

        return false;
    }
}