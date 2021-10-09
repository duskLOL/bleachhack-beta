package me.stupitdog.bhp.module.modules.movement;

import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;

public class Step extends Module {

	private Setting<Float> height = this.register(new Setting<Float>("Height",2.5f,0f,2.5f));
	
	public Step() {
		super("Step","Vanilla",Category.MOVEMENT);
	}
	
	public void onDisable() {
		try {
		mc.player.stepHeight = 0.5f;
		}
		catch(Exception e) {
		}
	}
	
	public void onUpdate() {
		mc.player.stepHeight = height.getValue();
	}
}