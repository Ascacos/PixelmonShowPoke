package com.github.ascacos.command;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.OpEntry;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.server.permission.PermissionAPI;

/**
 * Utility class for commands
 */
public class CommandUtils {

    /**
     * Checks if a player has a permission.
     * @param player The ServerPlayerEntity to check
     * @param permission The permission to check as a string.
     * @return true if the player has permission, false otherwise.
     */
    public static boolean hasPermission(ServerPlayerEntity player, String permission) {
        if (ServerLifecycleHooks.getCurrentServer().isSingleplayer()) {
            return player.hasPermissions(2);
        } else {
            MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
            OpEntry entry = (OpEntry)currentServer.getPlayerList().getOps().get(player.getGameProfile());
            return entry != null ? true : PermissionAPI.hasPermission(player, permission);
        }
    }
}
