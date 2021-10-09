package me.stupitdog.bhp.module.modules.client;

import me.stupitdog.bhp.BhpRpc;
import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class RPC extends Module {
	
	public RPC() {
		super("RPC", "", Category.CLIENT);
	}
	
	public void onEnable() {
		BhpRpc.startRPC();
	}
	
	public void onDisable() {
		BhpRpc.stopRPC();
		mc.player.connection.sendPacket(new SPacketPlayerPosLook());
	}
}
