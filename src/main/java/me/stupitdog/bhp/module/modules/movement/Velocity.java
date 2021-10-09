package me.stupitdog.bhp.module.modules.movement;

import me.stupitdog.bhp.event.events.PacketEvent;
import me.stupitdog.bhp.event.events.PlayerCollisionEvent;
import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {

	public Velocity() {
		super("Velocity","",Category.MOVEMENT);
	}
	
	@SubscribeEvent
	public void listener(PacketEvent.Receive event) {
		if (event.getPacket() instanceof SPacketEntityVelocity){
			if (((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId()) {
				event.cancel();
			}
		}
		if (event.getPacket() instanceof SPacketExplosion){
			event.cancel();
		}
	}
	
	@SubscribeEvent
	public void listener(PlayerSPPushOutOfBlocksEvent event) {
		event.setCanceled(true);
	}
	
	@SubscribeEvent
	public void listener(PlayerCollisionEvent event) {
		event.cancel();
	}
}
