package me.stupitdog.bhp.module.modules.player;

import org.lwjgl.input.Keyboard;

import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import net.minecraft.client.gui.GuiChat;

public class GuiMove extends Module {
	
	public GuiMove() {
		super("GuiMove","",Category.PLAYER);
	}
	
	public void onUpdate() {
		if (mc.currentScreen != null){
			if (!(mc.currentScreen instanceof GuiChat)){
				try {
					if (Keyboard.isKeyDown(200)){
						mc.player.rotationPitch -= 5;
					}
					if (Keyboard.isKeyDown(208)){
						mc.player.rotationPitch += 5;
					}
					if (Keyboard.isKeyDown(205)){
						mc.player.rotationYaw += 5;
					}
					if (Keyboard.isKeyDown(203)){
						mc.player.rotationYaw -= 5;
					}
					if (mc.player.rotationPitch > 90){
						mc.player.rotationPitch = 90;
					}
					if (mc.player.rotationPitch < -90){
						mc.player.rotationPitch = -90;
					}
				}
				catch(Exception e) {}
			}
		}
	}
}
