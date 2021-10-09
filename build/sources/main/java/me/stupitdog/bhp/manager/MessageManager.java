package me.stupitdog.bhp.manager;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.module.modules.client.ClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;

public class MessageManager {

	public static void sendChatMessage(String message) {
		String string = TextFormatting.GRAY + "<" + Bhp.instance.cmdColor + "bleachhack+" + TextFormatting.GRAY + "> " + message;
		ITextComponent sendingMessage = new TextComponentString(string).setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("frank alachi"))));
		
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(sendingMessage, 5936);
	}
	
	public static void sendMultiLineMsg(String message) {
		String string = TextFormatting.GRAY + "<" + Bhp.instance.cmdColor + "bleachhack+" + TextFormatting.GRAY + "> " + message;
		
		Minecraft.getMinecraft().player.sendMessage(new TextComponentString(string));
	}
	
	public static void sendChatErrorMessage(String message) {
		String string = TextFormatting.GRAY + "<" + Bhp.instance.cmdColor + "bleachhack+" + TextFormatting.GRAY + "> " + TextFormatting.RED + TextFormatting.BOLD + "ERROR: " + TextFormatting.RESET + TextFormatting.RED + message;
		ITextComponent sendingMessage = new TextComponentString(string).setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("frank alachi"))));
		
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(sendingMessage, 5936);
	}
}
