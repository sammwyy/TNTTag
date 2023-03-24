package dev._2lstudios.tnttag.commands.impl;

import dev._2lstudios.tnttag.commands.Command;
import dev._2lstudios.tnttag.commands.CommandContext;
import dev._2lstudios.tnttag.commands.CommandListener;
import dev._2lstudios.tnttag.commands.impl.admin.CreateSubcommand;
import dev._2lstudios.tnttag.commands.impl.admin.DeleteSubcommand;

@Command(name = "tnttag")
public class TNTTagAdminCommand extends CommandListener {
  public TNTTagAdminCommand() {
    this.addSubcommand(new CreateSubcommand());
    this.addSubcommand(new DeleteSubcommand());
  }

  @Override
  public void onExecute(CommandContext ctx) {
    ctx.getExecutor().sendI18nMessage("admin.help");
  }
}
