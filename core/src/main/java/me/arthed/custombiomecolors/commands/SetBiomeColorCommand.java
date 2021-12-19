package me.arthed.custombiomecolors.commands;

import me.arthed.custombiomecolors.CustomBiomeColors;
import me.arthed.custombiomecolors.integration.WorldEditHandler;
import me.arthed.custombiomecolors.nms.NmsServer;
import me.arthed.custombiomecolors.utils.objects.BiomeColorType;
import me.arthed.custombiomecolors.utils.objects.BiomeKey;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SetBiomeColorCommand implements CommandExecutor, TabExecutor {

    private static final WorldEditHandler worldEditHandler = CustomBiomeColors.getInstance().getWorldEditHandler();
    private static final NmsServer nmsServer = CustomBiomeColors.getInstance().getNmsServer();

    private final String command;
    private final BiomeColorType colorType;

    public SetBiomeColorCommand(String command, BiomeColorType colorType) {
        this.command = command;
        this.colorType = colorType;
    }

    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase(this.command)) {
            if(args.length > 0) {
                Block[] blocks = worldEditHandler.getSelectedBlocks(sender.getName());
                if (blocks.length == 0) {
                    sender.sendMessage(ChatColor.RED + "Make a region selection first.");
                    return true;
                }

                int color;
                try {
                    color = Integer.parseInt(args[0].replace("#", ""), 16);
                } catch( NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid color. Please use a valid hex color code.");
                    return true;
                }


                Runnable runWhenDone = () -> {
                    sender.sendMessage(ChatColor.GREEN + "Biome color was changed for approximately " + blocks.length + " blocks.");
                    sender.sendMessage(ChatColor.GREEN + "You must re-join to see the changes.");
                };

                if(args.length > 1) {
                    if(!args[1].contains(":")) {
                        sender.sendMessage(ChatColor.RED + "The biome name must contain a colon. ( : )");
                        return true;
                    }
                    BiomeKey biomeKey = new BiomeKey(args[1]);
                    if(nmsServer.doesBiomeExist(biomeKey)) {
                        sender.sendMessage(ChatColor.RED + "There already exists a biome with that name. Please use another one");
                        return true;
                    }

                    CustomBiomeColors.getInstance().getBiomeManager().changeBiomeColor(blocks, this.colorType, color, biomeKey, runWhenDone);
                }
                else {
                    CustomBiomeColors.getInstance().getBiomeManager().changeBiomeColor(blocks, this.colorType, color, runWhenDone);
                }

                sender.sendMessage(ChatColor.GRAY + "Changing the biome of " + blocks.length + " blocks...");
                if(blocks.length > 200000)
                    sender.sendMessage(ChatColor.GRAY + "This might take a while.");

                return true;
            }
        }

        return false;
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if(args.length == 1) {
            return Collections.singletonList("#HEXCODE");
        }
        else if(args.length == 2) {
            return Collections.singletonList("biome:name");
        }
        return null;
    }
}
