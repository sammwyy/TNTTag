package dev._2lstudios.tnttag.commands.impl.admin;

import dev._2lstudios.tnttag.arenas.TNTArenaManager;
import dev._2lstudios.tnttag.commands.Argument;
import dev._2lstudios.tnttag.commands.Command;
import dev._2lstudios.tnttag.commands.CommandContext;
import dev._2lstudios.tnttag.commands.CommandListener;
import dev._2lstudios.tnttag.players.TNTPlayer;

@Command(name = "create", arguments = {
        Argument.STRING }, minArguments = 1, usageKey = "admin.create.usage", permission = "tnttag.admin.create")
public class CreateSubcommand extends CommandListener {
    @Override
    public void onExecute(CommandContext ctx) {
        TNTPlayer player = ctx.getPlayer();
        TNTArenaManager arenas = ctx.getPlugin().getArenaManager();
        String id = ctx.getArguments().getString(0);

        if (arenas.addArena(id)) {
            player.sendI18nMessage("admin.create.success");
        } else {
            player.sendI18nMessage("admin.create.already-exist");
        }
    }
}
