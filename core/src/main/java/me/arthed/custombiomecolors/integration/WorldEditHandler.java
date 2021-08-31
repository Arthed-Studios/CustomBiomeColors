package me.arthed.custombiomecolors.integration;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class WorldEditHandler {

    private final WorldEdit worldEdit = WorldEdit.getInstance();

    @NotNull
    public Block[] getSelectedBlocks(String authorsName) {
        LocalSession worldEditSession = worldEdit.getSessionManager().findByName(authorsName);
        if(worldEditSession != null) {
            if(worldEditSession.getSelectionWorld() != null) {
                RegionSelector regionSelector = worldEditSession.getRegionSelector(worldEditSession.getSelectionWorld());
                if(regionSelector.isDefined()) {
                    try {
                        Region region = regionSelector.getRegion();
                        World world = Bukkit.getWorld(Objects.requireNonNull(region.getWorld()).getName());
                        Block[] blocks = new Block[(int) region.getVolume()];

                        int i = 0;
                        for(BlockVector3 blockVector3 : region) {
                            blocks[i] = new Location(world, blockVector3.getX(), blockVector3.getY(), blockVector3.getZ()).getBlock();
                            i++;
                        }

                        return blocks;
                    } catch (IncompleteRegionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return new Block[0];
    }

}
