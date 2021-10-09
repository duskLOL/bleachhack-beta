package me.stupitdog.bhp.module.modules.movement;

import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import net.minecraft.client.settings.KeyBinding;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint","",Category.MOVEMENT);
	}
	
	public void onUpdate() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
	}
	
	public void onDisable() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
	}
}
