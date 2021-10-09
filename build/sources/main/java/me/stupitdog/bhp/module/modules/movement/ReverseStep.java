package me.stupitdog.bhp.module.modules.movement;

import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;

public class ReverseStep extends Module {
	
	public ReverseStep() {
		super("ReverseStep","",Category.MOVEMENT);
	}
	
	public void onUpdate() {
        if (mc.player.onGround && !mc.player.isInWater() && !mc.player.isInLava() && !mc.player.isOnLadder()) {
            --mc.player.motionY;
        }
	}
}
