package com.github.ascacos.command;

import com.github.ascacos.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.pokemon.stats.EVStore;
import com.pixelmonmod.pixelmon.api.pokemon.stats.IVStore;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;

public class ShowPokeCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("showpoke").executes(context -> {
            // incomplete command
            return 1;
        }).then(Commands.argument("slot", IntegerArgumentType.integer()).executes(context -> {

            CommandSource source = context.getSource();
            ServerPlayerEntity player = source.getPlayerOrException();


            // Permission check.
            if (! CommandUtils.hasPermission(player, "pixelmonshowpoke.use")) {
                source.sendFailure(new StringTextComponent(TextFormatting.RED + "This command is not available yet."));
                return -1;
            }

            int slot = context.getArgument("slot", Integer.class);
            if (slot > 0 && slot <= 6) {
            } else {
                player.sendMessage(new StringTextComponent(TextFormatting.RED +"Invalid slot: " + slot), player.getUUID());
                return -1;
            }

            final Pokemon pokemon = StorageProxy.getParty(player.getUUID()).get(slot - 1);

            StringBuilder string = new StringBuilder();

            // First line "Stats of PLAYER's level X POKEMON
            string.append(TextFormatting.YELLOW + "Stats of "
                    + TextFormatting.GOLD + player.getName().getString()
                    + TextFormatting.YELLOW + "'s "
                    + TextFormatting.YELLOW + "level " + pokemon.getPokemonLevel()
                    + TextFormatting.GOLD + " " + pokemon.getSpecies().getName() + "\n");

            // Current IVs
            string.append(TextFormatting.AQUA + "Current IVs" + TextFormatting.WHITE + ":\n");

            // IV percentage -> 105/186 (56%)
            string.append(TextFormatting.WHITE + "➡ " + TextFormatting.GREEN + pokemon.getIVs().getTotal()
                    + TextFormatting.WHITE + "/" + TextFormatting.GREEN + "186 "
                    + TextFormatting.WHITE + "("
                    + TextFormatting.GREEN + pokemon.getIVs().getPercentage(0) + "%"
                    + TextFormatting.WHITE + ")\n");

            // IV breakdown
            IVStore ivs = pokemon.getIVs();
            string.append(TextFormatting.WHITE + "➡ "
                    + TextFormatting.GREEN + ivs.getStat(BattleStatsType.HP) + TextFormatting.DARK_GREEN + " HP" + TextFormatting.WHITE + ", "
                    + TextFormatting.GREEN + ivs.getStat(BattleStatsType.ATTACK) + TextFormatting.DARK_GREEN + " Atk" + TextFormatting.WHITE + ", "
                    + TextFormatting.GREEN + ivs.getStat(BattleStatsType.DEFENSE) + TextFormatting.DARK_GREEN + " Def" + TextFormatting.WHITE + ", "
                    + TextFormatting.GREEN + ivs.getStat(BattleStatsType.SPECIAL_ATTACK) + TextFormatting.DARK_GREEN + " SAtk" + TextFormatting.WHITE + ", "
                    + TextFormatting.GREEN + ivs.getStat(BattleStatsType.SPECIAL_DEFENSE) + TextFormatting.DARK_GREEN + " SDef" + TextFormatting.WHITE + ", "
                    + TextFormatting.GREEN + ivs.getStat(BattleStatsType.SPEED) + TextFormatting.DARK_GREEN + " Spd\n");

            // Current EVs
            string.append(TextFormatting.AQUA + "Current EVs" + TextFormatting.WHITE + ":\n");

            int totalEVs = pokemon.getEVs().getTotal();
            int percentageEVs = totalEVs / 510 * 100;
            // EV percentage -> 105/186 (56%)
            string.append(TextFormatting.WHITE + "➡ " + TextFormatting.GREEN + totalEVs
                    + TextFormatting.WHITE + "/" + TextFormatting.GREEN + "510 "
                    + TextFormatting.WHITE + "("
                    + TextFormatting.GREEN + percentageEVs + "%"
                    + TextFormatting.WHITE + ")\n" );

            // EV breakdown
            EVStore evs = pokemon.getEVs();
            string.append(TextFormatting.WHITE + "➡ "
                    + TextFormatting.GREEN + evs.getStat(BattleStatsType.HP) + TextFormatting.DARK_GREEN + " HP" + TextFormatting.WHITE + ", "
                    + TextFormatting.GREEN + evs.getStat(BattleStatsType.ATTACK) + TextFormatting.DARK_GREEN + " Atk" + TextFormatting.WHITE + ", "
                    + TextFormatting.GREEN + evs.getStat(BattleStatsType.DEFENSE) + TextFormatting.DARK_GREEN + " Def" + TextFormatting.WHITE + ", "
                    + TextFormatting.GREEN + evs.getStat(BattleStatsType.SPECIAL_ATTACK) + TextFormatting.DARK_GREEN + " SAtk" + TextFormatting.WHITE + ", "
                    + TextFormatting.GREEN + evs.getStat(BattleStatsType.SPECIAL_DEFENSE) + TextFormatting.DARK_GREEN + " SDef" + TextFormatting.WHITE + ", "
                    + TextFormatting.GREEN + evs.getStat(BattleStatsType.SPEED) + TextFormatting.DARK_GREEN + " Spd\n");

            // Extra info
            String gender = "";
            switch (pokemon.getGender()) {
                case MALE: gender = "male"; break;
                case FEMALE: gender = "female"; break;
                case NONE: gender = "genderless"; break;
            }

            string.append(TextFormatting.AQUA + "Extra info" + TextFormatting.WHITE + ":\n"
                    + TextFormatting.WHITE + "➡ " + TextFormatting.GREEN + "It is " + TextFormatting.DARK_GREEN + pokemon.getGrowth().toString().toLowerCase() + "\n"
                    + TextFormatting.WHITE + "➡ " + TextFormatting.GREEN + "It is " + TextFormatting.DARK_GREEN + gender + "\n"
                    + TextFormatting.WHITE + "➡ " + TextFormatting.GREEN + "It is " + TextFormatting.DARK_GREEN + pokemon.getNature().getLocalizedName().toLowerCase() + "\n"
                    + TextFormatting.WHITE + "➡ " + TextFormatting.GREEN + "It has the "
                    + TextFormatting.DARK_GREEN + pokemon.getAbility().getName().toLowerCase() + TextFormatting.GREEN + " ability");

            // Create chat message component + hover info
            TextComponent specsComponent = new StringTextComponent(string.toString());

            String broadcastMessage = "\n" + TextFormatting.GOLD + player.getName()
                    + TextFormatting.YELLOW + " is showing off a "
                    + TextFormatting.GOLD + pokemon.getSpecies().getName()
                    + TextFormatting.YELLOW + ", hover for info!\n";

            TextComponent message = new StringTextComponent(broadcastMessage);
            HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, specsComponent);
            message.setStyle(message.getStyle().withHoverEvent(hover));

            MessageUtils.broadcastToAll(message);
            return 1;
        })));
    }

}
