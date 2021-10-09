package me.stupitdog.bhp.module.modules.player;

import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastProjectile extends Module {

	public FastProjectile() {
		super("FastProjectile","",Category.PLAYER);
	}
	
	@SubscribeEvent
	public void onBow(ArrowNockEvent event) {
	}
}
