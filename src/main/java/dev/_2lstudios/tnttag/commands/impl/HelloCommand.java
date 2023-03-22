package dev._2lstudios.tnttag.commands.impl;

import dev._2lstudios.tnttag.commands.Command;
import dev._2lstudios.tnttag.commands.CommandContext;
import dev._2lstudios.tnttag.commands.CommandListener;

@Command(name = "hello")
public class HelloCommand extends CommandListener {
  public HelloCommand() {
    this.addSubcommand(new WorldSubCommand());
  }

  @Override
  public void onExecute(CommandContext ctx) {
    ctx.getExecutor().sendI18nMessage("hello");
  }
}
