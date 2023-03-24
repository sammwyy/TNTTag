package dev._2lstudios.tnttag.commands.impl;

import dev._2lstudios.tnttag.commands.Command;
import dev._2lstudios.tnttag.commands.CommandContext;
import dev._2lstudios.tnttag.commands.CommandListener;
import dev._2lstudios.tnttag.commands.impl.tnttag.JoinSubcommand;
import dev._2lstudios.tnttag.commands.impl.tnttag.LeaveSubcommand;

@Command(name = "tnttag")
public class TNTTagCommand extends CommandListener {
  public TNTTagCommand() {
    this.addSubcommand(new JoinSubcommand());
    this.addSubcommand(new LeaveSubcommand());
  }

  @Override
  public void onExecute(CommandContext ctx) {
    ctx.getExecutor().sendI18nMessage("help");
  }
}
