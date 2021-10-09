package me.stupitdog.bhp.module.modules.movement;

import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlowDown extends Module {

	public NoSlowDown() {
		super("NoSlowDown","",Category.MOVEMENT);
	}
	
	@SubscribeEvent
	public void listener(InputUpdateEvent event) {
        if (mc.player.isHandActive() && !mc.player.isRiding()) {
            event.getMovementInput().moveStrafe *= 5;
            event.getMovementInput().moveForward *= 5;
        }
	}
}
