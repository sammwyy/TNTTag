package dev._2lstudios.tnttag.commands.impl.admin;

import dev._2lstudios.tnttag.arenas.TNTArenaManager;
import dev._2lstudios.tnttag.commands.Argument;
import dev._2lstudios.tnttag.commands.Command;
import dev._2lstudios.tnttag.commands.CommandContext;
import dev._2lstudios.tnttag.commands.CommandListener;
import dev._2lstudios.tnttag.players.TNTPlayer;

@Command(name = "delete", arguments = {
        Argument.STRING }, minArguments = 1, usageKey = "admin.delete.usage", permission = "tnttag.admin.delete")
public class DeleteSubcommand extends CommandListener {
    @Override
    public void onExecute(CommandContext ctx) {
        String id = ctx.getArguments().getString(0);

        TNTPlayer player = ctx.getPlayer();
        TNTArenaManager arenas = ctx.getPlugin().getArenaManager();
        boolean deleted = arenas.deleteArena(id);

        if (deleted) {
            player.sendI18nMessage("admin.delete.success");
        } else {
            player.sendI18nMessage("admin.delete.not-exist");
        }
    }
}
