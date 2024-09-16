package com.github.ascacos;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.List;

public class MessageUtils {

    /**
     * Broadcasts a message to all online players
     * @param message The message, as a TextComponent, to broadcast
     */
    public static void broadcastToAll(TextComponent message) {
        List<ServerPlayerEntity> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();

        for(ServerPlayerEntity playerToMsg : players) {
            playerToMsg.sendMessage(message, playerToMsg.getUUID());
        }
    }

    /**
     * Sends a chat message to a list of players
     * @param players The players to message
     * @param message The message to send
     */
    public static void broadcastToList(List<ServerPlayerEntity> players, TextComponent message) {
        for(ServerPlayerEntity playerToMsg : players) {
            playerToMsg.sendMessage(message, playerToMsg.getUUID());
        }
    }

    /***
     * Sends a message to a specified player. The message should have any TextFormatting applied before being passed to
     * this.
     * @param player The player to message
     * @param message The message to send
     */
    public static void sendMessage(ServerPlayerEntity player, String message) {
        player.sendMessage(new StringTextComponent(message), player.getUUID());
    }
}
