package me.stupitdog.bhp.command.commands;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.command.Command;
import me.stupitdog.bhp.manager.MessageManager;
import net.minecraft.util.text.TextFormatting;

public class CommandsCommand extends Command {
    public CommandsCommand() {
        super("commands", "shows all the possible commands","command");
    }

    @Override
    public void runCommand(String[] args) {
        try {
        	for(Command c : Bhp.instance.commandManager.commands) {
        		MessageManager.sendMultiLineMsg(TextFormatting.WHITE + c.getName() + TextFormatting.GRAY + " " + c.getDescription() + " syntax: " + c.getSyntax());
        	}
        } catch (Exception e) {}
    }
}
