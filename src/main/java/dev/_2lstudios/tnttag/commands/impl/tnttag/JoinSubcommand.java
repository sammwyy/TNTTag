package dev._2lstudios.tnttag.commands.impl.tnttag;

import dev._2lstudios.tnttag.TNTTag;
import dev._2lstudios.tnttag.arenas.TNTArena;
import dev._2lstudios.tnttag.commands.Argument;
import dev._2lstudios.tnttag.commands.Command;
import dev._2lstudios.tnttag.commands.CommandContext;
import dev._2lstudios.tnttag.commands.CommandListener;
import dev._2lstudios.tnttag.players.TNTPlayer;

@Command(name = "join", arguments = { Argument.STRING })
public class JoinSubcommand extends CommandListener {
    private void randomJoin(TNTPlayer player, TNTTag plugin) {
        TNTArena arena = plugin.getArenaMAnager().cherryPicking();
        if (arena == null) {
            player.sendI18nMessage("join.no-available-arena");
            return;
        }
        player.join(arena);
    }

    private void selectiveJoin(TNTPlayer player, TNTTag plugin, String arenaId) {
        TNTArena arena = plugin.getArenaMAnager().getArena(arenaId);
        if (arena == null) {
            player.sendI18nMessage("join.not-exist");
        } else {
            player.join(arena);
        }
    }

    @Override
    public void onExecuteByPlayer(CommandContext ctx) {
        String arenaId = ctx.getArguments().getString(0);

        if (arenaId == null)
            this.randomJoin(ctx.getPlayer(), ctx.getPlugin());
        else
            this.selectiveJoin(ctx.getPlayer(), ctx.getPlugin(), arenaId);
    }
}
