package me.stupitdog.bhp.command.commands;

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.command.Command;
import me.stupitdog.bhp.manager.MessageManager;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.module.modules.client.ClickGui;
import me.stupitdog.bhp.setting.Bind;

public class BindCommand extends Command{
	
    public BindCommand() {
        super("bind", "Binds modules to a key.","bind" + " " + "[module]" + " " + "[key]");
    }

    @Override
    public void runCommand(String[] args) {
        if (args.length > 2) {
            for (Module module : Bhp.instance.moduleManager.modules) {
            	Bhp.instance.moduleManager.getModuleByClass(ClickGui.class).bind.setValue(new Bind(Keyboard.KEY_RSHIFT));
                if (module.getName().equalsIgnoreCase(args[1])) {
                    try {
                        module.setBind(Integer.parseInt(args[2]));
                        MessageManager.sendMultiLineMsg(ChatFormatting.WHITE + module.getName() + ChatFormatting.GRAY + " is now bound to " + ChatFormatting.WHITE + Keyboard.getKeyName(Integer.parseInt(args[2])).toUpperCase());
                    } catch (Exception e) {
                    }
                }
            }
        } else {
        	MessageManager.sendChatErrorMessage("invalid syntax");
        }
    }
}
