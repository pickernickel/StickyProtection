package net.simplycrafted.StickyProtection;

import net.simplycrafted.StickyLocks.Database;
import net.simplycrafted.StickyLocks.StickyLocks;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.material.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

/**
 * Copyright © Brian Ronald
 * 04/08/14
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
public class StickyProtection extends JavaPlugin implements Listener {
    StickyLocks stickyLocks;
    Database database;

    @Override
    public void onEnable() {
        stickyLocks = (StickyLocks) getServer().getPluginManager().getPlugin("StickyLocks");
        database = new Database();
        getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {

    }

    boolean checkProtection(Block block, Player player) {
        // Check whether this block is protected by StickyLocks
        if (accessDenied(block, player)) {
            return true;
        }
        // Check whether this block is supporting a block above it which is protected by StickyLocks
        Block relativeBlock = block.getRelative(BlockFace.UP);
        switch (relativeBlock.getType()) {
            case IRON_DOOR:
            case WOODEN_DOOR:
            case REDSTONE_COMPARATOR_ON:
            case REDSTONE_COMPARATOR_OFF:
            case DIODE_BLOCK_ON:
            case DIODE_BLOCK_OFF:
            case SIGN_POST:
            case DRAGON_EGG:
            case ANVIL:
            case GOLD_PLATE:
            case IRON_PLATE:
            case STONE_PLATE:
            case WOOD_PLATE:
            case GRAVEL:
            case SAND:
            case REDSTONE_WIRE:
                if(accessDenied(relativeBlock, player)) {
                    return true;
                }
                break;
            case TORCH:
            case REDSTONE_TORCH_ON:
            case REDSTONE_TORCH_OFF:
            case WOOD_BUTTON:
            case STONE_BUTTON:
            case LEVER:
                try {
                    if (((SimpleAttachableMaterialData) relativeBlock.getState().getData()).getAttachedFace().equals(BlockFace.DOWN) & accessDenied(relativeBlock, player)) {
                        return true;
                    }
                } catch (ClassCastException e) {
                    getLogger().info("ClassCastException (safe to ignore)");
                }
        }
        // This is interesting... an anonymous set to iterate, so I don't have to write code four times.
        for (BlockFace lateralFace : new HashSet<BlockFace>(){{
            add(BlockFace.EAST);
            add(BlockFace.NORTH);
            add(BlockFace.WEST);
            add(BlockFace.SOUTH);
        }}) {
            // Things which hang off the side of a block
            relativeBlock = block.getRelative(lateralFace);
            switch (relativeBlock.getType()) {
                case TRAP_DOOR:
                // Future: case IRON_TRAPDOOR:
                case WALL_SIGN:
                case TORCH:
                case REDSTONE_TORCH_ON:
                case REDSTONE_TORCH_OFF:
                case WOOD_BUTTON:
                case STONE_BUTTON:
                case LEVER:
                case LADDER:
                case TRIPWIRE_HOOK:
                    try {
                        if (((SimpleAttachableMaterialData) relativeBlock.getState().getData()).getAttachedFace().equals(lateralFace.getOppositeFace()) & accessDenied(relativeBlock, player)) {
                            return true;
                        }
                    } catch (ClassCastException e) {
                        getLogger().info("ClassCastException (safe to ignore)");
                    }
            }
            // Things which hang from under a block
            relativeBlock = block.getRelative(BlockFace.DOWN);
            switch (relativeBlock.getType()) {
                case WOOD_BUTTON:
                case STONE_BUTTON:
                case LEVER:
                    try {
                        if (((SimpleAttachableMaterialData) relativeBlock.getState().getData()).getAttachedFace().equals(BlockFace.UP) & accessDenied(relativeBlock, player)) {
                            return true;
                        }
                    } catch (ClassCastException e) {
                        getLogger().info("ClassCastException (safe to ignore)");
                    }
            }
        }
        return false;
    }

    boolean checkProtection(Block block) {
        // Check whether this block is protected by StickyLocks
        if (accessDenied(block)) {
            return true;
        }
        // Check whether this block is supporting a block above it which is protected by StickyLocks
        Block relativeBlock = block.getRelative(BlockFace.UP);
        switch (relativeBlock.getType()) {
            case IRON_DOOR:
            case WOODEN_DOOR:
            case REDSTONE_COMPARATOR_ON:
            case REDSTONE_COMPARATOR_OFF:
            case DIODE_BLOCK_ON:
            case DIODE_BLOCK_OFF:
            case SIGN_POST:
            case DRAGON_EGG:
            case ANVIL:
            case GOLD_PLATE:
            case IRON_PLATE:
            case STONE_PLATE:
            case WOOD_PLATE:
            case GRAVEL:
            case SAND:
            case REDSTONE_WIRE:
                if(accessDenied(relativeBlock)) {
                    return true;
                }
                break;
            case TORCH:
            case REDSTONE_TORCH_ON:
            case REDSTONE_TORCH_OFF:
            case WOOD_BUTTON:
            case STONE_BUTTON:
            case LEVER:
                try {
                    if (((SimpleAttachableMaterialData) relativeBlock.getState().getData()).getAttachedFace().equals(BlockFace.DOWN) & accessDenied(relativeBlock)) {
                        return true;
                    }
                } catch (ClassCastException e) {
                    getLogger().info("ClassCastException (safe to ignore)");
                }
        }
        // This is interesting... an anonymous set to iterate, so I don't have to write code four times.
        for (BlockFace lateralFace : new HashSet<BlockFace>(){{
            add(BlockFace.EAST);
            add(BlockFace.NORTH);
            add(BlockFace.WEST);
            add(BlockFace.SOUTH);
        }}) {
            // Things which hang off the side of a block
            relativeBlock = block.getRelative(lateralFace);
            switch (relativeBlock.getType()) {
                case TRAP_DOOR:
                    // Future: case IRON_TRAPDOOR:
                case WALL_SIGN:
                case TORCH:
                case REDSTONE_TORCH_ON:
                case REDSTONE_TORCH_OFF:
                case WOOD_BUTTON:
                case STONE_BUTTON:
                case LEVER:
                case LADDER:
                case TRIPWIRE_HOOK:
                    try {
                        if (((SimpleAttachableMaterialData) relativeBlock.getState().getData()).getAttachedFace().equals(lateralFace.getOppositeFace()) & accessDenied(relativeBlock)) {
                            return true;
                        }
                    } catch (ClassCastException e) {
                        getLogger().info("ClassCastException (safe to ignore)");
                    }
            }
            // Things which hang from under a block
            relativeBlock = block.getRelative(BlockFace.DOWN);
            switch (relativeBlock.getType()) {
                case WOOD_BUTTON:
                case STONE_BUTTON:
                case LEVER:
                    try {
                        if (((SimpleAttachableMaterialData) relativeBlock.getState().getData()).getAttachedFace().equals(BlockFace.UP) & accessDenied(relativeBlock)) {
                            return true;
                        }
                    } catch (ClassCastException e) {
                        getLogger().info("ClassCastException (safe to ignore)");
                    }
            }
        }
        return false;
    }

    private boolean accessDenied(Block block, Player player) {
        return database.getProtection(block).isProtected() && database.accessDenied(player, block);
    }

    private boolean accessDenied(Block block) {
        return database.getProtection(block).isProtected();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (checkProtection(event.getBlock(),event.getPlayer())) {
            if (event.getPlayer()!= null && !event.getPlayer().hasPermission("stickylocks.ghost")) {
                event.setCancelled(true);
                stickyLocks.sendMessage(event.getPlayer(),"This block is protected from destruction",false);
            }
        }
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (event.isCancelled()) {
            return;
        }
        for (Block block : event.getBlocks()) {
            if (checkProtection(block)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (event.isCancelled() | !event.isSticky()) {
            return;
        }
        // Check the block beyond the piston extension, which is two blocks away
        if (checkProtection(event.getBlock().getRelative(event.getDirection()).getRelative(event.getDirection()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (checkProtection(event.getBlock())) {
            event.setCancelled(true);
        }
    }
}
