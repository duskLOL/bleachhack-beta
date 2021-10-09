package me.stupitdog.bhp.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.command.Command;
import me.stupitdog.bhp.manager.MessageManager;
import me.stupitdog.bhp.module.Module;
import net.minecraft.util.text.TextFormatting;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("toggle", "Allows you to toggle a module.","toggle" + " " + "[module]");
    }

    @Override
    public void runCommand(String[] args) {
        if (args.length > 1) {
            try {
                for (Module m : Bhp.instance.moduleManager.modules) {
                    if (m.getName().equalsIgnoreCase(args[1])) {
                        m.toggle();
                        if (m.isEnabled()) {
                            MessageManager.sendChatMessage(ChatFormatting.WHITE + m.getName() + ChatFormatting.GRAY + " has been " + TextFormatting.GREEN + "enabled");
                        } else {
                        	MessageManager.sendChatMessage(ChatFormatting.WHITE + m.getName() + ChatFormatting.GRAY + " has been " + TextFormatting.RED + "disabled");
                        }
                    }
                }
            } catch (Exception e) {}
        } else {
        	MessageManager.sendChatErrorMessage("invalid syntax");
        }
    }
}
