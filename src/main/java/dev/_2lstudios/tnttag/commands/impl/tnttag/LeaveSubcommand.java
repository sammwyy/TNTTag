package dev._2lstudios.tnttag.commands.impl.tnttag;

import dev._2lstudios.tnttag.commands.Command;
import dev._2lstudios.tnttag.commands.CommandContext;
import dev._2lstudios.tnttag.commands.CommandListener;
import dev._2lstudios.tnttag.players.TNTPlayer;

@Command(name = "leave")
public class LeaveSubcommand extends CommandListener {
    @Override
    public void onExecuteByPlayer(CommandContext ctx) {
        TNTPlayer player = ctx.getPlayer();
        if (player.leave()) {
            player.sendI18nMessage("leave.success");
        } else {
            player.sendI18nMessage("leave.not-in-arena");
        }
    }
}
