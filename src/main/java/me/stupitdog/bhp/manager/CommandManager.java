package me.stupitdog.bhp.manager;

import java.util.ArrayList;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.command.Command;
import me.stupitdog.bhp.command.commands.*;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommandManager {
    public CommandManager() {
    	MinecraftForge.EVENT_BUS.register(this);
        init();
    }

    public String prefix = ";";

    public ArrayList<Command> commands = new ArrayList<>();

    public void init() {
        commands.add(new CommandsCommand());
        commands.add(new ToggleCommand());
        commands.add(new BindCommand());
    }

    @SubscribeEvent
    public void chatEvent(ClientChatEvent event) {
        String[] args = event.getMessage().split(" ");
        if (event.getMessage().startsWith(prefix)) {
            event.setCanceled(true);
            for (Command command : commands) {
                if (args[0].equalsIgnoreCase(prefix + command.getName())) {
                    command.runCommand(args);
                    return;
                }
            }
            MessageManager.sendChatMessage("no command found, try " + prefix + "commands");
        }
    }
}
